package com.cmcu.mcc.five.service;


import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.dto.ActHistoryDto;
import com.cmcu.mcc.five.dao.FiveOaEmployeeTransferExamineMapper;
import com.cmcu.mcc.five.dto.FiveOaEmployeeTransferExamineDto;
import com.cmcu.mcc.five.entity.FiveOaEmployeeTransferExamine;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class FiveOaEmployeeTransferExamineService {

    @Resource
    FiveOaEmployeeTransferExamineMapper fiveOaEmployeeTransferExamineMapper;
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
    @Autowired
    ActService actService;



    public void remove(int id,String userLogin){
       FiveOaEmployeeTransferExamine item = fiveOaEmployeeTransferExamineMapper.selectByPrimaryKey(id);
       Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");

       //流程作废
       handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);

   }

   public void update(FiveOaEmployeeTransferExamineDto item){
       Assert.state(item.getTransferDeptId()!=0,"请选择调入后单位");
       Assert.state(item.getDeptId()!=0,"请选择调入前单位");

       FiveOaEmployeeTransferExamine model = fiveOaEmployeeTransferExamineMapper.selectByPrimaryKey(item.getId());
       model.setName(item.getName());
       model.setLink(item.getLink());
       model.setLogin(item.getLogin());
       model.setTransferTime(item.getTransferTime());
       model.setTransferDeptName(item.getTransferDeptName());
       model.setTransferDeptId(item.getTransferDeptId());
       model.setDeptId(item.getDeptId());
       model.setDeptName(item.getDeptName());

       model.setGmtModified(item.getGmtModified());
       BeanValidator.validateObject(model);
       ModelUtil.setNotNullFields(model);
       Map variables = Maps.newHashMap();
       List<String> deptChargeMen = new ArrayList<>();

       if(model.getTransferDeptId()!=0) {
           List<String> deptChargeMen1 = selectEmployeeService.getDeptChargeMen(model.getTransferDeptId());
           for(String man:deptChargeMen1) {
               deptChargeMen.add(man);
           }
       }
       if(model.getDeptId()!=0) {
           List<String> deptChargeMen2 = selectEmployeeService.getDeptChargeMen(model.getDeptId());
           for(String man:deptChargeMen2) {
               deptChargeMen.add(man);
           }
       }

       variables.put("processDescription","职工内部调整审批单："+item.getName());
       variables.put("hrChargeMen", selectEmployeeService.getUserListByRoleName("人力专员"));
       variables.put("financeMen", selectEmployeeService.getUserListByRoleName("财务部门(内部调整)"));
       variables.put("deptChargeMen", deptChargeMen);
       List<String> countSignMen =selectEmployeeService.getUserListByRoleName("人员调整(会签)");
       variables.put("countSignMen",countSignMen);
       myActService.setVariables(model.getProcessInstanceId(),variables);
       BeanValidator.check(model);
       fiveOaEmployeeTransferExamineMapper.updateByPrimaryKey(model);

   }

    public FiveOaEmployeeTransferExamineDto getModelById(int id){

           return getDto(fiveOaEmployeeTransferExamineMapper.selectByPrimaryKey(id));
    }

    public FiveOaEmployeeTransferExamineDto getDto(FiveOaEmployeeTransferExamine item) {
        FiveOaEmployeeTransferExamineDto dto=FiveOaEmployeeTransferExamineDto.adapt(item);
        dto.setProcessName("已完成");

        if (!dto.getProcessEnd()){
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");

            if(customProcessInstance ==null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveOaEmployeeTransferExamineMapper.updateByPrimaryKey(dto);
                //部门 变动
                HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(item.getLogin());
                hrEmployeeDto.setDeptId(item.getTransferDeptId());
                hrEmployeeMapper.updateByPrimaryKey(hrEmployeeDto);
            }
            if(customProcessInstance!=null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());

            }
        }
        return dto;
    }

   public int getNewModel(String userLogin){
       FiveOaEmployeeTransferExamine item=new FiveOaEmployeeTransferExamine();
       HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);

       item.setName(hrEmployeeDto.getUserName());
       item.setLink(hrEmployeeDto.getMobile());
       item.setLogin(hrEmployeeDto.getUserLogin());

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
       fiveOaEmployeeTransferExamineMapper.insert(item);

       String businessKey= EdConst.FIVE_OA_EMPLOYEETRANSFEREXAMINE+ "_" + item.getId();
       Map variables = Maps.newHashMap();
       variables.put("userLogin", userLogin);
       variables.put("processDescription","职工内部调整审批单："+item.getCreatorName());

       String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_EMPLOYEETRANSFEREXAMINE,businessKey, variables, MccConst.APP_CODE);
       item.setProcessInstanceId(processInstanceId);
       item.setBusinessKey(businessKey);
       fiveOaEmployeeTransferExamineMapper.updateByPrimaryKey(item);
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
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaEmployeeTransferExamineMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaEmployeeTransferExamine item=(FiveOaEmployeeTransferExamine)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }


    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveOaEmployeeTransferExamine item = fiveOaEmployeeTransferExamineMapper.selectByPrimaryKey(id);
        data.put("name",item.getName());
        data.put("login",item.getLogin());
        data.put("link",item.getLink());
        data.put("deptName",item.getDeptName());
        data.put("entryDeptName",item.getTransferDeptName());
        data.put("entryTime",item.getTransferTime());

        data.put("creatorName",item.getCreatorName());
        data.put("gmtCreate",item.getGmtCreate());

        List<ActHistoryDto> actHistoryDtos = actService.listPassedHistoryDto(item.getProcessInstanceId());
        for (ActHistoryDto dto:actHistoryDtos){
            if (dto.getActivityName()==null){
                break;
            }
            if ("人力专员发起".equals(dto.getActivityName())){
                data.put("startMen",dto);
            }
            if ("人员调整(会签)".equals(dto.getActivityName())){
                data.put("deptChargeMen",dto);
            }
            if ("财务部门（内部调整）".equals(dto.getActivityName())){
                data.put("financeDepartmentMen",dto);
            }
            if ("各单位第一负责人".equals(dto.getActivityName())){
                data.put("companyPrincipal",dto);
            }
        }
        data.put("nodes",actHistoryDtos);

        return data;
    }
}
