package com.cmcu.mcc.ed.service;

import com.cmcu.act.dto.CustomProcessInstance;
import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessHandleService;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.dao.CommonFormDataMapper;
import com.cmcu.common.dto.TplConfigDto;
import com.cmcu.common.dto.UserDto;
import com.cmcu.common.entity.CommonForm;
import com.cmcu.common.entity.CommonFormData;
import com.cmcu.common.entity.CommonFormDetail;
import com.cmcu.common.service.CommonDirService;
import com.cmcu.common.service.CommonFormDataService;
import com.cmcu.common.service.CommonFormService;
import com.cmcu.common.service.CommonUserService;
import com.cmcu.common.util.JsonMapper;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.ed.dao.EdProjectStepMapper;
import com.cmcu.mcc.ed.dao.EdProjectTreeMapper;
import com.cmcu.mcc.ed.entity.EdProjectStep;
import com.cmcu.mcc.ed.entity.EdProjectTree;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class EdFormService {

    @Resource
    EdProjectTreeMapper edProjectTreeMapper;

    @Resource
    EdProjectStepMapper edProjectStepMapper;

    @Resource
    CommonUserService commonUserService;

    @Resource
    ProcessQueryService processQueryService;

    @Resource
    ProcessHandleService processHandleService;

    @Resource
    CommonFormService commonFormService;

    @Resource
    CommonFormDataService commonFormDataService;

    @Resource
    CommonFormDataMapper commonFormDataMapper;

    @Resource
    CommonDirService commonDirService;


    @Resource
    TaskHandleService taskHandleService;




    /**
     * 获取数据
     * @param nodeId
     * @param enLogin
     * @return
     */
    public List<Map> listDataByNodeId(int nodeId, String enLogin, String excludeInfo) {
        List<Map> list = Lists.newArrayList();
        EdProjectTree tree = edProjectTreeMapper.selectByPrimaryKey(nodeId);
        String referType = tree.getReferType();
        if (StringUtils.isNotEmpty(referType)) {
            Map params = Maps.newHashMap();
            params.put("referTypeList", MyStringUtil.getStringList(referType));
            params.put("referId", tree.getForeignKey());
            params.put("deleted", false);
            List<CommonFormData> formDataList = commonFormDataMapper.selectAll(params);
            for (CommonFormData formData : formDataList) {
                Map data = com.common.util.JsonMapper.string2Map(formData.getFormData());
                Map item = Maps.newHashMap();
                item.put("id", formData.getId());
                item.put("businessKey", formData.getBusinessKey());
                item.put("initiatorName", formData.getCreatorName());
                item.put("initiatorTime", formData.getGmtCreate());
                item.put("processEnd", formData.getProcessEnd());
                item.put("processDefinitionName", tree.getNodeName());
                Map formDataInfo = commonFormDataService.getFormDataInfo(formData.getBusinessKey(), excludeInfo);
                item.put("keyInfo", formDataInfo.getOrDefault("keyInfo", "").toString());
                item.put("modelCategory", formDataInfo.getOrDefault("modelCategory", "").toString());
                item.put("first", true);
                item.put("majorName", data.getOrDefault("majorName", ""));
                item.put("haveContent", commonDirService.haveContent(formData.getBusinessKey(), 0));
                if (StringUtils.isNotEmpty(formData.getProcessInstanceId())) {
                    CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance("", formData.getBusinessKey(), enLogin);
                    if (customProcessInstance != null) {
                        item.put("currentTaskName", customProcessInstance.getCurrentTaskName());
                        item.put("myRunningTaskName", customProcessInstance.getMyRunningTaskName());
                        item.put("taskId", customProcessInstance.getTaskId());
                        item.put("first", customProcessInstance.isFirstTask());

                        if (customProcessInstance.getInstance() != null) {
                            item.put("processDefinitionName", customProcessInstance.getInstance().getProcessDefinitionName());
                            item.put("processInstanceId", customProcessInstance.getInstance().getId());
                        }
                    }


                    if (!formData.getProcessEnd() &&(customProcessInstance==null||customProcessInstance.isFinished())) {
                        formData.setProcessEnd(true);
                        commonFormDataMapper.updateByPrimaryKey(formData);
                        item.put("processEnd", true);
                    }
                }
                list.add(item);
            }
        }
        return list;
    }





    /**
     * 获取数据
     * @param nodeId
     * @return
     */
    public long getDataCount(int nodeId) {
        EdProjectTree tree = edProjectTreeMapper.selectByPrimaryKey(nodeId);
        if (StringUtils.isNotEmpty(tree.getReferType())) {
            Map params = Maps.newHashMap();
            params.put("referTypeList", MyStringUtil.getStringList(tree.getReferType()));
            params.put("referId", tree.getForeignKey());
            params.put("deleted",false);
            return PageHelper.count(()->commonFormDataService.commonFormDataMapper.selectAll(params));
        }
        return 0;
    }


    @Transactional
    public CommonFormData  getNewModel(int nodeId,String enLogin) {

        EdProjectTree treeData = edProjectTreeMapper.selectByPrimaryKey(nodeId);
        String referType = treeData.getReferType();
        int referId = treeData.getForeignKey();
        Assert.state(StringUtils.isNotEmpty(referType), "表单标识不能为空!");

        UserDto userDto = commonUserService.selectByEnLogin(enLogin);
        String tenetId = userDto.getTenetId();
        Map allVariables = getAllData(treeData.getForeignKey(), enLogin);

        Map data = Maps.newHashMap();
        CommonForm commonForm = commonFormService.getModel(tenetId, referType, 0);
        List<CommonFormDetail> details = commonFormService.listDetail(commonForm.getId());
        details.forEach(p -> data.put(p.getInputCode(), allVariables.getOrDefault(p.getInputCode(), "")));

        CommonFormData item = new CommonFormData();
        item.setTenetId(tenetId);
        item.setReferType(referType);
        item.setFormData(JsonMapper.obj2String(data));
        item.setDeptId(userDto.getDeptId());
        item.setDeptName(userDto.getDeptName());
        item.setFormKey(referType);
        item.setFormVersion(commonForm.getFormVersion());
        item.setReferId(referId);
        item.setCreator(enLogin);
        item.setCreatorName(userDto.getCnName());
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setSeq(1);
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        ModelUtil.setNotNullFields(item);
        commonFormDataService.commonFormDataMapper.insert(item);

        TplConfigDto tplConfigDto = TplConfigDto.getInstance(commonForm.getOtherTplConfig());
        String businessKey = item.getFormKey() + "_" + item.getId();
        item.setBusinessKey(businessKey);
        if (StringUtils.isNotEmpty(tplConfigDto.getProcessKey())) {
            item.setProcessEnd(false);
            Map processVariables = Maps.newHashMap();
            processVariables.put("initiator", enLogin);
            String processInstanceId = taskHandleService.startProcess(tplConfigDto.getProcessKey(), businessKey, processVariables, tenetId);
            item.setProcessInstanceId(processInstanceId);
        }
        commonFormDataService.commonFormDataMapper.updateByPrimaryKey(item);
        return item;
    }


    private Map getAllData(int stepId, String enLogin) {
        Map data = commonFormDataService.getUserBasicVariable(enLogin);
        EdProjectStep edProjectStep = edProjectStepMapper.selectByPrimaryKey(stepId);
        if (edProjectStep != null) {
            Map<String, Object> map = JsonMapper.string2Map(JsonMapper.obj2String(edProjectStep));
            data.putAll(map);
        }
        return data;
    }

}
