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
import com.cmcu.mcc.dto.ActHistoryDto;
import com.cmcu.mcc.five.dao.FiveOaEmployeeRetireExamineMapper;
import com.cmcu.mcc.five.dto.FiveOaEmployeeRetireExamineDto;
import com.cmcu.mcc.five.entity.FiveOaEmployeeRetireExamine;
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
public class FiveOaEmployeeRetireExamineService {

    @Resource
    FiveOaEmployeeRetireExamineMapper fiveOaEmployeeRetireExamineMapper;
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
    @Autowired
    ActService actService;


   public void remove(int id,String userLogin){
       FiveOaEmployeeRetireExamine item = fiveOaEmployeeRetireExamineMapper.selectByPrimaryKey(id);
       Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");

       //流程作废
       handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);

   }

   public void update(FiveOaEmployeeRetireExamineDto item){

       FiveOaEmployeeRetireExamine model = fiveOaEmployeeRetireExamineMapper.selectByPrimaryKey(item.getId());
       model.setName(item.getName());
       model.setLink(item.getLink());
       model.setLogin(item.getLogin());
       model.setRetireTime(item.getRetireTime());
       model.setRetireDeptName(item.getRetireDeptName());
       model.setRetireDeptId(item.getRetireDeptId());
       model.setDeptId(item.getDeptId());
       model.setDeptName(item.getDeptName());

       model.setGmtModified(item.getGmtModified());
       BeanValidator.validateObject(model);
       ModelUtil.setNotNullFields(model);
       Map variables = Maps.newHashMap();
       List<String> deptChargeMen = new ArrayList<>();
       if(model.getRetireDeptId()!=0) {
           List<String> deptChargeMen1 = selectEmployeeService.getDeptChargeMen(model.getRetireDeptId());
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

       variables.put("processDescription","职工退休审批单："+item.getName());
       variables.put("hrChargeMen", selectEmployeeService.getUserListByRoleName("人力专员"));
       variables.put("financeMen", selectEmployeeService.getUserListByRoleName("财务部门(内部调整)"));
       variables.put("deptChargeMen", deptChargeMen);
       List<String> countSignMen =selectEmployeeService.getUserListByRoleName("人员调整(会签)");
       variables.put("countSignMen",countSignMen);
       myActService.setVariables(model.getProcessInstanceId(),variables);
       BeanValidator.check(model);
       fiveOaEmployeeRetireExamineMapper.updateByPrimaryKey(model);

   }

    public FiveOaEmployeeRetireExamineDto getModelById(int id){

           return getDto(fiveOaEmployeeRetireExamineMapper.selectByPrimaryKey(id));
    }

    public FiveOaEmployeeRetireExamineDto getDto(FiveOaEmployeeRetireExamine item) {
        FiveOaEmployeeRetireExamineDto dto=FiveOaEmployeeRetireExamineDto.adapt(item);
        dto.setProcessName("已完成");

        if (!dto.getProcessEnd()){
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");

            if(customProcessInstance ==null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveOaEmployeeRetireExamineMapper.updateByPrimaryKey(dto);
                //修改退休状态
                HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(item.getLogin());
                hrEmployeeDto.setUserStatus("退休");
                hrEmployeeMapper.updateByPrimaryKey(hrEmployeeDto);
            }
            if(customProcessInstance!=null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }
        return dto;
    }

   public int getNewModel(String userLogin){
       FiveOaEmployeeRetireExamine item=new FiveOaEmployeeRetireExamine();
       HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);

       item.setDeptId(hrEmployeeDto.getDeptId());
      item.setDeptName(hrEmployeeDto.getDeptName());
       item.setCreator(hrEmployeeDto.getUserLogin());
       item.setCreatorName(hrEmployeeDto.getUserName());

       item.setName(hrEmployeeDto.getUserName());
       item.setLink(hrEmployeeDto.getMobile());
       item.setLogin(hrEmployeeDto.getUserLogin());

       item.setDeleted(false);
       item.setProcessEnd(false);
       item.setCreator(userLogin);
       item.setGmtModified(new Date());
       item.setGmtCreate(new Date());
       ModelUtil.setNotNullFields(item);
       fiveOaEmployeeRetireExamineMapper.insert(item);

       String businessKey= EdConst.FIVE_OA_EMPLOYEERETIREEXAMINE+ "_" + item.getId();
       Map variables = Maps.newHashMap();
       variables.put("userLogin", userLogin);
       variables.put("processDescription","职工退休审批单："+item.getCreatorName());

       String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_EMPLOYEERETIREEXAMINE,businessKey, variables, MccConst.APP_CODE);
       item.setProcessInstanceId(processInstanceId);
       item.setBusinessKey(businessKey);
       fiveOaEmployeeRetireExamineMapper.updateByPrimaryKey(item);
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
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaEmployeeRetireExamineMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaEmployeeRetireExamine item=(FiveOaEmployeeRetireExamine)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }


    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveOaEmployeeRetireExamine item = fiveOaEmployeeRetireExamineMapper.selectByPrimaryKey(id);
        data.put("name",item.getName());
        data.put("login",item.getLogin());
        data.put("link",item.getLink());
        data.put("deptName",item.getDeptName());
        data.put("entryDeptName",item.getRetireDeptName());
        data.put("entryTime",item.getRetireTime());

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
