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
import com.cmcu.mcc.ed.entity.EdProjectStep;
import com.cmcu.mcc.ed.service.EdProjectStepService;
import com.cmcu.mcc.ed.service.EdService;
import com.cmcu.mcc.five.dao.FiveEdQualityIssueMapper;
import com.cmcu.mcc.five.dto.FiveEdQualityIssueDto;
import com.cmcu.mcc.five.entity.FiveEdQualityIssue;
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
public class FiveEdQualityIssueService{

    @Resource
    FiveEdQualityIssueMapper fiveEdQualityIssueMapper;

    @Autowired
    MyActService myActService;

    @Autowired
    ProcessQueryService processQueryService;

    @Autowired
    EdService edService;

    @Autowired
    EdProjectStepService edProjectStepService;
    @Autowired
    TaskHandleService taskHandleService;
    @Resource
    HandleFormService handleFormService;


    public void remove(int id, String userLogin) {
        FiveEdQualityIssue item=fiveEdQualityIssueMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");

        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }

    public void update(FiveEdQualityIssueDto dto) {
        FiveEdQualityIssue item=fiveEdQualityIssueMapper.selectByPrimaryKey(dto.getId());
        item.setDesignMen(dto.getDesignMen());
        item.setCheckMan(dto.getCheckMan());
        item.setInformationSource(dto.getInformationSource());
        item.setIssueContent(dto.getIssueContent());
        item.setSolutionContent(dto.getSolutionContent());
        item.setCheckMan(dto.getCheckMan());
        item.setChargeMan(dto.getChargeMan());
        item.setDesignMen(dto.getDesignMen());
        item.setRemark(dto.getRemark());
        item.setGmtModified(new Date());
        ModelUtil.setNotNullFields(item);
        BeanValidator.check(item);
        fiveEdQualityIssueMapper.updateByPrimaryKey(item);

    }

    public FiveEdQualityIssueDto getModelById(int id) {
        return getDto(fiveEdQualityIssueMapper.selectByPrimaryKey(id));
    }

    public int getNewModel(int stepId, String userLogin) {
        Map information = edService.getNewInformation(stepId, EdConst.FIVE_ED_QUALITY_ISSUE, userLogin);
        EdProjectStep edProjectStep=edProjectStepService.getModelById(stepId);
        BusinessContractDto businessContract = (BusinessContractDto) information.get("businessContract");
        String totalDesigner=businessContract.getBusinessContractDetail().getTotalDesigner();
        String chargeMen=businessContract.getChargeMen();

        FiveEdQualityIssue model =new FiveEdQualityIssue();
        model.setTotalDesigner(totalDesigner);
        model.setTotalDesignerName(businessContract.getBusinessContractDetail().getTotalDesignerName());
        model.setChargeMen(chargeMen);
        model.setChargeMenName(businessContract.getChargeMenName());
        model.setReviewType(businessContract.getBusinessContractDetail().getReviewType());
        model.setStepId(stepId);
        model.setProjectName(edProjectStep.getProjectName());
        model.setProjectNo(information.get("projectNo").toString());
        model.setProjectId(edProjectStep.getProjectId());
        model.setStepName(edProjectStep.getStepName());
        model.setStageName(edProjectStep.getStageName());
        model.setContractNo(edProjectStep.getContractNo());
        model.setFormNo("TXC-ZYWJ-SJ-08-2018a");
        model.setCreatorName(information.get("userName").toString());
        model.setDeleted(false);
        model.setProcessEnd(false);
        model.setCreator(userLogin);
        model.setGmtModified(new Date());
        model.setGmtCreate(new Date());

        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", information.get("processDescription").toString()+"重大质量问题");

        //部门负责人暂时去得 分期负责人
        variables.put("deptChargeMen", MyStringUtil.getStringList(chargeMen));

        ModelUtil.setNotNullFields(model);
        fiveEdQualityIssueMapper.insert(model);

        String businessKey=EdConst.FIVE_ED_QUALITY_ISSUE + "_" + model.getProjectId() + "_" + model.getId();
        model.setBusinessKey(businessKey);

        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_ED_QUALITY_ISSUE,model.getBusinessKey(), variables, MccConst.APP_CODE);

        model.setProcessInstanceId(processInstanceId);
        fiveEdQualityIssueMapper.updateByPrimaryKey(model);
        return model.getId();
    }

    public FiveEdQualityIssueDto getDto(FiveEdQualityIssue item) {

        FiveEdQualityIssueDto dto= FiveEdQualityIssueDto.adapt(item);
        dto.setProcessName("已完成");

        if (!dto.getProcessEnd()){
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");
            if(customProcessInstance == null || customProcessInstance.isFinished()) {
                dto.setProcessEnd(true);
                fiveEdQualityIssueMapper.updateByPrimaryKey(dto);
            }
            if(customProcessInstance != null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }

        return dto;
    }

    public List<FiveEdQualityIssueDto> listDataByForeignKey(int stepId) {
        Map params = Maps.newHashMap();
        params.put("deleted", "0");
        params.put("stepId", stepId);
        List<FiveEdQualityIssue> oList = fiveEdQualityIssueMapper.selectAll(params);
        List<FiveEdQualityIssueDto> list = Lists.newArrayList();
        oList.forEach(p -> list.add(getDto(p)));
        return list;
    }
    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();//返回的map
//        FiveEdQualityIssue item = fiveEdQualityIssueMapper.selectByPrimaryKey(id);//修改为对应查询的实体类
//        data.put("formNo",item.getFormNo());
//        data.put("projectName", item.getProjectName());
//        data.put("examineLeve", item.getExamineLeve());
//        data.put("designMan", item.getDesignMan());
//        data.put("checkMan", item.getCheckMan());
//        data.put("informationSource", item.getInformationSource());
//        data.put("designContent", item.getDesignContent());
//        data.put("dealContent", item.getDealContent());
//        data.put("remark", item.getRemark());
//
//        ProcessInstanceDto processInstanceDto=actService.getProcessInstanceDto(item.getProcessInstanceId());
        return data;
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref, int pageNum, int pageSize) {
        params.put("deleted",false);

        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveEdQualityIssueMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveEdQualityIssue item=(FiveEdQualityIssue)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

}
