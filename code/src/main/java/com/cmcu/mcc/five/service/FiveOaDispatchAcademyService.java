package com.cmcu.mcc.five.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.five.dao.FiveOaDispatchAcademyMapper;

import com.cmcu.mcc.five.dto.FiveOaDispatchAcademyDto;

import com.cmcu.mcc.five.entity.FiveOaDispatchAcademy;


import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.HrEmployeeService;
import com.cmcu.mcc.hr.service.HrEmployeeSysService;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class FiveOaDispatchAcademyService {

    @Resource
    FiveOaDispatchAcademyMapper fiveOaDispatchAcademyMapper;

    @Resource
    HrEmployeeMapper hrEmployeeMapper;
    @Resource
    CommonCodeService commonCodeService;
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
    HrEmployeeService hrEmployeeService;
    @Resource
    HandleFormService handleFormService;
    public void remove(int id,String userLogin){
        FiveOaDispatchAcademy item = fiveOaDispatchAcademyMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");

        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);

    }

    public void update(FiveOaDispatchAcademyDto fiveOaDispatchAcademyDto){
        FiveOaDispatchAcademy model = fiveOaDispatchAcademyMapper.selectByPrimaryKey(fiveOaDispatchAcademyDto.getId());
        model.setFileId(fiveOaDispatchAcademyDto.getFileId());
        model.setDispatchType(fiveOaDispatchAcademyDto.getDispatchType());
        model.setSigner(fiveOaDispatchAcademyDto.getSigner());
        model.setSignerName(fiveOaDispatchAcademyDto.getSignerName());
        model.setCountersign(fiveOaDispatchAcademyDto.getCountersign());
        model.setCountersignName(fiveOaDispatchAcademyDto.getCountersignName());
        model.setSecretGrade(fiveOaDispatchAcademyDto.getSecretGrade());
        model.setAllottedTime(fiveOaDispatchAcademyDto.getAllottedTime());
        model.setCompanyOffice(fiveOaDispatchAcademyDto.getCompanyOffice());
        model.setCompanyOfficeName(fiveOaDispatchAcademyDto.getCompanyOfficeName());
        model.setCompanySecurity(fiveOaDispatchAcademyDto.getCompanySecurity());
        model.setCompanySecurityName(fiveOaDispatchAcademyDto.getCompanySecurityName());
        model.setAuditMan(fiveOaDispatchAcademyDto.getAuditMan());
        model.setAuditManName(fiveOaDispatchAcademyDto.getAuditManName());

        model.setDrafter(fiveOaDispatchAcademyDto.getDrafter());
        model.setDrafterName(fiveOaDispatchAcademyDto.getDrafterName());
        model.setDispatch(fiveOaDispatchAcademyDto.getDispatch());
        model.setDispatchTitle(fiveOaDispatchAcademyDto.getDispatchTitle());

        model.setRealSendManName(fiveOaDispatchAcademyDto.getRealSendManName());
        model.setCopySendManName(fiveOaDispatchAcademyDto.getCopySendManName());
        model.setSubjectTerm(fiveOaDispatchAcademyDto.getSubjectTerm());
        model.setTypist(fiveOaDispatchAcademyDto.getTypist());
        model.setTypistName(fiveOaDispatchAcademyDto.getTypistName());
        model.setProofreader(fiveOaDispatchAcademyDto.getProofreader());
        model.setProofreaderName(fiveOaDispatchAcademyDto.getProofreaderName());
        model.setPrintNumber(fiveOaDispatchAcademyDto.getPrintNumber());
        model.setGmtModified(fiveOaDispatchAcademyDto.getGmtModified());
        BeanValidator.validateObject(model);
        ModelUtil.setNotNullFields(model);
        Map variables = Maps.newHashMap();
        if (fiveOaDispatchAcademyDto.getDeptId()!=model.getId()){
            variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(model.getDeptId()));
            model.setDeptName(fiveOaDispatchAcademyDto.getDeptName());
            model.setDeptId(fiveOaDispatchAcademyDto.getDeptId());
        }
        fiveOaDispatchAcademyMapper.updateByPrimaryKey(model);
        variables.put("companyOfficeMan", MyStringUtil.getStringList(model.getCompanyOffice()));
        variables.put("companyLeader", MyStringUtil.getStringList(model.getSigner()));
        variables.put("processDescription", model.getDispatchTitle());
        BeanValidator.check(model);
        myActService.setVariables(model.getProcessInstanceId(),variables);


    }

    public FiveOaDispatchAcademyDto getModelById(int id){

        return getDto(fiveOaDispatchAcademyMapper.selectByPrimaryKey(id));
    }

    public FiveOaDispatchAcademyDto getDto(FiveOaDispatchAcademy item) {
        FiveOaDispatchAcademyDto dto=FiveOaDispatchAcademyDto.adapt(item);
        dto.setProcessName("已完成");

        if (!dto.getProcessEnd()) {

            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");

            if (customProcessInstance == null || customProcessInstance.isFinished()) {
                dto.setProcessEnd(true);
                fiveOaDispatchAcademyMapper.updateByPrimaryKey(dto);
            }
            if (customProcessInstance != null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())) {
                dto.setProcessName(customProcessInstance.getCurrentTaskName());

            }
        }
        return dto;
    }

    public int getNewModel(String userLogin){
        List<String> secretary =  hrEmployeeService.selectUserByRoleNames("机要秘书");

        Assert.state(secretary.size()>0,"未找到职务为 机要秘书 人员");
        FiveOaDispatchAcademy item=new FiveOaDispatchAcademy();

        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        item.setDispatchType("五洲工程设计研究院发文单");
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setSecretGrade(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"五洲秘密等级").toString());
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        ModelUtil.setNotNullFields(item);
        fiveOaDispatchAcademyMapper.insert(item);
        String businessKey= EdConst.FIVE_OA_DISPATCH_ACADEMY+ "_" + item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", item.getDispatchType());
        variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(item.getDeptId()));
        variables.put("secretary",secretary);
        item.setBusinessKey(businessKey);
        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_DISPATCH_ACADEMY,item.getBusinessKey(), variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        item.setBusinessKey(businessKey);
        fiveOaDispatchAcademyMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);
         List<Integer> deptIdList=hrEmployeeSysService.getMyDeptListOa(userLogin,uiSref);
         if (deptIdList.size()==0){
            params.put("creator",userLogin);
        }else {
            params.put("deptIdList",deptIdList);
        }
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaDispatchAcademyMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaDispatchAcademy item=(FiveOaDispatchAcademy)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }
}
