package com.cmcu.mcc.five.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.act.model.MyProcessInstance;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.act.service.MyHistoryService;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.five.dao.FiveHrQualifyExternalMapper;
import com.cmcu.mcc.five.dto.FiveHrQualifyExternalDto;
import com.cmcu.mcc.five.entity.FiveHrQualifyExternal;
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
import java.util.*;

/**
 * @author hnz
 * @date 2019/10/29
 */
@Service
public class FiveHrQualifyExternalService {
    @Resource
    FiveHrQualifyExternalMapper fiveHrQualifyExternalMapper;
    @Resource
    SelectEmployeeService selectEmployeeService;
    @Resource
    HrDeptService hrDeptService;

    @Autowired
    MyActService myActService;

    @Autowired
    ProcessQueryService processQueryService;
    @Autowired
    TaskHandleService taskHandleService;
    @Autowired
    CommonCodeService commonCodeService;
    @Resource
    HandleFormService handleFormService;

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref, int pageNum, int pageSize) {
        params.put("deleted", false);
        params.remove("userLogin");
        boolean IsChangreMen = selectEmployeeService.getPrincipalByUserLogin(userLogin);
        if (IsChangreMen){
            List<Integer> deptIdList =new ArrayList<>();
            deptIdList.add(selectEmployeeService.selectByUserLogin(userLogin).getDeptId());
            params.put("deptIdList", deptIdList);
        }else {
            params.put("creator",userLogin);
        }
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveHrQualifyExternalMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> list.add(getDto((FiveHrQualifyExternal) p)));
        pageInfo.setList(list);
        return pageInfo;
    }

    public int getNewModel(String uiSref,String userLogin) {
        List<Integer> deptIdList = selectEmployeeService.getMyDeptList(userLogin, uiSref);
        HrEmployeeDto hrEmployeeDto = selectEmployeeService.selectByUserLogin(userLogin);

        Date now = new Date();
        String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        FiveHrQualifyExternal model = new FiveHrQualifyExternalDto();
        model.setGmtCreate(now);
        model.setGmtModified(now);
        model.setFormNo("TXC-ZYWJ-SJ-03-2018a");
        model.setCheckYear(year);
        model.setPlanPost(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"拟聘岗位").toString());
        model.setPlanMajor(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"设计专业").toString());
        model.setUserType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"聘用人员类型").toString());
        model.setProbationSalary(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"试用期工资").toString());
        model.setEducationBackground(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"学历").toString());
        model.setMan(true);
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
        fiveHrQualifyExternalMapper.insert(model);

        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", "外聘技术人员设计资格审查");
        variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(model.getDeptId()));
        String businessKey = "fiveHrExternalApply_" + model.getId();
        model.setBusinessKey(businessKey);

        String processId = taskHandleService.startProcess("fiveHrExternalApply", businessKey, variables, MccConst.APP_CODE);
        model.setProcessInstanceId(processId);
        fiveHrQualifyExternalMapper.updateByPrimaryKey(model);
        return model.getId();
    }


    public FiveHrQualifyExternalDto getModelById(int id){
        return getDto(fiveHrQualifyExternalMapper.selectByPrimaryKey(id));
    }

    public void update(FiveHrQualifyExternalDto item){
        FiveHrQualifyExternal model=fiveHrQualifyExternalMapper.selectByPrimaryKey(item.getId());
        Map variables=Maps.newHashMap();
        if(!model.getDeptId().equals(item.getDeptId())){
            variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(model.getDeptId()));
        }
        model.setUserLogin(item.getUserLogin());
        model.setCheckYear(item.getCheckYear());
        model.setDeptId(item.getDeptId());
        model.setDeptName(item.getDeptName());
        model.setUserName(item.getUserName());
        model.setMan(item.getMan());
        model.setEducationBackground(item.getEducationBackground());
        model.setAge(item.getAge());
        model.setPlanMajor(item.getPlanMajor());
        model.setPlanPost(item.getPlanPost());
        model.setQualificationCertificate(item.getQualificationCertificate());
        model.setWorkExperience(item.getWorkExperience());
        model.setPerformance(item.getPerformance());
        model.setGraduateCollege(item.getGraduateCollege());
        model.setGraduateMajor(item.getGraduateMajor());
        model.setTitle(item.getTitle());
        model.setUserType(item.getUserType());
        model.setPlanSalary(item.getPlanSalary());
        model.setUserDepartmentOpinion(item.getUserDepartmentOpinion());
        model.setHrDepartmentOpinion(item.getHrDepartmentOpinion());
        model.setTechnologyDepartmentOpinion(item.getTechnologyDepartmentOpinion());
        model.setChargeHrLeaderOpinion(item.getChargeHrLeaderOpinion());
        model.setRemark(item.getRemark());
        model.setGmtModified(new Date());
        fiveHrQualifyExternalMapper.updateByPrimaryKey(model);

        if(variables.size()>0) {
            myActService.setVariables(model.getProcessInstanceId(), variables);
        }
    }


    public void remove(int id,String userLogin){
        FiveHrQualifyExternal item = fiveHrQualifyExternalMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin), "只允许创建人删除!");
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }

    private FiveHrQualifyExternalDto getDto(FiveHrQualifyExternal item){
        FiveHrQualifyExternalDto dto = FiveHrQualifyExternalDto.adapt(item);
        dto.setProcessName("已完成");

        if (!dto.getProcessEnd()){
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");

            if (customProcessInstance == null || customProcessInstance.isFinished()) {
                item.setProcessEnd(true);
                fiveHrQualifyExternalMapper.updateByPrimaryKey(item);
                dto.setProcessEnd(true);
            }
            if(customProcessInstance != null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());

            }
        }

        return dto;
    }
}
