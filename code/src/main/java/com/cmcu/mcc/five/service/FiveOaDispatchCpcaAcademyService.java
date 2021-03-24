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
import com.cmcu.mcc.five.dao.FiveOaDispatchCpcaAcademyMapper;
import com.cmcu.mcc.five.dto.FiveOaDispatchCpcaAcademyDto;
import com.cmcu.mcc.five.entity.FiveOaDispatchCpcaAcademy;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.HrEmployeeService;
import com.cmcu.mcc.hr.service.HrEmployeeSysService;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.service.impl.HandleFormService;
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
public class FiveOaDispatchCpcaAcademyService {
    @Resource
    FiveOaDispatchCpcaAcademyMapper fiveOaDispatchCpcaAcademyMapper;
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
    @Resource
    HandleFormService handleFormService;

    public void remove(int id,String userLogin){
        FiveOaDispatchCpcaAcademy item = fiveOaDispatchCpcaAcademyMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");

        //流程作废
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);

    }

    public void update(FiveOaDispatchCpcaAcademyDto fiveOaDispatchCpcaAcademyDto){
        FiveOaDispatchCpcaAcademy model = fiveOaDispatchCpcaAcademyMapper.selectByPrimaryKey(fiveOaDispatchCpcaAcademyDto.getId());
        model.setFileId(fiveOaDispatchCpcaAcademyDto.getFileId());
        model.setDispatchType(fiveOaDispatchCpcaAcademyDto.getDispatchType());
        model.setSigner(fiveOaDispatchCpcaAcademyDto.getSigner());
        model.setSignerName(fiveOaDispatchCpcaAcademyDto.getSignerName());
        model.setCountersign(fiveOaDispatchCpcaAcademyDto.getCountersign());
        model.setCountersignName(fiveOaDispatchCpcaAcademyDto.getCountersignName());
        model.setAuditMan(fiveOaDispatchCpcaAcademyDto.getAuditMan());
        model.setAuditManName(fiveOaDispatchCpcaAcademyDto.getAuditManName());
        model.setDrafter(fiveOaDispatchCpcaAcademyDto.getDrafter());
        model.setDrafterName(fiveOaDispatchCpcaAcademyDto.getDrafterName());
        model.setDispatchTitle(fiveOaDispatchCpcaAcademyDto.getDispatchTitle());
        model.setRealSendManName(fiveOaDispatchCpcaAcademyDto.getRealSendManName());
        model.setCopySendManName(fiveOaDispatchCpcaAcademyDto.getCopySendManName());
        model.setSubjectTerm(fiveOaDispatchCpcaAcademyDto.getSubjectTerm());
        model.setCpcWordSize(fiveOaDispatchCpcaAcademyDto.getCpcWordSize());
        model.setDispatching(fiveOaDispatchCpcaAcademyDto.getDispatching());
        model.setPrintNumber(fiveOaDispatchCpcaAcademyDto.getPrintNumber());
        model.setGmtModified(fiveOaDispatchCpcaAcademyDto.getGmtModified());
        BeanValidator.validateObject(model);
        ModelUtil.setNotNullFields(model);
        Map variables = Maps.newHashMap();
        if (fiveOaDispatchCpcaAcademyDto.getDeptId()!=model.getId()){
            variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(model.getDeptId()));
            model.setDeptName(fiveOaDispatchCpcaAcademyDto.getDeptName());
            model.setDeptId(fiveOaDispatchCpcaAcademyDto.getDeptId());
        }
        fiveOaDispatchCpcaAcademyMapper.updateByPrimaryKey(model);
        //核稿人
        variables.put("companyOfficeMan", MyStringUtil.getStringList(model.getAuditMan()));
        //签发人
        variables.put("companyLeader", MyStringUtil.getStringList(model.getSigner()));
        variables.put("processDescription", model.getDispatchTitle());
        BeanValidator.check(model);
        myActService.setVariables(model.getProcessInstanceId(),variables);


    }

    public FiveOaDispatchCpcaAcademyDto getModelById(int id){
        return getDto(fiveOaDispatchCpcaAcademyMapper.selectByPrimaryKey(id));
    }

    public FiveOaDispatchCpcaAcademyDto getDto(FiveOaDispatchCpcaAcademy item) {
        FiveOaDispatchCpcaAcademyDto dto=FiveOaDispatchCpcaAcademyDto.adapt(item);
        dto.setProcessName("已完成");
        if (!dto.getProcessEnd()) {
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");

            if (customProcessInstance == null || customProcessInstance.isFinished()) {
                dto.setProcessEnd(true);
                fiveOaDispatchCpcaAcademyMapper.updateByPrimaryKey(dto);
            }
            if (customProcessInstance != null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())) {
                dto.setProcessName(customProcessInstance.getCurrentTaskName());

            }
        }
        return dto;
    }

    public int getNewModel(String userLogin){
        List<String> secretary =  hrEmployeeService.selectUserByRoleNames("机要秘书");
        Assert.state(secretary.size()>0,"未找到职务为 机要秘书 人员");
        FiveOaDispatchCpcaAcademy item=new FiveOaDispatchCpcaAcademy();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        item.setDispatchType("中共五洲工程设计研究院委员会发文单");
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
        fiveOaDispatchCpcaAcademyMapper.insert(item);
        String businessKey= EdConst.FIVE_OA_DISPATCH_CPCA_ACADEMY+ "_" + item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", item.getDispatchType());
        variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(item.getDeptId()));
        variables.put("secretary",secretary);

        item.setBusinessKey(businessKey);
        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_DISPATCH_CPCA_ACADEMY,item.getBusinessKey(), variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        item.setBusinessKey(businessKey);
        fiveOaDispatchCpcaAcademyMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, int pageNum, int pageSize) {
        params.put("deleted",false);
        String userLogin = params.get("userLogin").toString();
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaDispatchCpcaAcademyMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaDispatchCpcaAcademy item=(FiveOaDispatchCpcaAcademy)p;
            FiveOaDispatchCpcaAcademyDto dto = getDto(item);
            if (dto.getAttendUser().contains(userLogin)){
                list.add(dto);
            }
        });
        pageInfo.setList(list);
        return pageInfo;
    }
}
