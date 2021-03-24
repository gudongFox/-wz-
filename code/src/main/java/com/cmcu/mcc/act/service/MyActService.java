package com.cmcu.mcc.act.service;

import com.cmcu.common.act.dto.UserTaskDto;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.util.SimpleContext;
import org.activiti.bpmn.model.*;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.xmlbeans.impl.xb.xmlconfig.Usertypeconfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.el.ExpressionFactory;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MyActService {


    @Autowired
    TaskService taskService;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    HistoryService historyService;

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    SelectEmployeeService selectEmployeeService;

    @Resource
    HrEmployeeMapper hrEmployeeMapper;



    public void setVariables(String processInstanceId ,Map<String,Object> variables){
        List<Task> tasks=taskService.createTaskQuery().processInstanceId(processInstanceId).active().list();
        if(tasks.size()>0) {
            String taskId =tasks.get(0).getId();
            taskService.setVariables(taskId, variables);
        }
    }
    public void setVariable(String processInstanceId ,String key,Object value){
        Map variables=Maps.newHashMap();
        variables.put(key,value);
        List<Task> tasks=taskService.createTaskQuery().processInstanceId(processInstanceId).active().list();
        if(tasks.size()>0) {
            String taskId =tasks.get(0).getId();
            taskService.setVariables(taskId, variables);
        }
    }



    public void delete(String processInstanceId,String reason,String userLogin) {
        Assert.state(taskService.createTaskQuery().processInstanceId(processInstanceId).count() > 0, "流程已结束!");
        List<String> myTaskIds=taskService.createTaskQuery().processInstanceId(processInstanceId).taskAssignee(userLogin).list().stream().map(TaskInfo::getId).collect(Collectors.toList());
        if(taskService.createTaskQuery().processInstanceId(processInstanceId).count()>myTaskIds.size()){
            throw new IllegalArgumentException("流程被其他用户办理中!");
        }
        runtimeService.deleteProcessInstance(processInstanceId, reason);
        historyService.deleteHistoricProcessInstance(processInstanceId);
    }


    public String startProcess(String processKey, int projectId, int businessId, Map<String,Object> variables) {
        String businessKey=processKey + "_" + projectId + "_" + businessId;
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processKey, businessKey, variables);
        return processInstance.getId();
    }

    /**
     * 发起流程
     * @param processKey
     * @param businessKey
     * @param variables
     * @return
     */
    public String startProcess(String processKey, String businessKey, Map<String,Object> variables) {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processKey, businessKey, variables);
        if(variables.containsKey("userLogin")&&!variables.containsKey("initiator")){
            variables.put("initiator",variables.get("userLogin"));
        }
        return processInstance.getId();
    }


    public String startProcess(String processKey,int businessId,Map<String,Object> variables) {
        String businessKey = processKey + "_" + businessId;
        if(variables.containsKey("userLogin")&&!variables.containsKey("initiator")){
            variables.put("initiator",variables.get("userLogin"));
        }
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processKey, businessKey, variables);
        return processInstance.getId();
    }


    public void handleSimpleTask(String taskId,boolean passed,String flowComment) {
        if(passed) {
            List<UserTaskDto> userTaskDtoList = listNextUserTask(taskId);


        }

    }

    /**
     * 得到下一流程走向
     * @param taskId
     * @param keyVariables
     * @return
     */
    public List<Map> listNextStep(String taskId,Map keyVariables) {
        List<Map> list = Lists.newArrayList();

        Map<String,Object> originalVariables=taskService.getVariables(taskId);
        for(Object key:originalVariables.keySet()){
            keyVariables.put(key,originalVariables.get(key));
        }

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processDefinitionId = historyService.createHistoricActivityInstanceQuery().processInstanceId(task.getProcessInstanceId()).list().get(0).getProcessDefinitionId();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        String taskDefinitionKey = task.getTaskDefinitionKey();
        FlowNode currentFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(taskDefinitionKey);
        List<SequenceFlow> sequenceFlowList = currentFlowNode.getOutgoingFlows();
        FlowElement nextFlowElement = bpmnModel.getMainProcess().getFlowElement(sequenceFlowList.get(0).getTargetRef());
        if (nextFlowElement instanceof UserTask) {
            UserTask nextUserTask = (UserTask) nextFlowElement;
            Map map = getUserTaskCandicate(keyVariables,nextUserTask, null);
            map.put("type", "single");
            if(nextUserTask.getLoopCharacteristics()!=null){
                map.put("type", "multiple");
            }
            list.add(map);
        }
        else if(nextFlowElement instanceof EndEvent){
            Map map= Maps.newHashMap();
            map.put("taskKey",nextFlowElement.getId());
            map.put("activityName","结束");
            map.put("type","end");
            list.add(map);
        }
        else if (nextFlowElement instanceof ParallelGateway) {
            //并行网关
            List<SequenceFlow> parallelFlows = ((ParallelGateway)nextFlowElement).getOutgoingFlows();
            for (SequenceFlow parallelFlow : parallelFlows) {
                FlowElement parallelTask = bpmnModel.getMainProcess().getFlowElement(parallelFlow.getTargetRef());
                Map map = getUserTaskCandicate(keyVariables,(UserTask) parallelTask, null);
                map.put("type","parallel");
                list.add(map);
            }
        }

        else if (nextFlowElement instanceof ExclusiveGateway) {
            //排他网关
            ExpressionFactory factory = new ExpressionFactoryImpl();
            SimpleContext context = new SimpleContext();
            for(Object key:keyVariables.keySet()){
                context.setVariable(key.toString(), factory.createValueExpression(keyVariables.get(key), keyVariables.get(key).getClass()));
            }
            SequenceFlow defaultSequenceFlow=null;
            for(SequenceFlow outgoingFlow:((ExclusiveGateway) nextFlowElement).getOutgoingFlows()){
                if(StringUtils.isNotEmpty(outgoingFlow.getConditionExpression())){
                    if((boolean)factory.createValueExpression(context,outgoingFlow.getConditionExpression(),boolean.class).getValue(context)){
                        defaultSequenceFlow=outgoingFlow;
                        break;
                    }
                }else{
                    defaultSequenceFlow=outgoingFlow;
                }

            }
            if(defaultSequenceFlow==null) throw new IllegalArgumentException(nextFlowElement.getName()+ "条件表达式缺少必要的参数!");
            nextFlowElement = bpmnModel.getMainProcess().getFlowElement(defaultSequenceFlow.getTargetRef());

            Map map = getUserTaskCandicate(keyVariables,(UserTask) nextFlowElement, null);
            map.put("type", "exclusive");
            list.add(map);
        }else if(nextFlowElement instanceof InclusiveGateway){
            //包容网关
            ExpressionFactory factory = new ExpressionFactoryImpl();
            SimpleContext context = new SimpleContext();
            for(Object key:keyVariables.keySet()){
                context.setVariable(key.toString(), factory.createValueExpression(keyVariables.get(key), keyVariables.get(key).getClass()));
            }
            for(SequenceFlow outgoingFlow:((InclusiveGateway) nextFlowElement).getOutgoingFlows()){
                if(StringUtils.isNotEmpty(outgoingFlow.getConditionExpression())){
                    if((boolean)factory.createValueExpression(context,outgoingFlow.getConditionExpression(),boolean.class).getValue(context)){
                        Map map = getUserTaskCandicate(keyVariables,(UserTask) bpmnModel.getMainProcess().getFlowElement(outgoingFlow.getTargetRef()), null);
                        map.put("type", "inclusive");
                        list.add(map);
                    }
                }else{
                    Map map = getUserTaskCandicate(keyVariables,(UserTask) bpmnModel.getMainProcess().getFlowElement(outgoingFlow.getTargetRef()), null);
                    map.put("type", "inclusive");
                    list.add(map);
                }

            }
        }
        return list;
    }


    /**
     * 得到某个用户任务的候选人
     * @param userTask
     * @return
     */
    private  Map getUserTaskCandicate(Map variables, UserTask userTask, HistoricActivityInstance historicActivityInstance) {
        Map map = Maps.newHashMap();
        map.put("activityName", userTask.getName());
        map.put("taskKey", userTask.getId());
        map.put("assignee", userTask.getAssignee());
        map.put("candidateUsers", userTask.getCandidateUsers());
        map.put("candidateGroups", userTask.getCandidateGroups());
        map.put("incomingId", userTask.getIncomingFlows().get(0).getId());
        map.put("type","single");
        boolean multiple=false;
        List<Map> users = Lists.newArrayList();
        if (userTask.getLoopCharacteristics() != null) {
            ExpressionFactory factory = new ExpressionFactoryImpl();
            SimpleContext context = new SimpleContext();
            for (Object key : variables.keySet()) {
                context.setVariable(key.toString(), factory.createValueExpression(variables.get(key), variables.get(key).getClass()));
            }
            List<String> userLoginList = (List<String>) factory.createValueExpression(context, userTask.getLoopCharacteristics().getInputDataItem(), List.class).getValue(context);
            for (String userLogin : userLoginList) {
                Map user = Maps.newHashMap();
                user.put("userLogin", userLogin);
                user.put("userName", selectEmployeeService.getNameByUserLogin(userLogin));
                user.put("selected", true);
                users.add(user);
            }
            multiple = true;
        }
        else if (StringUtils.isNotEmpty(userTask.getAssignee())) {
            String assigne = userTask.getAssignee();
            if (historicActivityInstance != null) {
                assigne = historicActivityInstance.getAssignee();
            } else if (variables != null && assigne.contains("${")) {
                ExpressionFactory factory = new ExpressionFactoryImpl();
                SimpleContext context = new SimpleContext();
                for (Object key : variables.keySet()) {
                    context.setVariable(key.toString(), factory.createValueExpression(variables.get(key), variables.get(key).getClass()));
                }
                assigne = factory.createValueExpression(context, assigne, String.class).getValue(context).toString();
            }
            Map user = Maps.newHashMap();
            user.put("userLogin", assigne);
            user.put("userName", selectEmployeeService.getNameByUserLogin(assigne));
            user.put("selected", true);
            users.add(user);
        } else if (userTask.getCandidateUsers().size() > 0) {
            users = hrEmployeeMapper.listByUserLoginList(userTask.getCandidateUsers().toArray(new String[0]));
        } else if (userTask.getCandidateGroups().size() > 0) {
            //users = hrEmployeeMapper.listByRoleName(userTask.getCandidateGroups().toArray(new String[0]));
        }


        if (!multiple){
            if (historicActivityInstance != null) {
                for (Map user : users) {
                    user.put("selected", user.get("userLogin").equals(historicActivityInstance.getAssignee()));
                }
            } else {
                for (int i = 0; i < users.size(); i++) {
                    users.get(i).put("selected", i == 0);
                }
            }
        }
        map.put("users", users);
        return map;
    }




    public List<UserTaskDto> listNextUserTask(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        Map variables = taskService.getVariables(taskId);
        String taskDefinitionKey = task.getTaskDefinitionKey();
        List<UserTaskDto> dtoList = Lists.newArrayList();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        List<HistoricActivityInstance> historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(task.getProcessInstanceId())
                .orderByHistoricActivityInstanceStartTime().desc()
                .list().stream()
                .filter(p -> p.getActivityType().equals("userTask")).collect(Collectors.toList());
        handleNextFlowElement(task.getProcessInstanceId(), taskDefinitionKey, variables, bpmnModel, historicActivityInstanceList, dtoList);

        //只要有其他节点就不显示结束
        if (dtoList.stream().anyMatch(p -> !p.getTaskName().equalsIgnoreCase("结束"))) {
            dtoList = dtoList.stream().filter(p -> !p.getTaskName().equalsIgnoreCase("结束")).collect(Collectors.toList());
        }
        return dtoList;
    }

    private  void handleNextFlowElement(String processInstanceId,String definitionKey, Map keyVariables, BpmnModel bpmnModel, List<HistoricActivityInstance> historicActivityInstances, List<UserTaskDto> list) {
        FlowNode currentFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(definitionKey);
        List<SequenceFlow> sequenceFlowList = currentFlowNode.getOutgoingFlows();
        FlowElement nextFlowElement = bpmnModel.getMainProcess().getFlowElement(sequenceFlowList.get(0).getTargetRef());

        int seq=1;
        if(list.stream().anyMatch(p->p.getTaskKey().equalsIgnoreCase(definitionKey))){
            seq=list.stream().filter(p->p.getTaskKey().equalsIgnoreCase(definitionKey)).map(p->p.getSeq()).findFirst().get()+1;
        }
        if(nextFlowElement instanceof EndEvent){
            EndEvent endEvent = (EndEvent) nextFlowElement;
            UserTaskDto model = new UserTaskDto();
            model.setTaskKey(endEvent.getId());
            model.setTaskName("结束");
            model.setMultiple(false);
            model.setSeq(seq);
            list.add(model);
        }
        else if (nextFlowElement instanceof UserTask) {
            UserTask nextUserTask = (UserTask) nextFlowElement;
            UserTaskDto model = new UserTaskDto();
            model.setTaskKey(nextUserTask.getId());
            model.setTaskName(nextUserTask.getName());
            model.setMultiple(nextUserTask.getLoopCharacteristics() != null);
            model.setSeq(seq);
            if (list.stream().noneMatch(p -> p.getTaskKey().equalsIgnoreCase(nextUserTask.getId()))) {
                model.setUsers(getUserList(processInstanceId,nextUserTask,historicActivityInstances,keyVariables));
                list.add(model);
                handleNextFlowElement(processInstanceId,nextUserTask.getId(), keyVariables, bpmnModel,historicActivityInstances, list);
            }
        } else if (nextFlowElement instanceof ParallelGateway) {
            //并行网关
            List<SequenceFlow> parallelFlows = ((ParallelGateway) nextFlowElement).getOutgoingFlows();
            for (SequenceFlow parallelFlow : parallelFlows) {
                FlowElement parallelTask = bpmnModel.getMainProcess().getFlowElement(parallelFlow.getTargetRef());
                if (parallelTask instanceof UserTask) {
                    UserTask nextParallelUserTask = (UserTask) parallelTask;
                    UserTaskDto model = new UserTaskDto();
                    model.setTaskKey(nextParallelUserTask.getId());
                    model.setTaskName(nextParallelUserTask.getName());
                    model.setMultiple(nextParallelUserTask.getLoopCharacteristics() != null);
                    model.setSeq(seq);
                    if (list.stream().noneMatch(p -> p.getTaskKey().equalsIgnoreCase(nextParallelUserTask.getId()))) {
                        model.setUsers(getUserList( processInstanceId,nextParallelUserTask,historicActivityInstances,keyVariables));
                        list.add(model);
                        handleNextFlowElement(processInstanceId,nextParallelUserTask.getId(), keyVariables, bpmnModel,historicActivityInstances,list);
                    }
                }else if(parallelTask instanceof EndEvent){
                    EndEvent endEvent = (EndEvent) parallelTask;
                    UserTaskDto model = new UserTaskDto();
                    model.setTaskKey(endEvent.getId());
                    model.setTaskName("结束");
                    model.setMultiple(false);
                    model.setSeq(seq);
                    list.add(model);
                }
            }

        } else if (nextFlowElement instanceof ExclusiveGateway) {
            //排他网关
            ExpressionFactory factory = new ExpressionFactoryImpl();
            SimpleContext context = new SimpleContext();
            for (Object key : keyVariables.keySet()) {
                context.setVariable(key.toString(), factory.createValueExpression(keyVariables.get(key), keyVariables.get(key).getClass()));
            }
            SequenceFlow defaultSequenceFlow = null;
            for (SequenceFlow outgoingFlow : ((ExclusiveGateway) nextFlowElement).getOutgoingFlows()) {
                if (StringUtils.isNotEmpty(outgoingFlow.getConditionExpression())) {
                    if ((boolean) factory.createValueExpression(context, outgoingFlow.getConditionExpression(), boolean.class).getValue(context)) {
                        defaultSequenceFlow = outgoingFlow;
                        break;
                    }
                } else {
                    defaultSequenceFlow = outgoingFlow;
                }

            }
            if (defaultSequenceFlow != null) {
                System.out.println(nextFlowElement.getName() + "条件表达式缺少必要的参数!");
                nextFlowElement = bpmnModel.getMainProcess().getFlowElement(defaultSequenceFlow.getTargetRef());
                if (nextFlowElement instanceof UserTask) {
                    UserTask nextExclusiveUserTask = (UserTask) nextFlowElement;
                    UserTaskDto model = new UserTaskDto();
                    model.setTaskKey(nextExclusiveUserTask.getId());
                    model.setTaskName(nextExclusiveUserTask.getName());
                    model.setMultiple(nextExclusiveUserTask.getLoopCharacteristics() != null);
                    model.setSeq(seq);
                    if (list.stream().noneMatch(p -> p.getTaskKey().equalsIgnoreCase(nextExclusiveUserTask.getId()))) {
                        model.setUsers(getUserList(processInstanceId,nextExclusiveUserTask, historicActivityInstances, keyVariables));
                        list.add(model);
                        handleNextFlowElement(processInstanceId,nextExclusiveUserTask.getId(), keyVariables, bpmnModel, historicActivityInstances, list);
                    }
                }else if(nextFlowElement instanceof Gateway){
                    handleNextFlowElement(processInstanceId,nextFlowElement.getId(), keyVariables, bpmnModel, historicActivityInstances, list);
                }
                else if(nextFlowElement instanceof EndEvent){
                    EndEvent endEvent = (EndEvent) nextFlowElement;
                    UserTaskDto model = new UserTaskDto();
                    model.setTaskKey(endEvent.getId());
                    model.setTaskName("结束");
                    model.setMultiple(false);
                    model.setSeq(seq);
                    list.add(model);
                }
            }

        } else if (nextFlowElement instanceof InclusiveGateway) {
            //包容网关
            ExpressionFactory factory = new ExpressionFactoryImpl();
            SimpleContext context = new SimpleContext();
            for (Object key : keyVariables.keySet()) {
                context.setVariable(key.toString(), factory.createValueExpression(keyVariables.get(key), keyVariables.get(key).getClass()));
            }
            for (SequenceFlow outgoingFlow : ((InclusiveGateway) nextFlowElement).getOutgoingFlows()) {
                if (StringUtils.isNotEmpty(outgoingFlow.getConditionExpression())) {
                    //满足这个条件
                    if ((boolean) factory.createValueExpression(context, outgoingFlow.getConditionExpression(), boolean.class).getValue(context)) {

                        UserTask userTask = (UserTask) bpmnModel.getMainProcess().getFlowElement(outgoingFlow.getTargetRef());
                        UserTaskDto model = new UserTaskDto();
                        model.setTaskKey(userTask.getId());
                        model.setTaskName(userTask.getName());
                        model.setMultiple(userTask.getLoopCharacteristics() != null);
                        model.setSeq(seq);
                        if (list.stream().noneMatch(p -> p.getTaskKey().equalsIgnoreCase(userTask.getId()))) {
                            model.setUsers(getUserList(processInstanceId,userTask,historicActivityInstances,keyVariables));
                            list.add(model);
                            handleNextFlowElement(processInstanceId,userTask.getId(), keyVariables, bpmnModel,historicActivityInstances,list);
                        }
                    }
                } else {
                    FlowElement targetFlowElement = bpmnModel.getMainProcess().getFlowElement(outgoingFlow.getTargetRef());
                    if (targetFlowElement instanceof UserTask) {
                        UserTask userTask = (UserTask) targetFlowElement;
                        UserTaskDto model = new UserTaskDto();
                        model.setTaskKey(userTask.getId());
                        model.setTaskName(userTask.getName());
                        model.setMultiple(userTask.getLoopCharacteristics() != null);
                        model.setSeq(seq);
                        if (list.stream().noneMatch(p -> p.getTaskKey().equalsIgnoreCase(userTask.getId()))) {
                            model.setUsers(getUserList(processInstanceId, userTask, historicActivityInstances, keyVariables));
                            list.add(model);
                            handleNextFlowElement(processInstanceId, userTask.getId(), keyVariables, bpmnModel, historicActivityInstances, list);
                        }
                    }else if (targetFlowElement instanceof EndEvent){
                        EndEvent endEvent = (EndEvent) targetFlowElement;
                        UserTaskDto model = new UserTaskDto();
                        model.setTaskKey(endEvent.getId());
                        model.setTaskName("结束");
                        model.setMultiple(false);
                        model.setSeq(seq);
                        list.add(model);
                    }else if(targetFlowElement instanceof Gateway){
                        handleNextFlowElement(processInstanceId,targetFlowElement.getId(), keyVariables, bpmnModel, historicActivityInstances, list);
                    }

                }
            }
        }
    }

    private  List<Map> getUserList(String processInstanceId,UserTask userTask, List<HistoricActivityInstance> historicActivityInstances, Map variables) {

        List<String> candidates=Lists.newArrayList();
        if(variables.containsKey("candidates")) {
            candidates=Arrays.stream(StringUtils.split(variables.get("candidates").toString(),","))
                    .filter(p->StringUtils.split(p,"_")[0].equalsIgnoreCase(userTask.getId())).map(p->StringUtils.split(p,"_")[1]).collect(Collectors.toList());
        }

        List<Map> users = Lists.newArrayList();
        if (userTask.getLoopCharacteristics() != null) {
            try {
                ExpressionFactory factory = new ExpressionFactoryImpl();
                SimpleContext context = new SimpleContext();
                for (Object key : variables.keySet()) {
                    if(variables.get(key)!=null) {
                        context.setVariable(key.toString(), factory.createValueExpression(variables.get(key), variables.get(key).getClass()));
                    }
                }
                List<String> userLoginList = (List<String>) factory.createValueExpression(context, userTask.getLoopCharacteristics().getInputDataItem(), List.class).getValue(context);
                for (String userLogin : userLoginList) {
                    Map user = Maps.newHashMap();
                    user.put("userLogin", userLogin);
                    user.put("userName", hrEmployeeMapper.getNameByUserLogin(userLogin));
                    user.put("selected", true);
                    if(historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId)
                            .taskAssignee(userLogin).taskDefinitionKey(userTask.getId()).orderByHistoricTaskInstanceEndTime().desc().count()>0){
                        String preTaskId=historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId)
                                .taskAssignee(userLogin).taskDefinitionKey(userTask.getId()).orderByHistoricTaskInstanceEndTime().desc().list().get(0).getId();
                        if(historyService.createHistoricVariableInstanceQuery().taskId(preTaskId).variableName("back_candidate").singleResult()==null){
                            //代表已经处理过了,并且是发送,所以就默认不选上了
                            user.put("selected",false);
                        }
                    }
                    users.add(user);
                }
            }catch (Exception ex){
                System.out.println(userTask.getName()+"获取用户失败!");
            }
        } else if (StringUtils.isNotEmpty(userTask.getAssignee())) {

            try {
                String assigne = userTask.getAssignee();
                if (assigne.contains("${")) {
                    ExpressionFactory factory = new ExpressionFactoryImpl();
                    SimpleContext context = new SimpleContext();
                    for (Object key : variables.keySet()) {
                        if(variables.get(key)!=null) {
                            context.setVariable(key.toString(), factory.createValueExpression(variables.get(key), variables.get(key).getClass()));
                        }
                    }
                    assigne = factory.createValueExpression(context, assigne, String.class).getValue(context).toString();
                }
                Map user = Maps.newHashMap();
                user.put("userLogin", assigne);
                user.put("userName", hrEmployeeMapper.getNameByUserLogin(assigne));
                user.put("selected", true);
                users.add(user);
            }catch (Exception ex){
                System.out.println(userTask.getName()+"获取用户失败!");
            }
        } else if (userTask.getCandidateUsers().size() > 0) {

            List<String> candidateUsers=Lists.newArrayList();
            for(String sysCandidateUser:userTask.getCandidateUsers()) {
                if (sysCandidateUser.contains("${")) {
                    ExpressionFactory factory = new ExpressionFactoryImpl();
                    SimpleContext context = new SimpleContext();
                    for (Object key : variables.keySet()) {
                        if(variables.get(key)!=null) {
                            context.setVariable(key.toString(), factory.createValueExpression(variables.get(key), variables.get(key).getClass()));
                        }
                    }
                    Object result = factory.createValueExpression(context, sysCandidateUser, List.class).getValue(context);
                    candidateUsers.addAll((ArrayList<String>) result);
                }else{
                    candidateUsers.add(sysCandidateUser);
                }
            }

            String pre = "";
            if (historicActivityInstances.stream().anyMatch(p -> p.getTaskId().equalsIgnoreCase(userTask.getId()))) {
                pre = historicActivityInstances.stream().filter(p -> p.getTaskId().equalsIgnoreCase(userTask.getId())).map(p -> p.getAssignee()).findFirst().get();
            }

            for (String userLogin : candidateUsers) {
                Map user = Maps.newHashMap();
                user.put("userLogin", userLogin);
                user.put("userName", hrEmployeeMapper.getNameByUserLogin(userLogin));
                user.put("selected", pre.equalsIgnoreCase(userLogin));
                users.add(user);
            }

            if(StringUtils.isEmpty(pre)&&users.size()>=1){
                users.get(0).put("selected",true);
            }
        }

        if(candidates.size()>0&&users.size()>1){
            for(Map user:users) {
                String userLogin = user.get("userLogin").toString();
                user.put("selected", candidates.contains(userLogin));
            }
        }
        return users;
    }


    /**
     * 得到打回数据
     *
     * @param taskId
     * @return
     */
    public List<UserTaskDto> listBackUserTask(String taskId) {

        List<UserTaskDto> list = Lists.newArrayList();
        //获得当前任务 判断任务状态
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String definitionKey = task.getTaskDefinitionKey();

        //根据任务id得到流程实例id
        String processInstanceId = task.getProcessInstanceId();
        String processDefinitionId = historyService.createHistoricActivityInstanceQuery().processInstanceId(task.getProcessInstanceId()).list().get(0).getProcessDefinitionId();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);


        Map variables = taskService.getVariables(taskId);
        //得到历史任务信息
        List<HistoricActivityInstance> historicActivityInstances = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByHistoricActivityInstanceStartTime().desc()
                .list().stream()
                .filter(p -> p.getActivityType().equals("userTask"))
                .filter(p -> !p.getTaskId().equalsIgnoreCase(definitionKey))
                .collect(Collectors.toList());

        //流程定义查找前面的userTaskKey
        UserTask currentUserTask = (UserTask) bpmnModel.getFlowElement(definitionKey);


        List<SequenceFlow> incomingFlows = currentUserTask.getIncomingFlows();
        FlowElement preElement = bpmnModel.getMainProcess().getFlowElement(incomingFlows.get(0).getSourceRef());
        //如果是并行网关、或者包容网关只允许打回到上一级
        if ((preElement instanceof ParallelGateway || preElement instanceof InclusiveGateway) && ((Gateway) preElement).getOutgoingFlows().size() > 1) {
            List<SequenceFlow> flows = ((Gateway) preElement).getIncomingFlows();
            FlowElement flowElement = bpmnModel.getMainProcess().getFlowElement(flows.get(0).getSourceRef());
            if (flowElement instanceof UserTask) {
                UserTask userTask = (UserTask) flowElement;
                UserTaskDto model = new UserTaskDto();
                model.setPreKey(definitionKey);
                model.setTaskKey(userTask.getId());
                model.setTaskName(userTask.getName());
                model.setMultiple(userTask.getLoopCharacteristics() != null);
                if (list.stream().noneMatch(p -> p.getTaskKey().equalsIgnoreCase(userTask.getId()))) {
                    model.setUsers(getUserList(processInstanceId, userTask, historicActivityInstances, variables));
                    list.add(model);
                }
            }
        } else {
            recursionFindPreTask(processInstanceId, historicActivityInstances, variables, (FlowNode) preElement, bpmnModel, list);
        }
        list.sort(Comparator.comparing(UserTaskDto::getPreKey));
        return list;
    }

    private void recursionFindPreTask(String processInstanceId, List<HistoricActivityInstance> historicActivityInstances, Map<String, Object> variables, FlowNode flowNode, BpmnModel bpmnModel, List<UserTaskDto> list) {

        List<SequenceFlow> sequenceFlowList = flowNode.getIncomingFlows();
        if (flowNode instanceof UserTask) {
            UserTask userTask = (UserTask) flowNode;
            UserTaskDto model = new UserTaskDto();
            model.setPreKey(sequenceFlowList.get(0).getSourceRef());
            model.setTaskKey(userTask.getId());
            model.setTaskName(userTask.getName());
            model.setMultiple(userTask.getLoopCharacteristics() != null);
            if (list.stream().anyMatch(p -> p.getTaskKey().equalsIgnoreCase(userTask.getId()))) {
                return;
            }
            model.setUsers(getUserList(processInstanceId, userTask, historicActivityInstances, variables));
            list.add(model);
        }
        if (sequenceFlowList.size() > 0) {
            for (SequenceFlow sequenceFlow : sequenceFlowList) {
                String sourceRef = sequenceFlow.getSourceRef();
                if (StringUtils.isNotEmpty(sourceRef)) {
                    FlowNode preFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(sourceRef);
                    if (preFlowNode instanceof StartEvent) {
                        return;
                    }
                    recursionFindPreTask(processInstanceId, historicActivityInstances, variables, preFlowNode, bpmnModel, list);
                }
            }
        }
    }



}
