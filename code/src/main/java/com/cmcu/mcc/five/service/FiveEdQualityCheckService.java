package com.cmcu.mcc.five.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyStringUtil;
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
import com.cmcu.mcc.five.dao.FiveEdQualityCheckDetailMapper;
import com.cmcu.mcc.five.dao.FiveEdQualityCheckMapper;
import com.cmcu.mcc.five.dto.FiveEdQualityCheckDto;
import com.cmcu.mcc.five.entity.FiveEdQualityCheck;
import com.cmcu.mcc.five.entity.FiveEdQualityCheckDetail;
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
import java.util.*;

@Service
public class FiveEdQualityCheckService {

    @Resource
    FiveEdQualityCheckMapper fiveEdQualityCheckMapper;

    @Resource
    FiveEdQualityCheckDetailMapper fiveEdQualityCheckDetailMapper;

    @Resource
    EdService edService;

    @Resource
    ProcessQueryService processQueryService;

    @Resource
    MyActService myActService;

    @Autowired
    EdProjectStepService edProjectStepService;

    @Autowired
    TaskHandleService taskHandleService;

    @Autowired
    ActService actService;
    @Resource
    HandleFormService handleFormService;

    public List<FiveEdQualityCheckDto> listDataByStepId(int stepId) {
        Map params= Maps.newHashMap();
        params.put("deleted",false);
        params.put("stepId",stepId);
        List<FiveEdQualityCheck> oList = fiveEdQualityCheckMapper.selectAll(params);
        List<FiveEdQualityCheckDto> list = Lists.newArrayList();
        oList.forEach(p -> list.add(getDto(p)));
        return list;
    }

    public FiveEdQualityCheckDto getModelById(int id) {
        return getDto(fiveEdQualityCheckMapper.selectByPrimaryKey(id));
    }

    public int getNewModel(int stepId, String userLogin) {
        Date now = new Date();
        EdProjectStep edProjectStep=edProjectStepService.getModelById(stepId);
        Map information = edService.getNewInformation(stepId, EdConst.FIVE_ED_PRODUCT, userLogin);
        BusinessContractDto businessContract = (BusinessContractDto) information.get("businessContract");
        String totalDesigner=businessContract.getBusinessContractDetail().getTotalDesigner();
        List<String> chargeMen= MyStringUtil.getStringList(businessContract.getChargeMen());
        FiveEdQualityCheck item = new FiveEdQualityCheck();
        item.setProjectId(edProjectStep.getProjectId());
        item.setProjectName(edProjectStep.getProjectName());
        item.setProjectNo(information.get("projectNo").toString());
        item.setStageName(edProjectStep.getStageName());
        item.setStepName(edProjectStep.getStepName());
        item.setStepId(edProjectStep.getId());
        item.setContractNo(edProjectStep.getContractNo());
        item.setDeptId(businessContract.getDeptId());
        item.setDeptName(businessContract.getDeptName());
        item.setCreatorName(information.get("userName").toString());
        item.setFormNo(information.get("formNo").toString());
        item.setStepId(stepId);
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setGmtModified(now);
        item.setGmtCreate(now);

        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", information.get("processDescription")+"-设计质量抽查");
        variables.put("totalDesigner",totalDesigner);
        variables.put("chargeMen",chargeMen);
        ModelUtil.setNotNullFields(item);
        fiveEdQualityCheckMapper.insert(item);

        String businessKey = EdConst.FIVE_ED_QUALITY_CHECK + "_" + item.getProjectId() + "_" + item.getId();
        item.setBusinessKey(businessKey);
        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_ED_QUALITY_CHECK, businessKey, variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        fiveEdQualityCheckMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public void update(FiveEdQualityCheck item) {
        FiveEdQualityCheck model = fiveEdQualityCheckMapper.selectByPrimaryKey(item.getId());
        model.setStepName(item.getStepName());
        model.setMajorName(item.getMajorName());
        model.setDesignMen(item.getDesignMen());
        model.setCheckMan(item.getCheckMan());
        model.setRemark(item.getRemark());
        model.setQualityDepartmentMen(item.getQualityDepartmentMen());
        model.setQualityDepartmentMenName(item.getQualityDepartmentMenName());
        model.setGmtModified(new Date());

        actService.setVariables(model.getProcessInstanceId(), "qualityDepartmentMen", model.getQualityDepartmentMen());

        BeanValidator.check(model);
        fiveEdQualityCheckMapper.updateByPrimaryKey(model);
    }

    public void remove(int id, String userLogin) {
        FiveEdQualityCheck item = fiveEdQualityCheckMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin), "只允许创建人删除!");

        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }

    private FiveEdQualityCheckDto getDto(FiveEdQualityCheck item) {
        FiveEdQualityCheckDto dto = FiveEdQualityCheckDto.adapt(item);
        dto.setProcessName("已完成");
        if (!dto.getProcessEnd()){
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");

            if (customProcessInstance == null || customProcessInstance.isFinished()) {
                item.setProcessEnd(true);
                fiveEdQualityCheckMapper.updateByPrimaryKey(item);
                dto.setProcessEnd(true);
            }
            if (customProcessInstance != null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());

            }
        }

        return dto;
    }

    public int getNewDetail(int qualityCheckId){
        FiveEdQualityCheck item=getModelById(qualityCheckId);
        Map params=Maps.newHashMap();
        params.put("qualityCheckId",qualityCheckId);
        int seq= (int) (PageHelper.count(()->fiveEdQualityCheckDetailMapper.selectAll(params))+1);
        FiveEdQualityCheckDetail detail=new FiveEdQualityCheckDetail();
        detail.setSeq(seq);
        detail.setQualityCheckId(qualityCheckId);
        ModelUtil.setNotNullFields(detail);
        fiveEdQualityCheckDetailMapper.insert(detail);
        return detail.getId();
    }

    public FiveEdQualityCheckDetail getDetailById(int id){
        return fiveEdQualityCheckDetailMapper.selectByPrimaryKey(id);
    }

    public void removeDetail(int id,String userLogin){
        int qualityCheckId = fiveEdQualityCheckDetailMapper.selectByPrimaryKey(id).getQualityCheckId();
        FiveEdQualityCheck fiveEdQualityCheck = fiveEdQualityCheckMapper.selectByPrimaryKey(qualityCheckId);
        Assert.state(fiveEdQualityCheck.getCreator().equalsIgnoreCase(userLogin),"只能由创建人删除");
        fiveEdQualityCheckDetailMapper.deleteByPrimaryKey(id);
    }

    public List<FiveEdQualityCheckDetail> listDetail(int qualityCheckId){
        Map params=Maps.newHashMap();
        params.put("qualityCheckId",qualityCheckId);
        return fiveEdQualityCheckDetailMapper.selectAll(params);
    }

    public void updateDetail(FiveEdQualityCheckDetail detail){
        FiveEdQualityCheckDetail model=fiveEdQualityCheckDetailMapper.selectByPrimaryKey(detail.getId());
        model.setDrawNo(detail.getDrawNo());
        model.setMarkCategory(detail.getMarkCategory());
        model.setMarkContent(detail.getMarkContent());
        model.setRuleNo(detail.getRuleNo());
        model.setSolutionContent(detail.getSolutionContent());
        model.setSeq(detail.getSeq());
        fiveEdQualityCheckDetailMapper.updateByPrimaryKey(model);
    }

    public XWPFTemplate getXWPFTemplate(String filePath,int id) {
        FiveEdQualityCheck item = fiveEdQualityCheckMapper.selectByPrimaryKey(id);
        //自定义策略
        Configure.ConfigureBuilder configureBuilder = Configure.newBuilder().customPolicy("detail_table",
                new AddRowTablePolicy(2,6,10));
        //自定义标签
        configureBuilder.addPlugin('%', new FlowsTableRenderPolicy());
        configureBuilder.addPlugin('$', new CheckTextRenderPolicy(2));
        XWPFTemplate xwpfTemplate = XWPFTemplate.compile(filePath,configureBuilder.build()).render(new HashMap<String, Object>(){{
            put("formNo",item.getFormNo());
            put("projectName", item.getProjectName());
            put("projectNo", item.getProjectNo());
            put("stepName", item.getStepName());
            put("stageName", item.getStageName());

            put("deptName", item.getDeptName());
            put("majorName", item.getMajorName());
            put("designMen", item.getDesignMen());
            put("checkMan", item.getCheckMan());
            put("qualityDepartmentMenName", item.getQualityDepartmentMenName());
            put("remark", item.getRemark());
            //子表

            List<List<String>> detail=Lists.newArrayList();
            List<FiveEdQualityCheckDetail> fiveEdQualityCheckDetails=listDetail(item.getId());

            for(int i=0;i<fiveEdQualityCheckDetails.size();i++){
                List<String> row = new ArrayList<>();
                row.add(String.valueOf(i+1));
                row.add(fiveEdQualityCheckDetails.get(i).getDrawNo());
                row.add(fiveEdQualityCheckDetails.get(i).getMarkCategory());
                row.add(fiveEdQualityCheckDetails.get(i).getMarkContent());
                row.add(fiveEdQualityCheckDetails.get(i).getRuleNo());
                row.add(fiveEdQualityCheckDetails.get(i).getSolutionContent());

                detail.add(row);
            }

            put("detail_table", detail);
            //流程表格
            put("flows", ExportUtils.getFlowTable(actService.listPassedHistoryDto
                    (item.getProcessInstanceId()),18.85f));

        }});
        return xwpfTemplate;
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref, int pageNum, int pageSize) {
        params.put("deleted",false);

        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveEdQualityCheckMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveEdQualityCheck item=(FiveEdQualityCheck)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }
}
