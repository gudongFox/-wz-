package com.cmcu.mcc.five.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.dto.ActHistoryDto;
import com.cmcu.mcc.five.dao.FiveOaContractSignMapper;
import com.cmcu.mcc.five.dto.FiveOaContractSignDto;
import com.cmcu.mcc.five.entity.FiveOaContractSign;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.HrEmployeeService;
import com.cmcu.mcc.hr.service.HrEmployeeSysService;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.service.ActService;
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
public class FiveOaContractSignService {
    @Resource
    FiveOaContractSignMapper fiveOaContractSignMapper;
    @Resource
    HrEmployeeMapper hrEmployeeMapper;
    @Resource
    CommonCodeService commonCodeService;
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
    @Autowired
    ActService actService;
    @Resource
    HandleFormService handleFormService;

    public void remove(int id,String userLogin){
        FiveOaContractSign item = fiveOaContractSignMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");

        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }

    public void update(FiveOaContractSignDto fiveOaContractSignDto){
        FiveOaContractSign model = fiveOaContractSignMapper.selectByPrimaryKey(fiveOaContractSignDto.getId());

        model.setRecordNo(fiveOaContractSignDto.getRecordNo());
        model.setDeptId(fiveOaContractSignDto.getDeptId());
        model.setDeptName(fiveOaContractSignDto.getDeptName());
        model.setClientName(fiveOaContractSignDto.getClientName());
        model.setClientContent(fiveOaContractSignDto.getClientContent());
        model.setContractNo(fiveOaContractSignDto.getContractNo());
        model.setSigner(fiveOaContractSignDto.getSigner());
        model.setSignerName(fiveOaContractSignDto.getSignerName());
        model.setAuditMan(fiveOaContractSignDto.getAuditMan());
        model.setAuditManName(fiveOaContractSignDto.getAuditManName());
        model.setCountersign(fiveOaContractSignDto.getCountersign());
        model.setCountersignName(fiveOaContractSignDto.getCountersignName());
        model.setReviewContractId(fiveOaContractSignDto.getReviewContractId());
        model.setReviewContractName(fiveOaContractSignDto.getReviewContractName());
        model.setSignTime(fiveOaContractSignDto.getSignTime());
        model.setRemark(fiveOaContractSignDto.getRemark());
        model.setGmtModified(new Date());


        Map variables = Maps.newHashMap();
        if ("".equals(model.getCountersign())){
            variables.put("flag",0);
        }else {
            variables.put("flag",1);
            variables.put("companyMen",MyStringUtil.getStringList(model.getCountersign()));//集团公司 -会签
        }
        variables.put("companyLeader", MyStringUtil.getStringList(model.getSigner())); //公司领导 -签发
        myActService.setVariables(model.getProcessInstanceId(),variables);
        BeanValidator.check(model);
        fiveOaContractSignMapper.updateByPrimaryKey(model);

    }

    public FiveOaContractSignDto getModelById(int id){

        return getDto(fiveOaContractSignMapper.selectByPrimaryKey(id));
    }

    public FiveOaContractSignDto getDto(FiveOaContractSign item) {
        FiveOaContractSignDto dto=FiveOaContractSignDto.adapt(item);
        dto.setProcessName("已完成");

        if (!dto.getProcessEnd()){
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");
            if(customProcessInstance == null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveOaContractSignMapper.updateByPrimaryKey(dto);
            }
            if(customProcessInstance != null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }

        return dto;
    }

    public int getNewModel(String userLogin){

        FiveOaContractSign item=new FiveOaContractSign();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);

        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setDrafter(hrEmployeeDto.getUserLogin());
        item.setDrafterName(hrEmployeeDto.getUserName());
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        ModelUtil.setNotNullFields(item);

        fiveOaContractSignMapper.insert(item);
        String businessKey= EdConst.FIVE_OA_CONTRACT_SIGN+ "_" + item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", "业务合同签发单："+item.getDeptName());
        variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(item.getDeptId()));//发起者部门领导

        item.setBusinessKey(businessKey);
        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_CONTRACT_SIGN,item.getBusinessKey(), variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        fiveOaContractSignMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);
         List<Integer> deptIdList=hrEmployeeSysService.getMyDeptListOa(userLogin,uiSref);
         if (deptIdList.size()==0){
            params.put("creator",userLogin);
        }else {
            params.put("deptIdList",deptIdList);
        }
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaContractSignMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaContractSign item=(FiveOaContractSign)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }
    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveOaContractSign item = fiveOaContractSignMapper.selectByPrimaryKey(id);
        data.put("deptName",item.getDeptName());
        data.put("drafterName",item.getDrafterName());
        data.put("clientName",item.getClientName());
        data.put("contractNo",item.getContractNo());
        data.put("signerName",item.getSignerName());
        data.put("auditManName",item.getAuditManName());
        data.put("reviewContractName",item.getReviewContractName());
        data.put("signTime",item.getSignTime());
        data.put("countersignName",item.getCountersignName());
        data.put("clientContent",item.getClientContent());

        data.put("creatorName",item.getCreatorName());
        data.put("gmtCreate",item.getGmtCreate());

        List<ActHistoryDto> actHistoryDtos = actService.listPassedHistoryDto(item.getProcessInstanceId());
        for (ActHistoryDto dto:actHistoryDtos){
            if (dto.getActivityName()==null){
                break;
            }
            if ("发起者".equals(dto.getActivityName())){
                data.put("deptChargeMen",dto);
            }
            if ("部门领导-协同".equals(dto.getActivityName())){
                data.put("deptChargeMen",dto);
            }
            if ("信息化建设与管理部人员（信息化设备）".equals(dto.getActivityName())){
                data.put("informationEquipmentMen",dto);
            }
            if ("网络运维中心网络处理".equals(dto.getActivityName())){
                data.put("computerLeader",dto);
            }
        }
        data.put("nodes",actHistoryDtos);

        return data;
    }
}
