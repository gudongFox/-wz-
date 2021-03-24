package com.cmcu.mcc.five.service;


import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.dao.CommonAttachMapper;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.*;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.dto.ActHistoryDto;
import com.cmcu.mcc.five.dao.FiveOaPrivilegeManagementMapper;
import com.cmcu.mcc.five.dto.FiveOaPrivilegeManagementDto;
import com.cmcu.mcc.five.entity.FiveOaPrivilegeManagement;
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
public class FiveOaPrivilegeManagementService extends BaseService {

    @Resource
    FiveOaPrivilegeManagementMapper fiveOaPrivilegeManagementMapper;
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


    public void remove(int id, String userLogin) {
        FiveOaPrivilegeManagement item = fiveOaPrivilegeManagementMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin), "该数据是用户" + item.getCreatorName() + "创建");

        handleFormService.removeProcessInstance(item.getBusinessKey(), userLogin);

    }

    public void update(FiveOaPrivilegeManagementDto item) {
        FiveOaPrivilegeManagement model = fiveOaPrivilegeManagementMapper.selectByPrimaryKey(item.getId());
        model.setFormNo(item.getFormNo());
        model.setDeptName(item.getDeptName());
        model.setDeptId(item.getDeptId());
        model.setApplicationManName(item.getApplicationManName());
        model.setApplicationMan(item.getApplicationMan());
        model.setApplicationTime(item.getApplicationTime());
        model.setFlow(item.getFlow());
        model.setDelegationMan(item.getDelegationMan());
        model.setDelegationManName(item.getDelegationManName());
        model.setDelegationPrivilege(item.getDelegationPrivilege());
        model.setDelegationTime(item.getDelegationTime());
        model.setRemark(item.getRemark());
        model.setGmtModified(new Date());

        BeanValidator.validateObject(model);
        ModelUtil.setNotNullFields(model);
        fiveOaPrivilegeManagementMapper.updateByPrimaryKey(model);
        Map variables = Maps.newHashMap();
        if (item.getDeptId() != 0) {
            variables.put("deptChargeMan", selectEmployeeService.getDeptChargeMen(item.getDeptId()));//发起者部门领导
        }
        variables.put("delegationMan", MyStringUtil.getStringList(model.getDelegationMan()));

        variables.put("processDescription", "权限调整：" + item.getFlow());
        BeanValidator.check(model);
        myActService.setVariables(model.getProcessInstanceId(), variables);

    }

    public FiveOaPrivilegeManagementDto getModelById(int id) {

        return getDto(fiveOaPrivilegeManagementMapper.selectByPrimaryKey(id));
    }

    public FiveOaPrivilegeManagementDto getDto(FiveOaPrivilegeManagement item) {
        FiveOaPrivilegeManagementDto dto = FiveOaPrivilegeManagementDto.adapt(item);
        dto.setProcessName("已完成");
        if (!dto.getProcessEnd()) {
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");
            if (customProcessInstance == null || customProcessInstance.isFinished()) {
                dto.setProcessEnd(true);
                fiveOaPrivilegeManagementMapper.updateByPrimaryKey(dto);
            } else {
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }

        }

        return dto;
    }

    public int getNewModel(String userLogin) {
        FiveOaPrivilegeManagement item = new FiveOaPrivilegeManagement();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        item.setApplicationManName(hrEmployeeDto.getUserName());
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setApplicationTime(MyDateUtil.getStringToday());
        item.setDelegationTime(MyDateUtil.getStringToday());
        item.setDelegationPrivilege(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE, "委托权限").toString());
        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        ModelUtil.setNotNullFields(item);
        fiveOaPrivilegeManagementMapper.insert(item);

        String businessKey = EdConst.FIVE_OA_PRIVILEGE_MANAGEMENT + "_" + item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", "权限调整：" + item.getCreatorName());
        variables.put("deptChargeMan", selectEmployeeService.getDeptChargeMen(item.getDeptId()));

        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_PRIVILEGE_MANAGEMENT, businessKey, variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        item.setBusinessKey(businessKey);
        fiveOaPrivilegeManagementMapper.updateByPrimaryKey(item);
        return item.getId();

    }

    public PageInfo<Object> listPagedData(Map<String, Object> params, String userLogin, String uiSref, int pageNum, int pageSize) {
        params.put("deleted", false);
        params.put("isLikeSelect", true);

        Map map = new HashMap();
        map.put("myDeptData", false);
        map.put("uiSref", uiSref);
        map.put("enLogin", userLogin);
        params.putAll(getDefaultRightParams(map));
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaPrivilegeManagementMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaPrivilegeManagement item = (FiveOaPrivilegeManagement) p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveOaPrivilegeManagement item = fiveOaPrivilegeManagementMapper.selectByPrimaryKey(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String number = sdf.format(new Date())+item.getCreator().substring((item.getCreator()).length() - 4);
        data.put("tableNo",number);
        data.put("deptName", item.getDeptName());
        data.put("applicationTime", item.getApplicationTime());
/*        data.put("applicationManName",item.getApplicationManName());
        data.put("equipmentNo",item.getEquipmentNo());
        data.put("roomNo",item.getRoomNo());
        data.put("ipAddress",item.getIpAddress());
        data.put("departmentComment",item.getDepartmentComment());
        data.put("scienceComment",item.getScienceComment());
        data.put("computerComment",item.getComputerComment());*/
        data.put("remark", item.getRemark());

        data.put("creatorName", item.getCreatorName());
        data.put("gmtCreate", item.getGmtCreate());

        List<ActHistoryDto> actHistoryDtos = actService.listPassedHistoryDto(item.getProcessInstanceId());
        for (ActHistoryDto dto : actHistoryDtos) {
            if (dto.getActivityName() == null) {
                break;
            }
            if ("部门领导-审批".equals(dto.getActivityName())) {
                data.put("deptChargeMen", dto);
            }
            if ("王峥嵘-审批".equals(dto.getActivityName())) {
                data.put("qualityDepartmentChargeMen", dto);
            }
            if ("王文豪-协同".equals(dto.getActivityName())) {
                data.put("computerChargeMen", dto);
            }
        }
        data.put("nodes", actHistoryDtos);

        return data;
    }
}

