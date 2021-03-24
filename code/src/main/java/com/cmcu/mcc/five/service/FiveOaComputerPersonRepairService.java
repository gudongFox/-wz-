package com.cmcu.mcc.five.service;


import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.five.dao.FiveOaComputerPersonRepairMapper;
import com.cmcu.mcc.five.dto.FiveOaComputerPersonRepairDto;
import com.cmcu.mcc.five.entity.FiveOaComputerPersonRepair;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FiveOaComputerPersonRepairService extends BaseService {

    @Resource
    FiveOaComputerPersonRepairMapper fiveOaComputerPersonRepairMapper;
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
    @Resource
    CommonCodeService commonCodeService;


    public void remove(int id,String userLogin){
        FiveOaComputerPersonRepair item = fiveOaComputerPersonRepairMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");

        //流程作废
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }

    public void update(FiveOaComputerPersonRepairDto item){

        FiveOaComputerPersonRepair model = fiveOaComputerPersonRepairMapper.selectByPrimaryKey(item.getId());
        model.setFormNo(item.getFormNo());
        model.setDeptName(item.getDeptName());
        model.setDeptId(item.getDeptId());
        model.setDutyPersonId(item.getDutyPersonId());
        model.setDutyPersonName(item.getDutyPersonName());
        model.setDeviceNo(item.getDeviceNo());
        model.setDeviceName(item.getDeviceName());
        model.setDeviceSecurity(item.getDeviceSecurity());
        model.setDeviceNo(item.getDeviceNo());
        model.setDeviceName(item.getDeviceName());
        model.setRepairMan(item.getRepairMan());
        model.setRepairManName(item.getRepairManName());
        model.setRepairIdCard(item.getRepairIdCard());
        model.setRepairTel(item.getRepairTel());
        model.setReason(item.getReason());
        model.setSolveWay(item.getSolveWay());
        model.setResult(item.getResult());
        model.setChangeIs(item.getChangeIs());
        model.setRemovelSituation(item.getRemovelSituation());
        model.setChangeSituation(item.getChangeSituation());
        model.setRepairMan(item.getRepairMan());
        model.setRepairManName(item.getRepairManName());
        model.setAccompanyMan(item.getAccompanyMan());
        model.setAccompanyManName(item.getAccompanyManName());
        model.setDeviceSecurity(item.getDeviceSecurity());
        model.setRemovelMan(item.getRemovelMan());
        model.setRemovelManName(item.getRemovelManName());
        model.setRemark(item.getRemark());
        model.setGmtModified(new Date());
        BeanValidator.validateObject(model);
        ModelUtil.setNotNullFields(model);

        Map variables = Maps.newHashMap();
/*        variables.put("processAuditMan",selectEmployeeService.getUserListByRoleName("网络运维中心网络处理"));
        variables.put("deptChargeList",selectEmployeeService.getDeptAllChargeMen(model.getDeptId()));
        variables.put("informationDeptMan",selectEmployeeService.getUserListByRoleName("信息化建设与管理部(信息化设备)"));
        variables.put("dutyMan",model.getDutyPersonId());*/
/*        List<String> attributeList=new ArrayList<>();
        if(selectEmployeeService.getDeptChargeMen(model.getDeptId())!=null){


        }*/
/*        String copyMen="2887,1983";
        if ("涉m网".equals(model.getDeviceLevel())){
            variables.put("sign",1);
            variables.put("deptSecurityMenList", MyStringUtil.getStringList(model.getDeptSecurityMan()));
            copyMen=copyMen+",2786";
        }
        variables.put("copyMen",copyMen);//MyStringUtil.getStringList(copyMen)*/
        fiveOaComputerPersonRepairMapper.updateByPrimaryKey(model);
        BeanValidator.check(model);
        myActService.setVariables(model.getProcessInstanceId(),variables);
    }


    public FiveOaComputerPersonRepairDto getModelById(int id){

        return getDto(fiveOaComputerPersonRepairMapper.selectByPrimaryKey(id));
    }

    public FiveOaComputerPersonRepairDto getDto(FiveOaComputerPersonRepair item) {
        FiveOaComputerPersonRepairDto dto=FiveOaComputerPersonRepairDto.adapt(item);
        dto.setProcessName("已完成");

        if (!dto.getProcessEnd()){
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");
            //dto.setAttendUser(myHistoryService.getAttendUser(dto.getProcessInstanceId()));
            if(customProcessInstance == null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveOaComputerPersonRepairMapper.updateByPrimaryKey(dto);
            }
            if(customProcessInstance != null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }

        dto.setBusinessKey(EdConst.FIVE_OA_DEPATRMENTPOST+ "_" + item.getId());
        return dto;
    }

    public int getNewModel(String userLogin){
        FiveOaComputerPersonRepair item=new FiveOaComputerPersonRepair();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        item.setDutyPersonId(hrEmployeeDto.getUserLogin());
        item.setDutyPersonName(hrEmployeeDto.getUserName());
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setDeviceSecurity(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"项目密级").toString());
        item.setChangeIs("否");
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        ModelUtil.setNotNullFields(item);
        fiveOaComputerPersonRepairMapper.insert(item);

        String businessKey= EdConst.FIVE_OA_COMPUTER_PERSON_REPAIR+ "_" + item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription","信息化设备外部人员现场维修情况记录表："+item.getCreatorName());


        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_COMPUTER_PERSON_REPAIR,businessKey, variables,
                MccConst.APP_CODE);
        item.setBusinessKey(businessKey);
        item.setProcessInstanceId(processInstanceId);
        fiveOaComputerPersonRepairMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);
         /*List<Integer> deptIdList=hrEmployeeSysService.getMyDeptListOa(userLogin,uiSref);
         if (deptIdList.size()==0){
            params.put("creator",userLogin);
        }else {
            params.put("deptIdList",deptIdList);
        }*/
        //如果有数据权限判断数据权限  myDeptData true查看当前部门 false查看创建人
        Map map =new HashMap();
        map.put("myDeptData",false);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));

        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaComputerPersonRepairMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaComputerPersonRepair item=(FiveOaComputerPersonRepair)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveOaComputerPersonRepair item = fiveOaComputerPersonRepairMapper.selectByPrimaryKey(id);
        data.put("deptName",item.getDeptName());
/*        data.put("deviceRoom",item.getDeviceRoom());
        data.put("deviceNo",item.getDeviceNo());
        data.put("deviceName",item.getDeviceName());
        data.put("deviceLevel",item.getDeviceLevel());
        data.put("deviceType",item.getDeviceType());
        data.put("chargeMan",item.getChargeMan());
        data.put("chargeNo",item.getChargeNo());
        data.put("chargeTel",item.getChargeTel());
        data.put("repairTime",item.getRepairTime());
        data.put("chargeComment",item.getChargeComment());
        data.put("computerComment",item.getComputerComment());
        data.put("PersonRepairManName",item.getPersonRepairManName());
        data.put("PersonRepairTime",item.getPersonRepairTime());
        data.put("receiveManName",item.getReceiveManName());
        data.put("receiveTime",item.getReceiveTime());
        data.put("comment",item.getComment());*/
        data.put("remark",item.getRemark());

        data.put("creatorName",item.getCreatorName());
        data.put("gmtCreate",item.getGmtCreate());

        data.put("nodes",actService.listPassedHistoryDto(item.getProcessInstanceId()));

        return data;
    }
}

