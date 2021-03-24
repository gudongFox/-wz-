package com.cmcu.mcc.five.service;


import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.entity.CommonEdBuild;
import com.cmcu.common.service.CommonEdBuildService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyDateUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.business.dto.BusinessContractDto;
import com.cmcu.mcc.business.entity.BusinessContract;
import com.cmcu.mcc.business.service.BusinessContractService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.ed.dto.EdProjectStepDto;
import com.cmcu.mcc.ed.service.EdProjectStepService;
import com.cmcu.mcc.five.dao.FiveEdDesignDrawingCheckMapper;
import com.cmcu.mcc.five.dto.FiveEdDesignDrawingCheckDto;
import com.cmcu.mcc.five.entity.FiveEdDesignDrawingCheck;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.HrEmployeeService;
import com.cmcu.mcc.hr.service.HrEmployeeSysService;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.service.ActService;
import com.cmcu.mcc.service.impl.HandleFormService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class FiveEdDesignDrawingCheckService {
    @Resource
    FiveEdDesignDrawingCheckMapper fiveEdDesignDrawingCheckMapper;
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
    HrEmployeeService hrEmployeeService;
    @Autowired
    ActService actService;
    @Resource
    HandleFormService handleFormService;
    @Resource
    EdProjectStepService edProjectStepService;
    @Resource
    CommonEdBuildService commonEdBuildService;
    @Resource
    BusinessContractService businessContractService;


    public void remove(int id,String userLogin){
        FiveEdDesignDrawingCheck item = fiveEdDesignDrawingCheckMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");

        //流程作废
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);

    }

    public void update(FiveEdDesignDrawingCheckDto fiveEdDesignDrawingCheckDto){
        FiveEdDesignDrawingCheck model = fiveEdDesignDrawingCheckMapper.selectByPrimaryKey(fiveEdDesignDrawingCheckDto.getId());
        BeanUtils.copyProperties(fiveEdDesignDrawingCheckDto,model);
        fiveEdDesignDrawingCheckMapper.updateByPrimaryKey(model);
    }

    public FiveEdDesignDrawingCheckDto getModelById(int id){

        return getDto(fiveEdDesignDrawingCheckMapper.selectByPrimaryKey(id));
    }

    public FiveEdDesignDrawingCheckDto getDto(FiveEdDesignDrawingCheck item) {
        FiveEdDesignDrawingCheckDto dto=FiveEdDesignDrawingCheckDto.adapt(item);
        dto.setProcessName("已完成");

        if (!dto.getProcessEnd()){
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");

            if(customProcessInstance == null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveEdDesignDrawingCheckMapper.updateByPrimaryKey(dto);
            }
            if(customProcessInstance != null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());

            }
        }

        //是否是默认项
        dto.setDefaultItem(getDefaultItem(item.getStepId()).getId()==item.getId());
        return dto;
    }

    /**
     *
     * @param userLogin
     * @param stepId 阶段Id ed_project_step
     * @return
     */

    public FiveEdDesignDrawingCheck getNewModel(int stepId,String userLogin){
        FiveEdDesignDrawingCheck item=new FiveEdDesignDrawingCheck();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);

        //设计阶段分期信息
        EdProjectStepDto edProjectStepDto = edProjectStepService.getModelById(stepId);
        //项目启动
        BusinessContractDto businessContractDto = businessContractService.getModelById(edProjectStepDto.getProjectId());
        //限制创建人为项目总师
        Assert.state((edProjectStepDto.getChargeMan()+edProjectStepDto.getOtherChargeMan()).contains(hrEmployeeDto.getUserLogin()),"只能由项目总师:"+edProjectStepDto.getChargeManName()+","+edProjectStepDto.getOtherChargeManName()+"发起！");

        item.setProjectName(edProjectStepDto.getProjectName());//项目名称
        item.setProjectId(edProjectStepDto.getProjectId());

        String stageNames = edProjectStepDto.getStageName();//获取当前设计阶段
        if (stageNames.contains("前期")){
            item.setParentNo(businessContractDto.getRecordNoEarly());
        }else if (stageNames.contains("初步")){
            item.setParentNo(businessContractDto.getRecordNoFirst());
        }else if (stageNames.contains("施工")){
            item.setParentNo(businessContractDto.getRecordNoConstruction());
        }

        item.setProjectNo(edProjectStepDto.getProjectNo());//项目编号
        item.setStageName(edProjectStepDto.getStageName());//阶段名称
        item.setStepId(edProjectStepDto.getId());
        item.setStepName(edProjectStepDto.getStepName());
        item.setFormNo("");//表单编号
        item.setCheckTime(MyDateUtil.getStringToday());
        item.setSecretLevel("非密");

        item.setContractNo(edProjectStepDto.getContractNo());//合同号
        item.setBuildName(StringUtils.join(getBuildList(stepId+""),","));//建筑物名称
        item.setProjectChargeMan(edProjectStepDto.getProjectChargeMan());
        item.setProjectChargeManName(edProjectStepDto.getProjectChargeManName());

        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        ModelUtil.setNotNullFields(item);
        fiveEdDesignDrawingCheckMapper.insert(item);
        String businessKey= EdConst.FIVE_ED_DESIGN_DRAWING_CHECK+"_" + item.getProjectId()+ "_" + item.getId();
        Map variables = Maps.newHashMap();

        variables.put("userLogin", userLogin);
        variables.put("projectChargeMen", MyStringUtil.getStringList(item.getProjectChargeMan()));
        variables.put("processDescription", "设计图纸资料交验："+item.getStageName());

        item.setBusinessKey(businessKey);
        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_ED_DESIGN_DRAWING_CHECK,item.getBusinessKey(), variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        fiveEdDesignDrawingCheckMapper.updateByPrimaryKey(item);
        return item;
    }

    /**
     * 获取建筑物名称
     * @param businessKey
     * @return
     */
    public List<String> getBuildList(String businessKey){
        List<String> list= Lists.newArrayList();
        List<CommonEdBuild> commonEdBuilds = commonEdBuildService.listData(businessKey);
        for (CommonEdBuild build:commonEdBuilds){
            list.add(build.getBuildName());
        }
        return list;
    }

    public List<FiveEdDesignDrawingCheckDto> listDataByStepId(int stepId,String userLogin) {
        Map params= Maps.newHashMap();
        params.put("deleted",false);
        params.put("stepId",stepId);
        List<FiveEdDesignDrawingCheck> oList = fiveEdDesignDrawingCheckMapper.selectAll(params);
        List<FiveEdDesignDrawingCheckDto> list = Lists.newArrayList();
        oList.forEach(p -> list.add(getDto(p)));
        return list;
    }

    /**
     * 默认生效项 流程已完成 的最新的 如果没有已完成的 去最新的
     * @param stepId
     * @return
     */
    public FiveEdDesignDrawingCheck getDefaultItem(int stepId){
        Map params= Maps.newHashMap();
        params.put("deleted",false);
        params.put("stepId",stepId);
        params.put("processEnd",true);
        List<FiveEdDesignDrawingCheck> oList = fiveEdDesignDrawingCheckMapper.selectAll(params);
        if (oList.size()>0){
            return oList.get(0);
        }else {
            params.remove("processEnd");
            oList = fiveEdDesignDrawingCheckMapper.selectAll(params);
            if (oList.size()>0){
                return oList.get(0);
            }else {
                return null;
            }
        }
    }

}
