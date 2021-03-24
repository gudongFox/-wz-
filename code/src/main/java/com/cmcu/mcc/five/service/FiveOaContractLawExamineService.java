package com.cmcu.mcc.five.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyFormNo;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.dto.ActHistoryDto;
import com.cmcu.mcc.five.dao.FiveOaContractLawExamineMapper;
import com.cmcu.mcc.five.dto.FiveOaContractLawExamineDto;
import com.cmcu.mcc.five.entity.FiveOaContractLawExamine;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
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
public class FiveOaContractLawExamineService {

    @Resource
    FiveOaContractLawExamineMapper fiveOaContractLawExamineMapper;
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
    ActService actService;
    @Resource
    HandleFormService handleFormService;

   public void remove(int id,String userLogin){
       FiveOaContractLawExamine item = fiveOaContractLawExamineMapper.selectByPrimaryKey(id);
       Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");

       //流程作废
       handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);

   }

   public void update(FiveOaContractLawExamineDto item){
       FiveOaContractLawExamine model = fiveOaContractLawExamineMapper.selectByPrimaryKey(item.getId());
       model.setDeptId(item.getDeptId());
       model.setDeptName(item.getDeptName());
       model.setFormNo(item.getFormNo());
       model.setExamineTime(item.getExamineTime());
       model.setContractNo(item.getContractNo());
       model.setContractProperty(item.getContractProperty());
       model.setFirstParty(item.getFirstParty());
       model.setSecondParty(item.getSecondParty());
       model.setThirdParty(item.getThirdParty());
       model.setFourthParty(item.getFourthParty());
       model.setSubmitMan(item.getSubmitMan());
       model.setSubmitManName(item.getSubmitManName());
       model.setLink(item.getLink());
       model.setProjectName(item.getProjectName());
       model.setContractPrice(item.getContractPrice());
       model.setPerformanceDeadline(item.getPerformanceDeadline());
       model.setProjectAddress(item.getProjectAddress());
       model.setRemark(item.getRemark());
       model.setLawExamineComment(item.getLawExamineComment());
       model.setSubmitDeptLeaderComment(item.getSubmitDeptLeaderComment());
       model.setCompanyLeaderComment(item.getCompanyLeaderComment());
       model.setChangeReason(item.getChangeReason());
       model.setLawAuditComment(item.getLawAuditComment());
       model.setFlag(item.getFlag());

       model.setGmtModified(item.getGmtModified());
       BeanValidator.validateObject(model);
       ModelUtil.setNotNullFields(model);
       Map variables = Maps.newHashMap();
      if (item.getDeptId()!=model.getId()){


          model.setDeptName(item.getDeptName());
          model.setDeptId(item.getDeptId());
      }
       fiveOaContractLawExamineMapper.updateByPrimaryKey(model);
       if ("是".contains(model.getFlag())){
           variables.put("flag",1);
           variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(item.getDeptId()));//部门领导
           variables.put("deptOtherChargeMen",selectEmployeeService.getOtherDeptChargeMan(item.getDeptId()));//分管领导
       }else {
           variables.put("flag",0);
       }
       variables.put("processDescription","合同法律审查工作单："+item.getProjectName());
       BeanValidator.check(model);
       myActService.setVariables(model.getProcessInstanceId(),variables);


   }


    public FiveOaContractLawExamineDto getModelById(int id){

           return getDto(fiveOaContractLawExamineMapper.selectByPrimaryKey(id));
    }

    public FiveOaContractLawExamineDto getDto(FiveOaContractLawExamine item) {
        FiveOaContractLawExamineDto dto= FiveOaContractLawExamineDto.adapt(item);
        dto.setProcessName("已完成");

        if (!dto.getProcessEnd()){
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");
            if(customProcessInstance == null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveOaContractLawExamineMapper.updateByPrimaryKey(dto);
            }
            if(customProcessInstance != null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }

        return dto;
    }

   public int getNewModel(String userLogin){
       FiveOaContractLawExamine item=new FiveOaContractLawExamine();
       HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);

       item.setDeptId(hrEmployeeDto.getDeptId());
       item.setDeptName(hrEmployeeDto.getDeptName());
       item.setCreator(hrEmployeeDto.getUserLogin());
       item.setCreatorName(hrEmployeeDto.getUserName());
       item.setContractProperty(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"公司合同性质").toString());
       item.setFlag("否");
       item.setDeleted(false);
       item.setProcessEnd(false);
       item.setCreator(userLogin);
       item.setGmtModified(new Date());
       item.setGmtCreate(new Date());
       ModelUtil.setNotNullFields(item);
       fiveOaContractLawExamineMapper.insert(item);
       Map variables = Maps.newHashMap();
       String businessKey= EdConst.FIVE_OA_CONTRACTLAWEXAMINE+ "_" + item.getId();

       variables.put("userLogin", userLogin);
       variables.put("processDescription","合同法律审查工作单："+item.getCreatorName());
       variables.put("legalReviewMen", selectEmployeeService.getUserListByRoleName("法律审查"));//法律审查
       variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(item.getDeptId()));//部门领导
       variables.put("deptOtherChargeMen",selectEmployeeService.getOtherDeptChargeMan(item.getDeptId()));//分管领导
       String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_CONTRACTLAWEXAMINE,businessKey, variables, MccConst.APP_CODE);
       item.setProcessInstanceId(processInstanceId);
       item.setBusinessKey(businessKey);
       item.setFormNo(MyFormNo.getFormNo(taskHandleService.getDeploymentId(EdConst.FIVE_OA_CONTRACTLAWEXAMINE),item.getId()));

       fiveOaContractLawExamineMapper.updateByPrimaryKey(item);
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
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaContractLawExamineMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaContractLawExamine item=(FiveOaContractLawExamine)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }
    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveOaContractLawExamine item = fiveOaContractLawExamineMapper.selectByPrimaryKey(id);
        data.put("formNo",item.getFormNo());
        data.put("contractNo",item.getContractNo());
        data.put("deptName",item.getDeptName());
        data.put("flag",item.getFlag());
        data.put("firstParty",item.getFirstParty());
        data.put("secondParty",item.getSecondParty());
        data.put("thirdParty",item.getThirdParty());
        data.put("fourthParty",item.getFourthParty());
        data.put("submitManName",item.getSubmitManName());
        data.put("link",item.getLink());
        data.put("projectName",item.getProjectName());
        data.put("projectAddress",item.getProjectAddress());
        data.put("performanceDeadline",item.getPerformanceDeadline());
        data.put("examineTime",item.getExamineTime());


        data.put("changeReason",item.getChangeReason());
        //data.put("lawAuditComment",item.getLawAuditComment());
        //data.put("submitDeptLeaderComment",item.getSubmitDeptLeaderComment());
        // data.put("companyLeaderComment",item.getCompanyLeaderComment());

        data.put("creatorName",item.getCreatorName());
        data.put("gmtCreate",item.getGmtCreate());
        List<ActHistoryDto> actHistoryDtos = actService.listPassedHistoryDto(item.getProcessInstanceId());

        for (ActHistoryDto dto:actHistoryDtos){
            if (dto.getActivityName()==null){
                break;
            }
            if (dto.getActivityName().contains("法律审查-审批")){
                data.put("lawExamineComment",dto.getComment());
            }
            if (dto.getActivityName().contains("部门领导-审批")){
                data.put("submitDeptLeaderComment",dto.getComment());
            }
            if (dto.getActivityName().contains("分管领导-审批")){
                data.put("companyLeaderComment",dto.getComment());
            }
            if (dto.getActivityName().contains("法律审查归档")){
                data.put("lawAuditComment",dto.getComment());
            }
        }
        data.put("nodes",actHistoryDtos);

        return data;
    }


}
