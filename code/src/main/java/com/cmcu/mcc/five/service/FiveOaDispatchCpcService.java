package com.cmcu.mcc.five.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.five.dao.FiveOaDispatchCpcMapper;
import com.cmcu.mcc.five.dto.FiveOaDispatchCpcDto;
import com.cmcu.mcc.five.entity.FiveOaDispatchCpc;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.HrEmployeeService;
import com.cmcu.mcc.hr.service.HrEmployeeSysService;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class FiveOaDispatchCpcService {
   @Resource
   FiveOaDispatchCpcMapper fiveOaDispatchCpcMapper;
    @Resource
    HrEmployeeMapper hrEmployeeMapper;
    @Autowired
    MyActService myActService;
    @Autowired
    ProcessQueryService processQueryService;
    @Autowired
    HrEmployeeSysService hrEmployeeSysService;
    @Autowired
    SelectEmployeeService selectEmployeeService;
    @Autowired
    TaskHandleService taskHandleService;
    @Autowired
    HrEmployeeService hrEmployeeService;

    public void remove(int id,String userLogin){
        FiveOaDispatchCpc model = fiveOaDispatchCpcMapper.selectByPrimaryKey(id);
        Assert.state(model.getCreator().equals(userLogin),"该数据是用户"+model.getCreatorName()+"创建");
        //流程作废
        myActService.delete(model.getProcessInstanceId(),"删除",userLogin);
        model.setDeleted(true);
        model.setGmtModified(new Date());
        BeanValidator.validateObject(model);
        fiveOaDispatchCpcMapper.updateByPrimaryKey(model);

    }
    public void update(FiveOaDispatchCpcDto fiveOaDispatchCpcDto){
        FiveOaDispatchCpc model = fiveOaDispatchCpcMapper.selectByPrimaryKey(fiveOaDispatchCpcDto.getId());
        model.setFileId(fiveOaDispatchCpcDto.getFileId());
        model.setDispatchType(fiveOaDispatchCpcDto.getDispatchType());
        model.setSigner(fiveOaDispatchCpcDto.getSigner());
        model.setSignerName(fiveOaDispatchCpcDto.getSignerName());
        model.setCountersign(fiveOaDispatchCpcDto.getCountersign());
        model.setCountersignName(fiveOaDispatchCpcDto.getCountersignName());
        model.setAuditMan(fiveOaDispatchCpcDto.getAuditMan());
        model.setAuditManName(fiveOaDispatchCpcDto.getAuditManName());
        model.setDrafter(fiveOaDispatchCpcDto.getDrafter());
        model.setDrafterName(fiveOaDispatchCpcDto.getDrafterName());
        model.setDispatchTitle(fiveOaDispatchCpcDto.getDispatchTitle());
        model.setRealSendManName(fiveOaDispatchCpcDto.getRealSendManName());
        model.setCopySendManName(fiveOaDispatchCpcDto.getCopySendManName());
        model.setSubjectTerm(fiveOaDispatchCpcDto.getSubjectTerm());
        model.setCpcWordSize(fiveOaDispatchCpcDto.getCpcWordSize());
        model.setDispatching(fiveOaDispatchCpcDto.getDispatching());
        model.setPrintNumber(fiveOaDispatchCpcDto.getPrintNumber());
        model.setGmtModified(fiveOaDispatchCpcDto.getGmtModified());
        BeanValidator.validateObject(model);
        ModelUtil.setNotNullFields(model);
        Map variables = Maps.newHashMap();
        if (fiveOaDispatchCpcDto.getDeptId()!=model.getId()){
            variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(model.getDeptId()));
            model.setDeptName(fiveOaDispatchCpcDto.getDeptName());
            model.setDeptId(fiveOaDispatchCpcDto.getDeptId());
        }
        fiveOaDispatchCpcMapper.updateByPrimaryKey(model);
        //核稿人
        variables.put("companyOfficeMan", MyStringUtil.getStringList(model.getAuditMan()));
        //签发人
        variables.put("companyLeader", MyStringUtil.getStringList(model.getSigner()));

        variables.put("processDescription", model.getDispatchTitle());
        BeanValidator.check(model);
        myActService.setVariables(model.getProcessInstanceId(),variables);


    }

    public FiveOaDispatchCpcDto getModelById(int id){
        return getDto(fiveOaDispatchCpcMapper.selectByPrimaryKey(id));
    }

    public FiveOaDispatchCpcDto getDto(FiveOaDispatchCpc item) {
        FiveOaDispatchCpcDto dto=FiveOaDispatchCpcDto.adapt(item);
        dto.setProcessName("已完成");
        if (!dto.getProcessEnd()) {
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");

            if(customProcessInstance ==null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveOaDispatchCpcMapper.updateByPrimaryKey(dto);
            }
            if(customProcessInstance!=null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }
        return dto;
    }

    public int getNewModel(String userLogin){
        List<String> secretary =  hrEmployeeService.selectUserByRoleNames("机要秘书");
        Assert.state(secretary.size()>0,"未找到职务为 机要秘书 人员");
        FiveOaDispatchCpc item=new FiveOaDispatchCpc();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        item.setDispatchType("中共中国五洲工程设计集团有限公式委员会发文文稿");
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        ModelUtil.setNotNullFields(item);
        fiveOaDispatchCpcMapper.insert(item);
        String businessKey= EdConst.FIVE_OA_DISPATCH_CPC+ "_" + item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", item.getDispatchType());
        variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(item.getDeptId()));
        variables.put("secretary",secretary);

        item.setBusinessKey(businessKey);
        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_DISPATCH_CPC,item.getBusinessKey(), variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        item.setBusinessKey(businessKey);
        fiveOaDispatchCpcMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, int pageNum, int pageSize) {
        params.put("deleted",false);
        String userLogin = params.get("userLogin").toString();
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaDispatchCpcMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaDispatchCpc item=(FiveOaDispatchCpc)p;
            FiveOaDispatchCpcDto dto = getDto(item);
            if (dto.getAttendUser().contains(userLogin)){
                list.add(dto);
            }
        });
        pageInfo.setList(list);
        return pageInfo;
    }

}
