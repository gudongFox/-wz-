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
import com.cmcu.mcc.dto.ActHistoryDto;
import com.cmcu.mcc.five.dao.FiveOaComputerApplicationMapper;
import com.cmcu.mcc.five.dto.FiveOaComputerApplicationDto;
import com.cmcu.mcc.five.entity.FiveOaComputerApplication;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.HrEmployeeSysService;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.service.ActService;
import com.cmcu.mcc.service.impl.HandleFormService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Strings;
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
public class FiveOaComputerApplicationService extends BaseService {

    @Resource
    FiveOaComputerApplicationMapper fiveOaComputerApplicationMapper;
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
        FiveOaComputerApplication item = fiveOaComputerApplicationMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");

        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);

    }

    public void update(FiveOaComputerApplicationDto item){

        FiveOaComputerApplication model = fiveOaComputerApplicationMapper.selectByPrimaryKey(item.getId());
        model.setFormNo(item.getFormNo());
        model.setDeptName(item.getDeptName());
        model.setDeptId(item.getDeptId());
        model.setApplicationMan(item.getApplicationMan());
        model.setApplicationManName(item.getApplicationManName());
        model.setApplicationTime(item.getApplicationTime());
        model.setEquipmentNo(item.getEquipmentNo());
        model.setRoomNo(item.getRoomNo());
        model.setIpAddress(item.getIpAddress());
        model.setDepartmentComment(item.getDepartmentComment());
        model.setScienceComment(item.getScienceComment());
        model.setComputerComment(item.getComputerComment());
        model.setRemark(item.getRemark());
        model.setGmtModified(new Date());
        BeanValidator.validateObject(model);
        ModelUtil.setNotNullFields(model);
        fiveOaComputerApplicationMapper.updateByPrimaryKey(model);
        Map variables = Maps.newHashMap();
        if(!Strings.isNullOrEmpty(item.getDeptId())) {
            variables.put("deptChargeMan", selectEmployeeService.getDeptChargeMen(Integer.parseInt(item.getDeptId())));//发起者部门领导
        }
        BeanValidator.check(model);
        myActService.setVariables(model.getProcessInstanceId(),variables);

    }


    public FiveOaComputerApplicationDto getModelById(int id){

        return getDto(fiveOaComputerApplicationMapper.selectByPrimaryKey(id));
    }

    public FiveOaComputerApplicationDto getDto(FiveOaComputerApplication item) {
        FiveOaComputerApplicationDto dto=FiveOaComputerApplicationDto.adapt(item);
        dto.setProcessName("已完成");

        if (!dto.getProcessEnd()){
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");
            if(customProcessInstance == null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveOaComputerApplicationMapper.updateByPrimaryKey(dto);
            }
            if(customProcessInstance != null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }

       // dto.setBusinessKey(EdConst.FIVE_OA_DEPATRMENTPOST+ "_" + item.getId());
        return dto;
    }

    public int getNewModel(String userLogin){
        FiveOaComputerApplication item=new FiveOaComputerApplication();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);

        item.setDeptId(hrEmployeeDto.getDeptId()+"");
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setRemark("责任人保证公用计算机及公用U盘只进行非密信息交换，在以后的使用中能按照保密要求不在公用计算机及公用U盘中处理、接收、存储含有敏感信息的文件及图纸，愿意承担公司管理规定责任。");
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        ModelUtil.setNotNullFields(item);
        fiveOaComputerApplicationMapper.insert(item);

        String businessKey= EdConst.FIVE_OA_COMPUTERAPPLICATION+ "_" + item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription","公用计算机及U盘申请："+item.getCreatorName());
        //variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(item.getDeptId()));

        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_COMPUTERAPPLICATION,businessKey, variables,
                MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        item.setBusinessKey(businessKey);
        fiveOaComputerApplicationMapper.updateByPrimaryKey(item);
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

        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaComputerApplicationMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaComputerApplication item=(FiveOaComputerApplication)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }
    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveOaComputerApplication item = fiveOaComputerApplicationMapper.selectByPrimaryKey(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String number = sdf.format(new Date())+item.getCreator().substring((item.getCreator()).length() - 4);
        data.put("tableNo",number);
        data.put("deptName",item.getDeptName());
        data.put("applicationTime",item.getApplicationTime());
        data.put("applicationManName",item.getApplicationManName());
        data.put("equipmentNo",item.getEquipmentNo());
        data.put("roomNo",item.getRoomNo());
        data.put("ipAddress",item.getIpAddress());
        data.put("departmentComment",item.getDepartmentComment());
        data.put("scienceComment",item.getScienceComment());
        data.put("computerComment",item.getComputerComment());
        data.put("remark",item.getRemark());

        data.put("creatorName",item.getCreatorName());
        data.put("gmtCreate",item.getGmtCreate());

        List<ActHistoryDto> actHistoryDtos = actService.listPassedHistoryDto(item.getProcessInstanceId());
        for (ActHistoryDto dto:actHistoryDtos){
            if (dto.getActivityName()==null){
                break;
            }
            if ("部门领导-审批".equals(dto.getActivityName())){
                data.put("deptChargeMen",dto);
            }
            if ("王峥嵘-审批".equals(dto.getActivityName())){
                data.put("qualityDepartmentChargeMen",dto);
            }
            if ("王文豪-协同".equals(dto.getActivityName())){
                data.put("computerChargeMen",dto);
            }
        }
        data.put("nodes",actHistoryDtos);

        return data;
    }
}

