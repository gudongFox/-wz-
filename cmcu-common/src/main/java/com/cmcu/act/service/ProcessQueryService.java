package com.cmcu.act.service;

import com.cmcu.act.dto.*;
import com.cmcu.act.extend.CustomProcessDiagramGenerator;
import com.cmcu.act.extend.IPropertyConstants;
import com.cmcu.act.extend.IVariableConstants;
import com.cmcu.common.dao.CommonCodeMapper;
import com.cmcu.common.dto.CommonCodeDto;
import com.cmcu.common.dto.FastUserDto;
import com.cmcu.common.dto.UserDto;
import com.cmcu.common.entity.CommonCode;
import com.cmcu.common.entity.CommonForm;
import com.cmcu.common.service.*;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.common.util.WebUtil;
import com.common.model.JsTreeDto;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.util.SimpleContext;
import org.activiti.bpmn.model.*;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.*;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.*;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.el.ExpressionFactory;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProcessQueryService implements IPropertyConstants, IVariableConstants {

    @Resource
    TaskService taskService;
    @Resource
    HistoryService historyService;
    @Resource
    RuntimeService runtimeService;
    @Resource
    ActCacheService actCacheService;

    @Resource
    RepositoryService repositoryService;

    @Resource
    CommonActActionService commonActActionService;

    @Resource
    CommonUserService commonUserService;

    @Resource
    CommonFormService commonFormService;

    @Resource
    CommonFormDataService commonFormDataService;

    @Resource
    CommonCodeService commonCodeService;

    @Resource
    CommonDirService commonDirService;

    @Resource
    CommonCodeMapper commonCodeMapper;


    public static final String FINISH_NAME="已完成";

    /**
     * 流程定义
     * involvedUser 参与的任务 equal匹配
     * initiator    发起的任务 equal匹配
     * @param pageNum
     * @param pageSize
     * @param params
     * @return
     */
    public PageInfo<Object> listPagedProcessInstance(int pageNum, int pageSize, Map params) {

        String enLogin = params.getOrDefault("enLogin", "").toString();
        String involvedUser = params.getOrDefault(INVOLVED_USER, "").toString();
        String tenetId = commonUserService.getTenetId(enLogin);

        HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery();
        if (params.containsKey("modelTenetId")) {
            String modelTenetId = params.get("modelTenetId").toString();
            historicProcessInstanceQuery = historicProcessInstanceQuery.processInstanceTenantIdLike("%" + modelTenetId + "%");
        }
        if (params.containsKey(INITIATOR)) {
            String initiator = params.get(INITIATOR).toString();
            historicProcessInstanceQuery = historicProcessInstanceQuery.startedBy(initiator);
        } else if (StringUtils.isNotEmpty(involvedUser)) {
            historicProcessInstanceQuery = historicProcessInstanceQuery.involvedUser(involvedUser).variableValueNotEquals(INITIATOR, involvedUser);
        }
        if (params.containsKey("processDefinitionKey")) {
            String processDefinitionKey = params.get("processDefinitionKey").toString();
            historicProcessInstanceQuery = historicProcessInstanceQuery.processDefinitionKey(processDefinitionKey);
        } else {
            if (params.containsKey(PROCESS_DEFINITION_NAME)) {
                historicProcessInstanceQuery = historicProcessInstanceQuery.processInstanceNameLikeIgnoreCase("%" + params.get(PROCESS_DEFINITION_NAME).toString() + "%");
            }
            if (params.containsKey("modelCategory")) {

                List<CommonCodeDto> commonCodes = commonCodeService.listDataWithChild(tenetId, "流程类别", params.get("modelCategory").toString());
                List<String> processDefinitionKeyList = Lists.newArrayList();
                for (CommonCode commonCode : commonCodes) {
                    for (ProcessDefinition processDefinition : repositoryService.createProcessDefinitionQuery().processDefinitionCategory(commonCode.getCode()).list()) {
                        if (!processDefinitionKeyList.contains(processDefinition.getKey())) {
                            processDefinitionKeyList.add(processDefinition.getKey());
                        }
                    }
                }
                if (processDefinitionKeyList.size() > 0) {
                    historicProcessInstanceQuery = historicProcessInstanceQuery.processDefinitionKeyIn(processDefinitionKeyList);
                }
            }
        }


        if (params.containsKey(INITIATOR_NAME)) {
            String initiator = params.get(INITIATOR_NAME).toString();
            historicProcessInstanceQuery = historicProcessInstanceQuery.variableValueLikeIgnoreCase(INITIATOR_NAME, "%" + initiator + "%");
        }

        if (params.containsKey("qInitiator")) {
            String qInitiator = params.get("qInitiator").toString();
            historicProcessInstanceQuery = historicProcessInstanceQuery.variableValueLikeIgnoreCase(INITIATOR_NAME, "%" + qInitiator + "%");
        }

        if (params.containsKey("modelKey")) {
            String modelKey = params.get("modelKey").toString();
            historicProcessInstanceQuery = historicProcessInstanceQuery.variableValueLikeIgnoreCase(PROCESS_DEFINITION_KEY, "%" + modelKey + "%");
        } else if (params.containsKey("modelName")) {
            String modelName = params.get("modelName").toString();
            historicProcessInstanceQuery = historicProcessInstanceQuery.processInstanceNameLikeIgnoreCase("%" + modelName + "%");
        }

        if (params.containsKey("businessKey")) {
            String businessKey = params.get("businessKey").toString();
            historicProcessInstanceQuery = historicProcessInstanceQuery.variableValueLikeIgnoreCase(BUSINESS_KEY, "%" + businessKey + "%");
        }
        //查询条件
        if (params.containsKey("processDescription")) {
            String processDescription = params.get("processDescription").toString();
            historicProcessInstanceQuery = historicProcessInstanceQuery.variableValueLikeIgnoreCase(PROCESS_DESCRIPTION, "%" + processDescription + "%");
        }
        if (params.containsKey("myProcessDefinitionName")) {
            String myProcessDefinitionName = params.get("myProcessDefinitionName").toString();
            historicProcessInstanceQuery = historicProcessInstanceQuery.processInstanceNameLikeIgnoreCase("%" + myProcessDefinitionName + "%");
        }
        //流程是否已完成
        if (params.containsKey("finished")) {
            String finished = params.get("finished").toString();
            if (finished.equals("1")) {
                historicProcessInstanceQuery = historicProcessInstanceQuery.finished();
            } else if (finished.equals("0")) {
                historicProcessInstanceQuery = historicProcessInstanceQuery.unfinished();
            } else {

            }
        }


        List<HistoricProcessInstance> oList = historicProcessInstanceQuery.orderByProcessInstanceStartTime().desc().listPage((pageNum - 1) * pageSize, pageSize);
        List<Map> list = Lists.newArrayList();

        if (StringUtils.isNotEmpty(involvedUser)) {
            oList.forEach(p -> list.add(getProcessMap(p, enLogin)));
        } else {
            oList.forEach(p -> list.add(getProcessMap(p, enLogin)));
        }
        return WebUtil.buildPageInfo(pageNum, pageSize, (int) historicProcessInstanceQuery.count(), list);
    }

    public Map listProcessInstanceByH5(String initiator,String involvedUser,String enLogin,Map params,int firstResult, int maxResults) {
        Map result = Maps.newHashMap();
        HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery();
        if (StringUtils.isNotEmpty(initiator)) {
            historicProcessInstanceQuery = historicProcessInstanceQuery.startedBy(initiator);
        } else if (StringUtils.isNotEmpty(involvedUser)) {
            historicProcessInstanceQuery = historicProcessInstanceQuery.involvedUser(involvedUser).variableValueNotEquals(INITIATOR, involvedUser);
        }else if(StringUtils.isNotEmpty(enLogin)){
            historicProcessInstanceQuery = historicProcessInstanceQuery.involvedUser(enLogin);
        }
        if (params.containsKey("processDefinitionKeys")) {
            List<String> processDefinitionKeys = (List<String>) params.get("processDefinitionKeys");
            historicProcessInstanceQuery = historicProcessInstanceQuery.processDefinitionKeyIn(processDefinitionKeys);
        }
        if (params.containsKey("q")) {
            historicProcessInstanceQuery = historicProcessInstanceQuery.processInstanceNameLikeIgnoreCase("%" + params.get("q").toString() + "%");
        }
        if(params.containsKey(PROCESS_DEFINITION_NAME)){
            historicProcessInstanceQuery = historicProcessInstanceQuery.processDefinitionName(params.get(PROCESS_DEFINITION_NAME).toString());
        }
        List<HistoricProcessInstance> oList = historicProcessInstanceQuery.orderByProcessInstanceStartTime().desc().listPage(firstResult, maxResults);
        List<CustomProcessInstance> list = Lists.newArrayList();
        oList.forEach(p -> list.add(getCustomProcessInstance(p, enLogin)));
        result.put("total", historicProcessInstanceQuery.count());
        result.put("list", list);
        return result;
    }

    public List<Map> listProcessInstanceGroupByCad(String tenetId,String enLogin) {
        List<Map> list = Lists.newArrayList();
        List<ProcessDefinition> processDefinitions = actCacheService.listProcessDefinitions().stream().filter(p -> p.getTenantId().equalsIgnoreCase(tenetId)).collect(Collectors.toList());
        for (String name : processDefinitions.stream().map(ProcessDefinition::getName).distinct().collect(Collectors.toList())) {
            List<String> runningIds = taskService.createTaskQuery().processDefinitionName(name).taskCandidateOrAssigned(enLogin).list().stream().map(TaskInfo::getProcessInstanceId).distinct().collect(Collectors.toList());
            List<String> allIds = historyService.createHistoricProcessInstanceQuery().processDefinitionName(name).involvedUser(enLogin).list().stream().map(HistoricProcessInstance::getId).distinct().collect(Collectors.toList());
            long total=allIds.stream().filter(p->!runningIds.contains(p)).count();
            if (total > 0) {
                Map item = Maps.newHashMap();
                item.put("name", name);
                item.put("total", total);
                list.add(item);
            }
        }
        return list;
    }

    /**
     * 查询已完成的任务
     * @param processDefinitionName
     * @param enLogin
     * @param firstResult
     * @param maxResults
     * @return
     */
    public Map listProcessInstanceByCad(String processDefinitionName,String enLogin,int firstResult,int maxResults) {
        Map result = Maps.newHashMap();

        long total = historyService.createHistoricProcessInstanceQuery().involvedUser(enLogin)
                .processDefinitionName(processDefinitionName).count();

        List<String> processInstanceIds = taskService.createTaskQuery().processDefinitionName(processDefinitionName).taskCandidateOrAssigned(enLogin).list().stream().map(TaskInfo::getProcessInstanceId).distinct().collect(Collectors.toList());

        int index = 0;
        List<CustomSimpleProcessInstance> list = Lists.newArrayList();
        for (HistoricProcessInstance instance : historyService.createHistoricProcessInstanceQuery()
                .involvedUser(enLogin).processDefinitionName(processDefinitionName).orderByProcessInstanceStartTime().desc()
                .list()) {
            if(!processInstanceIds.contains(instance.getId())) {
                if (firstResult >= index) {
                    CustomSimpleProcessInstance simpleProcessInstance = getCustomSimpleProcessInstance(instance, enLogin);
                    String keyInfo = commonFormDataService.getFormDataInfo(instance.getBusinessKey(), "").getOrDefault("keyInfo", "").toString();
                    simpleProcessInstance.setKeyInfo(keyInfo);
                    simpleProcessInstance.setHaveContent(commonDirService.haveContent(instance.getBusinessKey(),0));
                    ModelUtil.setNotNullFields(simpleProcessInstance);
                    list.add(simpleProcessInstance);
                }
                if (list.size() == maxResults) {
                    break;
                }
                index++;
            }
        }
        result.put("total", total - processInstanceIds.size());
        result.put("list", list);
        return result;
    }





    public boolean IsActiveProcess(String processInstanceId) {
        return runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).count() > 0;
    }

    public InputStream getPngInputStream(String processInstanceId) {
        CustomProcessDiagramGenerator p = new CustomProcessDiagramGenerator();

        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        BpmnModel bpmnModel = actCacheService.getActBpmnModel(historicProcessInstance.getProcessDefinitionId()).getBpmnModel();
        List<HistoricTaskInstance> historicTaskInstances = historyService
                .createHistoricTaskInstanceQuery().processInstanceId(processInstanceId)
                .orderByHistoricTaskInstanceStartTime().desc().includeTaskLocalVariables().list();

        List<String> passedTaskList = Lists.newArrayList();
        List<String> backedTaskList = Lists.newArrayList();
        List<String> currentTaskKeyList = Lists.newArrayList();


        Map handleInfoMap = Maps.newHashMap();
        List<String> taskDefinitionKeyList = historicTaskInstances.stream().map(TaskInfo::getTaskDefinitionKey).distinct().collect(Collectors.toList());
        for (String taskDefinitionKey : taskDefinitionKeyList) {
            UserTask userTask = (UserTask) bpmnModel.getFlowElement(taskDefinitionKey);
            if (userTask.getLoopCharacteristics() == null) {
                HistoricTaskInstance latest = historicTaskInstances.stream().filter(t -> t.getTaskDefinitionKey().equalsIgnoreCase(taskDefinitionKey)).findFirst().get();
                if (latest.getEndTime() == null) {
                    currentTaskKeyList.add(taskDefinitionKey);
                } else {
                    boolean passed = Boolean.parseBoolean(latest.getTaskLocalVariables().getOrDefault(PASSED_BACKED_STATE, true).toString());
                    if (passed) {
                        passedTaskList.add(taskDefinitionKey);
                    } else {
                        backedTaskList.add(taskDefinitionKey);
                    }
                }
                handleInfoMap.put(taskDefinitionKey, getHandleInfoList(latest));

            } else {
                List<String> checkedAssigneeList=Lists.newArrayList();
                List<String> handleInfoList=Lists.newArrayList();
                List<HistoricTaskInstance> tasks= historicTaskInstances.stream().filter(t -> t.getTaskDefinitionKey().equalsIgnoreCase(taskDefinitionKey)).collect(Collectors.toList());
                for(HistoricTaskInstance task:tasks){
                    String currentAssignee=task.getAssignee();
                    if(StringUtils.isEmpty(currentAssignee)) currentAssignee="";
                    if(!checkedAssigneeList.contains(currentAssignee)) {
                        checkedAssigneeList.add(currentAssignee);
                        handleInfoList.addAll(getHandleInfoList(task));
                        if (task.getEndTime() != null) {
                            boolean passed = Boolean.parseBoolean(task.getTaskLocalVariables().getOrDefault(PASSED_BACKED_STATE, true).toString());
                            if (passed) {
                                passedTaskList.add(taskDefinitionKey);
                            } else {
                                backedTaskList.add(taskDefinitionKey);
                            }
                        }
                    }

                }

                if (tasks.stream().anyMatch(o->o.getEndTime()==null)) {
                    currentTaskKeyList.add(taskDefinitionKey);
                }

                handleInfoMap.put(taskDefinitionKey, handleInfoList);



//                if(tasks.stream().anyMatch(t->t.getEndTime()==null)){
//                    currentTaskKeyList.add(taskDefinitionKey);
//                    List<String> runInfoList=Lists.newArrayList();
//                    tasks.stream().filter(t->t.getEndTime()==null).forEach(t-> {
//                        runInfoList.addAll(getHandleInfoList(t));
//                    });
//                    handleInfoMap.put(taskDefinitionKey,runInfoList);
//                }else{
//                    boolean passed=true;
//                    for(HistoricTaskInstance task:tasks){
//                         passed = Boolean.parseBoolean(task.getTaskLocalVariables().getOrDefault(PASSED_BACKED_STATE, true).toString());
//                         if(!passed){
//                             break;
//                         }
//                    }
//
//                    if(passed){
//                        passedTaskList.add(taskDefinitionKey);
//                    }else{
//                        backedTaskList.add(taskDefinitionKey);
//                    }
//                }
//                Optional<HistoricTaskInstance> historicTaskInstance = historicTaskInstances.stream().filter(t -> t.getTaskDefinitionKey().equalsIgnoreCase(taskDefinitionKey)).findFirst();
//                if (historicTaskInstance.isPresent()) {
//                    HistoricTaskInstance latest = historicTaskInstance.get();
//                    if (latest.getEndTime() == null) {
//                        currentTaskKeyList.add(taskDefinitionKey);
//                    } else {
//                        boolean passed = Boolean.parseBoolean(latest.getTaskLocalVariables().getOrDefault(PASSED_BACKED_STATE, true).toString());
//                        if (passed) {
//                            passedTaskList.add(taskDefinitionKey);
//                        } else {
//                            backedTaskList.add(taskDefinitionKey);
//                        }
//                    }
//                    handleInfoMap.put(taskDefinitionKey, getHandleInfoList(latest));
//                }
            }
        }
        InputStream inputStream = p.generateDiagramNew(bpmnModel, passedTaskList, backedTaskList, currentTaskKeyList, handleInfoMap);
        return inputStream;
    }

    private List<String> getHandleInfoList(HistoricTaskInstance task) {
        List<String> infoList = Lists.newArrayList();
        StringBuilder sb = new StringBuilder();
        sb.append(commonUserService.getCnNames(task.getAssignee()));
        sb.append(" ");
        if (task.getEndTime() != null) {
            sb.append(DateFormatUtils.format(task.getEndTime(), "MM-dd HH:mm"));
        }
        infoList.add(sb.toString());
        List<String> ccNames=Lists.newArrayList();
        List<HistoricTaskInstance> subList = historyService.createHistoricTaskInstanceQuery().taskParentTaskId(task.getId()).list();
        if (subList.size() > 0) {
            subList.forEach(s -> ccNames.add(commonUserService.getCnNames(s.getAssignee())));
            infoList.add("抄送 " + StringUtils.join(ccNames.stream().distinct().toArray(), ","));
        }
        return infoList;
    }

    /**
     * 得到流程情况（复杂版，速度慢）
     * @param processInstanceId
     * @param businessKey
     * @param enLogin
     * @return
     */
    public CustomProcessInstance getCustomProcessInstance(String processInstanceId, String businessKey, String enLogin) {
        if(StringUtils.isEmpty(processInstanceId)&& StringUtils.isEmpty(businessKey)){
            return null;
        }
        HistoricProcessInstance historicProcessInstance = null;
        if (StringUtils.isNotEmpty(processInstanceId)) {
            historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        } else if (StringUtils.isNotEmpty(businessKey)) {
            if(historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(businessKey).count()>0) {
                historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(businessKey).list().get(0);
            }
        }
        if(historicProcessInstance==null) return null;
        CustomProcessInstance customProcessInstance= getCustomProcessInstance(historicProcessInstance, enLogin);
        return customProcessInstance;
    }


    /**
     * 得到流程情况(简易版，速度快）
     * 优先用这个
     * @param processInstanceId
     * @param businessKey
     * @param enLogin
     * @return
     */
    public CustomSimpleProcessInstance getCustomSimpleProcessInstance(String processInstanceId, String businessKey, String enLogin) {
        HistoricProcessInstance historicProcessInstance = null;
        if (StringUtils.isNotEmpty(processInstanceId)) {
            historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        } else if (StringUtils.isNotEmpty(businessKey)) {
            if (historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(businessKey).count() > 0) {
                historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(businessKey).list().get(0);
            }
        }
        if(historicProcessInstance==null) return null;
        return getCustomSimpleProcessInstance(historicProcessInstance,enLogin);
    }

    public CustomSimpleProcessInstance getCustomSimpleProcessInstance(HistoricProcessInstance historicProcessInstance,String enLogin){
        CustomSimpleProcessInstance model = new CustomSimpleProcessInstance();
        if (historicProcessInstance != null) {
            model.setBusinessKey(historicProcessInstance.getBusinessKey());
            model.setFinished(historicProcessInstance.getEndTime() != null);
            model.setInstance(historicProcessInstance);
            model.setEnLogin(enLogin);
            model.setInitiatorName(commonUserService.getCnNames(historicProcessInstance.getStartUserId()));
            if(!model.isFinished()) {
                List<Task> tasks = taskService.createTaskQuery().processInstanceId(historicProcessInstance.getId()).active().list();
                for (String taskName : tasks.stream().map(TaskInfo::getName).distinct().collect(Collectors.toList())) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(taskName);
                    List<String> assigneeList = tasks.stream().filter(p -> taskName.equalsIgnoreCase(p.getName())).filter(p -> StringUtils.isNotEmpty(p.getAssignee())).map(TaskInfo::getAssignee).distinct().collect(Collectors.toList());
                    if (assigneeList.size() > 0) {
                        sb.append("(");
                        List<String> cnNames = Lists.newArrayList();
                        for (String assignee : assigneeList) {
                            cnNames.add(commonUserService.getCnNames(assignee));
                        }
                        sb.append(StringUtils.join(cnNames, ","));
                        sb.append(")");
                    }
                    if (StringUtils.isNotEmpty(enLogin) && assigneeList.contains(enLogin)) {
                        model.getMyRunningTaskNameList().add(sb.toString());
                    }
                    model.getRunningTaskNameList().add(sb.toString());
                }
                model.setMyRunningTaskName(StringUtils.join(model.getMyRunningTaskNameList(), ","));
                model.setCurrentTaskName(StringUtils.join(model.getRunningTaskNameList(), ","));
                if (StringUtils.isNotEmpty(enLogin)) {
                    if (tasks.stream().anyMatch(p -> enLogin.equalsIgnoreCase(p.getAssignee()))) {
                        Task task = tasks.stream().filter(p -> enLogin.equalsIgnoreCase(p.getAssignee())).findFirst().get();
                        model.setTaskId(task.getId());
                        model.setTaskName(task.getName());
                        model.setFirstTask(actCacheService.getActBpmnModel(historicProcessInstance.getProcessDefinitionId()).getFirstUserTaskList().contains(task.getTaskDefinitionKey()));
                    }
                }
            }
        }
        ModelUtil.setNotNullFields(model);
        return model;
    }


    public  String getProcessName(String processInstanceId){
        if(StringUtils.isNotEmpty(processInstanceId)) {
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            if(historicProcessInstance!=null&&historicProcessInstance.getEndTime()==null){
                List<Task> tasks=taskService.createTaskQuery().processInstanceId(processInstanceId).list();
                List<String> stateList=Lists.newArrayList();
                for (String taskName : tasks.stream().map(TaskInfo::getName).distinct().collect(Collectors.toList())) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(taskName);
                    List<String> assigneeList = tasks.stream().filter(p -> taskName.equalsIgnoreCase(p.getName())).filter(p -> StringUtils.isNotEmpty(p.getAssignee())).map(TaskInfo::getAssignee).distinct().collect(Collectors.toList());
                    if (assigneeList.size() > 0) {
                        sb.append("(");
                        List<String> cnNames = Lists.newArrayList();
                        for (String assignee : assigneeList) {
                            cnNames.add(commonUserService.getCnNames(assignee));
                        }
                        sb.append(StringUtils.join(cnNames, ","));
                        sb.append(")");
                    }
                    stateList.add(sb.toString());
                }
                return StringUtils.join(stateList,",");
            }
        }
        return FINISH_NAME;
    }


    public CustomProcessInstance getAdminCustomProcessInstance(String processInstanceId, String businessKey) {
        String enLogin = "";
        List<Task> tasks = Lists.newArrayList();
        if (StringUtils.isNotEmpty(processInstanceId)) {
            tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).active().list();
        } else if (StringUtils.isNotEmpty(businessKey)) {
            tasks = taskService.createTaskQuery().processInstanceBusinessKey(businessKey).active().list();
        }

        if (tasks.size() > 0) {
            if (tasks.stream().anyMatch(p -> StringUtils.isNotEmpty(p.getAssignee()))) {
                enLogin = tasks.stream().filter(p -> StringUtils.isNotEmpty(p.getAssignee())).findFirst().get().getAssignee();
            } else {
                Task task = tasks.get(0);
                List<IdentityLink> identityLinks = taskService.getIdentityLinksForTask(task.getId());
                for (IdentityLink identityLink : identityLinks) {
                    if (StringUtils.isNotEmpty(identityLink.getUserId())) {
                        enLogin = identityLink.getUserId();
                        break;
                    }
                }
            }
        }
        return getCustomProcessInstance(processInstanceId, businessKey, enLogin);
    }

    /**
     * 未带流程变量否则序列化要出错
     * @param historicProcessInstance
     * @param enLogin
     * @return
     */
    private CustomProcessInstance getCustomProcessInstance(HistoricProcessInstance historicProcessInstance, String enLogin) {
        CustomProcessInstance model = new CustomProcessInstance();

        String processInstanceId = historicProcessInstance.getId();
        String tenetId = historicProcessInstance.getTenantId();
        model.setInstance(historicProcessInstance);
        model.setFinished(historicProcessInstance.getEndTime() != null);
        model.setInitiatorTime(historicProcessInstance.getStartTime());
        FastUserDto userDto = commonUserService.getFastByEnLogin(historicProcessInstance.getStartUserId());
        model.setInitiatorName(userDto.getCnName());
        model.setInitiatorNameDept(userDto.getCnName() + "(" + userDto.getDeptName() + ")");
        model.setStared(commonActActionService.getStared(tenetId, historicProcessInstance.getId(), enLogin));
        model.setEnLogin(enLogin);
        //ProcessDefinition processDefinition = actCacheService.getProcessDefinitionById(historicProcessInstance.getProcessDefinitionId());
        //model.setProcessCategory(processDefinition.getCategory());
        HistoricVariableInstance historicVariableInstance=historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId).variableName(PROCESS_DESCRIPTION).singleResult();
        if(historicVariableInstance!=null){
            model.setProcessDescription(historicVariableInstance.getValue().toString());
        }

        if (!model.isFinished()) {
            List<Task> tasks = taskService.createTaskQuery().processInstanceId(historicProcessInstance.getId()).active().list();
            if (tasks.size() > 0) {
                for (String taskName : tasks.stream().map(TaskInfo::getName).distinct().collect(Collectors.toList())) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(taskName);
                    List<String> assigneeList = tasks.stream().filter(p -> taskName.equalsIgnoreCase(p.getName())).filter(p -> StringUtils.isNotEmpty(p.getAssignee())).map(TaskInfo::getAssignee).distinct().collect(Collectors.toList());
                    if (assigneeList.size() > 0) {
                        sb.append("(");
                        sb.append(commonUserService.getCnNames(StringUtils.join(assigneeList, ",")));
                        sb.append(")");
                        if (StringUtils.isNotEmpty(enLogin) && assigneeList.contains(enLogin)) {
                            model.getMyRunningTaskNameList().add(sb.toString());
                        }
                    }
                    model.getRunningTaskNameList().add(sb.toString());
                }
                model.setMyRunningTaskName(StringUtils.join(model.getMyRunningTaskNameList(), ","));
                model.setCurrentTaskName(StringUtils.join(model.getRunningTaskNameList(), ","));
                if (StringUtils.isNotEmpty(enLogin)) {

                    List<Task> myTasks = taskService.createTaskQuery().processInstanceId(historicProcessInstance.getId()).taskCandidateOrAssigned(enLogin).active().orderByTaskCreateTime().asc().list();
                    if (myTasks.size() > 0) {
                        ActBpmnModel bpmnModel = actCacheService.getActBpmnModel(historicProcessInstance.getProcessDefinitionId());
                        Task task = myTasks.get(0);
                        model.setTaskId(task.getId());
                        model.setTaskName(task.getName());
                        model.setFirstTask(bpmnModel.getFirstUserTaskList().contains(task.getTaskDefinitionKey()));
                        model.setStartTime(task.getCreateTime());
                        model.setClaimTime(task.getClaimTime());
                        model.setLastTask(bpmnModel.getLastUserTaskList().contains(task.getTaskDefinitionKey()));

                        if (StringUtils.isEmpty(task.getAssignee())) {
                            model.setTakeAble(true);
                            return model;
                        } else if (StringUtils.isNotEmpty(task.getOwner()) && StringUtils.isNotEmpty(task.getAssignee()) && !task.getAssignee().equalsIgnoreCase(task.getOwner())) {
                            model.setResolveAble(true);
                            return model;
                        } else {
                            model.setSendAble(true);

                            //是否可以打回
                            if (bpmnModel.getBackUserTaskList().contains(task.getTaskDefinitionKey())) {
                                model.setBackAble(true);
                            }

                            //是否可以委托任务
                            if (task.getDelegationState() != DelegationState.PENDING && bpmnModel.getDelegateUserTaskList().contains(task.getTaskDefinitionKey())) {
                                model.setDelegateAble(true);
                            }

                            //是否可以转移任务
                            if (bpmnModel.getTransferUserTaskList().contains(task.getTaskDefinitionKey())) {
                                model.setTransferAble(true);
                            }

                            //是否可以抄送
                            if (bpmnModel.getCopyUserTaskList().contains(task.getTaskDefinitionKey())) {
                                model.setCcAble(true);
                                String ccUser = "";
                                if (task.getTaskLocalVariables().containsKey(CC_USER)) {
                                    ccUser = task.getTaskLocalVariables().get(CC_USER).toString();
                                }
                                if (StringUtils.isEmpty(ccUser)) {
                                    ccUser = bpmnModel.getCopyUserTaskList().stream().filter(p -> p.containsKey(task.getTaskDefinitionKey())).findFirst().get().get(task.getTaskDefinitionKey());
                                    if (StringUtils.isNotEmpty(ccUser) && ccUser.startsWith("${")) {
                                        Map variables = task.getTaskLocalVariables();
                                        Map processVariables = task.getProcessVariables();
                                        for (Object key : processVariables.keySet()) {
                                            if (!variables.containsKey(key)) {
                                                variables.put(key, processVariables.get(key));
                                            }
                                        }
                                        ExpressionFactory factory = new ExpressionFactoryImpl();
                                        SimpleContext context = new SimpleContext();
                                        for (Object key : variables.keySet()) {
                                            if (variables.get(key) != null) {
                                                context.setVariable(key.toString(), factory.createValueExpression(variables.get(key), variables.get(key).getClass()));
                                            }
                                        }
                                        Object exeValue = factory.createValueExpression(context, ccUser, Object.class).getValue(context);
                                        if (exeValue instanceof String) {
                                            ccUser = exeValue.toString();
                                        } else {
                                            ccUser = StringUtils.join((List<String>) exeValue, ",");
                                        }
                                    }
                                }
                                List<FastUserDto> users = commonUserService.listFastUserByEnLoginList(bpmnModel.getTenetId(), MyStringUtil.getStringList(ccUser));
                                model.setCcUser(StringUtils.join(users.stream().map(FastUserDto::getEnLogin).collect(Collectors.toList()), ","));
                                model.setCcUserName(StringUtils.join(users.stream().map(FastUserDto::getCnName).collect(Collectors.toList()), ","));
                            }

                            //不知道这是什么情况需要如此设置时间
                            if (model.isFirstTask()) {
                                try {
                                    if (enLogin.equalsIgnoreCase(task.getAssignee()) && task.getClaimTime() == null) {
                                        taskService.claim(task.getId(), enLogin);
                                        model.setClaimTime(new Date());
                                    }
                                } catch (Exception ex) {
                                }
                            }
                        }
                    }

                    //是否可以取回
                    if (!model.isFirstTask()) {
                        ActBpmnModel bpmnModel = actCacheService.getActBpmnModel(historicProcessInstance.getProcessDefinitionId());
                        if (tasks.stream().noneMatch(p -> bpmnModel.getFirstUserTaskList().contains(p.getTaskDefinitionKey()))) {

                            //没有认领的任务
                            //if (tasks.size() == 1 && tasks.stream().noneMatch(p -> p.getClaimTime() != null)) {
                            //五洲要求改为没有处理的、所以去掉逻辑
                            if (tasks.stream().map(TaskInfo::getTaskDefinitionKey).distinct().count() == 1) {
                                String taskDefinitionKey = tasks.get(0).getTaskDefinitionKey();
                                List<HistoricTaskInstance> finishTaskList = historyService.createHistoricTaskInstanceQuery()
                                        .processInstanceId(processInstanceId).orderByHistoricTaskInstanceEndTime().desc().finished().list()
                                        .stream().filter(p -> enLogin.equalsIgnoreCase(p.getAssignee())).filter(p -> !p.getTaskDefinitionKey().equalsIgnoreCase(taskDefinitionKey)).collect(Collectors.toList());
                                if (finishTaskList.size() > 0) {

                                    //发起人：流程没结束，到任何节点发起人都能取回
                                    HistoricTaskInstance fetchTask = null;
                                    for (HistoricTaskInstance taskInstance : finishTaskList) {
                                        UserTask userTask = (UserTask) bpmnModel.getBpmnModel().getFlowElement(taskInstance.getTaskDefinitionKey());
                                        if (userTask.getLoopCharacteristics() == null) {
                                            FlowElement preElement = userTask.getIncomingFlows().get(0).getSourceFlowElement();
                                            //前置节点不是并行网关开始
                                            if (!((preElement instanceof ParallelGateway || preElement instanceof InclusiveGateway) && preElement.getId().startsWith("begin_"))) {
                                                if (enLogin.equalsIgnoreCase(historicProcessInstance.getStartUserId())) {
                                                    fetchTask = taskInstance;
                                                    break;
                                                } else {
                                                    //其他节点：发送出去，下一个节点还没审批，可以取回
                                                    if (enLogin.equalsIgnoreCase(taskInstance.getAssignee())) {
                                                        fetchTask = taskInstance;
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (fetchTask != null) {
                                        model.setFetchAble(true);
                                        model.setFetchTaskId(fetchTask.getId());
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }

        //其他任务都没有判断是否有抄送任务
        if(StringUtils.isNotEmpty(enLogin)&&StringUtils.isEmpty(model.getTaskId())){
            if(taskService.createTaskQuery().taskAssignee(enLogin).taskNameLike("%[抄送]%").count()>0) {
                List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).orderByTaskCreateTime().desc().list();
                for (HistoricTaskInstance historicTaskInstance : historicTaskInstances) {
                    List<HistoricTaskInstance> ccTasks = historyService.createHistoricTaskInstanceQuery().taskAssignee(enLogin).taskParentTaskId(historicTaskInstance.getId()).unfinished().list();
                    if (ccTasks.size() > 0) {
                        HistoricTaskInstance ccTask = ccTasks.get(0);
                        model.setCcTaskId(ccTask.getId());
                        model.setCcTaskName(ccTask.getName());
                        model.setCcFinishAble(true);
                        return model;
                    }
                }
            }
        }

        return model;
    }


    private Map  getProcessMap(HistoricProcessInstance historicProcessInstance,String enLogin) {
        Map item = Maps.newHashMap();
        String processInstanceId = historicProcessInstance.getId();
        item.put("businessKey", historicProcessInstance.getBusinessKey());
        item.put("startTime", historicProcessInstance.getStartTime());
        item.put("endTime", historicProcessInstance.getEndTime());
        item.put("processName", "已完成");
        item.put("id",processInstanceId);
        HistoricVariableInstance historicVariableInstance = historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId).variableName(PROCESS_DESCRIPTION).singleResult();
        if (historicVariableInstance != null) {
            item.put(PROCESS_DESCRIPTION, historicVariableInstance.getValue().toString());
        }
        item.put("processDefinitionName", historicProcessInstance.getProcessDefinitionName());

        FastUserDto initiator=commonUserService.getFastByEnLogin(historicProcessInstance.getStartUserId());
        if(initiator!=null) {
            item.put("initiator", initiator.getEnLogin());
            item.put("initiatorName", initiator.getCnName());
        }


        if(historicProcessInstance.getEndTime() == null) {
            List<Task> tasks=taskService.createTaskQuery().processInstanceId(processInstanceId).active().list();
            if(tasks.size()>0) {

                boolean myTask=false;
                List<String> currentNameList=Lists.newArrayList();
                for (String taskName : tasks.stream().map(TaskInfo::getName).distinct().collect(Collectors.toList())) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(taskName);
                    List<String> assigneeList = tasks.stream().filter(p -> taskName.equalsIgnoreCase(p.getName())).filter(p -> StringUtils.isNotEmpty(p.getAssignee())).map(TaskInfo::getAssignee).distinct().collect(Collectors.toList());
                    if (assigneeList.size() > 0) {
                        sb.append("(");
                        sb.append(commonUserService.getCnNames(StringUtils.join(assigneeList, ",")));
                        sb.append(")");
                    }
                    currentNameList.add(sb.toString());

                    if(StringUtils.isNotEmpty(enLogin)&&assigneeList.contains(enLogin)) myTask=true;

                }
                item.put("processName", StringUtils.join(currentNameList,","));
                item.put("myTask",myTask);

                if (myTask &&tasks.size()==1&&enLogin.equalsIgnoreCase(historicProcessInstance.getStartUserId())) {
                    boolean firstTask;
                    if(tasks.stream().filter(p->p.getName().equalsIgnoreCase("发起人")).anyMatch(p->enLogin.equalsIgnoreCase(p.getAssignee()))){
                        firstTask=true;
                    }else{
                        ActBpmnModel bpmnModel = actCacheService.getActBpmnModel(historicProcessInstance.getProcessDefinitionId());
                        firstTask=bpmnModel.getFirstUserTaskList().contains(tasks.get(0).getTaskDefinitionKey());
                    }
                    item.put("firstTask",firstTask);
                }
            }
        }
        return item;
    }




    public List<JsTreeDto> listProcessCategoryTree(String tenetId,String enLogin) {
        List<JsTreeDto> list = Lists.newArrayList();
        if(StringUtils.isEmpty(tenetId)) tenetId = commonUserService.getTenetId(enLogin);
        List<CommonCodeDto> categoryList =commonCodeService.listDataByCatalog(tenetId,"流程类别");
        List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery().processDefinitionTenantId(tenetId).latestVersion().active().list();
        List<ProcessDefinition> filteredProcessDefinitionList = Lists.newArrayList();
        List<String> checkedKeyList = Lists.newArrayList();
        List<Task> tasks = taskService.createTaskQuery().taskCandidateOrAssigned(enLogin).active().list().stream().filter(p->StringUtils.isNotEmpty(p.getProcessDefinitionId())).collect(Collectors.toList());

        List<String> processKeyList=historyService.createHistoricProcessInstanceQuery().involvedUser(enLogin).list().stream().map(HistoricProcessInstance::getProcessDefinitionKey).distinct().collect(Collectors.toList());

        for (ProcessDefinition processDefinition : processDefinitionList) {
            if (!checkedKeyList.contains(processDefinition.getKey())) {
                checkedKeyList.add(processDefinition.getKey());
                filteredProcessDefinitionList.add(processDefinition);
            }
        }

        for (CommonCode category : categoryList) {
            JsTreeDto categoryTree = new JsTreeDto();
            categoryTree.setText(category.getName());
            categoryTree.setId(category.getCode());
            categoryTree.setIcon(category.getRemark());
            if (category.getParentId().equals(0)) {
                categoryTree.setParent("#");
            } else {
                Optional<CommonCodeDto> parent = categoryList.stream().filter(p -> p.getId().equals(category.getParentId())).findFirst();
                categoryTree.setParent(parent.isPresent() ? parent.get().getCode() : "#");
            }
            list.add(categoryTree);
            long totalTaskCount = 0;
            List<JsTreeDto> todoList = Lists.newArrayList();
            List<JsTreeDto> doneList = Lists.newArrayList();
            for (ProcessDefinition processDefinition : filteredProcessDefinitionList.stream().filter(p -> p.getCategory().equalsIgnoreCase(category.getCode())).collect(Collectors.toList())) {
                long taskCount = tasks.stream().filter(p -> p.getProcessDefinitionId().startsWith(processDefinition.getKey() + ":")).count();
                //if (taskCount == 0)
                //后续历史流程太多了，可能需要用这个count = historyService.createHistoricProcessInstanceQuery().processDefinitionKey(processDefinition.getKey()).involvedUser(enLogin).count();

                if (taskCount > 0 || processKeyList.stream().anyMatch(p -> processDefinition.getKey().equalsIgnoreCase(p))) {
                    JsTreeDto processTree = new JsTreeDto();
                    processTree.setId(processDefinition.getKey());
                    processTree.setText(processDefinition.getName());
                    processTree.setParent(category.getCode());
                    processTree.setData(processDefinition.getKey());
                    CommonForm commonForm = commonFormService.getModel(tenetId, processDefinition.getKey(), -1);
                    if (commonForm != null && StringUtils.isNotEmpty(commonForm.getFormIcon())) {
                        processTree.setIcon(commonForm.getFormIcon());
                    }
                    if (taskCount > 0) {
                        processTree.setText(processDefinition.getName() + "(" + taskCount + ")");
                        processTree.setIcon(processTree.getIcon() + " font-blue");
                        todoList.add(processTree);
                        totalTaskCount = totalTaskCount + taskCount;
                    } else {
                        doneList.add(processTree);
                    }
                }
            }
            if (totalTaskCount > 0) {
                categoryTree.setText(categoryTree.getText() + "(" + totalTaskCount + ")");
                categoryTree.getState().setOpened(true);
            }
            list.addAll(todoList);
            list.addAll(doneList);
        }

        return list;
    }

}
