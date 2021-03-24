package com.cmcu.mcc.five.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.act.model.MyProcessInstance;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.act.service.MyHistoryService;
import com.cmcu.mcc.business.dto.BusinessContractDto;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.comm.exportDoc.AddRowTablePolicy;
import com.cmcu.mcc.comm.exportDoc.CheckTextRenderPolicy;
import com.cmcu.mcc.comm.exportDoc.ExportUtils;
import com.cmcu.mcc.comm.exportDoc.FlowsTableRenderPolicy;
import com.cmcu.mcc.ed.entity.EdProjectStep;
import com.cmcu.mcc.ed.service.EdProjectStepService;
import com.cmcu.mcc.ed.service.EdService;
import com.cmcu.mcc.five.dao.FiveEdQualityAnalysisMapper;
import com.cmcu.mcc.five.dto.FiveEdQualityAnalysisDto;
import com.cmcu.mcc.five.entity.FiveEdQualityAnalysis;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.service.ActService;
import com.cmcu.mcc.service.impl.HandleFormService;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hnz
 * @date 2019/12/2
 */
@Service
public class FiveEdQualityAnalysisService {

    @Resource
    FiveEdQualityAnalysisMapper fiveEdQualityAnalysisMapper;

    @Autowired
    MyActService myActService;

    @Autowired
    ProcessQueryService processQueryService;

    @Autowired
    EdService edService;

    @Autowired
    EdProjectStepService edProjectStepService;

    @Autowired
    SelectEmployeeService selectEmployeeService;

    @Autowired
    TaskHandleService taskHandleService;
    @Resource
    HandleFormService handleFormService;

    @Autowired
    ActService actService;
    public void remove(int id, String userLogin) {
        FiveEdQualityAnalysis item=fiveEdQualityAnalysisMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);

    }

    public void update(FiveEdQualityAnalysisDto fiveEdQualityAnalysisDto) {
        FiveEdQualityAnalysis model=fiveEdQualityAnalysisMapper.selectByPrimaryKey(fiveEdQualityAnalysisDto.getId());
        model.setRemark(fiveEdQualityAnalysisDto.getRemark());
        model.setQualityDepartmentMen(fiveEdQualityAnalysisDto.getQualityDepartmentMen());
        model.setQualityDepartmentMenName(fiveEdQualityAnalysisDto.getQualityDepartmentMenName());

        actService.setVariables(model.getProcessInstanceId(), "qualityDepartmentMen", model.getQualityDepartmentMen());
        model.setGmtModified(new Date());
        BeanValidator.validateObject(model);

        fiveEdQualityAnalysisMapper.updateByPrimaryKey(model);

    }

    public FiveEdQualityAnalysisDto getModelById(int id) {
        return getDto(fiveEdQualityAnalysisMapper.selectByPrimaryKey(id));
    }


    public int getNewModel(int stepId, String userLogin) {
        Map information = edService.getNewInformation(stepId, EdConst.FIVE_ED_DESIGN_RULE, userLogin);
        FiveEdQualityAnalysis model =new FiveEdQualityAnalysis();
        EdProjectStep edProjectStep=edProjectStepService.getModelById(stepId);
        BusinessContractDto businessContract = (BusinessContractDto) information.get("businessContract");
        String totalDesigner=businessContract.getBusinessContractDetail().getTotalDesigner();
        if(!totalDesigner.contains(userLogin)){
            throw new IllegalArgumentException("只能由项目总师"+ selectEmployeeService.getNameByUserLogin(totalDesigner)+"发起流程!");
        }

        List<String> deptChargeMen=selectEmployeeService.getDeptChargeMen(businessContract.getDeptId());
        Assert.state(deptChargeMen.size()>0,"项目所属部门负责人未配置!");

        model.setStepId(stepId);
        model.setProjectId(edProjectStep.getProjectId());
        model.setProjectName(edProjectStep.getProjectName());
        model.setProjectNo(information.get("projectNo").toString());
        model.setStepName(edProjectStep.getStepName());
        model.setStageName(edProjectStep.getStageName());
        model.setFormNo("TXC-ZYWJ-21-2018a");
        model.setContractNo(businessContract.getContractNo());
        model.setCreatorName(information.get("userName").toString());
        model.setDeleted(false);
        model.setProcessEnd(false);
        model.setCreator(userLogin);
        model.setGmtModified(new Date());
        model.setGmtCreate(new Date());

        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", information.get("processDescription").toString()+"-设计质量剖析");
        variables.put("deptChargeMen",deptChargeMen);
        ModelUtil.setNotNullFields(model);
        fiveEdQualityAnalysisMapper.insert(model);

        String businessKey=EdConst.FIVE_ED_QUALITY_ANALYSIS + "_" + model.getProjectId() + "_" + model.getId();
        model.setBusinessKey(businessKey);
        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_ED_QUALITY_ANALYSIS,model.getBusinessKey(), variables, MccConst.APP_CODE);
        model.setProcessInstanceId(processInstanceId);

        fiveEdQualityAnalysisMapper.updateByPrimaryKey(model);
        return model.getId();
    }

    public List<FiveEdQualityAnalysisDto> listDataByForeignKey(int stepId) {
        Map params = Maps.newHashMap();
        params.put("deleted", "0");
        params.put("stepId", stepId);
        List<FiveEdQualityAnalysis> oList = fiveEdQualityAnalysisMapper.selectAll(params);
        List<FiveEdQualityAnalysisDto> list = Lists.newArrayList();
        oList.forEach(p -> list.add(getDto(p)));
        return list;
    }

    public FiveEdQualityAnalysisDto getDto(FiveEdQualityAnalysis item) {
        FiveEdQualityAnalysisDto dto=FiveEdQualityAnalysisDto.adapt(item);
        dto.setProcessName("已完成");

        if (!dto.getProcessEnd()){
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");

            if(customProcessInstance == null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveEdQualityAnalysisMapper.updateByPrimaryKey(dto);
            }
            if(customProcessInstance != null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }

        return dto;
    }
    public XWPFTemplate getXWPFTemplate(String filePath, int id) {
        FiveEdQualityAnalysis item = fiveEdQualityAnalysisMapper.selectByPrimaryKey(id);
        //自定义策略
        Configure.ConfigureBuilder configureBuilder = Configure.newBuilder().customPolicy("detail_table",
                new AddRowTablePolicy(2,4,10));
        //自定义标签
        configureBuilder.addPlugin('%', new FlowsTableRenderPolicy());
        configureBuilder.addPlugin('$', new CheckTextRenderPolicy(2));
        XWPFTemplate xwpfTemplate = XWPFTemplate.compile(filePath,configureBuilder.build()).render(new HashMap<String, Object>(){{
            put("formNo",item.getFormNo());
            put("projectName", item.getProjectName());
            put("projectNo", item.getProjectNo());
            put("stageName", item.getStageName());
            put("stepName", item.getStepName());

            put("remark", item.getRemark());
            put("qualityDepartmentMenName", item.getQualityDepartmentMenName());

            //流程表格
            put("flows", ExportUtils.getFlowTable(actService.listPassedHistoryDto
                    (item.getProcessInstanceId()),18.85f));

        }});
        return xwpfTemplate;
    }


    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref, int pageNum, int pageSize) {
        params.put("deleted",false);

        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveEdQualityAnalysisMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveEdQualityAnalysis item=(FiveEdQualityAnalysis)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }
}
