package com.cmcu.act.service;

import com.cmcu.act.dto.ActBpmnModel;
import com.cmcu.act.dto.ActCandidateTask;
import com.cmcu.act.dto.ActCandidateUser;
import com.cmcu.act.extend.IPropertyConstants;
import com.cmcu.act.extend.IVariableConstants;
import com.cmcu.common.dto.FastUserDto;
import com.cmcu.common.dto.UserDto;
import com.cmcu.common.service.CommonFormDataService;
import com.cmcu.common.service.CommonUserService;
import com.cmcu.common.service.IWxMessageService;
import com.cmcu.common.util.JsonMapper;
import com.cmcu.common.util.MyStringUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.util.SimpleContext;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.*;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.*;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.el.ExpressionFactory;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TaskHandleService implements IVariableConstants, IPropertyConstants {

    @Resource
    RuntimeService runtimeService;

    @Resource
    HistoryService historyService;

    @Resource
    TaskService taskService;

    @Resource
    RepositoryService repositoryService;

    @Resource
    TaskQueryService taskQueryService;

    @Resource
    ModelService modelService;

    @Resource
    ActCacheService actCacheService;

    @Resource
    CommonUserService commonUserService;

    @Resource
    CommonFormDataService commonFormDataService;


    @Resource
    IWxMessageService wxMessageService;





    /**
     * 发起流程
     * @param processDefinitionKey
     * @param businessKey
     * @param variables
     * @return
     */
    public String startProcess(String processDefinitionKey, String businessKey, Map<String,Object> variables,String tenetId) {
        if (variables.containsKey(USER_LOGIN) && !variables.containsKey(INITIATOR)) {
            variables.put(INITIATOR, variables.get(USER_LOGIN));
        }
        if (variables.containsKey(INITIATOR) && !variables.containsKey(USER_LOGIN)) {
            variables.put(USER_LOGIN, variables.get(INITIATOR));
        }
        Assert.state(variables.containsKey(INITIATOR) && StringUtils.isNotEmpty(variables.get(INITIATOR).toString()), "流程发起人不能为空!");
        variables.put(INITIATOR_TIME, new Date());
        String initiator = variables.get(INITIATOR).toString();
        FastUserDto userDto=commonUserService.getFastByEnLogin(initiator);
        variables.put(INITIATOR_NAME, userDto.getCnName());
        variables.put(INITIATOR_DEPT_NAME,userDto.getDeptName());
        ProcessDefinition processDefinition = actCacheService.getProcessDefinition(processDefinitionKey);
        variables.put(PROCESS_DEFINITION_NAME, processDefinition.getName());
        variables.put(PROCESS_DEFINITION_KEY, processDefinition.getKey());
        variables.put(BUSINESS_KEY, businessKey);
        variables.put("modelCategory",processDefinition.getCategory());
        if (!variables.containsKey(PROCESS_DESCRIPTION)) {
            variables.put(PROCESS_DESCRIPTION, processDefinition.getName());
        }

        if(StringUtils.isNotEmpty(processDefinition.getId())) {
            ActBpmnModel actBpmnModel = actCacheService.getActBpmnModel(processDefinition.getId());
            List<UserTask> userTasks = actBpmnModel.getBpmnModel().getMainProcess().findFlowElementsOfType(UserTask.class);
            for (UserTask userTask : userTasks) {
                if (StringUtils.isNotEmpty(userTask.getId()) && userTask.getLoopCharacteristics() != null && StringUtils.isNotEmpty(userTask.getLoopCharacteristics().getCompletionCondition())) {
                    //会签节点默认变量 是否一键打回
                    variables.put(userTask.getId() + "Do", false);
                }
            }
        }


        setAuthenticatedUserId(initiator);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKeyAndTenantId(processDefinitionKey, businessKey, variables, tenetId);
        return processInstance.getId();
    }

    public void fetchTask(String taskId,String enLogin,String agent) {
        HistoricTaskInstance taskInstance = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
        String processDefinitionId = taskInstance.getProcessDefinitionId();
        String processInstanceId = taskInstance.getProcessInstanceId();

        ActBpmnModel actBpmnModel = actCacheService.getActBpmnModel(processDefinitionId);
        List<Task> taskList = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
        Assert.state(taskList.stream().map(TaskInfo::getTaskDefinitionKey).distinct().count() == 1, "后续存在多个任务无法取回!");
        BpmnModel bpmnModel = actBpmnModel.getBpmnModel();
        UserTask currentUserTask = (UserTask) bpmnModel.getMainProcess().getFlowElement(taskList.get(0).getTaskDefinitionKey());
        UserTask targetUserTask = (UserTask) bpmnModel.getMainProcess().getFlowElement(taskInstance.getTaskDefinitionKey());
        List<SequenceFlow> originalOutGoingFlows = Lists.newArrayList(currentUserTask.getOutgoingFlows());
        currentUserTask.getOutgoingFlows().clear();
        List<SequenceFlow> newSequenceFlowList = new ArrayList<>();
        SequenceFlow newSequenceFlow = new SequenceFlow();
        newSequenceFlow.setId("tempSequenceFlow_" + UUID.randomUUID().toString());
        newSequenceFlow.setSourceFlowElement(currentUserTask);
        newSequenceFlow.setTargetFlowElement(targetUserTask);
        newSequenceFlowList.add(newSequenceFlow);
        currentUserTask.setOutgoingFlows(newSequenceFlowList);

        for (Task t : taskList) {
            for (HistoricTaskInstance copyTask : historyService.createHistoricTaskInstanceQuery().taskParentTaskId(t.getId()).list()) {
                if (copyTask.getEndTime() == null) {
                    taskService.deleteTask(copyTask.getId());
                }
            }
            if (t.getDelegationState()==DelegationState.PENDING) {
                taskService.resolveTask(t.getId());
            }
            addComment(t.getId(), enLogin, commonUserService.getCnNames(enLogin) + "取回任务.",agent);
            taskService.complete(t.getId());
        }

        currentUserTask.setOutgoingFlows(originalOutGoingFlows);
    }

    public void resolveTask(String taskId,String enLogin,String comment,String agent) {
        Task task = taskService.createTaskQuery().taskId(taskId).includeTaskLocalVariables().includeProcessVariables().singleResult();
        Assert.state(task != null, "当前任务不存在!");
        Assert.state(task.getAssignee().equalsIgnoreCase(enLogin), "没有权限,当前任务办理人是" + commonUserService.getCnNames(task.getAssignee()));
        addComment(taskId,enLogin,comment,agent);
        checkFormData(task,enLogin);
        taskService.resolveTask(taskId);
    }

    public void transferTask(String taskId,String newOwner,String comment,String enLogin,String agent) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        Assert.state(task != null, "当前任务不存在!");
        if (StringUtils.isEmpty(enLogin)) {
            enLogin = task.getAssignee();
        }
        Assert.state(task.getAssignee().equalsIgnoreCase(enLogin), "没有权限,当前任务办理人是" + commonUserService.getCnNames(task.getAssignee()));
        if (StringUtils.isNotEmpty(task.getOwner())) {
            Assert.state(!task.getOwner().equalsIgnoreCase(newOwner), "当前任务拥有人已是选择的用户!");
        } else {
            Assert.state(!task.getAssignee().equalsIgnoreCase(newOwner), "当前任务拥有人已是选择的用户!");
        }

        String cnName=commonUserService.getCnNames(newOwner);
        Assert.state(StringUtils.isNotEmpty(cnName),"新任务人"+newOwner+"不存在!");
        addComment(taskId, enLogin,comment,agent);
        taskService.setOwner(taskId, newOwner);
        taskService.setAssignee(taskId, newOwner);
    }


    public void forceNewAssignee(String taskId,String newAssignee) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        Assert.state(task != null, "当前任务不存在!");
        if (!newAssignee.equalsIgnoreCase(task.getOwner()))
            taskService.setOwner(taskId, newAssignee);
        if (!newAssignee.equalsIgnoreCase(task.getAssignee()))
            taskService.setAssignee(taskId, newAssignee);
    }



    public void delegateTask(String taskId,String enLogin,String newAssignee,String comment,String agent) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        Assert.state(task != null, "当前任务不存在!");
        Assert.state(task.getAssignee().equalsIgnoreCase(enLogin), "没有权限,当前任务办理人是" + commonUserService.getCnNames(task.getAssignee()));
        Assert.state(!task.getAssignee().equalsIgnoreCase(newAssignee), "当前任务办理人已是选择的用户!");
        addComment(taskId, enLogin,comment,agent);
        if (newAssignee.equalsIgnoreCase(task.getOwner())) {
            if (task.getDelegationState() == DelegationState.PENDING) {
                taskService.resolveTask(task.getId());
            }
        }
        taskService.delegateTask(taskId, newAssignee);
    }

    public void takeTask(String taskId,String enLogin) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        Assert.state(task != null, "当前任务不存在!");
        if(StringUtils.isEmpty(task.getOwner())&&StringUtils.isEmpty(task.getAssignee())){
            if(StringUtils.isEmpty(enLogin)){
                List<ActCandidateUser> users=listActCandidateUser(taskId);
                Assert.state(users.size()>0,"待处理用户为空!");
                enLogin=users.get(0).getEnLogin();
            }
            taskService.claim(taskId,enLogin);
        }
    }

    public void ccFinishTask(String taskId,String comment,String agent) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        Map variables=Maps.newHashMap();
        variables.put(PASSED_BACKED_STATE,true);
        taskService.setVariablesLocal(taskId,variables);
        if (StringUtils.isEmpty(comment)){
            comment="办结抄送";
        }
        addComment(taskId,task.getAssignee(),comment,agent);
        taskService.complete(task.getId());
    }

    public void sendTask(String taskId, String ccUser, String comment, String[] candidateUsers,String enLogin,boolean complete,String agent) {
        Task task = taskService.createTaskQuery().taskId(taskId).includeTaskLocalVariables().includeProcessVariables().singleResult();
        addComment(taskId, enLogin,comment,agent);
        checkFormData(task, enLogin);
        Map<String, Object> localVariables = task.getTaskLocalVariables();
        localVariables.put(CANDIDATE_USER, candidateUsers);
        localVariables.put(PASSED_BACKED_STATE, true);
        localVariables.put(CC_USER, ccUser);
        taskService.setVariablesLocal(taskId, localVariables);
        if (complete) {
            completeTask(task.getId(),false);
        }
    }

    public void setComment(String taskId,String comment,String agent,String enLogin) {
        addComment(taskId, enLogin, comment, agent);
    }

    public void setVariables(String processInstanceId,Map variables) {
        if (taskService.createTaskQuery().processInstanceId(processInstanceId).count() > 0) {
            Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).list().get(0);
            taskService.setVariables(task.getId(), variables);
        }
    }

    private void completeTask(String taskId,boolean simpleMethod) {


        Task task = taskService.createTaskQuery().taskId(taskId).includeTaskLocalVariables().includeProcessVariables().singleResult();

        List<String> ccUsers = Lists.newArrayList();
        List<IdentityLink> links = Lists.newArrayList();
        String taskKey = task.getTaskDefinitionKey();
        ActBpmnModel actBpmnModel = actCacheService.getActBpmnModel(task.getProcessDefinitionId());
        if (actBpmnModel.getCopyUserTaskList().stream().anyMatch(p -> p.containsKey(taskKey))) {

            Map variables = task.getProcessVariables();
            variables.putAll(task.getTaskLocalVariables());
            String variableValue = variables.getOrDefault(CC_USER, "").toString();
            if (variableValue.startsWith("[")) {
                variableValue = variableValue.substring(1, variableValue.length() - 1);
            }
            ccUsers = MyStringUtil.getStringList(variableValue);

            if(ccUsers.size()==0&&simpleMethod) {
                String defaultCcUser = actBpmnModel.getCopyUserTaskList()
                        .stream().filter(p -> p.containsKey(taskKey))
                        .findFirst().get().get(taskKey);
                if (StringUtils.isNotEmpty(defaultCcUser)) {
                    if (defaultCcUser.startsWith("${") && defaultCcUser.endsWith("}")) {
                        try {
                            ExpressionFactory factory = new ExpressionFactoryImpl();
                            SimpleContext context = new SimpleContext();
                            for (Object key : variables.keySet()) {
                                if (variables.get(key) != null) {
                                    context.setVariable(key.toString(), factory.createValueExpression(variables.get(key), variables.get(key).getClass()));
                                }
                            }
                            Object exeValue = factory.createValueExpression(context, defaultCcUser, Object.class).getValue(context);
                            if (exeValue instanceof String) {
                                ccUsers = MyStringUtil.getStringList(exeValue.toString());
                            }
                        } catch (Exception ex) {

                        }
                    } else {
                        ccUsers = MyStringUtil.getStringList(defaultCcUser);
                    }
                }
            }
        }




        Map<String, Object> variables=task.getProcessVariables();
        Map<String, Object> localVariables=task.getTaskLocalVariables();
        String sourceKey = task.getTaskDefinitionKey();
        //多人任务且不是最后一个
        if (variables.containsKey("nrOfInstances") && ((int) variables.get("nrOfCompletedInstances") + 1 < (int) variables.get("nrOfInstances"))) {
            System.out.println("多人任务中,不是最后一个,直接发送");
        }
        else {
            UserTask currentUserTask = (UserTask) actBpmnModel.getBpmnModel().getMainProcess().getFlowElement(sourceKey);
            String[] candidateUsers = new String[]{};
            if (localVariables.containsKey(CANDIDATE_USER)) {
                candidateUsers = (String[]) localVariables.get(CANDIDATE_USER);
            }
            variables.put(CANDIDATE_USER,candidateUsers);

            boolean checkMulti=false;
            List<String> allParallelKey=Lists.newArrayList();

            String endGateWay="";
            //不是多人任务、并且在最简路径上,则可以直接发送
            if (actBpmnModel.getSimplestPath().stream().anyMatch(n -> n.getId().equalsIgnoreCase(sourceKey))) {
                if(checkConfigDirectBack(currentUserTask)) {
                    checkMulti = true;
                    allParallelKey.add(sourceKey);
                }
            }
            else {
                List<FlowNode> myPath = actBpmnModel.getPathList().stream().filter(p -> p.stream().anyMatch(n -> n.getId().equalsIgnoreCase(sourceKey))).findFirst().get();
                if (myPath.indexOf(currentUserTask) + 2 < myPath.size()) {
                    System.out.println("任务还在直线上运行,未到网关。");
                } else if (myPath.get(myPath.size() - 1) instanceof ExclusiveGateway) {
                    System.out.println("在单线排他网关上运行,直接发送。");
                    //需要考虑该任务是否会签任务
                } else {
                    allParallelKey = actBpmnModel.listAllParallelTask(sourceKey);
                    allParallelKey.add(sourceKey);
                    //超过2个并行任务（自己算一个）
                    checkMulti=allParallelKey.size()>1;
                    endGateWay=myPath.get(myPath.size()-1).getId();
                }
            }

            if(checkMulti){
                //最后一个任务
                List<String> allParallelKeyList=new ArrayList<>(allParallelKey);
                long taskCount=taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).list().stream().filter(p -> allParallelKeyList.contains(p.getTaskDefinitionKey())).count();
                if (taskCount== 1) {

                    List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
                            .processInstanceId(task.getProcessInstanceId()).includeTaskLocalVariables()
                            .orderByHistoricTaskInstanceEndTime().desc().finished().list();
                    List<String> checkedTaskKeyList = Lists.newArrayList();
                    for (HistoricTaskInstance historicTaskInstance : historicTaskInstances) {
                        //只检查在本次的并行任务里面的
                        if (!allParallelKey.contains(historicTaskInstance.getTaskDefinitionKey())) {
                            break;
                        }

                        if (!checkedTaskKeyList.contains(historicTaskInstance.getTaskDefinitionKey())) {
                            checkedTaskKeyList.add(historicTaskInstance.getTaskDefinitionKey());
                            if (allParallelKey.contains(historicTaskInstance.getTaskDefinitionKey())) {
                                if (historicTaskInstance.getTaskLocalVariables().containsKey(PASSED_BACKED_STATE)
                                        && !(boolean) historicTaskInstance.getTaskLocalVariables().get(PASSED_BACKED_STATE)
                                        && historicTaskInstance.getTaskLocalVariables().containsKey(CANDIDATE_USER)) {

                                    List<String> destTargets = Arrays.stream((String[]) historicTaskInstance.getTaskLocalVariables().get(CANDIDATE_USER)).filter(p -> StringUtils.split(p, "|").length == 2).map(p -> StringUtils.split(p, "|")[0]).distinct().collect(Collectors.toList());

                                    //删除并行任务通过的点
                                    if(allParallelKeyList.size()>1&&StringUtils.isNotEmpty(endGateWay)){
                                        deleteExecution(task.getProcessInstanceId(),endGateWay);
                                    }

                                    backToDest(task, variables, localVariables, destTargets.get(0));
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }


        //任务没结束之前添加抄送人为参与人
        if(ccUsers.size()>0){
            for(String ccUser:ccUsers) {
                if (links.stream().noneMatch(p -> p.getUserId().equalsIgnoreCase(ccUser))) {
                    runtimeService.addParticipantUser(task.getProcessInstanceId(), ccUser);
                }
            }
        }


        taskService.complete(task.getId(),variables);


        String businessKey=variables.get("businessKey").toString();
        //添加抄送任务
        if(ccUsers.size()>0) {
            List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
                    .processInstanceId(task.getProcessInstanceId())
                    .taskDefinitionKey(task.getTaskDefinitionKey())
                    .list();

            List<FastUserDto> ccUserDtoList=Lists.newArrayList();
            for (String user : ccUsers) {
                boolean alreadyCc = false;
                for (HistoricTaskInstance historicTaskInstance : historicTaskInstances) {
                    if (!alreadyCc) {
                        alreadyCc=historyService.createHistoricTaskInstanceQuery().taskAssignee(user).taskParentTaskId(historicTaskInstance.getId()).count()>0;
                    }
                }
                if (!alreadyCc) {
                    Task ccTask = taskService.newTask();
                    ccTask.setDescription(task.getDescription());
                    ccTask.setTenantId(task.getTenantId());
                    ccTask.setOwner(user);
                    ccTask.setAssignee(user);
                    ccTask.setCategory(task.getCategory());
                    ccTask.setDelegationState(DelegationState.RESOLVED);
                    ccTask.setName("[抄送]" + task.getName());
                    ccTask.setParentTaskId(task.getId());//父任务id
                    taskService.saveTask(ccTask);
                    ccUserDtoList.add(commonUserService.getFastByEnLogin(user));
                }
            }

            wxMessageService.sendCommonFormDataMessage(businessKey,ccUserDtoList);
        }


    }

    @Resource
    JdbcTemplate actJdbcTemplate;
    /**
     * 并行任务打回后回到初始状态
     * @param processInstanceId
     * @param taskKey
     */
    private void deleteExecution(String processInstanceId,String taskKey) {
        try {
            List<Map<String, Object>> list = actJdbcTemplate.queryForList("SELECT  ID_ FROM act_ru_execution where proc_inst_id_='" + processInstanceId + "' and ACT_ID_='" + taskKey + "' order by start_time_ asc");
            for (Map map : list) {
                String id = map.get("ID_").toString();
                actJdbcTemplate.execute("delete FROM act_ru_variable  WHERE EXECUTION_ID_=" + id);
                actJdbcTemplate.execute("delete FROM act_ru_execution where ID_=" + id);
            }
        }catch (Exception ex){

        }
    }




    public void sendTaskSimple(String taskId, String comment, String enLogin,String agent) {
        // Map<String, Object> variables = taskService.getVariables(taskId);
        Task task = taskService.createTaskQuery().taskId(taskId).includeTaskLocalVariables().singleResult();
        if (StringUtils.isEmpty(comment)) comment = "同意";
        addComment(taskId, enLogin,comment,agent);
        checkFormData(task, enLogin);
        Map<String, Object> localVariables = task.getTaskLocalVariables();
        localVariables.put(PASSED_BACKED_STATE, true);
        taskService.setVariablesLocal(taskId, localVariables);
        completeTask(taskId, true);
    }

    /**
     * 打回给发起人
     * @param taskId
     * @param comment
     * @param enLogin
     */
    public void backTaskSimple(String taskId, String comment, String enLogin,String agent) {
        Task task = taskService.createTaskQuery().taskId(taskId).includeProcessVariables().singleResult();
        checkFormData(task,enLogin);
        ActBpmnModel actBpmnModel = actCacheService.getActBpmnModel(task.getProcessDefinitionId());
        String firstTask = actBpmnModel.getFirstUserTaskList().get(0);
        if(StringUtils.isEmpty(comment)) comment="打回";
        backTask(taskId, comment, new String[]{firstTask + "|" + task.getProcessVariables().get("initiator")}, enLogin, true,agent);
    }


    /**
     * 检查表单数据是否完成
     * @param task
     * @param enLogin
     */
    private  void checkFormData(Task task,String enLogin){
        if(task!=null) {
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
            if(processInstance!=null){
                commonFormDataService.checkFormData(processInstance.getBusinessKey(),enLogin);
            }
        }
    }

    public void backTask(String taskId,String comment, String[] candidateUsers,String enLogin,boolean completeTask,String agent) {
        Assert.state(candidateUsers.length > 0, "打回节点不能为空!");
        Task task = taskService.createTaskQuery().taskId(taskId).includeTaskLocalVariables().includeProcessVariables().singleResult();
        List<String> destTargets = Arrays.stream(candidateUsers).filter(p -> StringUtils.split(p, "|").length == 2).map(p -> StringUtils.split(p, "|")[0]).distinct().collect(Collectors.toList());
        String sourceKey=task.getTaskDefinitionKey();
        String targetKey=destTargets.get(0);

        addComment(taskId,enLogin,comment,agent);

        Map<String, Object> localVariables = task.getTaskLocalVariables();
        localVariables.put(CANDIDATE_USER,candidateUsers);
        localVariables.put(PASSED_BACKED_STATE,false);
        taskService.setVariablesLocal(taskId,localVariables);
        if (completeTask) {

            Map<String, Object> variables = task.getProcessVariables();
            variables.put(CANDIDATE_USER, candidateUsers);

            ActBpmnModel actBpmnModel = actCacheService.getActBpmnModel(task.getProcessDefinitionId());
            Map preVariables = taskService.getVariables(taskId);
            boolean needBackNow = true;

            UserTask currentUserTask = (UserTask) actBpmnModel.getBpmnModel().getFlowElement(task.getTaskDefinitionKey());
            if (checkConfigDirectBack(currentUserTask)) {
                needBackNow = true;
            }
            else if (preVariables.containsKey("nrOfActiveInstances") && (int) preVariables.get("nrOfActiveInstances") == 1) {
                needBackNow = true;
            }
            //首先判断自己是否搞定了
            else if (preVariables.containsKey("nrOfInstances") && ((int) preVariables.get("nrOfCompletedInstances") + 1 < (int) preVariables.get("nrOfInstances"))) {
                needBackNow = false;
                System.out.println("自己没搞定，不需要打回");
            } else {
                //开始在最简路径上,则可以直接打回
                if (actBpmnModel.getSimplestPath().stream().anyMatch(n -> n.getId().equalsIgnoreCase(sourceKey))) {
                    needBackNow = true;
                    System.out.println("开始在最简路径上,则可以直接打回");
                }
                //两者均在同一组网关内
                else if (actBpmnModel.getPathList().stream().anyMatch(p -> p.stream().map(BaseElement::getId).collect(Collectors.toList()).contains(sourceKey) && p.stream().map(BaseElement::getId).collect(Collectors.toList()).contains(targetKey))) {
                    needBackNow = true;
                    System.out.println("两者均在同一组网关内,直接打回");
                } else {
                    List<FlowNode> sourcePath = actBpmnModel.getPathList().stream().filter(p -> p.stream().map(BaseElement::getId).collect(Collectors.toList()).contains(sourceKey)).findFirst().get();
                    //开始是处于排他网关上,可以打回
                    if (sourcePath.get(0) instanceof ExclusiveGateway) {
                        needBackNow = true;
                        System.out.println("开始是处于排他网关上,可以直接打回");

                    } else {
                        List<String> runningTaskIdList = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).list().stream().map(TaskInfo::getTaskDefinitionKey).distinct().collect(Collectors.toList());
                        List<String> allParallelTask = actBpmnModel.listAllParallelTask(sourceKey);
                        //正在运行的任务均不在同等并行或者包容网关支线上
                        needBackNow = runningTaskIdList.stream().noneMatch(allParallelTask::contains);
                        System.out.println("正在运行的任务均不在同等并行或者包容网关支线上：" + needBackNow);
                    }
                }
            }

            if (!needBackNow) {
                FlowNode currentFlowNode = (FlowNode) actBpmnModel.getBpmnModel().getMainProcess().getFlowElement(task.getTaskDefinitionKey());
                //1.连线生成
                ArrayList originalOutGoingFlows = new ArrayList<>(currentFlowNode.getOutgoingFlows());
                currentFlowNode.getOutgoingFlows().clear();
                taskService.complete(taskId, variables);
                currentFlowNode.setOutgoingFlows(originalOutGoingFlows);
            } else {
                backToDest(task, preVariables, variables, targetKey);
            }
        }
    }

    private void backToDest(Task currentTask, Map preVariables, Map newVariables, String targetKey) {
        BpmnModel bpmnModel = actCacheService.getActBpmnModel(currentTask.getProcessDefinitionId()).getBpmnModel();
        UserTask currentUserTask = (UserTask) bpmnModel.getMainProcess().getFlowElement(currentTask.getTaskDefinitionKey());
        UserTask targetUserTask = (UserTask) bpmnModel.getMainProcess().getFlowElement(targetKey);

        Map recoverVariables = Maps.newHashMap();
        String[] candidateBackUsers = (String[]) newVariables.get(CANDIDATE_USER);

        if(StringUtils.isNotEmpty(currentUserTask.getId())&&currentUserTask.getLoopCharacteristics()!=null&&StringUtils.isNotEmpty(currentUserTask.getLoopCharacteristics().getCompletionCondition())) {
            newVariables.put(currentUserTask.getId() + "Do", true);
            recoverVariables.put(currentUserTask.getId() + "Do", false);
        }


        ArrayList originalOutGoingFlows = new ArrayList<>(currentUserTask.getOutgoingFlows());
        currentUserTask.getOutgoingFlows().clear();

        List<SequenceFlow> newSequenceFlowList = new ArrayList<>();
        SequenceFlow newSequenceFlow = new SequenceFlow();
        newSequenceFlow.setId("tempSequenceFlow_" + UUID.randomUUID().toString());
        newSequenceFlow.setSourceFlowElement(currentUserTask);
        newSequenceFlow.setTargetFlowElement(targetUserTask);
        newSequenceFlowList.add(newSequenceFlow);
        currentUserTask.setOutgoingFlows(newSequenceFlowList);


        if (targetUserTask.getLoopCharacteristics() != null) {
            String key = targetUserTask.getLoopCharacteristics().getInputDataItem().substring(2, targetUserTask.getLoopCharacteristics().getInputDataItem().length() - 1);
            List<String> originalUsers = ((List<String>) preVariables.get(key));
            if (originalUsers.size() > 1) {
                List<String> reSelectUsers = Arrays.stream(candidateBackUsers).filter(p -> p.contains(targetKey + "|")).map(p -> StringUtils.split(p, "|")[1]).collect(Collectors.toList());
                if (originalUsers.size() != reSelectUsers.size()) {
                    newVariables.put(key, reSelectUsers);
                    recoverVariables.put(key, originalUsers);
                }
            }
        }


        newVariables.put("testMultiSeqDo",true);
        taskService.complete(currentTask.getId(), newVariables);
        currentUserTask.setOutgoingFlows(originalOutGoingFlows);

        if (recoverVariables.size() > 0) {
            if (taskService.createTaskQuery().processInstanceId(currentTask.getProcessInstanceId()).count() > 0) {
                Task next = taskService.createTaskQuery().processInstanceId(currentTask.getProcessInstanceId()).listPage(0, 1).get(0);
                taskService.setVariables(next.getId(), recoverVariables);
            }
        }
    }

    public List<ActCandidateTask> listNextCandidateTask(String taskId) {
        List<ActCandidateTask> list = Lists.newArrayList();
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (StringUtils.isEmpty(task.getProcessInstanceId())) return list;

        Map variables = taskService.getVariables(taskId);
        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        FlowNode currentFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(task.getTaskDefinitionKey());
        handleNextFlowElement(task.getTaskDefinitionKey(), currentFlowNode, 0, variables, bpmnModel, list);
        return list.stream().sorted(Comparator.comparing(ActCandidateTask::getSeq)).collect(Collectors.toList());
    }

    private void handleNextFlowElement(String preUserTaskKey, FlowNode currentFlowNode, int seq, Map keyVariables, BpmnModel bpmnModel, List<ActCandidateTask> list) {
        if (currentFlowNode instanceof EndEvent) return;
        try {
            seq++;
            List<SequenceFlow> outgoingFlows = currentFlowNode.getOutgoingFlows();
            if (currentFlowNode instanceof ExclusiveGateway || currentFlowNode instanceof InclusiveGateway) {
                ExpressionFactory factory = new ExpressionFactoryImpl();
                SimpleContext context = new SimpleContext();
                for (Object key : keyVariables.keySet()) {
                    if (keyVariables.get(key) != null) {
                        context.setVariable(key.toString(), factory.createValueExpression(keyVariables.get(key), keyVariables.get(key).getClass()));
                    }
                }
                for (SequenceFlow outgoingFlow : outgoingFlows) {
                    if (StringUtils.isNotEmpty(outgoingFlow.getConditionExpression())) {
                        if ((boolean) factory.createValueExpression(context, outgoingFlow.getConditionExpression(), boolean.class).getValue(context)) {
                            addActCandidateTask(preUserTaskKey, outgoingFlow.getTargetRef(), seq, keyVariables, bpmnModel, list);
                            if (currentFlowNode instanceof ExclusiveGateway) {
                                break;
                            }
                        }
                    } else {
                        addActCandidateTask(preUserTaskKey, outgoingFlow.getTargetRef(), seq, keyVariables, bpmnModel, list);
                    }
                }
            } else {
                for (SequenceFlow outgoingFlow : outgoingFlows) {
                    addActCandidateTask(preUserTaskKey, outgoingFlow.getTargetRef(), seq, keyVariables, bpmnModel, list);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            log.warn(ex.getMessage() +" > variables:"+ JsonMapper.obj2String(keyVariables));
        }
    }

    public List<ActCandidateTask> listBackCandidateTask(String taskId)
    {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        List<ActCandidateTask> list = Lists.newArrayList();
        Map variables = taskService.getVariables(taskId);
        ActBpmnModel actBpmnModel=actCacheService.getActBpmnModel(task.getProcessDefinitionId());
        BpmnModel bpmnModel = actBpmnModel.getBpmnModel();
        StartEvent startEvent = bpmnModel.getMainProcess().findFlowElementsOfType(StartEvent.class).get(0);
        handleNextFlowElement(task.getTaskDefinitionKey(), startEvent, 0, variables, bpmnModel, list);

        List<String> preTasks=actBpmnModel.listAllPreTask(task.getTaskDefinitionKey()).stream().filter(p->actBpmnModel.getAcceptBackUserTaskList().contains(p)).collect(Collectors.toList());
        list = list.stream()
                .filter(p->actBpmnModel.getAcceptBackUserTaskList().contains(p.getCurrentTaskKey()))
                .filter(p -> preTasks.contains(p.getCurrentTaskKey())).sorted(Comparator.comparing(ActCandidateTask::getSeq).reversed()).collect(Collectors.toList());

        if (list.size() > 0) {
            int seq = list.get(0).getSeq();
            list.forEach(p -> {
                if (p.getSeq() == seq) {
                    //从用户的candidateUsers来判断是否选择不再合适
                    List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
                            .processInstanceId(task.getProcessInstanceId()).taskDefinitionKey(p.getCurrentTaskKey())
                            .list();
                    List<String> selectedUsers = historicTaskInstances.stream().map(TaskInfo::getAssignee).collect(Collectors.toList());
                    p.getCandidateUserList().forEach(u -> u.setSelected(selectedUsers.contains(u.getEnLogin())));
                } else {
                    p.getCandidateUserList().forEach(u -> u.setSelected(false));
                }
            });
        }

        return list;
    }

    private void addActCandidateTask(String preUserTaskKey, String nextTaskKey, int seq, Map keyVariables, BpmnModel bpmnModel, List<ActCandidateTask> list) {
        if (list.stream().noneMatch(p -> p.getCurrentTaskKey().equals(nextTaskKey))) {
            FlowNode nextFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(nextTaskKey);
            if (nextFlowNode instanceof UserTask) {
                UserTask nextUserTask = (UserTask) nextFlowNode;
                ActCandidateTask model = new ActCandidateTask();
                model.setPreTaskKey(preUserTaskKey);
                model.setCurrentTaskKey(nextFlowNode.getId());
                model.setName(nextFlowNode.getName());
                model.setAssignee(nextUserTask.getAssignee());
                model.setSeq(seq);
                if (nextUserTask.getLoopCharacteristics() != null) {
                    model.setLoopCharacteristics(nextUserTask.getLoopCharacteristics().getInputDataItem());
                }
                model.setCandidateUsers(nextUserTask.getCandidateUsers());
                model.setCandidateGroups(nextUserTask.getCandidateGroups());
                model.setCandidateUserList(listActCandidateUser((UserTask) nextFlowNode, keyVariables));
                list.add(model);
                preUserTaskKey = nextFlowNode.getId();
            }
            handleNextFlowElement(preUserTaskKey, nextFlowNode, seq, keyVariables, bpmnModel, list);

        }
    }

    public List<ActCandidateUser> listActCandidateUser(String taskId){
        Task task=taskService.createTaskQuery().taskId(taskId).includeProcessVariables().singleResult();
        if(task!=null&&StringUtils.isEmpty(task.getAssignee())){
            ProcessInstance processInstance=runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
            if(processInstance!=null){
                ActBpmnModel actBpmnModel=actCacheService.getActBpmnModel(processInstance.getProcessDefinitionId());
                UserTask userTask= (UserTask) actBpmnModel.getBpmnModel().getFlowElement(task.getTaskDefinitionKey());
                return listActCandidateUser(userTask,task.getProcessVariables());
            }
        }
        return Lists.newArrayList();
    }

    private List<ActCandidateUser> listActCandidateUser(UserTask userTask, Map variables) {
        List<ActCandidateUser> list = Lists.newArrayList();
        try {
            ExpressionFactory factory = new ExpressionFactoryImpl();
            SimpleContext context = new SimpleContext();
            for (Object key : variables.keySet()) {
                if (variables.get(key) != null) {
                    context.setVariable(key.toString(), factory.createValueExpression(variables.get(key), variables.get(key).getClass()));
                }
            }
            List<String> enLoginList = Lists.newArrayList();
            if (userTask.getLoopCharacteristics() != null) {
                enLoginList=( List<String>)(factory.createValueExpression(context, userTask.getLoopCharacteristics().getInputDataItem(), List.class).getValue(context));
            } else if (StringUtils.isNotEmpty(userTask.getAssignee())) {
                String assignee = userTask.getAssignee();
                if (assignee.contains("${")) {
                    assignee = factory.createValueExpression(context, assignee, String.class).getValue(context).toString();
                }
                enLoginList.add(assignee);
            } else if (userTask.getCandidateUsers().size() > 0) {
                for (String candidateUser : userTask.getCandidateUsers()) {
                    if (candidateUser.contains("${")) {
                        Object exeValue = factory.createValueExpression(context, candidateUser, Object.class).getValue(context);
                        if (exeValue instanceof String) {
                            enLoginList.add(exeValue.toString());
                        } else {
                            enLoginList.addAll((List<String>) exeValue);
                        }
                    } else if(candidateUser.startsWith("pr_")){
                        enLoginList.addAll(commonUserService.listUserByPR("",Lists.newArrayList(candidateUser)));
                    }
                    else {
                        enLoginList.add(candidateUser);
                    }
                }
            }else if(userTask.getCandidateGroups().size()>0){
                enLoginList.addAll(commonUserService.listUserByPR("",userTask.getCandidateGroups()));
            }

            String[] candidateUsers = new String[]{};
            if (variables.containsKey(CANDIDATE_USER)) {
                candidateUsers = (String[]) variables.get(CANDIDATE_USER);
            }


            List<FastUserDto> users=commonUserService.listFastUserByEnLoginList("",enLoginList);

            for (String enLogin : enLoginList) {

                if(users.stream().anyMatch(p->p.getEnLogin().equalsIgnoreCase(enLogin))) {
                    FastUserDto current = users.stream().filter(p -> p.getEnLogin().equalsIgnoreCase(enLogin)).findFirst().get();
                    ActCandidateUser model = new ActCandidateUser();
                    model.setTaskDefinitionKey(userTask.getId());
                    model.setCnName(current.getCnName());
                    model.setEnLogin(current.getEnLogin());
                    model.setSelected(true);
                    if (candidateUsers.length > 0) {
                        if(Arrays.stream(candidateUsers).anyMatch(p->p.contains((userTask.getId())))) {
                            model.setSelected(Arrays.stream(candidateUsers).anyMatch(o -> o.equalsIgnoreCase(userTask.getId() + "|" + enLogin)));
                        }
                    }
                    list.add(model);
                }
            }

            if (list.size() > 0 && list.stream().noneMatch(ActCandidateUser::isSelected)) {
                list.get(0).setSelected(true);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return list;
    }

    private void addComment(String taskId,String enLogin,String comment,String agent){
        try {
            HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
            List<Comment> commentList = taskService.getTaskComments(taskId).stream().filter(p -> StringUtils.isNotEmpty(p.getUserId())).collect(Collectors.toList());
            boolean needAdd = true;
            if (commentList.size() > 0) {
                String specialKey="^";
                Comment first = commentList.get(0);
                String preMessage=first.getFullMessage();
                if(preMessage.contains(specialKey)) {
                    preMessage = preMessage.substring(preMessage.indexOf(specialKey) + 1);
                }

                if (!first.getUserId().equalsIgnoreCase(enLogin)) {

                } else if (preMessage.equalsIgnoreCase(comment)) {
                    needAdd = false;
                } else {
                    taskService.deleteComment(first.getId());
                }
            }
            if (needAdd) {
                setAuthenticatedUserId(enLogin);
                if (StringUtils.isEmpty(agent)) {
                    agent = "pc";
                }
                taskService.addComment(taskId, historicTaskInstance.getProcessInstanceId(),agent+"^"+comment.trim());
            }
        }catch (Exception ex){
            System.out.println("add comment failed:"+ex.getMessage());
        }
    }

    private void setAuthenticatedUserId(String enLogin){
        if(!enLogin.equalsIgnoreCase(Authentication.getAuthenticatedUserId())){
            Authentication.setAuthenticatedUserId(enLogin);
        }
    }

    /**
     * 获取流程定义ID
     * @param businessKey
     * @return
     */
    public String getDeploymentId(String businessKey){
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().processDefinitionKey(businessKey).orderByProcessDefinitionVersion().desc().list();
        return   list.get(0).getDeploymentId();
    }


    /**
     * 判断是否约定配置得直接打回多人任务节点
     * @param userTask
     * @return
     */
    private boolean checkConfigDirectBack(UserTask userTask){
        if(StringUtils.isEmpty(userTask.getId())) return false;

        if(userTask.getLoopCharacteristics()==null) return false;

        if(StringUtils.isEmpty(userTask.getLoopCharacteristics().getCompletionCondition())) return false;


        if(userTask.getLoopCharacteristics().getCompletionCondition().equalsIgnoreCase("${"+userTask.getId()+"Do}")) return true;

        return false;
    }
}
