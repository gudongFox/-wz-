package com.cmcu.mcc.five.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;

import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.five.dao.FiveOaGeneralCountersignMapper;
import com.cmcu.mcc.five.dto.FiveOaGeneralCountersignDto;
import com.cmcu.mcc.five.entity.FiveOaGeneralCountersign;
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
public class FiveOaGeneralCountersignService {
    @Resource
    FiveOaGeneralCountersignMapper fiveOaGeneralCountersignMapper;
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
        FiveOaGeneralCountersign item = fiveOaGeneralCountersignMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");

        //流程作废
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);

    }

    public void update(FiveOaGeneralCountersignDto fiveOaGeneralCountersignDto){
        FiveOaGeneralCountersign model = fiveOaGeneralCountersignMapper.selectByPrimaryKey(fiveOaGeneralCountersignDto.getId());
        model.setChargeMan(fiveOaGeneralCountersignDto.getChargeMan());
        model.setChargeManName(fiveOaGeneralCountersignDto.getChargeManName());
        model.setDeptId(fiveOaGeneralCountersignDto.getDeptId());
        model.setDeptName(fiveOaGeneralCountersignDto.getDeptName());
        model.setChargeMan(fiveOaGeneralCountersignDto.getChargeMan());
        model.setChargeManName(fiveOaGeneralCountersignDto.getChargeManName());
        model.setSubmitTime(fiveOaGeneralCountersignDto.getSubmitTime());
        model.setManager(fiveOaGeneralCountersignDto.getManager());
        model.setManagerName(fiveOaGeneralCountersignDto.getManagerName());
        model.setContent(fiveOaGeneralCountersignDto.getContent());
        model.setLegalReview(fiveOaGeneralCountersignDto.getLegalReview());
        model.setProcessFlag(fiveOaGeneralCountersignDto.getProcessFlag());
        model.setGmtModified(new Date());

        Map variables = Maps.newHashMap();
        variables.put("processDescription","通用会签单："+model.getContent());
        variables.put("flag",model.getProcessFlag());
        myActService.setVariables(model.getProcessInstanceId(),variables);
        BeanValidator.check(model);
        fiveOaGeneralCountersignMapper.updateByPrimaryKey(model);

    }

    public FiveOaGeneralCountersignDto getModelById(int id){

        return getDto(fiveOaGeneralCountersignMapper.selectByPrimaryKey(id));
    }

    public FiveOaGeneralCountersignDto getDto(FiveOaGeneralCountersign item) {
        FiveOaGeneralCountersignDto dto=FiveOaGeneralCountersignDto.adapt(item);
        dto.setProcessName("已完成");
        if (!dto.getProcessEnd()) {
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");
            if(customProcessInstance ==null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveOaGeneralCountersignMapper.updateByPrimaryKey(dto);
            }
            if(customProcessInstance!=null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }
        return dto;
    }

    public int getNewModel(String userLogin){


        FiveOaGeneralCountersign item=new FiveOaGeneralCountersign();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);

        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setProcessFlag(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"通用会签单流程走向").toString());
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        ModelUtil.setNotNullFields(item);

        fiveOaGeneralCountersignMapper.insert(item);
        String businessKey= EdConst.FIVE_OA_GENERAL_COUNTERSIGN+ "_" + item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription","通用会签单："+item.getCreatorName());
        variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(item.getDeptId()));//发起者部门领导

        item.setBusinessKey(businessKey);
        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_GENERAL_COUNTERSIGN,item.getBusinessKey(), variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        fiveOaGeneralCountersignMapper.updateByPrimaryKey(item);
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
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaGeneralCountersignMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaGeneralCountersign item=(FiveOaGeneralCountersign)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

}
