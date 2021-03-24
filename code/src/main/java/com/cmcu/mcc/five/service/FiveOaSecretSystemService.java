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
import com.cmcu.mcc.five.dao.FiveOaSecretSystemMapper;
import com.cmcu.mcc.five.dto.FiveOaSecretSystemDto;
import com.cmcu.mcc.five.entity.FiveOaSecretSystem;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.service.impl.HandleFormService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class FiveOaSecretSystemService extends BaseService {

    @Resource
    FiveOaSecretSystemMapper fiveOaSecretSystemMapper;
    @Resource
    HandleFormService handleFormService;
    @Resource
    ProcessQueryService processQueryService;
    @Resource
    HrEmployeeMapper hrEmployeeMapper;
    @Resource
    TaskHandleService taskHandleService;
    @Resource
    SelectEmployeeService selectEmployeeService;
    @Resource
    MyActService myActService;

    public void remove(int id,String userLogin){
        FiveOaSecretSystem item = fiveOaSecretSystemMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");

        //流程作废
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);

    }


   public void update(FiveOaSecretSystemDto fiveOaSecretSystemDto){
       FiveOaSecretSystem model = fiveOaSecretSystemMapper.selectByPrimaryKey(fiveOaSecretSystemDto.getId());
       model.setApplyUserName(fiveOaSecretSystemDto.getApplyUserName());
       model.setApplyUserLogin(fiveOaSecretSystemDto.getApplyUserLogin());
       model.setApplyUserNo(fiveOaSecretSystemDto.getApplyUserNo());
       model.setDeptId(fiveOaSecretSystemDto.getDeptId());
       model.setDeptName(fiveOaSecretSystemDto.getDeptName());
       model.setPhone(fiveOaSecretSystemDto.getPhone());
       model.setSystemName(fiveOaSecretSystemDto.getSystemName());
       model.setSecretLevel(fiveOaSecretSystemDto.getSecretLevel());
       model.setAccountType(fiveOaSecretSystemDto.getAccountType());
       model.setJurisdictionType(fiveOaSecretSystemDto.getJurisdictionType());
       model.setApplyReason(fiveOaSecretSystemDto.getApplyReason());
       model.setExecuteType(fiveOaSecretSystemDto.getExecuteType());
       model.setExecuteRemark(fiveOaSecretSystemDto.getExecuteRemark());
       model.setRemark(fiveOaSecretSystemDto.getRemark());
       model.setGmtModified(new Date());
       BeanValidator.validateObject(model);
       fiveOaSecretSystemMapper.updateByPrimaryKey(model);
       Map variables = Maps.newHashMap();
       variables.put("deptChargeMen", selectEmployeeService.getParentDeptChargeMen(model.getDeptId(),1,true));//部门负责人
       variables.put("flag",false);
       if (model.getSystemName().contains("安全保密管理平台")&&model.getJurisdictionType().contains("账号")){
           variables.put("flag",true);
           variables.put("humanDeptChargeMen", "2991");//部门负责人
       }

       List<String> copyMenList = selectEmployeeService.getUserListByRoleName("涉密信息系统审计员");
       copyMenList.addAll(selectEmployeeService.getUserListByRoleName("涉密信息系统安全员"));
       copyMenList.stream().distinct();//去除重复
       variables.put("copyMen",  StringUtils.join(copyMenList,","));//抄送 系统安全员 系统审计员
       variables.put("processDescription","涉密信息系统账户及权限开通、变更申请:"+model.getApplyUserName());
       myActService.setVariables(model.getProcessInstanceId(),variables);

   }

    public FiveOaSecretSystemDto getModelById(int id){

        return getDto(fiveOaSecretSystemMapper.selectByPrimaryKey(id));
    }

    public FiveOaSecretSystemDto getDto(FiveOaSecretSystem item) {
        FiveOaSecretSystemDto dto=FiveOaSecretSystemDto.adapt(item);
        dto.setProcessName("已完成");
        if(!dto.getProcessEnd() && StringUtils.isNotEmpty(item.getProcessInstanceId())) {
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");
            if (customProcessInstance==null || customProcessInstance.isFinished()) {
                dto.setProcessEnd(true);
                fiveOaSecretSystemMapper.updateByPrimaryKey(dto);
            }else {
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }
        return dto;
    }


    public int getNewModel(String userLogin){
        FiveOaSecretSystem item=new FiveOaSecretSystem();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);


         item.setApplyUserName(hrEmployeeDto.getUserName());
        item.setApplyUserLogin(hrEmployeeDto.getUserLogin());
        item.setPhone(hrEmployeeDto.getMobile());
        item.setApplyUserNo(hrEmployeeDto.getUserNo());
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());

        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setPhone(hrEmployeeDto.getMobile());

        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        ModelUtil.setNotNullFields(item);
        fiveOaSecretSystemMapper.insert(item);

        String businessKey= EdConst.FIVE_OA_SECRET_SYSTEM+ "_" + item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription","涉密信息系统账户及权限开通、变更申请:"+item.getCreatorName());
        variables.put("secretDeptChargeMen",selectEmployeeService.getParentDeptChargeMen(100,3,false));//保密办公室 正+副
        variables.put("informationDeptChargeMen",selectEmployeeService.getUserListByRoleName("信息化建设与管理部(信息化设备)"));//信息化建设与管理部
        variables.put("networkDeptChargeMen",selectEmployeeService.getParentDeptChargeMen(50,1,false));//网络运维中心负责人
        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_SECRET_SYSTEM,businessKey, variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        item.setBusinessKey(businessKey);
        fiveOaSecretSystemMapper.updateByPrimaryKey(item);
        return item.getId();
    }


    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref, int pageNum, int pageSize) {

        params.put("deleted",false);

        Map map =Maps.newHashMap();
        map.put("myDeptData",false);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));

        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaSecretSystemMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaSecretSystem item=(FiveOaSecretSystem)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveOaSecretSystem item = fiveOaSecretSystemMapper.selectByPrimaryKey(id);
        data.put("deptName",item.getDeptName());


        data.put("creatorName",item.getCreatorName());
        data.put("gmtCreate",item.getGmtCreate());


        return data;
    }
}
