package com.cmcu.mcc.act.service;

import com.cmcu.common.service.CommonFormDataService;
import com.cmcu.mcc.act.model.MyHistoryTask;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.activiti.bpmn.model.*;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MySendService {


    @Autowired
    JdbcTemplate actJdbcTemplate;

    @Autowired
    TaskService taskService;

    @Autowired
    HistoryService historyService;

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    MyHistoryService myHistoryService;

    @Autowired
    SelectEmployeeService selectEmployeeService;

    @Resource
    TaskExecutor taskExecutor;


    @Resource
    CommonFormDataService commonFormDataService;



    /**
     * 发起人取回任务
     * @param processInstanceId
     * @param userLogin
     * @param comment
     */
    public void fetchFlow(String processInstanceId,String userLogin,String comment) {
        String userName = selectEmployeeService.getNameByUserLogin(userLogin);

        List<MyHistoryTask> allTasks = myHistoryService.listHistoryTask(processInstanceId);
        List<MyHistoryTask> doingTasks = allTasks.stream().filter(p -> p.getEndTime() == null).collect(Collectors.toList());
        List<MyHistoryTask> doneTasks = allTasks.stream().filter(p -> p.getEndTime() != null).collect(Collectors.toList());
        if (doingTasks.stream().noneMatch(p -> userLogin.equalsIgnoreCase(p.getAssignee())) && doingTasks.stream().noneMatch(p -> p.getClaimTime() != null) && doneTasks.size() > 0) {
            MyHistoryTask firstDoneTask = doneTasks.get(0);
            MyHistoryTask latestDoneTask = doneTasks.stream().sorted(Comparator.comparing(MyHistoryTask::getEndTime).reversed()).findFirst().get();
            //最近完成的任务是第一个任务
            if (firstDoneTask.getTaskDefinitionKey().equalsIgnoreCase(latestDoneTask.getTaskDefinitionKey())) {

                String taskDefinitionKey = firstDoneTask.getTaskDefinitionKey();
                HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
                String processDefinitionId = historicProcessInstance.getProcessDefinitionId();
                BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);

                Map original = Maps.newHashMap();
                //处于并行任务、或者会签任务
                for (int i = 0; i < doingTasks.size(); i++) {
                    MyHistoryTask doingTask = doingTasks.get(i);
                    String processInstanceDefinitionKey = doingTask.getTaskDefinitionKey();
                    FlowNode currentFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(processInstanceDefinitionKey);
                    original.put(doingTask.getTaskDefinitionKey(), new ArrayList<>(currentFlowNode.getOutgoingFlows()));
                    currentFlowNode.getOutgoingFlows().clear();
                    if (i == 0) {
                        FlowNode destFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(taskDefinitionKey);
                        List<SequenceFlow> newSequenceFlowList = new ArrayList<>();
                        SequenceFlow newSequenceFlow = new SequenceFlow();
                        newSequenceFlow.setId("tempSequenceFlow_" + UUID.randomUUID().toString());
                        newSequenceFlow.setSourceFlowElement(currentFlowNode);
                        newSequenceFlow.setTargetFlowElement(destFlowNode);
                        newSequenceFlowList.add(newSequenceFlow);
                        currentFlowNode.setOutgoingFlows(newSequenceFlowList);
                    }
                    Map<String,Object> variables=Maps.newHashMap();
                    variables.put("opt","取回");
                    variables.put("comment",userName + "取回任务("+comment+")");
                    taskService.setVariablesLocal(doingTask.getId(),variables);
                    taskService.complete(doingTask.getId());
                }
                for (Object key : original.keySet().toArray()) {
                    FlowNode currentFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(key.toString());
                    currentFlowNode.setOutgoingFlows((List<SequenceFlow>) original.get(key));
                }
            }
        }
    }

    /**
     * 发送任务
     * @param taskId
     * @param users 用户选择的用户列表
     * @param comment
     */
    public void sendFlow(String taskId,String[] users,String comment) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        List<String> candidates = Lists.newArrayList();
        if (users.length > 0) {
            candidates = Arrays.stream(users).filter(p -> StringUtils.split(p, "_").length == 2).collect(Collectors.toList());
        }
        String processDefinitionId = task.getProcessDefinitionId();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        List<String> nextTaskKeys = candidates.stream().map(p -> StringUtils.split(p, "_")[0]).distinct().collect(Collectors.toList());
        for (String nextTaskKey : nextTaskKeys) {
            UserTask userTask = (UserTask) bpmnModel.getMainProcess().getFlowElement(nextTaskKey);
            if (userTask.getLoopCharacteristics() != null) {
                String key = StringUtils.substring(userTask.getLoopCharacteristics().getInputDataItem(), 2, userTask.getLoopCharacteristics().getInputDataItem().length() - 1);
                taskService.setVariable(taskId, key, candidates.stream()
                        .filter(p -> StringUtils.split(p, "_")[0].equalsIgnoreCase(userTask.getId()))
                        .map(p -> StringUtils.split(p, "_")[1]).distinct().collect(Collectors.toList()));
            }
        }

        FlowNode currentFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(task.getTaskDefinitionKey());
        taskService.setVariableLocal(taskId, "passed", true);
        taskService.addComment(taskId, task.getProcessInstanceId(), comment);
        taskService.setVariable(taskId, "candidates", StringUtils.join(candidates, ","));

        Map variables = taskService.getVariables(taskId);
        Map result = getUserTaskDirectPassable(taskId);


        Map localVariables = Maps.newHashMap();
        localVariables.put("opt", "发送");
        localVariables.put("comment", comment);
        taskService.setVariablesLocal(taskId, localVariables);
        if (result.size() == 0) {
            taskService.complete(taskId, variables);
            List<Task> tasks = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).taskUnassigned().list();
            for (Task nextTask : tasks) {
                if (candidates.stream().anyMatch(p -> StringUtils.split(p, "_")[0].equalsIgnoreCase(nextTask.getTaskDefinitionKey()))) {
                    String assigne = candidates.stream().filter(p -> StringUtils.split(p, "_")[0].equalsIgnoreCase(nextTask.getTaskDefinitionKey())).map(p -> StringUtils.split(p, "_")[1]).findFirst().get();
                    taskService.setAssignee(nextTask.getId(), assigne);
                }
            }
        } else {
            //记录原活动方向
            List<SequenceFlow> originalOutGoingFlows = new ArrayList<>(currentFlowNode.getOutgoingFlows());
            currentFlowNode.getOutgoingFlows().clear();
            List<SequenceFlow> newSequenceFlowList = new ArrayList<>();
            SequenceFlow newSequenceFlow = new SequenceFlow();
            newSequenceFlow.setId("tempSequenceFlow_" + UUID.randomUUID().toString());
            newSequenceFlow.setSourceFlowElement(currentFlowNode);
            newSequenceFlow.setTargetFlowElement((FlowElement) result.get("flowNode"));
            newSequenceFlowList.add(newSequenceFlow);
            currentFlowNode.setOutgoingFlows(newSequenceFlowList);
            taskService.complete(taskId, variables);
            List<Task> tasks = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).taskUnassigned().list();
            for (Task nextTask : tasks) {
                if (candidates.stream().anyMatch(p -> StringUtils.split(p, "_")[0].equalsIgnoreCase(nextTask.getTaskDefinitionKey()))) {
                    String assigne = candidates.stream().filter(p -> StringUtils.split(p, "_")[0].equalsIgnoreCase(nextTask.getTaskDefinitionKey())).map(p -> StringUtils.split(p, "_")[1]).findFirst().get();
                    taskService.setAssignee(nextTask.getId(), assigne);
                }
            }
            currentFlowNode.setOutgoingFlows(originalOutGoingFlows);
            if (result.get("delete_execution") != null) {
                deleteExecution(task.getProcessInstanceId(), result.get("delete_execution").toString());
            }
        }
    }



    /**
     * 并行分支必须是单节点
     * 排他包容网关判断条件的值必须为全局变量
     * @param taskId 当前任务节点Id
     * @param users 打回的节点_用户,[userProofread_luodong,userAudit_hnz]
     * @param comment 当前任务打回审核信息
     */
    public void backFlow(String taskId,String[] users,String comment) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).list();
        List<String> candidates = Lists.newArrayList();
        if (users.length > 0) {
            candidates = Arrays.stream(users).filter(p -> StringUtils.split(p, "_").length == 2).collect(Collectors.toList());
        }

        List<String> preGroup = candidates.stream().map(p -> StringUtils.split(StringUtils.split(p, "_")[0], "-")[0]).distinct().collect(Collectors.toList());
        Assert.state(candidates.size() > 0, "请选择你要打回到的节点!");
        Assert.state(preGroup.size() == 1, "只能打回到某个确定的节点!");


        Map localVariables = Maps.newHashMap();
        localVariables.put("opt", "打回");
        localVariables.put("comment", comment);
        localVariables.put("back_candidate", StringUtils.join(candidates, ","));
        taskService.setVariablesLocal(taskId, localVariables);

        String taskDefinitionKey = task.getTaskDefinitionKey();
        String processDefinitionId = historyService.createHistoricActivityInstanceQuery().processInstanceId(task.getProcessInstanceId()).list().get(0).getProcessDefinitionId();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        FlowNode currentFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(taskDefinitionKey);
        List<String> nextTaskKeys = candidates.stream().map(p -> StringUtils.split(StringUtils.split(p, "_")[0], "-")[1]).distinct().collect(Collectors.toList());
        for (String nextTaskKey : nextTaskKeys) {
            UserTask userTask = (UserTask) bpmnModel.getMainProcess().getFlowElement(nextTaskKey);
            if (userTask.getLoopCharacteristics() != null) {
                String key = StringUtils.substring(userTask.getLoopCharacteristics().getInputDataItem(), 2, userTask.getLoopCharacteristics().getInputDataItem().length() - 1);
                taskService.setVariable(taskId, key, candidates.stream()
                        .filter(p -> StringUtils.split(StringUtils.split(p, "_")[0], "-")[1].equalsIgnoreCase(userTask.getId()))
                        .map(p -> StringUtils.split(p, "_")[1]).distinct().collect(Collectors.toList()));
            }
        }


        //只剩当前任务,则直接打回
        boolean directBack = false;
        if (tasks.size() == 1) {
            directBack = true;
        }
        Map variables = taskService.getVariables(taskId);
        //会签：关于该节点只剩当前这个任务了,则可以处理流程打回
        if (directBack) {
            if (nextTaskKeys.size() > 0) {

                List<SequenceFlow> originalOutGoingFlows = new ArrayList<>(currentFlowNode.getOutgoingFlows());
                currentFlowNode.getOutgoingFlows().clear();


                FlowNode destFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(nextTaskKeys.get(0));
                FlowNode destPreFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(destFlowNode.getIncomingFlows().get(0).getSourceRef());
                //如果是并行任务
                if (destPreFlowNode instanceof ParallelGateway) {
                    List<SequenceFlow> newSequenceFlowList = new ArrayList<>();
                    SequenceFlow newSequenceFlow = new SequenceFlow();
                    newSequenceFlow.setId("tempSequenceFlow_" + UUID.randomUUID().toString());
                    newSequenceFlow.setSourceFlowElement(currentFlowNode);
                    newSequenceFlow.setTargetFlowElement(destPreFlowNode);
                    newSequenceFlowList.add(newSequenceFlow);
                    currentFlowNode.setOutgoingFlows(newSequenceFlowList);
                } else {
                    List<SequenceFlow> newSequenceFlowList = new ArrayList<>();
                    SequenceFlow newSequenceFlow = new SequenceFlow();
                    newSequenceFlow.setId("tempSequenceFlow_" + UUID.randomUUID().toString());
                    newSequenceFlow.setSourceFlowElement(currentFlowNode);
                    newSequenceFlow.setTargetFlowElement(destFlowNode);
                    newSequenceFlowList.add(newSequenceFlow);
                    currentFlowNode.setOutgoingFlows(newSequenceFlowList);
                }
                taskService.complete(taskId, variables);
                currentFlowNode.setOutgoingFlows(originalOutGoingFlows);

                tasks = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).list();
                for (Task nextTask : tasks) {

                    if (StringUtils.isEmpty(nextTask.getAssignee())) {
                        if (candidates.stream().anyMatch(p -> StringUtils.split(StringUtils.split(p, "_")[0], "-")[1].equalsIgnoreCase(nextTask.getTaskDefinitionKey()))) {
                            String assigne = candidates.stream().filter(p -> StringUtils.split(StringUtils.split(p, "_")[0], "-")[1].equalsIgnoreCase(nextTask.getTaskDefinitionKey())).map(p -> StringUtils.split(p, "_")[1]).findFirst().get();
                            taskService.setAssignee(nextTask.getId(), assigne);
                        }
                    }
                    //相当于打回到并行任务的网关上
                    if (destPreFlowNode instanceof ParallelGateway) {
                        if (!nextTaskKeys.contains(nextTask.getTaskDefinitionKey())) {
                            taskService.addComment(nextTask.getId(), nextTask.getProcessInstanceId(), "AUTO_FINISH");
                            taskService.complete(nextTask.getId());
                        }
                    }
                }

                //如果当前任务是并行网关任务,则手动强制删除一些EXECUTION数据
                FlowNode preFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(currentFlowNode.getIncomingFlows().get(0).getSourceRef());
                if (preFlowNode instanceof ParallelGateway) {
                    deleteExecution(task.getProcessInstanceId(), currentFlowNode.getOutgoingFlows().get(0).getTargetRef());
                }
            }
        } else {
            taskService.complete(taskId, variables);
        }
    }




    /**
     * 判断某个任务是否可以直接通过
     * 主要是并行和会签 可能其他人选择了打回
     * 通过则返回null
     * 不可以则返回需要打回的上个节点
     * @param taskId
     * @return
     */
    private Map getUserTaskDirectPassable2(String taskId) {

        Map result=Maps.newHashMap();
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processDefinitionId = task.getProcessDefinitionId();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        FlowNode currentFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(task.getTaskDefinitionKey());

        //代表是会签
        if (((UserTask) currentFlowNode).getLoopCharacteristics() != null) {

            //会签任务不是最后一个
            if(taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).taskDefinitionKey(task.getTaskDefinitionKey()).count()>1){
                return result;
            }


            List<HistoricTaskInstance> historicTaskInstances=historyService.createHistoricTaskInstanceQuery()
                    .processInstanceId(task.getProcessInstanceId())
                    .orderByHistoricTaskInstanceEndTime().desc().list();
            for(HistoricTaskInstance historicTaskInstance:historicTaskInstances){
                if(!historicTaskInstance.getTaskDefinitionKey().equalsIgnoreCase(task.getTaskDefinitionKey())){
                    break;
                }
                HistoricVariableInstance backCandidate=historyService.createHistoricVariableInstanceQuery().taskId(historicTaskInstance.getId()).variableName("back_candidate").singleResult();
                if(backCandidate!=null){
                    result.put("back_candidate",backCandidate.getValue());
                    break;
                }
            }
            if(result.containsKey("back_candidate")) {
                List<SequenceFlow> sequenceFlows = currentFlowNode.getIncomingFlows();
                FlowNode preFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(sequenceFlows.get(0).getSourceRef());
                if (preFlowNode instanceof Gateway) {
                    List<SequenceFlow> incomingFlows = preFlowNode.getIncomingFlows();
                    result.put("flowNode", bpmnModel.getMainProcess().getFlowElement(incomingFlows.get(0).getSourceRef()));
                } else if (preFlowNode instanceof UserTask) {
                    result.put("flowNode",preFlowNode);
                }
            }
        } else {
            List<SequenceFlow> sequenceFlows = currentFlowNode.getIncomingFlows();
            FlowNode preFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(sequenceFlows.get(0).getSourceRef());
            //代表是并行任务
            //并行任务前面必须是网关、网关前面必须是用户任务
            //并行任务后面必须是网关
            if (preFlowNode instanceof ParallelGateway) {

                List<SequenceFlow> outComingFlows=preFlowNode.getOutgoingFlows();
                List<String> parallelTaskKeys=outComingFlows.stream().map(SequenceFlow::getTargetRef).collect(Collectors.toList());

                //并行任务不是最后一个
                if(taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).list().stream().filter(p->parallelTaskKeys.contains(p.getTaskDefinitionKey())).count()>1){
                    return result;
                }

                List<HistoricTaskInstance> historicTaskInstances=historyService.createHistoricTaskInstanceQuery()
                        .processInstanceId(task.getProcessInstanceId())
                        .orderByHistoricTaskInstanceEndTime().desc().list();
                for(HistoricTaskInstance historicTaskInstance:historicTaskInstances){
                    if(!parallelTaskKeys.contains(historicTaskInstance.getTaskDefinitionKey())){
                        break;
                    }
                    HistoricVariableInstance backCandidate=historyService.createHistoricVariableInstanceQuery().taskId(historicTaskInstance.getId()).variableName("back_candidate").singleResult();
                    if(backCandidate!=null){
                        result.put("back_candidate",backCandidate.getValue());
                        result.put("delete_execution",currentFlowNode.getOutgoingFlows().get(0).getTargetRef());
                        break;
                    }
                }


                if(result.containsKey("back_candidate")) {
                    List<SequenceFlow> incomingFlows = preFlowNode.getIncomingFlows();
                    result.put("flowNode", bpmnModel.getMainProcess().getFlowElement(incomingFlows.get(0).getSourceRef()));
                }
            }
        }
        return result;
    }


    private Map getUserTaskDirectPassable(String taskId) {

        Map result = Maps.newHashMap();
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processDefinitionId = task.getProcessDefinitionId();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        FlowNode currentFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(task.getTaskDefinitionKey());

        //代表是会签
        if (((UserTask) currentFlowNode).getLoopCharacteristics() != null) {

            //会签任务不是最后一个
            if (taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).taskDefinitionKey(task.getTaskDefinitionKey()).count() > 1) {
                return result;
            }


            List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
                    .processInstanceId(task.getProcessInstanceId())
                    .taskDefinitionKey(task.getTaskDefinitionKey())
                    .includeTaskLocalVariables()
                    .orderByHistoricTaskInstanceEndTime().desc().list();
            List<String> checkedUserList = Lists.newArrayList();
            for (HistoricTaskInstance historicTaskInstance : historicTaskInstances) {
                if (!checkedUserList.contains(historicTaskInstance.getAssignee())) {
                    checkedUserList.add(historicTaskInstance.getAssignee());
                    if (historicTaskInstance.getTaskLocalVariables().containsKey("back_candidate")) {
                        result.put("back_candidate", historicTaskInstance.getTaskLocalVariables().get("back_candidate"));
                        break;
                    }
                }
            }
            if (result.containsKey("back_candidate")) {
                List<SequenceFlow> sequenceFlows = currentFlowNode.getIncomingFlows();
                FlowNode preFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(sequenceFlows.get(0).getSourceRef());
                if (preFlowNode instanceof Gateway) {
                    List<SequenceFlow> incomingFlows = preFlowNode.getIncomingFlows();
                    result.put("flowNode", bpmnModel.getMainProcess().getFlowElement(incomingFlows.get(0).getSourceRef()));
                } else if (preFlowNode instanceof UserTask) {
                    result.put("flowNode", preFlowNode);
                }
                return result;
            }
        }

        List<SequenceFlow> sequenceFlows = currentFlowNode.getIncomingFlows();
        FlowNode preFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(sequenceFlows.get(0).getSourceRef());
        //代表是并行任务
        //并行任务前面必须是网关、网关前面必须是用户任务
        //并行任务后面必须是网关
        if (preFlowNode instanceof ParallelGateway) {

            List<SequenceFlow> outComingFlows = preFlowNode.getOutgoingFlows();
            List<String> parallelTaskKeys = outComingFlows.stream().map(SequenceFlow::getTargetRef).collect(Collectors.toList());

            //并行任务不是最后一个
            if (taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).list().stream().filter(p -> parallelTaskKeys.contains(p.getTaskDefinitionKey())).count() > 1) {
                return result;
            }

            List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
                    .processInstanceId(task.getProcessInstanceId())
                    .orderByHistoricTaskInstanceEndTime().desc().list();
            for (HistoricTaskInstance historicTaskInstance : historicTaskInstances) {
                if (!parallelTaskKeys.contains(historicTaskInstance.getTaskDefinitionKey())) {
                    break;
                }
                HistoricVariableInstance backCandidate = historyService.createHistoricVariableInstanceQuery().taskId(historicTaskInstance.getId()).variableName("back_candidate").singleResult();
                if (backCandidate != null) {
                    result.put("back_candidate", backCandidate.getValue());
                    result.put("delete_execution", currentFlowNode.getOutgoingFlows().get(0).getTargetRef());
                    break;
                }
            }


            if (result.containsKey("back_candidate")) {
                List<SequenceFlow> incomingFlows = preFlowNode.getIncomingFlows();
                result.put("flowNode", bpmnModel.getMainProcess().getFlowElement(incomingFlows.get(0).getSourceRef()));
            }
        }

        return result;
    }


    /**
     *
     * @param processInstanceId
     * @param taskKey
     */
    private void deleteExecution(String processInstanceId,String taskKey) {
        try {
            List<Map<String, Object>> list = actJdbcTemplate.queryForList("SELECT  ID_ FROM db_mcc_act.act_ru_execution where proc_inst_id_='" + processInstanceId + "' and ACT_ID_='" + taskKey + "' order by start_time_ asc");
            for (Map map : list) {
                String id = map.get("ID_").toString();
                actJdbcTemplate.execute("delete FROM db_mcc_act.act_ru_variable  WHERE EXECUTION_ID_=" + id);
                actJdbcTemplate.execute("delete  FROM db_mcc_act.act_ru_execution where ID_=" + id);
            }
        }catch (Exception ex){

        }
    }

}
