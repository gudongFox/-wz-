package com.cmcu.act.service;


import com.cmcu.act.dto.ActBpmnModel;
import com.cmcu.act.dto.ActCommentDto;
import com.cmcu.act.dto.CustomHistoricTaskInstance;
import com.cmcu.act.extend.IPropertyConstants;
import com.cmcu.act.extend.IVariableConstants;
import com.cmcu.common.dto.CommonCodeDto;
import com.cmcu.common.dto.FastUserDto;
import com.cmcu.common.dto.UserDto;
import com.cmcu.common.entity.CommonCode;
import com.cmcu.common.service.*;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.common.util.WebUtil;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.util.SimpleContext;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.persistence.entity.CommentEntityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.el.ExpressionFactory;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TaskQueryService implements IVariableConstants, IPropertyConstants  {


    @Resource
    TaskService taskService;

    @Resource
    RuntimeService runtimeService;

    @Resource
    HistoryService historyService;

    @Resource
    CommonUserService commonUserService;

    @Resource
    CommonFormDataService commonFormDataService;

    @Resource
    IHandleFormService handleFormService;

    @Resource
    ActCacheService actCacheService;

    @Resource
    CommonActActionService commonActActionService;

    @Resource
    CommonCodeService commonCodeService;


    @Resource
    CommonDirService commonDirService;



    /**
     * 查询待办任务（包含抄送）
     * @param pageNum
     * @param pageSize
     * @param enLogin
     * @param params
     * @return
     */
    public PageInfo<Object> listPagedTask(int pageNum,int pageSize,String enLogin,Map params) {
        List<String> paramKeys = Lists.newArrayList("onlyCc", "containCc", "modelCategory", "taskName", "processDefinitionKey", "businessKey", "processDescription", "processDefinitionName", "initiatorName", "qInitiator");
        int total = 0;


        List<Map> list = Lists.newArrayList();
        if (paramKeys.stream().anyMatch(params::containsKey)) {
            int startIndex = (pageNum - 1) * pageSize;
            int endIndex = pageNum * pageSize - 1;
            List<Task> tasks = taskService.createTaskQuery().taskCandidateOrAssigned(enLogin).orderByTaskCreateTime().desc().list();
            if (params.containsKey("onlyCc")) {
                boolean onlyCc = Boolean.parseBoolean(params.get("onlyCc").toString());
                if (onlyCc) {
                    tasks = tasks.stream().filter(p -> StringUtils.isNotEmpty(p.getParentTaskId())).collect(Collectors.toList());
                }
            } else if (params.containsKey("containCc")) {
                boolean containCc = Boolean.parseBoolean(params.get("containCc").toString());
                if (!containCc) {
                    tasks = tasks.stream().filter(p -> StringUtils.isEmpty(p.getParentTaskId())).collect(Collectors.toList());
                }
            }

            for (Task task : tasks) {
                Map variables = getTaskVariables(task);
                if (IsGoodTask(task.getName(), task.getParentTaskId(), params, variables)) {
                    if (total >= startIndex && total <= endIndex) {
                        list.add(getRunningTaskMap(task, variables));
                    }
                    total++;
                }
            }

        } else {
            TaskQuery taskQuery = taskService.createTaskQuery().taskCandidateOrAssigned(enLogin).orderByTaskCreateTime().desc();
            total = (int) taskQuery.count();
            List<Task> tasks = taskQuery.listPage((pageNum - 1) * pageSize, pageSize);
            for (Task task : tasks) {
                Map variables = getTaskVariables(task);
                list.add(getRunningTaskMap(task, variables));
            }
        }
        return WebUtil.buildPageInfo(pageNum, pageSize, total, list);
    }


    public long getTaskCount(String enLogin,Map params) {
        long total= taskService.createTaskQuery().taskCandidateOrAssigned(enLogin).count();
        if(params.containsKey("containCc")){
            boolean containCc = Boolean.parseBoolean(params.get("containCc").toString());
            if (!containCc) {
                total=total-getCcTaskCount(enLogin,Maps.newHashMap());
            }
        }
        return total;
    }



    /**
     * 查询抄送任务 (包含待办和已完成的）
     * @param pageNum
     * @param pageSize
     * @param enLogin 用户
     * @param params 其他条件
     * @return
     */
    public PageInfo<Object> listPagedCcTask(int pageNum,int pageSize,String enLogin,Map params) {
        HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(enLogin).taskNameLike("%[抄送]%").orderByHistoricTaskInstanceStartTime().desc();
        long total = historicTaskInstanceQuery.count();
        List<Object> list = Lists.newArrayList();
        List<String> paramKeys = Lists.newArrayList("modelCategory","taskName","processDefinitionKey","businessKey","processDescription", "processDefinitionName","initiatorName","qInitiator");
        if(paramKeys.stream().anyMatch(params::containsKey)){
            int startIndex = (pageNum -1)* pageSize;
            int endIndex = pageNum  * pageSize - 1;
            total = 0;

            List<HistoricTaskInstance> all=historicTaskInstanceQuery.list();
            for (HistoricTaskInstance task : all) {
                Map variables = getTaskVariables(task);
                if (IsGoodTask(task.getName(),task.getParentTaskId(),params, variables)) {
                    if (total >= startIndex && total <= endIndex) {
                        list.add(getTaskMap(task,variables,enLogin));
                    }
                    total++;
                }
            }
        }else{
            List<HistoricTaskInstance> oList = historyService.createHistoricTaskInstanceQuery()
                    .taskAssignee(enLogin).taskNameLike("%[抄送]%").orderByHistoricTaskInstanceStartTime().desc()
                    .listPage((pageNum - 1) * pageSize, pageSize);
            oList.forEach(p -> list.add(getTaskMap(p,null,enLogin)));
        }

        return WebUtil.buildPageInfo(pageNum, pageSize, (int) total, list);
    }


    /**
     * 得到抄送任务数量
     * @param enLogin
     * @param params
     * @return
     */
    public long getCcTaskCount(String enLogin,Map params) {
        long total = 0;
        TaskQuery taskQuery = taskService.createTaskQuery().taskAssignee(enLogin).taskNameLikeIgnoreCase("%[抄送]%");
        List<String> paramKeys = Lists.newArrayList("modelCategory", "taskName", "processDefinitionKey", "businessKey", "processDescription", "processDefinitionName", "initiatorName", "qInitiator");
        if (paramKeys.stream().noneMatch(params::containsKey)) {
            total = taskQuery.count();
        } else {
            for (Task task : taskQuery.list()) {
                Map variables = getTaskVariables(task);
                if (IsGoodTask(task.getName(), task.getParentTaskId(), params, variables)) {
                    total++;
                }
            }
        }
        return total;
    }


    /**
     * 已办
     * @param params
     * @param enLogin
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo<Object> listPagedHistoricTask(Map params, String enLogin, int pageNum, int pageSize) {
        HistoricTaskInstanceQuery taskQuery = historyService.createHistoricTaskInstanceQuery().includeProcessVariables().taskAssignee(enLogin).finished();
        if (params.containsKey(PROCESS_DESCRIPTION)) {
            taskQuery=taskQuery.processVariableValueLikeIgnoreCase(PROCESS_DESCRIPTION, "%" + params.get(PROCESS_DESCRIPTION).toString() + "%");
        }
        if (params.containsKey("q")) {
            taskQuery=taskQuery.processVariableValueLikeIgnoreCase(PROCESS_DEFINITION_NAME, "%" + params.get("q").toString() + "%");
        } else if (params.containsKey(PROCESS_DEFINITION_NAME)) {
            taskQuery=taskQuery.processVariableValueLikeIgnoreCase(PROCESS_DEFINITION_NAME, "%" + params.get(PROCESS_DEFINITION_NAME).toString() + "%");
        }
        List<HistoricTaskInstance> oList = taskQuery.orderByHistoricTaskInstanceStartTime().desc().listPage((pageNum - 1) * pageSize, pageSize);
        List<Map> list = Lists.newArrayList();
        oList.stream().filter(Objects::nonNull).forEach(p -> list.add(getTaskMap(p,null,enLogin)));
        return WebUtil.buildPageInfo(pageNum, pageSize, (int) taskQuery.count(), list);
    }

    private Map getTaskMap(HistoricTaskInstance task,Map variables,String enLogin) {
        Map item = Maps.newHashMap();
        try {
            if(variables==null) {
                variables = getTaskVariables(task);
            }

            if (StringUtils.isNotEmpty(task.getParentTaskId())) {
                item.put("ccTask", true);
            }

            String businessKey = variables.getOrDefault("businessKey", "").toString();
            String initiator=variables.getOrDefault(INITIATOR, "").toString();
            item.put("id", task.getId());
            item.put(PROCESS_DEFINITION_NAME, variables.getOrDefault(PROCESS_DEFINITION_NAME, "").toString());
            item.put(PROCESS_DESCRIPTION, variables.getOrDefault(PROCESS_DESCRIPTION, "").toString());
            item.put(INITIATOR,initiator);
            item.put(INITIATOR_NAME, variables.getOrDefault(INITIATOR_NAME, "").toString());
            item.put(INITIATOR_TIME, variables.getOrDefault(INITIATOR_TIME, ""));

            //发起人附带部门名称（客户要求不显示）
            //FastUserDto userDto = commonUserService.getFastByEnLogin(item.get(INITIATOR);
            //item.put("initiatorNameDept", userDto.getCnName()+"("+userDto.getDeptName()+")");
            item.put("processInstanceId",task.getProcessInstanceId());
            item.put("assignee", task.getAssignee());
            item.put("businessKey", businessKey);
            item.put("owner", task.getOwner());
            item.put("takeAble", StringUtils.isEmpty(task.getAssignee()) && StringUtils.isEmpty(task.getOwner()));
            item.put("claimTime", task.getClaimTime());
            item.put("createTime", task.getCreateTime());
            item.put("name", task.getName());
            item.put("executionId", task.getExecutionId());
            item.put("dueDate", task.getDueDate());
            item.put("startTime", task.getStartTime());
            item.put("endTime", task.getEndTime());
            item.put("workTime", task.getWorkTimeInMillis());
            item.put("ccTask", false);
            item.put("stared",commonActActionService.getStared(task.getTenantId(),task.getProcessInstanceId(),enLogin));
            item.put("firstTask", false);
            if(initiator.equalsIgnoreCase(enLogin)&&StringUtils.isNotEmpty(task.getProcessDefinitionId())) {
                ActBpmnModel actBpmnModel = actCacheService.getActBpmnModel(task.getProcessDefinitionId());
                if (actBpmnModel.getFirstUserTaskList().contains(task.getTaskDefinitionKey())) {
                    item.put("firstTask", true);
                    if (historyService.createHistoricTaskInstanceQuery().processInstanceId(task.getProcessInstanceId()).count() == 1) {
                        item.put("firstNewTask", true);
                    }
                }
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return item;
    }

    public Map listTaskByH5(String enLogin,int firstResult, int maxResults,Map params) {
        Map result = Maps.newHashMap();
        String q=params.getOrDefault("q","").toString();
        String processDefinitionName=params.getOrDefault("processDefinitionName","").toString();
        List<Task> allTasks=taskService.createTaskQuery().taskCandidateOrAssigned(enLogin).orderByTaskCreateTime().desc().list();
        int count=0;
        List<Map> list = Lists.newArrayList();
        for(Task task:allTasks) {
            Map variables=getTaskVariables(task);
            if(StringUtils.isEmpty(q)&&StringUtils.isEmpty(processDefinitionName)){
                if(count>=firstResult&&list.size()<=maxResults) {
                    list.add(getRunningTaskMap(task,variables));
                }
                count++;
            }else{
                String processDefinitionId = task.getProcessDefinitionId();
                if (StringUtils.isEmpty(task.getProcessDefinitionId())) {
                    HistoricTaskInstance parentTask = historyService.createHistoricTaskInstanceQuery().taskId(task.getParentTaskId()).includeProcessVariables().singleResult();
                    if (parentTask != null) {
                        processDefinitionId=parentTask.getProcessDefinitionId();
                    }
                }
                ActBpmnModel actBpmnModel = actCacheService.getActBpmnModel(processDefinitionId);
                if (actBpmnModel != null) {
                    if(actBpmnModel.getName().equalsIgnoreCase(processDefinitionName)||(actBpmnModel.getName().contains(q)&&StringUtils.isNotEmpty(q))){
                        if(count>=firstResult&&list.size()<=maxResults) {
                            list.add(getRunningTaskMap(task,variables));
                        }
                        count++;
                    }
                }
            }
        }
        result.put("total", count);
        result.put("list", list);

        if(params.containsKey("fromCad")) {
            list.forEach(p -> p.put("haveContent", commonDirService.haveContent(p.get("businessKey").toString(), 0)));
        }


        return result;
    }


    public  List<Map> listTaskGroupByCad(String enLogin) {
        TaskQuery taskQuery = taskService.createTaskQuery().taskCandidateOrAssigned(enLogin);
        List<Task> tasks = taskQuery.orderByTaskCreateTime().desc().list();

        List<Map> list = Lists.newArrayList();
        for (Task task : tasks) {
            String processDefinitionId = task.getProcessDefinitionId();
            if (StringUtils.isEmpty(processDefinitionId) && StringUtils.isNotEmpty(task.getParentTaskId())) {
                HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery().taskId(task.getParentTaskId()).singleResult();
                if (historicTaskInstance != null) {
                    processDefinitionId = historicTaskInstance.getProcessDefinitionId();
                }
            }
            if(StringUtils.isNotEmpty(processDefinitionId)) {
                ActBpmnModel actBpmnModel = actCacheService.getActBpmnModel(processDefinitionId);
                if (actBpmnModel != null) {
                    String name = actBpmnModel.getName();
                    if (list.stream().anyMatch(p -> p.get("name").toString().equalsIgnoreCase(name))) {
                        Map item = list.stream().filter(p -> p.get("name").toString().equalsIgnoreCase(name)).findFirst().get();
                        item.put("total", (int) item.get("total") + 1);
                    } else {
                        Map item = Maps.newHashMap();
                        item.put("name", name);
                        item.put("total", 1);
                        list.add(item);
                    }
                }
            }
        }
        return list;
    }

    private Map getRunningTaskMap(Task task,Map variables) {
        Map item = Maps.newHashMap();
        try {
            item.put("id", task.getId());
            item.put("assignee", task.getAssignee());
            item.put("owner", task.getOwner());
            item.put("takeAble", StringUtils.isEmpty(task.getAssignee()) && StringUtils.isEmpty(task.getOwner()));
            item.put("claimTime", task.getClaimTime());
            item.put("createTime", task.getCreateTime());
            item.put("name", task.getName());
            item.put("executionId", task.getExecutionId());
            item.put("dueDate", task.getDueDate());
            long duration = new Date().getTime() - task.getCreateTime().getTime();
            item.put("duration", duration);
            item.put("durationRead", MyStringUtil.getDurationRead(duration));
            item.put("outTime", DateUtils.addDays(task.getCreateTime(), 15).getTime() <= new Date().getTime());
            String businessKey = variables.getOrDefault("businessKey", "").toString();
            String processInstanceId = variables.getOrDefault("processInstanceId", "").toString();
            Map formDataInfo = commonFormDataService.getFormDataInfo(businessKey, "");
            item.put("keyInfo", formDataInfo.getOrDefault("keyInfo", "").toString());
            item.put("modelCategory", formDataInfo.getOrDefault("modelCategory", "").toString());
            item.put(PROCESS_DEFINITION_NAME, variables.getOrDefault(PROCESS_DEFINITION_NAME, "").toString());
            item.put(PROCESS_DESCRIPTION, variables.getOrDefault(PROCESS_DESCRIPTION, "").toString());
            String initiator=variables.getOrDefault(INITIATOR,"").toString();
            item.put(INITIATOR, initiator);
            item.put(INITIATOR_NAME, variables.getOrDefault(INITIATOR_NAME, "").toString());
            item.put(INITIATOR_TIME, variables.getOrDefault(INITIATOR_TIME, ""));
            item.put("processInstanceId", variables.getOrDefault("processInstanceId", "").toString());
            item.put("businessKey", businessKey);
            item.put("ccTask", false);
            item.put("firstTask", false);
            item.put("firstNewTask", false);
            //FastUserDto userDto = commonUserService.getFastByEnLogin(initiator);
            //item.put("initiatorNameDept", userDto.getCnName() + "(" + userDto.getDeptName() + ")");
            if (StringUtils.isEmpty(task.getProcessInstanceId()) && StringUtils.isNotEmpty(task.getParentTaskId())) {
                item.put("ccTask", true);
            }else if(initiator.equalsIgnoreCase(task.getOwner())||initiator.equalsIgnoreCase(task.getAssignee())) {
                ProcessDefinition processDefinition = actCacheService.getProcessDefinitionById(task.getProcessDefinitionId());
                Boolean firstTask = actCacheService.getActBpmnModel(processDefinition.getId()).getFirstUserTaskList().contains(task.getTaskDefinitionKey());
                item.put("firstTask", firstTask);
                if (firstTask) {
                    if (historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).count() == 1) {
                        item.put("firstNewTask", true);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return item;
    }


    private boolean IsGoodTask(String taskName, String parentTaskId,Map params,Map variables) {

        //任务名称匹配
        if (params.containsKey("taskName")) {
            if (!taskName.contains(params.get("taskName").toString())) {
                return false;
            }
        }

        //抄送任务匹配
        if (params.containsKey("onlyCc")) {
            boolean onlyCc = Boolean.parseBoolean(params.get("onlyCc").toString());
            if (onlyCc) {
                if (StringUtils.isEmpty(parentTaskId)) {
                    return false;
                }
            }
        }else if(params.containsKey("containCc")){
            boolean containCc = Boolean.parseBoolean(params.get("containCc").toString());
            if (!containCc) {
                if (StringUtils.isNotEmpty(parentTaskId)) {
                    return false;
                }
            }
        }

        //关键流程变量匹配
        List<String> paramKeys = Lists.newArrayList("processDefinitionKey", "businessKey", "processDescription", "processDefinitionName", "initiatorName");
        for (String key : paramKeys) {
            if (params.containsKey(key)) {
                if (!variables.getOrDefault(key, "").toString().contains(params.getOrDefault(key, "").toString())) {
                    return false;
                }
            }
        }

        if(params.containsKey("qInitiator")){
            String qInitiator=params.get("qInitiator").toString();
            if(!variables.getOrDefault("initiator","").toString().contains(qInitiator)&&!variables.getOrDefault("initiatorName","").toString().contains(qInitiator)){
                return false;
            }
        }


        //流程分类
        if (params.containsKey("modelCategory")) {
            String tenetId = params.getOrDefault("tenetId", "").toString();
            String modelCategory = variables.getOrDefault("modelCategory", "").toString();
            List<CommonCodeDto> commonCodes = commonCodeService.listDataWithChild(tenetId, "流程类别", params.get("modelCategory").toString());
            List<String> categoryList = commonCodes.stream().map(CommonCode::getCode).distinct().collect(Collectors.toList());
            if (!categoryList.contains(modelCategory)) {
                return false;
            }
        }
        return true;
    }


//    private Map getTaskVariables(String  taskId) {
//
//        Map variables = Maps.newHashMap();
//        variables.put("ccTask",false);
//        HistoricTaskInstance historicTaskInstance = null;
//        if (taskService.createTaskQuery().taskId(taskId).count() > 0) {
//            Task task = taskService.createTaskQuery().taskId(taskId).includeProcessVariables().includeTaskLocalVariables().singleResult();
//            if (StringUtils.isNotEmpty(task.getParentTaskId())) {
//                variables.put("ccTask",true);
//                historicTaskInstance = historyService.createHistoricTaskInstanceQuery().taskId(task.getParentTaskId()).includeProcessVariables().includeTaskLocalVariables().singleResult();
//            } else {
//                variables = task.getProcessVariables();
//                variables.putAll(task.getTaskLocalVariables());
//                variables.put("processInstanceId", task.getProcessInstanceId());
//                variables.put("name",task.getName());
//            }
//        } else {
//            historicTaskInstance = historyService.createHistoricTaskInstanceQuery().taskId(taskId).includeProcessVariables().includeTaskLocalVariables().singleResult();
//            if (StringUtils.isNotEmpty(historicTaskInstance.getParentTaskId())) {
//                historicTaskInstance = historyService.createHistoricTaskInstanceQuery().taskId(historicTaskInstance.getParentTaskId()).includeProcessVariables().includeTaskLocalVariables().singleResult();
//            }
//        }
//
//        if (historicTaskInstance != null) {
//            variables.putAll(historicTaskInstance.getProcessVariables());
//            variables.putAll(historicTaskInstance.getTaskLocalVariables());
//            variables.put("processInstanceId", historicTaskInstance.getProcessInstanceId());
//            variables.put("name",historicTaskInstance.getName());
//        }
//        return variables;
//    }
//

    private Map getTaskVariables(TaskInfo  task) {

        Map variables = Maps.newHashMap();
        variables.put("ccTask",StringUtils.isNotEmpty(task.getParentTaskId()));

        String processInstanceId=task.getProcessInstanceId();
        if(StringUtils.isNotEmpty(task.getParentTaskId())){
            HistoricTaskInstance historicTaskInstance =historyService.createHistoricTaskInstanceQuery().taskId(task.getParentTaskId()).singleResult();
            if(historicTaskInstance!=null){
                processInstanceId=historicTaskInstance.getProcessInstanceId();
                variables.put("name",historicTaskInstance.getName());
            }
        }

        if(StringUtils.isNotEmpty(processInstanceId)){
            List<HistoricVariableInstance> historicVariableInstances = historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId).list();
            for (HistoricVariableInstance historicVariableInstance : historicVariableInstances) {
                variables.put(historicVariableInstance.getVariableName(), historicVariableInstance.getValue());
            }

            variables.put("processInstanceId", processInstanceId);

        }
        return variables;
    }



    public List<Map> listHistoricTaskMap(String processInstanceId) {
        List<Map> list = Lists.newArrayList();
        ActBpmnModel actBpmnModel = null;
        List<ActCommentDto> allComments = null;
        if (StringUtils.isNotEmpty(processInstanceId)) {
            List<HistoricTaskInstance> oList = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).orderByHistoricTaskInstanceStartTime().desc().list();
            for (HistoricTaskInstance task : oList) {
                if (actBpmnModel == null) actBpmnModel = actCacheService.getActBpmnModel(task.getProcessDefinitionId());
                if (allComments == null) {
                    allComments = listComment(taskService.getProcessInstanceComments(task.getProcessInstanceId()));
                }

                Map item = Maps.newHashMap();
                item.put("taskId",task.getId());
                item.put("name", task.getName());
                item.put("startTime", task.getStartTime());
                item.put("endTime", task.getEndTime());
                HistoricVariableInstance passedBackVariable = historyService.createHistoricVariableInstanceQuery().taskId(task.getId()).variableName(PASSED_BACKED_STATE).singleResult();
                if (passedBackVariable != null) item.put("passed", passedBackVariable.getValue());

                if (StringUtils.isNotEmpty(task.getAssignee())) {
                    item.put("assignee", task.getAssignee());
                    FastUserDto userDto = commonUserService.getFastByEnLogin(task.getAssignee());
                    item.put("assigneeName", userDto.getCnName());
                    item.put("avatar", userDto.getAvatar());
                    item.put("signPicUrl",userDto.getSignPicUrl());
                } else if (task.getEndTime() == null) {
                    List<String> candidateUsers = Lists.newArrayList();
                    List<IdentityLink> identityLinks = taskService.getIdentityLinksForTask(task.getId());
                    for (IdentityLink identityLink : identityLinks) {
                        candidateUsers.add(identityLink.getUserId());
                    }
                    String candidateUserNames = commonUserService.getCnNames(StringUtils.join(candidateUsers, ","));
                    if (candidateUsers.size() > 10)
                        candidateUserNames = candidateUserNames.substring(0, 5) + "...(" + candidateUserNames.length() + ")";

                    item.put("candidateUserNames", candidateUserNames);
                }
                String latestComment = "";
                List<ActCommentDto> comments = allComments.stream().filter(p -> p.getTaskId().equalsIgnoreCase(task.getId())).collect(Collectors.toList());
                item.put("comments", comments);
                if (comments.size() > 0) {
                    latestComment = comments.get(0).getMessage();
                    item.put("latestComment", latestComment);
                }

                if (actBpmnModel.getCopyUserTaskList().stream().anyMatch(p -> p.containsKey(task.getTaskDefinitionKey()))) {
                    List<String> ccUsers = Lists.newArrayList();
                    HistoricVariableInstance ccUserVariable = historyService.createHistoricVariableInstanceQuery().taskId(task.getId()).variableName(CC_USER).singleResult();
                    if (ccUserVariable != null)
                        ccUsers = MyStringUtil.getStringList(ccUserVariable.getValue().toString());
                    if(ccUsers.size()>0) {
                        for (HistoricTaskInstance cc : historyService.createHistoricTaskInstanceQuery().taskParentTaskId(task.getId()).list()) {
                            Map ccItem = Maps.newHashMap();
                            ccItem.put("name", cc.getName());
                            ccItem.put("startTime", cc.getStartTime());
                            ccItem.put("endTime", cc.getEndTime());
                            ccItem.put("passed", true);
                            if (StringUtils.isNotEmpty(cc.getAssignee())) {
                                UserDto ccUser = commonUserService.selectByEnLogin(cc.getAssignee());
                                ccItem.put("assigneeName", ccUser.getCnName());
                                ccItem.put("avatar", ccUser.getAvatar());
                            }
                            latestComment = "";
                            List<ActCommentDto> ccComments = listComment(cc.getId());
                            ccItem.put("comments", ccComments);
                            if (ccComments.size() > 0) {
                                latestComment = ccComments.get(0).getMessage();
                                ccItem.put("latestComment", latestComment);
                            }
                            if (cc.getEndTime() == null || StringUtils.isNotEmpty(latestComment)) {
                                list.add(ccItem);
                            }
                        }
                    }
                }


                if (task.getEndTime() == null || StringUtils.isNotEmpty(latestComment)) {
                    list.add(item);
                }
            }
        }
        return list;
    }



    /**
     * 获取任务列表及可操作状态
     * @param processInstanceId
     * @return
     */
    public List<CustomHistoricTaskInstance> listHistoricTaskInstance(String processInstanceId) {
        List<CustomHistoricTaskInstance> list = Lists.newArrayList();
        List<HistoricTaskInstance> oList = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId)
                .includeProcessVariables().includeTaskLocalVariables().orderByHistoricTaskInstanceStartTime().desc().list();
        oList.forEach(p -> {
            CustomHistoricTaskInstance customHistoricTaskInstance = getCustomHistoricTaskInstance(p);
            List<HistoricTaskInstance> ccList = historyService.createHistoricTaskInstanceQuery().taskParentTaskId(customHistoricTaskInstance.getId()).includeTaskLocalVariables().list();
            for(HistoricTaskInstance cc:ccList){
                list.add(getCustomHistoricTaskInstance(cc));
            }
            list.add(customHistoricTaskInstance);
        });
        return list.stream().filter(p->p.getEndTime()==null||StringUtils.isNotEmpty(p.getLatestComment())).collect(Collectors.toList());
    }


    public CustomHistoricTaskInstance getHistoricTaskInstance(String taskId) {
        HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
        return getCustomHistoricTaskInstance(historicTaskInstance);
    }


    public String getProcessInstanceId(String taskId){
        HistoricTaskInstance historicTaskInstance=historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
        return historicTaskInstance.getProcessInstanceId();
    }

    private CustomHistoricTaskInstance getCustomHistoricTaskInstance(HistoricTaskInstance task) {
        CustomHistoricTaskInstance model = new CustomHistoricTaskInstance();

        if (StringUtils.isNotEmpty(task.getAssignee())) {
            model.setAssignee(task.getAssignee());
            FastUserDto userDto=commonUserService.getFastByEnLogin(task.getAssignee());
            if(userDto!=null) {
                model.setAvatar(userDto.getAvatar());
                model.setAssigneeName(userDto.getCnName());
            }
        }else if(task.getEndTime()==null) {
            List<String> candidateUsers = Lists.newArrayList();
            List<IdentityLink> identityLinks = taskService.getIdentityLinksForTask(task.getId());
            for (IdentityLink identityLink : identityLinks) {
                candidateUsers.add(identityLink.getUserId());
            }
            List<FastUserDto> users = commonUserService.listFastUserByEnLoginList(task.getTenantId(), candidateUsers);
            model.setCandidateUserNames(StringUtils.join(users.stream().map(FastUserDto::getCnName).collect(Collectors.toList()), ","));
            model.setCandidateUsers(StringUtils.join(users.stream().map(FastUserDto::getEnLogin).collect(Collectors.toList()), ","));
            if(model.getCandidateUserNames().length()>10){
                model.setCandidateUserNames(users.get(0).getCnName()+"等"+users.size()+"人");
            }
        }
        model.setOwnerName(commonUserService.getCnNames(task.getOwner()));
        model.setId(task.getId());
        model.setTaskDefinitionKey(task.getTaskDefinitionKey());
        model.setProcessInstanceId(task.getProcessInstanceId());

        String processDefinitionId=task.getProcessDefinitionId();

        if (StringUtils.isNotEmpty(task.getParentTaskId())) {
            HistoricTaskInstance parentTask = historyService.createHistoricTaskInstanceQuery().taskId(task.getParentTaskId()).singleResult();
            model.setProcessInstanceId(parentTask.getProcessInstanceId());
            model.setTaskDefinitionKey(parentTask.getTaskDefinitionKey());
            processDefinitionId=parentTask.getProcessDefinitionId();
        }

        model.setName(task.getName());
        model.setStartTime(task.getStartTime());
        model.setClaimTime(task.getClaimTime());
        model.setEndTime(task.getEndTime());
        //model.setPassed((boolean) task.getTaskLocalVariables().getOrDefault(PASSED_BACKED_STATE, true));
        HistoricVariableInstance passedBackVariable = historyService.createHistoricVariableInstanceQuery().taskId(task.getId()).variableName(PASSED_BACKED_STATE).singleResult();
        if (passedBackVariable != null) model.setPassed((Boolean)passedBackVariable.getValue());


        ActBpmnModel actBpmnModel=actCacheService.getActBpmnModel(processDefinitionId);



        if(actBpmnModel.getCopyUserTaskList().stream().anyMatch(p->p.containsKey(task.getTaskDefinitionKey()))) {
            model.setCcAble(true);
            List<String> ccUsers=Lists.newArrayList();
            HistoricVariableInstance ccUserVariable = historyService.createHistoricVariableInstanceQuery().taskId(task.getId()).variableName(CC_USER).singleResult();
            if (ccUserVariable != null) ccUsers=MyStringUtil.getStringList(ccUserVariable.getValue().toString());

            if(ccUsers.size()==0) {
                String defaultCcUser = actBpmnModel.getCopyUserTaskList()
                        .stream().filter(p -> p.containsKey(task.getTaskDefinitionKey()))
                        .findFirst().get().get(task.getTaskDefinitionKey());
                if(StringUtils.isNotEmpty(defaultCcUser)){
                    if(defaultCcUser.startsWith("${")&&defaultCcUser.endsWith("}")){
                        try{
                            ExpressionFactory factory = new ExpressionFactoryImpl();
                            SimpleContext context = new SimpleContext();
                            Map variables=taskService.getVariables(task.getId());
                            for (Object key : variables.keySet()) {
                                if (variables.get(key) != null) {
                                    context.setVariable(key.toString(), factory.createValueExpression(variables.get(key), variables.get(key).getClass()));
                                }
                            }
                            Object exeValue = factory.createValueExpression(context, defaultCcUser, Object.class).getValue(context);
                            if (exeValue instanceof String) {
                                ccUsers=MyStringUtil.getStringList(exeValue.toString());
                            }
                        }catch (Exception ex){

                        }
                    }else{
                        ccUsers=MyStringUtil.getStringList(defaultCcUser);
                    }
                }
            }
            model.setCcUser(StringUtils.join(ccUsers,","));
            model.setCcUserName(commonUserService.getCnNames(model.getCcUser()));
        }
        List<ActCommentDto> comments = listComment(task.getId());
        model.setComments(comments);
        if (comments.size() > 0) {
            ActCommentDto latestComment = comments.get(0);
            model.setLatestComment(latestComment.getMessage());
            if (latestComment.getUserId().equalsIgnoreCase(task.getAssignee()) && latestComment.getGmtCreate().getTime() > task.getStartTime().getTime()) {
                model.setCurrentComment(latestComment.getMessage());
            }
        }
        ModelUtil.setNotNullFields(model);
        return model;
    }

    private List<ActCommentDto> listComment(String taskId) {
        List<Comment> comments = taskService.getTaskComments(taskId);
        return listComment(comments);
    }

    private List<ActCommentDto> listComment(List<Comment> comments){
        List<ActCommentDto> list=Lists.newArrayList();
        String specialKey="^";
        for(Comment comment:comments){
            ActCommentDto item=new ActCommentDto();
            item.setGmtCreate(comment.getTime());
            item.setId(comment.getId());
            item.setUserId(comment.getUserId());
            item.setAgent("pc");
            item.setTaskId(comment.getTaskId());
            FastUserDto userDto=commonUserService.getFastByEnLogin(comment.getUserId());
            if(userDto!=null){
                item.setCnName(userDto.getCnName());
                item.setAvatar(userDto.getAvatar());
            }
            String message=((CommentEntityImpl)comment).getMessage();
            if(message.length()>130&&message.endsWith("...")) {
                //hnz发现下面这样取有乱码情况
                message = comment.getFullMessage();
            }
            item.setMessage(message);
            if(message.contains(specialKey)){
                item.setAgent(message.substring(0,message.indexOf(specialKey)));
                item.setMessage(message.substring(message.indexOf(specialKey)+1));
            }
            ModelUtil.setNotNullFields(item);
            list.add(item);
        }
        return list;
    }

}
