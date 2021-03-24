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
import com.cmcu.mcc.five.dao.FiveOaComputerMaintainMapper;
import com.cmcu.mcc.five.dto.FiveOaComputerMaintainDto;
import com.cmcu.mcc.five.entity.FiveOaComputerMaintain;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FiveOaComputerMaintainService extends BaseService {

    @Resource
    FiveOaComputerMaintainMapper fiveOaComputerMaintainMapper;
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
        FiveOaComputerMaintain item = fiveOaComputerMaintainMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");

        //流程作废
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }

    public void update(FiveOaComputerMaintainDto item){

        FiveOaComputerMaintain model = fiveOaComputerMaintainMapper.selectByPrimaryKey(item.getId());
        model.setFormNo(item.getFormNo());
        model.setDeptName(item.getDeptName());
        model.setDeptId(item.getDeptId());
        model.setMaintainMan(item.getMaintainMan());
        model.setMaintainManName(item.getMaintainManName());
        model.setMaintainTime(item.getMaintainTime());
        model.setMaintainAdvice(item.getMaintainAdvice());
        model.setFailureCause(item.getFailureCause());
        model.setRepairTime(item.getRepairTime());
        model.setDeviceRoom(item.getDeviceRoom());
        model.setDeviceNo(item.getDeviceNo());
        model.setDeviceName(item.getDeviceName());
        model.setDeviceLevel(item.getDeviceLevel());
        model.setDeviceType(item.getDeviceType());
        model.setChargeMan(item.getChargeMan());
        model.setChargeName(item.getChargeName());
        model.setChargeNo(item.getChargeNo());
        model.setChargeTel(item.getChargeTel());
        model.setChargeComment(item.getChargeComment());
        model.setReceiveMan(item.getReceiveMan());
        model.setReceiveManName(item.getReceiveManName());
        model.setReceiveTime(item.getReceiveTime());
        model.setComputerComment(item.getComputerComment());
        model.setComment(item.getComment());
        model.setDeptSecurityMan(item.getDeptSecurityMan());
        model.setDeptSecurityManName(item.getDeptSecurityManName());
        model.setRemark(item.getRemark());
        model.setGmtModified(new Date());
        model.setOtherContent(item.getOtherContent());
        model.setErrorDescription(item.getErrorDescription());
        BeanValidator.validateObject(model);
        ModelUtil.setNotNullFields(model);

        Map variables = Maps.newHashMap();
        variables.put("processAuditMan",selectEmployeeService.getUserListByRoleName("网络运维中心网络处理"));
        variables.put("dutyMan",model.getMaintainMan());
        variables.put("deptChargeMen",selectEmployeeService.getParentDeptChargeMen(model.getDeptId(),2,true));

        fiveOaComputerMaintainMapper.updateByPrimaryKey(model);
        BeanValidator.check(model);
        myActService.setVariables(model.getProcessInstanceId(),variables);
    }


    public FiveOaComputerMaintainDto getModelById(int id){

        return getDto(fiveOaComputerMaintainMapper.selectByPrimaryKey(id));
    }

    public FiveOaComputerMaintainDto getDto(FiveOaComputerMaintain item) {
        FiveOaComputerMaintainDto dto=FiveOaComputerMaintainDto.adapt(item);
        dto.setProcessName("已完成");

        if (!dto.getProcessEnd()){
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");
            //dto.setAttendUser(myHistoryService.getAttendUser(dto.getProcessInstanceId()));
            if(customProcessInstance == null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveOaComputerMaintainMapper.updateByPrimaryKey(dto);
            }
            if(customProcessInstance !=null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }

        dto.setBusinessKey(EdConst.FIVE_OA_DEPATRMENTPOST+ "_" + item.getId());
        return dto;
    }

    public int getNewModel(String userLogin){
        FiveOaComputerMaintain item=new FiveOaComputerMaintain();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        item.setMaintainMan(hrEmployeeDto.getUserLogin());
        item.setMaintainManName(hrEmployeeDto.getUserName());
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setDeviceLevel(commonCodeService.selectCodeValue(MccConst.APP_CODE,"维修方式").toString());
        item.setDeviceType(commonCodeService.selectCodeValue(MccConst.APP_CODE,"信息安全防范措施").toString());
        ModelUtil.setNotNullFields(item);
        fiveOaComputerMaintainMapper.insert(item);

        String businessKey= EdConst.FIVE_OA_COMPUTER_MAINTAIN+ "_" + item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription","计算机及外设维修服务单："+item.getCreatorName());


        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_COMPUTER_MAINTAIN,businessKey, variables,
                MccConst.APP_CODE);
        item.setBusinessKey(businessKey);
        item.setProcessInstanceId(processInstanceId);
        fiveOaComputerMaintainMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);
        params.put("isLikeSelect",true);

        //如果有数据权限判断数据权限  myDeptData true查看当前部门 false查看创建人
        Map map =new HashMap();
        map.put("myDeptData",false);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));

        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaComputerMaintainMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaComputerMaintain item=(FiveOaComputerMaintain)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveOaComputerMaintain item = fiveOaComputerMaintainMapper.selectByPrimaryKey(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String number = sdf.format(new Date())+item.getCreator().substring((item.getCreator()).length() - 4);
        data.put("tableNo",number);
        data.put("deptName",item.getDeptName());
        data.put("deviceRoom",item.getDeviceRoom());
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
        data.put("maintainManName",item.getMaintainManName());
        data.put("maintainTime",item.getMaintainTime());
        data.put("receiveManName",item.getReceiveManName());
        data.put("receiveTime",item.getReceiveTime());
        data.put("comment",item.getComment());
        data.put("remark",item.getRemark());

        data.put("creatorName",item.getCreatorName());
        data.put("gmtCreate",item.getGmtCreate());

        data.put("nodes",actService.listPassedHistoryDto(item.getProcessInstanceId()));

        return data;
    }
}

