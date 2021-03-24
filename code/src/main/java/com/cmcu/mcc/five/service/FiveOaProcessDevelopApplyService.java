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
import com.cmcu.mcc.five.dao.FiveOaProcessDevelopApplyMapper;
import com.cmcu.mcc.five.dto.FiveOaProcessDevelopApplyDto;
import com.cmcu.mcc.five.entity.FiveOaProcessDevelopApply;
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
import java.util.*;

@Service
public class FiveOaProcessDevelopApplyService extends BaseService {

    @Resource
    FiveOaProcessDevelopApplyMapper fiveOaProcessDevelopApplyMapper;
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
        FiveOaProcessDevelopApply item = fiveOaProcessDevelopApplyMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin), "该数据是用户" + item.getCreatorName() + "创建");

        //流程作废
        handleFormService.removeProcessInstance(item.getBusinessKey(), userLogin);

    }

    public void update(FiveOaProcessDevelopApplyDto item) {

        FiveOaProcessDevelopApply model = fiveOaProcessDevelopApplyMapper.selectByPrimaryKey(item.getId());
        model.setTitle(item.getTitle());
        model.setContent(item.getContent());
        model.setFormMessage(item.getFormMessage());
        model.setProcessMessage(item.getProcessMessage());

        model.setDeptId(item.getDeptId());
        model.setDeptName(item.getDeptName());
        model.setRemark(item.getRemark());

        model.setGmtModified(new Date());
        ModelUtil.setNotNullFields(model);
        BeanValidator.validateObject(model);

        Map variables = Maps.newHashMap();
        List<String> deptChargeMen = new ArrayList<>();
        if (model.getDeptId() != 0) {
            deptChargeMen = selectEmployeeService.getDeptAllChargeMen(model.getDeptId());
        }

        variables.put("processDescription", "流程开发申请：" + item.getTitle());

        variables.put("deptChargeMen", deptChargeMen);
        myActService.setVariables(model.getProcessInstanceId(), variables);
        BeanValidator.check(model);
        fiveOaProcessDevelopApplyMapper.updateByPrimaryKey(model);

    }

    public FiveOaProcessDevelopApplyDto getModelById(int id) {
        return getDto(fiveOaProcessDevelopApplyMapper.selectByPrimaryKey(id));
    }

    public FiveOaProcessDevelopApplyDto getDto(FiveOaProcessDevelopApply item) {
        FiveOaProcessDevelopApplyDto dto = FiveOaProcessDevelopApplyDto.adapt(item);
        dto.setProcessName("已完成");
        if (!dto.getProcessEnd() && StringUtils.isNotEmpty(item.getProcessInstanceId())) {
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");
            if (customProcessInstance == null || customProcessInstance.isFinished()) {
                dto.setProcessEnd(true);
                fiveOaProcessDevelopApplyMapper.updateByPrimaryKey(dto);
            } else {
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }

        }
        return dto;
    }

    public int getNewModel(String userLogin) {
        FiveOaProcessDevelopApply item = new FiveOaProcessDevelopApply();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);

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
        fiveOaProcessDevelopApplyMapper.insert(item);

        String businessKey = EdConst.FIVE_OA_PROCESSDEVELOPAPPLY + "_" + item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", "流程开发申请：" + item.getCreatorName());

        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_PROCESSDEVELOPAPPLY, businessKey, variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        item.setBusinessKey(businessKey);
        fiveOaProcessDevelopApplyMapper.updateByPrimaryKey(item);
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

        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaProcessDevelopApplyMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaProcessDevelopApply item = (FiveOaProcessDevelopApply) p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveOaProcessDevelopApply item = fiveOaProcessDevelopApplyMapper.selectByPrimaryKey(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String number = sdf.format(new Date())+item.getCreator().substring((item.getCreator()).length() - 4);
        data.put("tableNo",number);
        data.put("title", item.getTitle());
        data.put("content", item.getContent());
        data.put("formMessage", item.getFormMessage());
        data.put("processMessage", item.getProcessMessage());
        data.put("deptName", item.getDeptName());
        data.put("remark", item.getRemark());

        data.put("creatorName", item.getCreatorName());
        data.put("gmtCreate", item.getGmtCreate());

        data.put("nodes", actService.listPassedHistoryDto(item.getProcessInstanceId()));

        return data;
    }


}
