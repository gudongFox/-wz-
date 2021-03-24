package com.cmcu.mcc.five.service;


import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;

import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;


import com.cmcu.mcc.five.dao.FiveOaRulelawexamineMapper;
import com.cmcu.mcc.five.dto.FiveOaRulelawexamineDto;
import com.cmcu.mcc.five.entity.FiveOaRulelawexamine;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
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
public class FiveOaRuleLawExamineService {

    @Resource
    FiveOaRulelawexamineMapper fiveOaRulelawexamineMapper;
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
    @Resource
    HandleFormService handleFormService;


   public void remove(int id,String userLogin){
       FiveOaRulelawexamine item = fiveOaRulelawexamineMapper.selectByPrimaryKey(id);
       Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");

       //流程作废
       handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);

   }

   public void update(FiveOaRulelawexamineDto item){

       FiveOaRulelawexamine model = fiveOaRulelawexamineMapper.selectByPrimaryKey(item.getId());

       model.setDeptId(item.getDeptId());
       model.setDeptName(item.getDeptName());
       model.setFormNo(item.getFormNo());
       model.setSendMan(item.getSendMan());
       model.setSendManName(item.getSendManName());
       model.setRuleName(item.getRuleName());
       model.setRuleDescription(item.getRuleDescription());
       model.setLawName(item.getLawName());
       model.setLawExamine(item.getLawExamine());
       model.setChangeReason(item.getChangeReason());
       model.setSendDeptLeaderComment(item.getSendDeptLeaderComment());
       model.setCounselorComment(item.getCounselorComment());
       model.setSendTime(item.getSendTime());
       model.setRemark(item.getRemark());

       model.setGmtModified(item.getGmtModified());
       BeanValidator.validateObject(model);
       ModelUtil.setNotNullFields(model);
       Map variables = Maps.newHashMap();
      if (item.getDeptId()!=model.getId()){
          variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(model.getDeptId()));//部门领导
          variables.put("deptOtherChargeMen",selectEmployeeService.getOtherDeptChargeMan(model.getDeptId()));//分管领导
          model.setDeptName(item.getDeptName());
          model.setDeptId(item.getDeptId());
      }
       fiveOaRulelawexamineMapper.updateByPrimaryKey(model);

       variables.put("processDescription","规章制度法律审查工作单:"+item.getRuleName());
       BeanValidator.check(model);
       myActService.setVariables(model.getProcessInstanceId(),variables);


   }


    public FiveOaRulelawexamineDto getModelById(int id){

           return getDto(fiveOaRulelawexamineMapper.selectByPrimaryKey(id));
    }

    public FiveOaRulelawexamineDto getDto(FiveOaRulelawexamine item) {
        FiveOaRulelawexamineDto dto = FiveOaRulelawexamineDto.adapt(item);
        dto.setProcessName("已完成");
        if (!dto.getProcessEnd()) {
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");

            if (customProcessInstance == null || customProcessInstance.isFinished()) {
                dto.setProcessEnd(true);
                fiveOaRulelawexamineMapper.updateByPrimaryKey(dto);
            } else {
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }
        return dto;
    }

   public int getNewModel(String userLogin){
       FiveOaRulelawexamine item=new FiveOaRulelawexamine();
       HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);

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
       fiveOaRulelawexamineMapper.insert(item);

       String businessKey= EdConst.FIVE_OA_RULELAWEXAMINE+ "_" + item.getId();
       Map variables = Maps.newHashMap();
       variables.put("userLogin", userLogin);
       variables.put("processDescription","规章制度法律审查工作单:"+item.getCreatorName());
       variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(item.getDeptId()));
       variables.put("legalReviewMan",selectEmployeeService.getUserListByRoleName("法律审查"));//法律审查 ：纪检工作部 部门负责人
       variables.put("deptOtherChargeMen",selectEmployeeService.getOtherDeptChargeMan(item.getDeptId()));//公司分管领导
       String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_RULELAWEXAMINE,businessKey, variables, MccConst.APP_CODE);
       item.setProcessInstanceId(processInstanceId);
       item.setBusinessKey(businessKey);
       fiveOaRulelawexamineMapper.updateByPrimaryKey(item);
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
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaRulelawexamineMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaRulelawexamine item=(FiveOaRulelawexamine)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }



}
