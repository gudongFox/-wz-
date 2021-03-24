package com.cmcu.mcc.five.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.act.model.MyProcessInstance;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.act.service.MyHistoryService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.five.dao.FiveHrQualifyChiefMapper;
import com.cmcu.mcc.five.dto.FiveHrQualifyChiefDto;
import com.cmcu.mcc.five.entity.FiveHrQualifyChief;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.HrDeptService;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class FiveHrQualifyChiefService {


    @Resource
    FiveHrQualifyChiefMapper fiveHrQualifyChiefMapper;

    @Resource
    SelectEmployeeService selectEmployeeService;
    @Autowired
    CommonCodeService commonCodeService;

    @Resource
    HrDeptService hrDeptService;

    @Autowired
    MyActService myActService;

    @Autowired
    ProcessQueryService processQueryService;
    @Autowired
    TaskHandleService taskHandleService;
    @Resource
    HandleFormService handleFormService;

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref, int pageNum, int pageSize) {
        params.put("deleted", false);
        List<Integer> deptIdList = selectEmployeeService.getMyDeptList(userLogin, uiSref);
        params.put("deptIdList", deptIdList);
        params.put("applyType",uiSref.contains("Pro") ? "兼职总设计师" : "总设计师");


        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveHrQualifyChiefMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> list.add(getDto((FiveHrQualifyChief) p)));
        pageInfo.setList(list);
        return pageInfo;
    }

    /**
     * 创建新数据
     * @param uiSref five.hrProChiefApply  five.hrChiefApply
     * @param userLogin luodong
     * @return
     */
    public int getNewModel(String uiSref,String userLogin) {
        List<Integer> deptIdList = selectEmployeeService.getMyDeptList(userLogin, uiSref);
        HrEmployeeDto hrEmployeeDto = selectEmployeeService.selectByUserLogin(userLogin);

        String type = uiSref.contains("Pro") ? "兼职总设计师" : "总设计师";
        Date now = new Date();
        String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        FiveHrQualifyChief model = new FiveHrQualifyChief();
        model.setGmtCreate(now);
        model.setGmtModified(now);
        model.setFormNo("TXC-ZYWJ-SJ-02-2018a");
        model.setApplyType(type);
        model.setCheckYear(year);
        model.setProjectTypeNow("");
        model.setProjectTypeApply(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"五洲项目类型").toString());
        model.setChiefUserLogin(hrEmployeeDto.getUserLogin());
        model.setChiefUserName(hrEmployeeDto.getUserName());
        model.setGender(hrEmployeeDto.getGender());
        model.setBirthDay(hrEmployeeDto.getBirthDay());
        model.setRanks(hrEmployeeDto.getRanks());
        model.setRankTime(hrEmployeeDto.getRankTime());
        model.setUserType(hrEmployeeDto.getUserType());
        model.setEducationBackground(hrEmployeeDto.getEducationBackground());
        model.setJoinCompanyTime(hrEmployeeDto.getJoinCompanyTime());
        model.setSpecialty(hrEmployeeDto.getSpecialty());
        model.setDeleted(false);
        model.setGmtCreate(new Date());
        model.setGmtModified(new Date());
        model.setProcessEnd(false);
        model.setHandled(false);
        model.setCreator(userLogin);
        model.setCreatorName(selectEmployeeService.getNameByUserLogin(userLogin));
        if (deptIdList.contains(hrEmployeeDto.getDeptId())) {
            model.setDeptId(hrEmployeeDto.getDeptId());
            model.setDeptName(hrEmployeeDto.getDeptName());
        } else {
            model.setDeptId(deptIdList.get(0));
            model.setDeptName(hrDeptService.getDeptNameById(model.getDeptId()));
        }
        ModelUtil.setNotNullFields(model);
        fiveHrQualifyChiefMapper.insert(model);

        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", type + "资格认定申报");
        variables.put("flag", 0);
        if (selectEmployeeService.getDeptDirector(model.getDeptId()).size()>0){
            variables.put("flag", 1);
            variables.put("deptChargeMen",selectEmployeeService.getDeptDirector(model.getDeptId()));//获取室主任
        }
        if (uiSref.contains("Approve")){
            variables.put("qualityChargeMan", selectEmployeeService.getUserListByRoleName("技术质量管理员"));
        }
        //部门总工
        variables.put("deptChiefEngineer", selectEmployeeService.selectUserByPositionNameAndDeptId("部门总工",model.getDeptId()));
        String processKey = uiSref.contains("Pro") ? EdConst.FIVE_HR_QUALIFY_PROCHIEF :EdConst.FIVE_HR_QUALIFY_CHIEF;
        String businessKey = processKey+"_" + model.getId();
        variables.put("businessKey",businessKey);
        String processInstanceId=  taskHandleService.startProcess(processKey,businessKey, variables, MccConst.APP_CODE);
        model.setProcessInstanceId(processInstanceId);
        model.setBusinessKey(businessKey);
        fiveHrQualifyChiefMapper.updateByPrimaryKey(model);
        return model.getId();
    }

    public FiveHrQualifyChiefDto getModelById(int id){
        return getDto(fiveHrQualifyChiefMapper.selectByPrimaryKey(id));
    }

    public void update(FiveHrQualifyChiefDto item){
        FiveHrQualifyChief model=fiveHrQualifyChiefMapper.selectByPrimaryKey(item.getId());
        model.setApplyType(item.getApplyType());
        model.setCheckYear(item.getCheckYear());

        Map variables=Maps.newHashMap();
        if(!model.getDeptId().equals(item.getDeptId())){
            variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(model.getDeptId()));
        }


        model.setDeptId(item.getDeptId());
        model.setDeptName(item.getDeptName());
        model.setChiefUserLogin(item.getChiefUserLogin());
        model.setChiefUserName(item.getChiefUserName());
        model.setPosition(item.getPosition());

        model.setProjectTypeNow(item.getProjectTypeNow());
        model.setPerformance(item.getPerformance());

        model.setRemark(item.getRemark());
        model.setGmtModified(new Date());
        fiveHrQualifyChiefMapper.updateByPrimaryKey(model);

        if(variables.size()>0) {
            myActService.setVariables(model.getProcessInstanceId(), variables);
        }
    }

    public void remove(int id,String userLogin){
        FiveHrQualifyChief item = fiveHrQualifyChiefMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin), "只允许创建人删除!");
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }

    private FiveHrQualifyChiefDto getDto(FiveHrQualifyChief item){
        FiveHrQualifyChiefDto dto = FiveHrQualifyChiefDto.adapt(item);
        dto.setProcessName("已完成");

        if (!dto.getProcessEnd()){
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");

            if (customProcessInstance == null || customProcessInstance.isFinished()) {
                dto.setProcessEnd(true);
                fiveHrQualifyChiefMapper.updateByPrimaryKey(dto);
            }
            if(customProcessInstance != null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }

        //申请人 个人基本信息
        dto.setHrEmployeeDto(selectEmployeeService.selectByUserLogin(item.getCreator()));
        return dto;
    }

}
