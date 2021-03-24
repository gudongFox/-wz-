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
import com.cmcu.mcc.five.dao.FiveOaEmployeeEntryExamineMapper;
import com.cmcu.mcc.five.dto.FiveOaEmployeeEntryExamineDto;
import com.cmcu.mcc.five.entity.FiveOaEmployeeEntryExamine;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dao.HrEmployeeSysMapper;
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
public class FiveOaEmployeeEntryExamineService {

    @Resource
    FiveOaEmployeeEntryExamineMapper fiveOaEmployeeEntryExamineMapper;
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
    HrEmployeeSysMapper hrEmployeeSysMapper;
    @Resource
    HandleFormService handleFormService;
    @Autowired
    ActService actService;


   public void remove(int id,String userLogin){
       FiveOaEmployeeEntryExamine item = fiveOaEmployeeEntryExamineMapper.selectByPrimaryKey(id);
       Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");

       //流程作废
       handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);

   }

   public void update(FiveOaEmployeeEntryExamineDto item){
       Assert.state(item.getEntryDeptId()!=0,"请选择调入后单位");
       FiveOaEmployeeEntryExamine model = fiveOaEmployeeEntryExamineMapper.selectByPrimaryKey(item.getId());

       Map params = Maps.newHashMap();
       params.put("userLogin", item.getLogin());
       Assert.state(hrEmployeeSysMapper.selectAll(params).size()==0,"该用户名已被其他人占用!");

       model.setName(item.getName());
       model.setLink(item.getLink());
       model.setLogin(item.getLogin());
       model.setEntryTime(item.getEntryTime());
       model.setEntryDeptName(item.getEntryDeptName());
       model.setEntryDeptId(item.getEntryDeptId());
       model.setDeptId(item.getDeptId());
       model.setDeptName(item.getDeptName());

       model.setGmtModified(item.getGmtModified());
       BeanValidator.validateObject(model);
       ModelUtil.setNotNullFields(model);
       Map variables = Maps.newHashMap();


       variables.put("deptChargeMen", selectEmployeeService.getDeptChargeMen(model.getEntryDeptId()));

       fiveOaEmployeeEntryExamineMapper.updateByPrimaryKey(model);
       variables.put("processDescription","职工入职审批单："+item.getName());
       List<String> countSignMen =selectEmployeeService.getUserListByRoleName("人员调整(会签)");

       variables.put("countSignMen",countSignMen);
       BeanValidator.check(model);
       myActService.setVariables(model.getProcessInstanceId(),variables);


   }

    public FiveOaEmployeeEntryExamineDto getModelById(int id){

           return getDto(fiveOaEmployeeEntryExamineMapper.selectByPrimaryKey(id));
    }

    public FiveOaEmployeeEntryExamineDto getDto(FiveOaEmployeeEntryExamine item) {
        FiveOaEmployeeEntryExamineDto dto=FiveOaEmployeeEntryExamineDto.adapt(item);
        dto.setProcessName("已完成");

        if (!dto.getProcessEnd()){
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");

            if(customProcessInstance ==null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveOaEmployeeEntryExamineMapper.updateByPrimaryKey(dto);

                //新增员工
                String userLogin = item.getLogin();
                String userName = item.getName();
                int deptId = item.getEntryDeptId();
                String userType = (String) commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"员工类型");
                hrEmployeeSysService.insert(userLogin,userName,deptId,userType);

            }
            if(customProcessInstance!=null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }
        return dto;
    }

   public int getNewModel(String userLogin){
       FiveOaEmployeeEntryExamine item=new FiveOaEmployeeEntryExamine();
       HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);

       item.setDeptId(hrEmployeeDto.getDeptId());
       item.setDeptName(hrEmployeeDto.getDeptName());
       item.setCreator(hrEmployeeDto.getUserLogin());
       item.setCreatorName(hrEmployeeDto.getUserName());

       //item.setName(hrEmployeeDto.getUserName());
       //item.setLink(hrEmployeeDto.getMobile());
       //item.setLogin(hrEmployeeDto.getUserLogin());

       item.setDeleted(false);
       item.setProcessEnd(false);
       item.setCreator(userLogin);
       item.setGmtModified(new Date());
       item.setGmtCreate(new Date());
       ModelUtil.setNotNullFields(item);
       fiveOaEmployeeEntryExamineMapper.insert(item);

       String businessKey= EdConst.FIVE_OA_EMPLOYEEENTRYEXAMINE+ "_" + item.getId();
       Map variables = Maps.newHashMap();
       variables.put("userLogin", userLogin);
       variables.put("processDescription","职工入职审批单："+item.getCreatorName());

       String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_EMPLOYEEENTRYEXAMINE,businessKey, variables, MccConst.APP_CODE);
       item.setProcessInstanceId(processInstanceId);
       item.setBusinessKey(businessKey);
       fiveOaEmployeeEntryExamineMapper.updateByPrimaryKey(item);
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
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaEmployeeEntryExamineMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaEmployeeEntryExamine item=(FiveOaEmployeeEntryExamine)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveOaEmployeeEntryExamine item = fiveOaEmployeeEntryExamineMapper.selectByPrimaryKey(id);
        data.put("name",item.getName());
        data.put("login",item.getLogin());
        data.put("link",item.getLink());
        data.put("deptName",item.getDeptName());
        data.put("entryDeptName",item.getEntryDeptName());
        data.put("entryTime",item.getEntryTime());

        data.put("creatorName",item.getCreatorName());
        data.put("gmtCreate",item.getGmtCreate());

/*        List<String> countSignMen =selectEmployeeService.getUserListByRoleName("人员调整(会签)");
        //党群工作部
        List<HrEmployeeDto> hr=new ArrayList<>();
        //信息化建设与管理部
        //档案图书室
        for(String userLogin:countSignMen){
            HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLogin(userLogin);
            if(hrEmployeeDto.getDeptName().equals("党群工作部")){
                hr.add(hrEmployeeDto);
            }
        }*/

        List<ActHistoryDto> actHistoryDtos = actService.listPassedHistoryDto(item.getProcessInstanceId());
        for (ActHistoryDto dto:actHistoryDtos){
            if (dto.getActivityName()==null){
                break;
            }
            if ("人力专员发起".equals(dto.getActivityName())){
                data.put("startMen",dto);
            }
            if ("人员调整会签".equals(dto.getActivityName())){
                data.put("deptChargeMen",dto);
            }
            if ("财务部门（入职）".equals(dto.getActivityName())){
                data.put("financeDepartmentMen",dto);
            }
            if ("单位第一负责人".equals(dto.getActivityName())){
                data.put("companyPrincipal",dto);
            }
        }
        data.put("nodes",actHistoryDtos);

        return data;
    }


}
