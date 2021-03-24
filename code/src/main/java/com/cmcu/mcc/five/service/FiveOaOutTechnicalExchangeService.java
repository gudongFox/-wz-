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
import com.cmcu.mcc.dto.ActHistoryDto;
import com.cmcu.mcc.five.dao.FiveOaOutTechnicalExchangeMapper;
import com.cmcu.mcc.five.dto.FiveOaOutTechnicalExchangeDto;
import com.cmcu.mcc.five.entity.FiveOaOutTechnicalExchange;
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
public class FiveOaOutTechnicalExchangeService extends BaseService {

    @Resource
    FiveOaOutTechnicalExchangeMapper fiveOaOutTechnicalExchangeMapper;
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
        FiveOaOutTechnicalExchange item = fiveOaOutTechnicalExchangeMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin), "该数据是用户" + item.getCreatorName() + "创建");

        //流程作废
        handleFormService.removeProcessInstance(item.getBusinessKey(), userLogin);

    }

    public void update(FiveOaOutTechnicalExchangeDto item) {

        FiveOaOutTechnicalExchange model = fiveOaOutTechnicalExchangeMapper.selectByPrimaryKey(item.getId());

        model.setFormNo(item.getFormNo());
        model.setMeetName(item.getMeetName());
        model.setMeetNotice(item.getMeetNotice());
        model.setAttend(item.getAttend());
        model.setAttendMan(item.getAttendMan());
        model.setAttendManName(item.getAttendManName());
        model.setPricePayment(item.getPricePayment());
        model.setSpecialistComment(item.getSpecialistComment());
        model.setDeptChargeComment(item.getDeptChargeComment());

        model.setRemark(item.getRemark());
        model.setGmtModified(new Date());

        BeanValidator.validateObject(model);
        ModelUtil.setNotNullFields(model);
        BeanValidator.check(model);
        fiveOaOutTechnicalExchangeMapper.updateByPrimaryKey(model);
        Map variables = Maps.newHashMap();
        variables.put("processDescription", "参加外部技术交流会议审批:" + item.getMeetName());
        variables.put("attendUserLists", MyStringUtil.getStringList(model.getAttendMan()));
        myActService.setVariables(model.getProcessInstanceId(), variables);


    }

    public FiveOaOutTechnicalExchangeDto getModelById(int id) {

        return getDto(fiveOaOutTechnicalExchangeMapper.selectByPrimaryKey(id));
    }

    public FiveOaOutTechnicalExchangeDto getDto(FiveOaOutTechnicalExchange item) {
        FiveOaOutTechnicalExchangeDto dto = FiveOaOutTechnicalExchangeDto.adapt(item);
        dto.setProcessName("已完成");
        if (!dto.getProcessEnd()) {
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");
            if (customProcessInstance == null || customProcessInstance.isFinished()) {
                dto.setProcessEnd(true);
                fiveOaOutTechnicalExchangeMapper.updateByPrimaryKey(dto);
            } else {
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }

        return dto;
    }

    public int getNewModel(String userLogin) {
        FiveOaOutTechnicalExchange item = new FiveOaOutTechnicalExchange();
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
        item.setAttend("是");
        ModelUtil.setNotNullFields(item);
        fiveOaOutTechnicalExchangeMapper.insert(item);

        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("notifyManList", MyStringUtil.getStringList("2887,2877,2655"));
        variables.put("specialistChargeMen", selectEmployeeService.getUserListByRoleName("专家委负责人"));//专家委负责人
        variables.put("processDescription", "参加外部技术交流会议审批:" + item.getCreatorName());


        String businessKey = EdConst.FIVE_OA_OUTTECHNICALEXCHANGE + "_" + item.getId();
        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_OUTTECHNICALEXCHANGE, businessKey, variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        item.setBusinessKey(businessKey);
        fiveOaOutTechnicalExchangeMapper.updateByPrimaryKey(item);
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

        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaOutTechnicalExchangeMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaOutTechnicalExchange item = (FiveOaOutTechnicalExchange) p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveOaOutTechnicalExchange item = fiveOaOutTechnicalExchangeMapper.selectByPrimaryKey(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String number = sdf.format(new Date())+item.getCreator().substring((item.getCreator()).length() - 4);
        data.put("tableNo",number);
        data.put("meetName", item.getMeetName());
        data.put("meetNotice", item.getMeetNotice());
        data.put("attend", item.getAttend());
        data.put("attendManName", item.getAttendManName());
        data.put("pricePayment", item.getPricePayment());
        data.put("specialistComment", item.getSpecialistComment());
        data.put("deptChargeComment", item.getDeptChargeComment());

        data.put("creatorName", item.getCreatorName());
        data.put("gmtCreate", item.getGmtCreate());

        List<ActHistoryDto> actHistoryDtos = actService.listPassedHistoryDto(item.getProcessInstanceId());
        for (ActHistoryDto dto : actHistoryDtos) {
            if (dto.getActivityName() == null) {
                break;
            }
            if ("专家委-审批".equals(dto.getActivityName())) {
                data.put("expertCommittee", dto);
            }
            if ("于万河-审批".equals(dto.getActivityName())) {
                data.put("deptChargeMan", dto);
            }
        }
        data.put("nodes", actHistoryDtos);

        return data;
    }

}
