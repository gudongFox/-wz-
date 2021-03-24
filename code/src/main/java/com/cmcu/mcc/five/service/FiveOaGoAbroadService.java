package com.cmcu.mcc.five.service;


import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;

import com.cmcu.common.service.BaseService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;

import com.cmcu.mcc.five.dao.FiveOaGoAbroadMapper;
import com.cmcu.mcc.five.dto.FiveOaGoAbroadDto;
import com.cmcu.mcc.five.entity.FiveOaGoAbroad;
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
public class FiveOaGoAbroadService extends BaseService {

    @Resource
    FiveOaGoAbroadMapper fiveOaGoAbroadMapper;
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
    ActService actService;
    @Resource
    HandleFormService handleFormService;

   public void remove(int id,String userLogin){
       FiveOaGoAbroad item = fiveOaGoAbroadMapper.selectByPrimaryKey(id);
       Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");

       //流程作废
       handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);

   }

   public void update(FiveOaGoAbroadDto item){

       FiveOaGoAbroad model = fiveOaGoAbroadMapper.selectByPrimaryKey(item.getId());
       model.setChargeLeader(item.getChargeLeader());
       model.setChargeLeaderName(item.getChargeLeaderName());
       model.setInstructions( item.getInstructions());
       model.setBusinessChargeLeader(item.getBusinessChargeLeader());
       model.setBusinessChargeLeaderName(item.getBusinessChargeLeaderName());
       model.setDepartmentChargeMen(item.getDepartmentChargeMen());
       model.setDepartmentChargeMenName(item.getDepartmentChargeMenName());
       model.setDrafter(item.getDrafter());
       model.setDrafterName(item.getDrafterName());

       model.setTitle(item.getTitle());
       model.setDepartmentComment(item.getDepartmentComment());
       model.setFlag(item.getFlag());
       model.setSign(item.getSign());
       model.setViceleader(item.getViceleader());
       model.setViceleaderName(item.getViceleaderName());
       model.setGmtModified(new Date());
       BeanValidator.validateObject(model);
       ModelUtil.setNotNullFields(model);
       Map variables = Maps.newHashMap();

      if (item.getDeptId()!=model.getId()){
          variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(model.getDeptId()));
          model.setDeptName(item.getDeptName());
          model.setDeptId(item.getDeptId());
      }
       //副职领导会签
       variables.put("viceLeaders",selectEmployeeService.getOtherDeptChargeMan(model.getDeptId()));

      //董事长 是否批示
       if ("是".contains(model.getSign())){
           variables.put("sign",1);
       }else {
           variables.put("sign",0);
       }

       fiveOaGoAbroadMapper.updateByPrimaryKey(model);
       variables.put("processDescription","因公出国内部审批单："+item.getTitle());
       BeanValidator.check(model);
       myActService.setVariables(model.getProcessInstanceId(),variables);


   }

    public FiveOaGoAbroadDto getModelById(int id){

           return getDto(fiveOaGoAbroadMapper.selectByPrimaryKey(id));
    }

    public FiveOaGoAbroadDto getDto(FiveOaGoAbroad item) {
        FiveOaGoAbroadDto dto=FiveOaGoAbroadDto.adapt(item);
        dto.setProcessName("已完成");

        if (!dto.getProcessEnd()){
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");

            if(customProcessInstance ==null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveOaGoAbroadMapper.updateByPrimaryKey(dto);
            }
            if(customProcessInstance!=null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }
        return dto;
    }

   public int getNewModel(String userLogin){
       FiveOaGoAbroad item=new FiveOaGoAbroad();
       HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);

       item.setDeptId(hrEmployeeDto.getDeptId());
       item.setDeptName(hrEmployeeDto.getDeptName());
       item.setCreator(hrEmployeeDto.getUserLogin());
       item.setCreatorName(hrEmployeeDto.getUserName());

       item.setFlag("否");
       item.setSign("否");
       item.setDeleted(false);
       item.setProcessEnd(false);
       item.setCreator(userLogin);
       item.setGmtModified(new Date());
       item.setGmtCreate(new Date());
       ModelUtil.setNotNullFields(item);
       fiveOaGoAbroadMapper.insert(item);

       String businessKey= EdConst.FIVE_OA_GOABROAD+ "_" + item.getId();
       Map variables = Maps.newHashMap();
       variables.put("userLogin", userLogin);
       variables.put("processDescription","因公出国内部审批单："+item.getCreatorName());
       variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(item.getDeptId()));
       variables.put("hrChargeLeader",selectEmployeeService.getOtherDeptChargeMan(38));
       variables.put("hrDeptChargeMen",selectEmployeeService.getDeptChargeMen(38));
       variables.put("hrDeptGoAbroadUser",selectEmployeeService.getUserListByRoleName("人力资源部人员(出国)-办理"));

       String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_GOABROAD,businessKey, variables, MccConst.APP_CODE);
       item.setProcessInstanceId(processInstanceId);
       item.setBusinessKey(businessKey);
       fiveOaGoAbroadMapper.updateByPrimaryKey(item);
       return item.getId();
   }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);

        Map map =Maps.newHashMap();
        map.put("myDeptData",false);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));

        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaGoAbroadMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaGoAbroad item=(FiveOaGoAbroad)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveOaGoAbroad item = fiveOaGoAbroadMapper.selectByPrimaryKey(id);
        data.put("deptName",item.getDeptName());
        data.put("flag",item.getFlag());
        data.put("sign",item.getSign());
        data.put("chargeLeaderName",item.getChargeLeaderName());
        data.put("instructions",item.getInstructions());
        data.put("businessChargeLeaderName",item.getBusinessChargeLeaderName());
        data.put("departmentChargeMenName",item.getDepartmentChargeMenName());
        data.put("drafterName",item.getDrafterName());
        data.put("title",item.getTitle());
        data.put("departmentComment",item.getDepartmentComment());

        data.put("creatorName",item.getCreatorName());
        data.put("gmtCreate",item.getGmtCreate());

        data.put("nodes",actService.listPassedHistoryDto(item.getProcessInstanceId()));

        return data;
    }

}
