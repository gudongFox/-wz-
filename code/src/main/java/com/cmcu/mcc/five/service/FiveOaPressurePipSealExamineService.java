package com.cmcu.mcc.five.service;


import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.five.dao.FiveOaPressurePipSealExamineMapper;
import com.cmcu.mcc.five.dto.FiveOaPressurePipSealExamineDto;
import com.cmcu.mcc.five.entity.FiveOaPressurePipSealExamine;
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
public class FiveOaPressurePipSealExamineService extends BaseService {

    @Resource
    FiveOaPressurePipSealExamineMapper fiveOaPressurePipSealExamineMapper;
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

    public void remove(int id, String userLogin) {
        FiveOaPressurePipSealExamine item = fiveOaPressurePipSealExamineMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin), "该数据是用户" + item.getCreatorName() + "创建");

        //流程作废
        handleFormService.removeProcessInstance(item.getBusinessKey(), userLogin);

    }

    public void update(FiveOaPressurePipSealExamineDto item) {

        FiveOaPressurePipSealExamine model = fiveOaPressurePipSealExamineMapper.selectByPrimaryKey(item.getId());
        Assert.state(item.getDeptId() != 0, "请先选择 用印单位");

        model.setFormNo(item.getFormNo());
        model.setSeal(item.getSeal());
        model.setDeptId(item.getDeptId());
        model.setDeptName(item.getDeptName());
        model.setApplyMan(item.getApplyMan());
        model.setApplyManName(item.getApplyManName());
        model.setApplyManLink(item.getApplyManLink());
        model.setUseTime(item.getUseTime());
        model.setProjectName(item.getProjectName());
        model.setPipDrawName(item.getPipDrawName());
        model.setDrawCompleteTime(item.getDrawCompleteTime());
        model.setPressurePipType(item.getPressurePipType());
        model.setDrawNum(item.getDrawNum());

        model.setDeptChargeMan(item.getDeptChargeMan());
        model.setDeptChargeManName(item.getDeptChargeManName());
        model.setDeptChargeManComment(item.getDeptChargeManComment());

        model.setPressurePipChargeMan(item.getPressurePipChargeMan());
        model.setPressurePipChargeManName(item.getPressurePipChargeManName());
        model.setPressurePipChargeManComment(item.getPressurePipChargeManComment());

        model.setTechnologyChargeMan(item.getTechnologyChargeMan());
        model.setTechnologyChargeManName(item.getTechnologyChargeManName());
        model.setTechnologyChargeManComment(item.getTechnologyChargeManComment());

        model.setSealMan(item.getSealMan());
        model.setSealManName(item.getSealManName());

        model.setSuperviseMan(item.getSuperviseMan());
        model.setSuperviseManName(item.getSuperviseManName());

        model.setRemark(item.getRemark());
        model.setGmtModified(new Date());

        BeanValidator.validateObject(model);
        ModelUtil.setNotNullFields(model);


        BeanValidator.check(model);
        fiveOaPressurePipSealExamineMapper.updateByPrimaryKey(model);
        Map variables = Maps.newHashMap();
        variables.put("processDescription", "压力管道设计资格印章使用审批表:" + item.getSeal());
        variables.put("pressurePipChargeMan", MyStringUtil.getStringList(item.getPressurePipChargeMan()));
        variables.put("technologyChargeMan", MyStringUtil.getStringList(item.getTechnologyChargeMan()));
        variables.put("deptChargeMen", selectEmployeeService.getDeptChargeMen(item.getDeptId()));
        if (model.getSeal().equals("线上")) {
            variables.put("flag", 1);
        } else {
            variables.put("flag", 0);
        }
        myActService.setVariables(model.getProcessInstanceId(), variables);


    }

    public FiveOaPressurePipSealExamineDto getModelById(int id) {

        return getDto(fiveOaPressurePipSealExamineMapper.selectByPrimaryKey(id));
    }

    public FiveOaPressurePipSealExamineDto getDto(FiveOaPressurePipSealExamine item) {
        FiveOaPressurePipSealExamineDto dto = FiveOaPressurePipSealExamineDto.adapt(item);
        dto.setProcessName("已完成");
        if (!dto.getProcessEnd()) {
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");
            if (customProcessInstance == null || customProcessInstance.isFinished()) {
                dto.setProcessEnd(true);
                fiveOaPressurePipSealExamineMapper.updateByPrimaryKey(dto);
            } else {
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }
        return dto;
    }

    public int getNewModel(String userLogin) {
        FiveOaPressurePipSealExamine item = new FiveOaPressurePipSealExamine();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);

        //技术质量部负责人
        item.setTechnologyChargeMan("2887");
        item.setTechnologyChargeManName("王峥嵘");
        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setApplyManName(hrEmployeeDto.getUserName());
        item.setApplyManLink(hrEmployeeDto.getMobile());

        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setSeal("线下");
        ModelUtil.setNotNullFields(item);
        fiveOaPressurePipSealExamineMapper.insert(item);

        String businessKey = EdConst.FIVE_OA_PRESSUREPIPSEALEXAMINE + "_" + item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", "压力管道设计资格印章使用审批表:" + item.getCreatorName());
        variables.put("finishedDeptChargeMen", selectEmployeeService.getDeptChargeMen(13));
        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_PRESSUREPIPSEALEXAMINE, businessKey, variables, MccConst.APP_CODE);
        item.setBusinessKey(businessKey);
        item.setProcessInstanceId(processInstanceId);
        fiveOaPressurePipSealExamineMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public PageInfo<Object> listPagedData(Map<String, Object> params, String userLogin, String uiSref, int pageNum, int pageSize) {
        params.put("deleted", false);
        params.put("isLikeSelect", true);

        //如果有数据权限判断数据权限  myDeptData true查看当前部门 false查看创建人
        Map map = new HashMap();
        map.put("myDeptData", false);
        map.put("uiSref", uiSref);
        map.put("enLogin", userLogin);
        params.putAll(getDefaultRightParams(map));

        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaPressurePipSealExamineMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaPressurePipSealExamine item = (FiveOaPressurePipSealExamine) p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveOaPressurePipSealExamine item = fiveOaPressurePipSealExamineMapper.selectByPrimaryKey(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String number = sdf.format(new Date())+item.getCreator().substring((item.getCreator()).length() - 4);
        data.put("tableNo",number);
        data.put("seal", item.getSeal());
        data.put("deptName", item.getDeptName());
        data.put("applyManName", item.getApplyManName());
        data.put("applyManLink", item.getApplyManLink());
        data.put("useTime", item.getUseTime());
        data.put("projectName", item.getProjectName());
        data.put("pipDrawName", item.getPipDrawName());
        data.put("drawCompleteTime", item.getDrawCompleteTime());
        data.put("pressurePipType", item.getPressurePipType());
        data.put("drawNum", item.getDrawNum());
        data.put("deptChargeManName", item.getDeptChargeManName());
        data.put("technologyChargeManName", item.getTechnologyChargeManName());
        data.put("pressurePipChargeManName", item.getPressurePipChargeManName());
        data.put("sealManName", item.getSealManName());
        data.put("superviseManName", item.getSuperviseManName());
        data.put("deptChargeManComment", item.getDeptChargeManComment());
        data.put("pressurePipChargeManComment", item.getPressurePipChargeManComment());
        data.put("technologyChargeManComment", item.getTechnologyChargeManComment());

        data.put("creatorName", item.getCreatorName());
        data.put("gmtCreate", item.getGmtCreate());

        data.put("nodes", actService.listPassedHistoryDto(item.getProcessInstanceId()));

        return data;
    }

}
