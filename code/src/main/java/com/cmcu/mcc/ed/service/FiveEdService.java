package com.cmcu.mcc.ed.service;


import com.cmcu.act.dto.CustomProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.common.dao.CommonEdArrangeMapper;
import com.cmcu.common.dao.CommonEdArrangeUserMapper;
import com.cmcu.common.dto.CommonFileDto;
import com.cmcu.common.entity.*;
import com.cmcu.common.service.*;
import com.cmcu.common.util.JsonMapper;

import com.cmcu.mcc.ed.dao.EdProjectTreeMapper;
import com.cmcu.mcc.ed.entity.EdProjectTree;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.activiti.engine.HistoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FiveEdService {


    @Resource
    CommonFormDataService commonFormDataService;


    @Resource
    EdProjectTreeMapper edProjectTreeMapper;

    @Resource
    CommonFileService commonFileService;



    @Resource
    CommonEdArrangeMapper commonEdArrangeMapper;

    @Resource
    CommonEdArrangeUserMapper commonEdArrangeUserMapper;

    @Resource
    CommonEdArrangeUserService commonEdArrangeUserService;

    @Resource
    ProcessQueryService processQueryService;

    @Resource
    IEdDataService edDataService;

    @Resource
    CommonUserService commonUserService;

    @Resource
    HistoryService historyService;


    /**
     * 得到设计与输入评审文件列表
     * @param treeNodeId
     * @return
     */
    public List<Map> listInputReviewFile(int treeNodeId){
        List<Map> list= Lists.newArrayList();
        EdProjectTree inputReviewListNode=edProjectTreeMapper.selectByPrimaryKey(treeNodeId);
        if(inputReviewListNode.getNodeUrl().equalsIgnoreCase("five.detail.doc")) {
            Map params = Maps.newHashMap();
            params.put("deleted", false);
            params.put("parentId", inputReviewListNode.getParentId());
            params.put("projectId", inputReviewListNode.getProjectId());

            List<EdProjectTree> stepNodeList=edProjectTreeMapper.selectAll(params).stream().sorted(Comparator.comparing(EdProjectTree::getSeq)).collect(Collectors.toList());
            for(EdProjectTree stepNode:stepNodeList){
                if(stepNode.getNodeUrl().equalsIgnoreCase("five.detail.stepShow")){
                    Map formParams=Maps.newHashMap();
                    formParams.put("deleted",false);
                    formParams.put("referType","fiveEdInputReview");
                    formParams.put("referId",stepNode.getForeignKey());
                    if(PageHelper.count(()->commonFormDataService.commonFormDataMapper.selectAll(formParams))>0){
                        List<CommonFormData> commonFormDataList=commonFormDataService.commonFormDataMapper.selectAll(formParams).stream().sorted(Comparator.comparing(CommonFormData::getId)).collect(Collectors.toList());
                        for(CommonFormData commonFormData:commonFormDataList) {
                            CustomProcessInstance customProcessInstance = processQueryService.getCustomProcessInstance("", commonFormData.getBusinessKey(), "");
                            List<CommonFileDto> files = commonFileService.listData(commonFormData.getBusinessKey(), -1, "");
                            for(CommonFileDto file:files){
                                Map item=Maps.newHashMap();
                                item.put("stepName",stepNode.getNodeName());
                                item.put("id",file.getId());
                                item.put("businessKey",file.getBusinessKey());
                                item.put("fileId",file.getId());
                                item.put("dirPath",file.getDirPath());
                                item.put("fileType",file.getFileType());
                                item.put("fileName",file.getFileName());
                                item.put("gmtCreate",file.getGmtCreate());
                                item.put("creatorName",file.getCreatorName());
                                item.put("stateName",customProcessInstance.isFinished()?"已审核":"待审核");
                                item.put("commonAttach",file.getCommonAttach());
                                list.add(item);
                            }
                        }
                    }
                }
            }

        }
        return list;
    }



    /**
     * wuzhou 从设计与任务树中初始化人员数据
     * @param arrangeFormDataId
     */
    public void initArrangeUserFromLatestEdTask(int arrangeFormDataId) {

        CommonFormData arrangeFormData =commonFormDataService.commonFormDataMapper.selectByPrimaryKey(arrangeFormDataId);
        String businessKey = arrangeFormData.getBusinessKey();

        Map edTaskParams = Maps.newHashMap();
        edTaskParams.put("deleted", false);
        edTaskParams.put("referId", arrangeFormData.getReferId());
        edTaskParams.put("referType", "fiveEdTask");
        edTaskParams.put("tenetId", arrangeFormData.getTenetId());
        List<CommonFormData> taskList = commonFormDataService.commonFormDataMapper.selectAll(edTaskParams);
        Assert.state(taskList.size() > 0, "未发现任何设计任务书!");

        CommonFormData taskFormData = taskList.stream().sorted(Comparator.comparing(CommonFormData::getId).reversed()).findFirst().get();
        String taskBusinessKey = taskFormData.getBusinessKey();


        Map params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("businessKey", taskBusinessKey);
        List<CommonEdArrange> preArrangeList = commonEdArrangeMapper.selectAll(params);
        params.put("businessKey", arrangeFormData.getBusinessKey());
        for (CommonEdArrange preArrange : preArrangeList) {
            params.put("buildName", preArrange.getBuildName());
            params.put("majorName", preArrange.getMajorName());
            int destArrangeId = 0;
            if (PageHelper.count(() -> commonEdArrangeMapper.selectAll(params)) > 0) {
                destArrangeId = commonEdArrangeMapper.selectAll(params).get(0).getId();
            } else {
                CommonEdArrange commonEdArrange = new CommonEdArrange();
                BeanUtils.copyProperties(preArrange, commonEdArrange);
                commonEdArrange.setBusinessKey(businessKey);
                commonEdArrangeMapper.insert(commonEdArrange);
                destArrangeId = commonEdArrange.getId();
            }


            Map userParams = Maps.newHashMap();
            userParams.put("deleted", false);
            userParams.put("businessKey", taskBusinessKey);
            userParams.put("arrangeId", preArrange.getId());
            List<CommonEdArrangeUser> preUserList = commonEdArrangeUserMapper.selectAll(userParams);
            for (CommonEdArrangeUser preUser : preUserList) {
                userParams.put("businessKey", businessKey);
                userParams.put("arrangeId", destArrangeId);
                userParams.put("roleCode",preUser.getRoleCode());
                if (PageHelper.count(() -> commonEdArrangeUserMapper.selectAll(userParams)) > 0) {
                    CommonEdArrangeUser user = commonEdArrangeUserMapper.selectAll(userParams).get(0);
                    user.setAllUser(preUser.getAllUser());
                    user.setAllUserName(preUser.getAllUserName());
                    user.setGmtModified(new Date());
                    commonEdArrangeUserMapper.updateByPrimaryKey(user);
                } else {
                    CommonEdArrangeUser user = new CommonEdArrangeUser();
                    BeanUtils.copyProperties(preUser, user);
                    user.setBusinessKey(businessKey);
                    user.setArrangeId(destArrangeId);
                    commonEdArrangeUserMapper.insert(user);
                }
            }
        }


        Map arrangeData = JsonMapper.string2Map(arrangeFormData.getFormData());
        Map taskData = JsonMapper.string2Map(taskFormData.getFormData());

        arrangeData.put("majorChargeMen", taskData.getOrDefault("majorChargeMen", ""));
        arrangeData.put("majorChargeMenName", taskData.getOrDefault("majorChargeMenName", ""));

        List<String> arrangeMajorList = JsonMapper.getListValue(arrangeData, "majorList");
        List<String> taskMajorList = JsonMapper.getListValue(taskData, "majorList");
        for (String taskMajor : taskMajorList) {
            if (!arrangeMajorList.contains(taskMajor)) {
                arrangeMajorList.add(taskMajor);
            }
        }
        arrangeData.put("majorList", arrangeMajorList.stream().distinct().collect(Collectors.toList()));

        arrangeFormData.setFormData(JsonMapper.obj2String(arrangeData));
        commonFormDataService.commonFormDataMapper.updateByPrimaryKey(arrangeFormData);
        commonFormDataService.updateProcessVariables(arrangeFormData.getProcessInstanceId(),arrangeData);


        edDataService.checkAttendUser(businessKey);
    }






}
