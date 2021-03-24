package com.cmcu.mcc.five.service;


import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.five.dao.FiveOaInstitutionCountSignMapper;
import com.cmcu.mcc.five.dto.FiveOaInstitutionCountSignDto;
import com.cmcu.mcc.five.entity.FiveOaInstitutionCountSign;
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
public class FiveOaInstitutionCountSignService {

    @Resource
    FiveOaInstitutionCountSignMapper fiveOaInstitutionCountSignMapper;
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
    @Resource
    HandleFormService handleFormService;


   public void remove(int id,String userLogin){
       FiveOaInstitutionCountSign item = fiveOaInstitutionCountSignMapper.selectByPrimaryKey(id);
       Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");

       //流程作废
       handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);

   }

   public void update(FiveOaInstitutionCountSignDto item){

       FiveOaInstitutionCountSign model = fiveOaInstitutionCountSignMapper.selectByPrimaryKey(item.getId());
       model.setDeptId(item.getDeptId());
       model.setDeptName(item.getDeptName());
       model.setFormNo(item.getFormNo());
       model.setInstitutionType(item.getInstitutionType());
       model.setInstitutionName(item.getInstitutionName());
       model.setLawExamine(item.getLawExamine());
       model.setProcessExamine(item.getProcessExamine());
       model.setManagerProductExamine(item.getManagerProductExamine());
       model.setDeptChargeMan(item.getDeptChargeMan());
       model.setDeptChargeManName(item.getDeptChargeManName());

       model.setAgent(item.getAgent());
       model.setAgentName(item.getAgentName());

       model.setSubmitTime(item.getSubmitTime());
       model.setRemark(item.getRemark());

       model.setGmtModified(item.getGmtModified());
       BeanValidator.validateObject(model);
       ModelUtil.setNotNullFields(model);
       Map variables = Maps.newHashMap();
      if (item.getDeptId()!=model.getId()){
          variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(model.getDeptId()));
          model.setDeptName(item.getDeptName());
          model.setDeptId(item.getDeptId());
      }
       fiveOaInstitutionCountSignMapper.updateByPrimaryKey(model);
       variables.put("flag",1);//流程审查 法律审查标志 ：1 启用 0 不启用
       variables.put("text",1);//制度文本审查标志
       variables.put("processDescription",item.getInstitutionName());
       BeanValidator.check(model);
       myActService.setVariables(model.getProcessInstanceId(),variables);


   }


    public FiveOaInstitutionCountSignDto getModelById(int id){

           return getDto(fiveOaInstitutionCountSignMapper.selectByPrimaryKey(id));
    }

    public FiveOaInstitutionCountSignDto getDto(FiveOaInstitutionCountSign item) {
        FiveOaInstitutionCountSignDto dto=FiveOaInstitutionCountSignDto.adapt(item);
        dto.setProcessName("已完成");
        if (!dto.getProcessEnd()) {

            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");

            if(customProcessInstance ==null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveOaInstitutionCountSignMapper.updateByPrimaryKey(dto);
            }
            if(customProcessInstance!=null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }
        dto.setBusinessKey(EdConst.FIVE_OA_DEPATRMENTPOST+ "_" + item.getId());
        return dto;
    }

   public int getNewModel(String userLogin){
       FiveOaInstitutionCountSign item=new FiveOaInstitutionCountSign();
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
       fiveOaInstitutionCountSignMapper.insert(item);

       String businessKey= EdConst.FIVE_OA_INSTITUTIONCOUNTSIGN+ "_" + item.getId();
       Map variables = Maps.newHashMap();
       variables.put("userLogin", userLogin);
       variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(item.getDeptId()));

       String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_INSTITUTIONCOUNTSIGN,businessKey, variables, MccConst.APP_CODE);
       item.setProcessInstanceId(processInstanceId);
       item.setBusinessKey(businessKey);
       fiveOaInstitutionCountSignMapper.updateByPrimaryKey(item);
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
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaInstitutionCountSignMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaInstitutionCountSign item=(FiveOaInstitutionCountSign)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }



}
