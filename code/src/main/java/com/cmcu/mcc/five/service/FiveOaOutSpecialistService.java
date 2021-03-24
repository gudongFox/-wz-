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
import com.cmcu.mcc.dto.ActHistoryDto;
import com.cmcu.mcc.five.dao.FiveOaOutSpecialistMapper;
import com.cmcu.mcc.five.dto.FiveOaOutSpecialistDto;
import com.cmcu.mcc.five.entity.FiveOaOutSpecialist;
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
public class FiveOaOutSpecialistService extends BaseService {

    @Resource
    FiveOaOutSpecialistMapper fiveOaOutSpecialistMapper;
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
        FiveOaOutSpecialist item = fiveOaOutSpecialistMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin), "该数据是用户" + item.getCreatorName() + "创建");

        //流程作废
        handleFormService.removeProcessInstance(item.getBusinessKey(), userLogin);

    }

    public void update(FiveOaOutSpecialistDto item) {

        FiveOaOutSpecialist model = fiveOaOutSpecialistMapper.selectByPrimaryKey(item.getId());

        model.setFormNo(item.getFormNo());
        model.setDeptId(item.getDeptId());
        model.setDeptName(item.getDeptName());
        model.setSubmitTime(item.getSubmitTime());
        model.setName(item.getName());
        model.setIdentityCardNo(item.getIdentityCardNo());
        model.setAddress(item.getAddress());
        model.setPosition(item.getPosition());
        model.setRanks(item.getRanks());
        model.setRankCode(item.getRankCode());
        model.setPoliticsStatus(item.getPoliticsStatus());
        model.setLabel(item.getLabel());
        model.setGraduateCollege(item.getGraduateCollege());
        model.setHighestEducation(item.getHighestEducation());
        model.setGood(item.getGood());
        model.setMajor(item.getMajor());
        model.setPhone(item.getPhone());
        model.setTelephone(item.getTelephone());
        model.setSpecialistGroup(item.getSpecialistGroup());
        model.setInstitutionName(item.getInstitutionName());
        model.setCompanyStamp(item.getCompanyStamp());
        model.setRemark(item.getRemark());

        model.setGmtModified(new Date());
        BeanValidator.validateObject(model);
        ModelUtil.setNotNullFields(model);
        Map variables = Maps.newHashMap();
        if (item.getDeptId() != model.getId()) {
            variables.put("deptChargeMen", selectEmployeeService.getDeptChargeMen(model.getDeptId()));
            model.setDeptName(item.getDeptName());
            model.setDeptId(item.getDeptId());
        }
        fiveOaOutSpecialistMapper.updateByPrimaryKey(model);
        variables.put("processDescription", "外部专家申请表：" + item.getName());
        BeanValidator.check(model);
        myActService.setVariables(model.getProcessInstanceId(), variables);


    }

    public FiveOaOutSpecialistDto getModelById(int id) {

        return getDto(fiveOaOutSpecialistMapper.selectByPrimaryKey(id));
    }

    public FiveOaOutSpecialistDto getDto(FiveOaOutSpecialist item) {
        FiveOaOutSpecialistDto dto = FiveOaOutSpecialistDto.adapt(item);
        dto.setProcessName("已完成");
        if (!dto.getProcessEnd()) {
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");
            if (customProcessInstance == null || customProcessInstance.isFinished()) {
                dto.setProcessEnd(true);
                fiveOaOutSpecialistMapper.updateByPrimaryKey(dto);
            } else {
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }
        return dto;
    }

    public int getNewModel(String userLogin) {
        FiveOaOutSpecialist item = new FiveOaOutSpecialist();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);

        item.setCompanyStamp("否");
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
        fiveOaOutSpecialistMapper.insert(item);

        String businessKey = EdConst.FIVE_OA_OUTSPECIALIST + "_" + item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", "外部专家申请表：" + item.getCreatorName());
        variables.put("deptChargeMen", selectEmployeeService.getDeptChargeMen(item.getDeptId()));
        variables.put("specialistChargeMen", selectEmployeeService.getUserListByRoleName("专家委负责人"));//专家委
        variables.put("qualityOtherDeptMan", selectEmployeeService.getOtherDeptChargeMan(11));//技术质量部与信息化部分管领导
        variables.put("qualityDeptMan", selectEmployeeService.getDeptChargeMen(11));//技术质量部与信息化部负责人
        item.setBusinessKey(businessKey);

        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_OUTSPECIALIST, businessKey, variables,
                MccConst.APP_CODE);

        item.setProcessInstanceId(processInstanceId);
        fiveOaOutSpecialistMapper.updateByPrimaryKey(item);
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

        /* List<Integer> deptIdList=hrEmployeeSysService.getMyDeptListOa(userLogin,uiSref);
         if (deptIdList.size()==0){
            params.put("creator",userLogin);
        }else {
            params.put("deptIdList",deptIdList);
        }*/
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaOutSpecialistMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaOutSpecialist item = (FiveOaOutSpecialist) p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveOaOutSpecialist item = fiveOaOutSpecialistMapper.selectByPrimaryKey(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String number = sdf.format(new Date())+item.getCreator().substring((item.getCreator()).length() - 4);
        data.put("tableNo",number);
        data.put("submitTime", item.getSubmitTime());
        data.put("name", item.getName());
        data.put("identityCardNo", item.getIdentityCardNo());
        data.put("deptName", item.getDeptName());
        data.put("address", item.getAddress());
        data.put("position", item.getPosition());
        data.put("rank", item.getRanks());
        data.put("rankCode", item.getRankCode());
        data.put("politicsStatus", item.getPoliticsStatus());
        data.put("label", item.getLabel());
        data.put("graduateCollege", item.getGraduateCollege());
        data.put("highestEducation", item.getHighestEducation());
        data.put("good", item.getGood());
        data.put("major", item.getMajor());
        data.put("phone", item.getPhone());
        data.put("telephone", item.getTelephone());
        data.put("specialistGroup", item.getSpecialistGroup());
        data.put("remark", item.getRemark());

        data.put("creatorName", item.getCreatorName());
        data.put("gmtCreate", item.getGmtCreate());

        List<ActHistoryDto> actHistoryDtos = actService.listPassedHistoryDto(item.getProcessInstanceId());
        for (ActHistoryDto dto : actHistoryDtos) {
            if (dto.getActivityName() == null) {
                break;
            }
            if ("发起者".equals(dto.getActivityName())) {
                data.put("startMen", dto);
            }
            if ("部门领导".equals(dto.getActivityName())) {
                data.put("deptChargeMen", dto);
            }
            if ("专家委-审批".equals(dto.getActivityName())) {
                data.put("specialistMen", dto);
            }
            if ("技术质量部与信息化部分管领导-审批".equals(dto.getActivityName())) {
                data.put("scienceLeader", dto);
            }
        }
        data.put("nodes", actHistoryDtos);

        return data;
    }

}

