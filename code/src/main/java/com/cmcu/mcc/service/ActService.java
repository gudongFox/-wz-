package com.cmcu.mcc.service;

import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.act.model.MyProcessInstance;
import com.cmcu.mcc.act.service.MyHistoryService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.dto.ActHistoryDto;
import com.cmcu.mcc.dto.ProcessInstanceDto;
import com.cmcu.mcc.five.dao.FiveEdHouseReferMapper;
import com.cmcu.mcc.five.dao.FiveEdHouseValidateMapper;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.sys.dao.SysCadMessageMapper;
import com.cmcu.mcc.sys.entity.SysCadMessage;
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
import org.activiti.engine.impl.persistence.entity.CommentEntityImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskInfo;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.el.ExpressionFactory;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ActService {


    @Autowired
    MyHistoryService myHistoryService;

    @Resource
    HistoryService historyService;


    @Resource
    RuntimeService runtimeService;

    @Resource
    TaskService taskService;

    @Resource
    RepositoryService repositoryService;

    @Resource
    SysCadMessageMapper sysCadMessageMapper;

    @Resource
    HrEmployeeMapper hrEmployeeMapper;

    @Autowired
    JdbcTemplate actJdbcTemplate;


    @Resource
    FiveEdHouseReferMapper fiveEdHouseReferMapper;

    @Resource
    FiveEdHouseValidateMapper fiveEdHouseValidateMapper;


    /**
     * 得到某个流程用户的状态
     * @param processInstanceId
     * @param userLogin
     * @return
     */
    public ProcessInstanceDto getProcessInstanceDto(String processInstanceId, String userLogin) {
        Assert.state(StringUtils.isNotEmpty(processInstanceId), "该任务并未发起流程!");
        String userName=hrEmployeeMapper.getNameByUserLogin(userLogin);
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        ProcessInstanceDto item = new ProcessInstanceDto();
        item.setProcessInstanceId(processInstanceId);
        item.setPassAble(false);
        item.setBackAble(false);
        item.setMyTaskName("");
        item.setMyTaskFirst(false);
        item.setProcessEnd(false);
        item.setProcessName("已完成");
        if(historicProcessInstance!=null) {
            item.setProcessDefinitionName(historicProcessInstance.getProcessDefinitionName());
            item.setBusinessKey(historicProcessInstance.getBusinessKey());
            item.setProcessTime(historyService.createHistoricActivityInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .orderByHistoricActivityInstanceStartTime().desc().listPage(1, 1).get(0).getStartTime());

            if (StringUtils.isNotEmpty(userLogin)) {
                item.setUserLogin(userLogin);
                item.setUserName(userName);
            }
            List<Map> taskList = Lists.newArrayList();
            if (taskService.createTaskQuery().processInstanceId(processInstanceId).count() > 0) {
                try {
                    List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
                    tasks.forEach(task -> {
                        Map taskMap = Maps.newHashMap();
                        taskMap.put("name", task.getName());
                        taskMap.put("assigne", task.getAssignee());
                        taskMap.put("assigneName", StringUtils.isNotEmpty(task.getAssignee()) ? hrEmployeeMapper.getNameByUserLogin(task.getAssignee()) : "");
                        taskMap.put("createTime", task.getCreateTime());
                        if (StringUtils.isNotEmpty(userLogin) && task.getAssignee().equals(userLogin)) {
                            if (!item.isPassAble()) {
                                item.setMyTaskId(task.getId());
                                if (StringUtils.isNotEmpty(item.getMyTaskName())) {
                                    item.setMyTaskName(item.getMyTaskName() + "," + task.getName());
                                } else {
                                    item.setMyTaskName(task.getName());
                                }
                                item.setMyTaskTime(task.getCreateTime());
                                item.setPassAble(true);
                                boolean backAble = false;
                                BpmnModel bpmnModel = repositoryService.getBpmnModel(historicProcessInstance.getProcessDefinitionId());
                                FlowNode currentFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(task.getTaskDefinitionKey());
                                List<SequenceFlow> sequenceFlowList = currentFlowNode.getIncomingFlows();
                                if (sequenceFlowList.size() > 0) {
                                    for (SequenceFlow sequenceFlow : sequenceFlowList) {
                                        String sourceRef = sequenceFlow.getSourceRef();
                                        if (StringUtils.isNotEmpty(sourceRef)) {
                                            FlowElement preElement = bpmnModel.getMainProcess().getFlowElement(sourceRef);
                                            if (preElement instanceof StartEvent) {
                                                item.setMyTaskFirst(true);
                                            } else {
                                                backAble = true;
                                            }
                                        }
                                    }
                                }
                                item.setBackAble(backAble);
                                if (task.getClaimTime() == null) {
                                    taskService.claim(task.getId(), userLogin);
                                }
                            }
                        }
                        taskList.add(taskMap);
                    });


                    StringBuilder sb = new StringBuilder();

                    for (String name : taskList.stream().map(p -> p.get("name").toString()).distinct().collect(Collectors.toList())) {
                        sb.append(name);
                        sb.append("(");
                        sb.append(StringUtils.join(taskList.stream().filter(p -> p.get("name").toString().equals(name)).map(p -> p.get("assigneName").toString()).distinct().collect(Collectors.toList()), ","));
                        sb.append(") ");
                    }
                    item.setProcessName(sb.toString());

                    String taskUsers = "";
                    for (String assigne : taskList.stream().map(p -> p.get("assigne").toString()).distinct().collect(Collectors.toList())) {
                        taskUsers = taskUsers + "," + assigne;
                    }

                    item.setTaskUsers(taskUsers);
                } catch (Exception ex) {
                }
            } else {
                item.setProcessEnd(true);
            }
            item.setTaskList(taskList);
            ModelUtil.setNotNullFields(item);
        }
        return item;
    }



    public ProcessInstanceDto getProcessInstanceDto(String processInstanceId){
        return getProcessInstanceDto(processInstanceId,"");
    }

    public void setVariables(String processInstanceId ,String key,Object message){
        String taskId=taskService.createTaskQuery().processInstanceId(processInstanceId).active().list().get(0).getId();
        taskService.setVariable(taskId,key,message);
    }





    /**
     * 得到某个流程的当前节点
     * @param processInstanceId
     * @return
     */
    public String getProcessCurrentName(String processInstanceId) {

        if (StringUtils.isEmpty(processInstanceId)) return "未发起流程";

        List<String> processList = Lists.newArrayList();

        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).active().list();
        for (Task task : tasks) {
            StringBuilder sb = new StringBuilder();
            sb.append(task.getName());
            if (StringUtils.isNotEmpty(task.getAssignee())) {
                sb.append("(");
                sb.append(hrEmployeeMapper.getNameByUserLogin(task.getAssignee()));
                sb.append(")");
            }
            processList.add(sb.toString());
        }
        return StringUtils.join(processList.toArray(), ",");
    }


    public Map getProcessCurrentMap(String processInstanceId) {

        Map result = Maps.newHashMap();
        result.put("processCurrentName", "");
        result.put("processCurrentUser", "");
        result.put("processCurrentUserName", "");


        if (StringUtils.isEmpty(processInstanceId)) return result;

        List<String> processList = Lists.newArrayList();
        List<String> userList = Lists.newArrayList();
        List<String> userNameList = Lists.newArrayList();
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).active().list();
        for (Task task : tasks) {
            processList.add(task.getName());
            if (StringUtils.isNotEmpty(task.getAssignee())) {

                userList.add(task.getAssignee());
                userNameList.add(hrEmployeeMapper.getNameByUserLogin(task.getAssignee()));
            }
        }
        result.put("processCurrentName", StringUtils.join(processList.toArray(), ","));
        result.put("processCurrentUser", StringUtils.join(userList.toArray(), ","));
        result.put("processCurrentUserName", StringUtils.join(userNameList.toArray(), ","));
        return result;
    }


    public List<ActHistoryDto> listHistoryDto(String processInstanceId) {

        List<ActHistoryDto> list = new ArrayList<>();

        List<HistoricActivityInstance> oList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByHistoricActivityInstanceStartTime().asc()
                .list().stream().filter(p -> p.getActivityType().contains("userTask")||p.getActivityType().contains("startEvent")||p.getActivityType().contains("endEvent")).collect(Collectors.toList());



        for (HistoricActivityInstance item : oList) {
            ActHistoryDto actHistoryDto = new ActHistoryDto();
            actHistoryDto.setStart(item.getStartTime());
            actHistoryDto.setEnd(item.getEndTime());
            actHistoryDto.setTaskId(item.getTaskId());
            actHistoryDto.setActivityName(item.getActivityName());

            actHistoryDto.setStartTime(item.getStartTime()==null?"": DateFormatUtils.format(item.getStartTime(),"yyyy-MM-dd HH:mm"));
            actHistoryDto.setEndTime(item.getEndTime()==null?"": DateFormatUtils.format(item.getEndTime(),"yyyy-MM-dd HH:mm"));
            actHistoryDto.setPassed(true);
            actHistoryDto.setUserName("");
            actHistoryDto.setComment("");
            if (StringUtils.isNotEmpty(item.getAssignee())) {
                actHistoryDto.setUserLogin(item.getAssignee());
                actHistoryDto.setUserName(hrEmployeeMapper.getNameByUserLogin(item.getAssignee()));
            }
            actHistoryDto.setFinished(item.getEndTime() != null);

           if(actHistoryDto.getActivityName()==null){
               actHistoryDto.setActivityName("结束");
           } else if (actHistoryDto.getActivityName().toLowerCase().equals("start")) {
                actHistoryDto.setActivityName("开始");
            } else if (actHistoryDto.getActivityName().toLowerCase().equals("end")) {
                actHistoryDto.setActivityName("结束");
            }
           else {
                List<Comment> comments = taskService.getTaskComments(item.getTaskId());
                if (comments.size() > 0) {
                    String message = ((CommentEntityImpl) comments.get(0)).getMessage();
                    actHistoryDto.setComment(message);//默认第一条评论
                }

                boolean passed = true;
                if (historyService.createHistoricVariableInstanceQuery().taskId(item.getTaskId()).variableName("passed").count() > 0) {
                    passed = Boolean.parseBoolean(historyService.createHistoricVariableInstanceQuery().taskId(item.getTaskId()).variableName("passed").singleResult().getValue().toString());
                }
                actHistoryDto.setPassed(passed);
            }
            if(!"AUTO_FINISH".equalsIgnoreCase(actHistoryDto.getComment())) {
                list.add(actHistoryDto);
            }
        }
        return list;
    }


    public List<ActHistoryDto> listPassedHistoryDto(String processInstanceId){
        //流程信息
        List<ActHistoryDto> nodes=Lists.newArrayList();
        List<ActHistoryDto> historicInstances=listHistoryDto(processInstanceId).stream().sorted(Comparator.comparing(ActHistoryDto::getStart).reversed()).collect(Collectors.toList());
        //2020-12-18HNZ 排除取回任务 没有节点处理人
        List<ActHistoryDto> collect = historicInstances.stream().filter(p -> p.isFinished()).filter(p -> p.isPassed()).filter(p ->!p.getComment().contains("取回") ).collect(Collectors.toList());
       //2020-12-18HNZ 意见排序倒序 方便取最新的意见内容
        collect.sort(Comparator.comparing(ActHistoryDto::getEnd).reversed());
        for(ActHistoryDto actHistoryDto:collect) {
            if (actHistoryDto.getActivityName().contains("结束") || actHistoryDto.getActivityName().contains("开始")) {

            } else if (nodes.stream().noneMatch(p -> (p.getActivityName().equalsIgnoreCase(actHistoryDto.getActivityName())
                    && p.getUserLogin().equalsIgnoreCase(actHistoryDto.getUserLogin())))) {
                nodes.add(actHistoryDto);
            }
        }
        nodes.sort(Comparator.comparing(ActHistoryDto::getEnd));
        if(nodes.size()%2>0){
            nodes.add(new ActHistoryDto());
        }
        return nodes;
    }


    /**
     * 发送流程
     * @param taskId
     * @param users
     * @param flowComment
     * @param variables
     */
    public void sendFlow(String taskId,String[] users,String flowComment,Map variables) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        taskService.setVariableLocal(taskId, "passed", true);
        taskService.addComment(taskId, task.getProcessInstanceId(), flowComment);

        String processDefinitionId =task.getProcessDefinitionId();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        String taskDefinitionKey = task.getTaskDefinitionKey();
        FlowNode currentFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(taskDefinitionKey);
        List<SequenceFlow> sequenceFlowList = currentFlowNode.getOutgoingFlows();
        FlowElement nextFlowElement = bpmnModel.getMainProcess().getFlowElement(sequenceFlowList.get(0).getTargetRef());
        if (nextFlowElement instanceof UserTask) {
            UserTask nextTask=((UserTask)nextFlowElement);
            List<String> selectedList=Lists.newArrayList();
            for (String selectedUser : users) {
                if (StringUtils.split(selectedUser, '_').length == 2 &&StringUtils.split(selectedUser, '_')[0].equals(nextTask.getId())) {
                    selectedList.add(StringUtils.split(selectedUser, '_')[1]);
                }
            }

            //会签此处已经设置了人,无需再设置人,所以完成后直接结束
            if(nextTask.getLoopCharacteristics()!=null){
                String key=StringUtils.replace(nextTask.getLoopCharacteristics().getInputDataItem(),"${","");
                key=key.substring(0,key.length()-1);
                variables.put(key,selectedList);
                taskService.complete(taskId,variables);
                return;
            }
        }

        taskService.complete(taskId,variables);


        List<Task> tasks = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).taskUnassigned().list();
        for (Task nextTask : tasks) {
            if(StringUtils.isEmpty(nextTask.getAssignee())) {
                for (String selectedUser : users) {
                    if (StringUtils.split(selectedUser, '_').length == 2 && StringUtils.split(selectedUser, '_')[0].equals(nextTask.getId())) {
                        taskService.setAssignee(nextTask.getId(), StringUtils.split(selectedUser, '_')[1]);
                        break;
                    }
                }
            }
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
    private Map getUserTaskDirectPassable(String taskId) {

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



    /**
     * 并行分支必须是单节点
     * 排他包容网关判断条件的值必须为全局变量
     * @param taskId 当前任务节点Id
     * @param users 打回的节点_用户,[userProofread_luodong,userAudit_hnz]
     * @param flowComment 当前任务打回审核信息
     */
    public void backFlow(String taskId,String[] users,String flowComment) {

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        List<Task> tasks=taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).list();
        List<String> candidates = Lists.newArrayList();
        if (users.length > 0) {
            candidates=Arrays.stream(users).filter(p -> StringUtils.split(p, "_").length == 2).collect(Collectors.toList());
        }

        List<String> preGroup=candidates.stream().map(p-> StringUtils.split( StringUtils.split(p,"_")[0],"-")[0]).distinct().collect(Collectors.toList());
        Assert.state(candidates.size()>0,"请选择你要打回到的节点!");
        Assert.state(preGroup.size()==1,"只能打回到某个确定的节点!");

        //获得当前任务 判断任务状态
        taskService.addComment(task.getId(), task.getProcessInstanceId(), flowComment);
        taskService.setVariableLocal(taskId, "back_candidate", StringUtils.join(candidates, ","));
        taskService.setVariableLocal(taskId, "passed", false);


        String taskDefinitionKey = task.getTaskDefinitionKey();
        String processDefinitionId = historyService.createHistoricActivityInstanceQuery().processInstanceId(task.getProcessInstanceId()).list().get(0).getProcessDefinitionId();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        FlowNode currentFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(taskDefinitionKey);
        List<String> nextTaskKeys=candidates.stream().map(p->StringUtils.split(StringUtils.split(p,"_")[0],"-")[1]).distinct().collect(Collectors.toList());
        for(String nextTaskKey:nextTaskKeys){
            UserTask userTask=(UserTask) bpmnModel.getMainProcess().getFlowElement(nextTaskKey);
            if(userTask.getLoopCharacteristics()!=null){
                String key=StringUtils.substring(userTask.getLoopCharacteristics().getInputDataItem(),2,userTask.getLoopCharacteristics().getInputDataItem().length()-1);
                taskService.setVariable(taskId,key,candidates.stream()
                        .filter(p ->StringUtils.split(StringUtils.split(p, "_")[0],"-")[1].equalsIgnoreCase(userTask.getId()))
                        .map(p->StringUtils.split(p,"_")[1]).distinct().collect(Collectors.toList()));
            }
        }



        //只剩当前任务,则直接打回
        boolean directBack=false;
        if(tasks.size()==1){
            directBack=true;
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
                if(destPreFlowNode instanceof ParallelGateway){
                    List<SequenceFlow> newSequenceFlowList = new ArrayList<>();
                    SequenceFlow newSequenceFlow = new SequenceFlow();
                    newSequenceFlow.setId("tempSequenceFlow_" + UUID.randomUUID().toString());
                    newSequenceFlow.setSourceFlowElement(currentFlowNode);
                    newSequenceFlow.setTargetFlowElement(destPreFlowNode);
                    newSequenceFlowList.add(newSequenceFlow);
                    currentFlowNode.setOutgoingFlows(newSequenceFlowList);
                }else {
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

                    if(StringUtils.isEmpty(nextTask.getAssignee())) {
                        if (candidates.stream().anyMatch(p -> StringUtils.split(StringUtils.split(p, "_")[0], "-")[1].equalsIgnoreCase(nextTask.getTaskDefinitionKey()))) {
                            String assigne = candidates.stream().filter(p -> StringUtils.split(StringUtils.split(p, "_")[0], "-")[1].equalsIgnoreCase(nextTask.getTaskDefinitionKey())).map(p -> StringUtils.split(p, "_")[1]).findFirst().get();
                            taskService.setAssignee(nextTask.getId(), assigne);
                        }
                    }
                    //相当于打回到并行任务的网关上
                    if(destPreFlowNode instanceof ParallelGateway){
                        if(!nextTaskKeys.contains(nextTask.getTaskDefinitionKey())){
                            taskService.addComment(nextTask.getId(), nextTask.getProcessInstanceId(), "AUTO_FINISH");
                            taskService.complete(nextTask.getId());
                        }
                    }
                }

                //如果当前任务是并行网关任务,则手动强制删除一些EXECUTION数据
                FlowNode preFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(currentFlowNode.getIncomingFlows().get(0).getSourceRef());
                if(preFlowNode instanceof ParallelGateway) {
                    deleteExecution(task.getProcessInstanceId(), currentFlowNode.getOutgoingFlows().get(0).getTargetRef());
                }
            }
        }else {

            taskService.complete(taskId, variables);
        }
    }




    /**
     * 处理简单流程
     * @param taskId
     * @param passed
     * @param comment
     */
    public void handleSimpleTask(String taskId,boolean passed,String comment) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String assigne=task.getAssignee();
        taskService.setVariableLocal(taskId, "passed", passed);
        if (StringUtils.isNotEmpty(comment)) {
            taskService.addComment(taskId, task.getProcessInstanceId(), comment);
        }

        if (passed) {
            taskService.complete(taskId);
        } else {
            String taskDefinitionKey = task.getTaskDefinitionKey();
            String processDefinitionId = historyService.createHistoricActivityInstanceQuery().processInstanceId(task.getProcessInstanceId()).list().get(0).getProcessDefinitionId();
            BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
            FlowNode currentFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(taskDefinitionKey);

            String destTaskKey = "";
            //当前节点得前一根线
            SequenceFlow incomingFlow = currentFlowNode.getIncomingFlows().get(0);
            String sourceRef = incomingFlow.getSourceRef();
            FlowElement preElement = bpmnModel.getMainProcess().getFlowElement(sourceRef);
            if (preElement instanceof UserTask) {
                destTaskKey = preElement.getId();
            } else if (preElement instanceof Gateway) {
                SequenceFlow incomingFlow1 = ((Gateway) preElement).getIncomingFlows().get(0);
                destTaskKey = incomingFlow1.getSourceRef();
            }


            HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery().processInstanceId(task.getProcessInstanceId()).taskDefinitionKey(destTaskKey).orderByHistoricTaskInstanceEndTime().desc().list().get(0);
            //记录原活动方向
            List<SequenceFlow> originalOutGoingFlows = new ArrayList<>(currentFlowNode.getOutgoingFlows());
            currentFlowNode.getOutgoingFlows().clear();


            FlowNode destFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(destTaskKey);
            FlowNode destPrePreFlowNode = null;
            List<SequenceFlow> incomingFlows = destFlowNode.getIncomingFlows();
            FlowNode destPreFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(incomingFlows.get(0).getSourceRef());

            if (!(destPreFlowNode instanceof UserTask || destPreFlowNode instanceof ExclusiveGateway || destPreFlowNode instanceof StartEvent)) {
                //如果不是结束的并行和包容网关
                if (destPreFlowNode.getOutgoingFlows().size() > 1) {
                    destPrePreFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(destPreFlowNode.getIncomingFlows().get(0).getSourceRef());
                }
            }


            List<SequenceFlow> newSequenceFlowList = new ArrayList<>();
            SequenceFlow newSequenceFlow = new SequenceFlow();
            newSequenceFlow.setId("tempSequenceFlow_" + UUID.randomUUID().toString());
            newSequenceFlow.setSourceFlowElement(currentFlowNode);
            newSequenceFlow.setTargetFlowElement(destPrePreFlowNode == null ? destFlowNode : destPrePreFlowNode);
            newSequenceFlowList.add(newSequenceFlow);
            currentFlowNode.setOutgoingFlows(newSequenceFlowList);
            taskService.complete(task.getId());

            if (destPrePreFlowNode != null) {
                for (Task preTempTask : taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).list()) {
                    taskService.addComment(preTempTask.getId(), task.getProcessInstanceId(), "系统自动完成");
                    taskService.complete(preTempTask.getId());
                }
            }

            List<Task> tempTasks = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).list();
            for (Task tempTask : tempTasks) {
                if (!destTaskKey.equals(tempTask.getTaskDefinitionKey())) {
                    taskService.addComment(tempTask.getId(), task.getProcessInstanceId(), "系统自动完成");
                    taskService.complete(tempTask.getId());
                } else {
                    taskService.setAssignee(tempTask.getId(), historicTaskInstance.getAssignee());
                }
            }
            currentFlowNode.setOutgoingFlows(originalOutGoingFlows);
        }

        List<Task> tempTasks = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).includeProcessVariables().list();
        for (Task tempTask : tempTasks) {
            String targeAssigne=tempTask.getAssignee();
            String description=tempTask.getProcessVariables().get("description").toString();
            SysCadMessage sysCadMessage=new SysCadMessage();
            sysCadMessage.setDelaySecond(30);
            sysCadMessage.setGmtCreate(new Date());
            sysCadMessage.setMessageContent("收到新的"+tempTask.getName()+"任务("+description+")!");
            sysCadMessage.setMessageType("notice");
            sysCadMessage.setReceived(false);
            sysCadMessage.setReceiver(targeAssigne);
            sysCadMessage.setReceiverName(hrEmployeeMapper.getNameByUserLogin(targeAssigne));
            sysCadMessage.setSender(assigne);
            sysCadMessage.setSenderName(hrEmployeeMapper.getNameByUserLogin(assigne));
            sysCadMessage.setMessageCaption("设计验证");
            sysCadMessageMapper.insert(sysCadMessage);
        }


    }


    /**
     * 得到打回数据
     * @param taskId
     * @return
     */
    public List<Map> listBackStep(String taskId) {


        List<Map> list = Lists.newArrayList();

        //获得当前任务 判断任务状态
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String definitionKey=task.getTaskDefinitionKey();

        //根据任务id得到流程实例id
        String processInstanceId = task.getProcessInstanceId();
        //得到定义文件
        String processDefinitionId = historyService.createHistoricActivityInstanceQuery().processInstanceId(task.getProcessInstanceId()).list().get(0).getProcessDefinitionId();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);


        //流程定义查找前面的userTaskKey
        List<UserTask> preTaskKeyList = Lists.newArrayList();
        String taskDefinitionKey = task.getTaskDefinitionKey();
        UserTask currentUserTask=(UserTask)bpmnModel.getFlowElement(definitionKey);
        //如果当前是会签任务,仅可打回到上一节点
        if(currentUserTask.getLoopCharacteristics()!=null){
            List<SequenceFlow> incomingFlows = currentUserTask.getIncomingFlows();
            FlowElement preElement = bpmnModel.getMainProcess().getFlowElement(incomingFlows.get(0).getSourceRef());
            if(preElement instanceof  UserTask){



            }
        }



        recursionFindPreTask(bpmnModel, taskDefinitionKey, preTaskKeyList);


        //得到历史任务信息
        List<HistoricActivityInstance> oHistoricActivityInstanceList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByHistoricActivityInstanceStartTime().desc()
                .list().stream()
                .filter(p -> p.getActivityType().equals("userTask")).filter(p -> !p.getTaskId().equals(taskId))
                .filter(p -> preTaskKeyList.stream().filter(o -> o.getId().equals(p.getActivityId())).count() == 1)
                .collect(Collectors.toList());


        //去重历史信息
        List<HistoricActivityInstance> historicActivityInstanceList = Lists.newArrayList();
        for (HistoricActivityInstance historicActivityInstance : oHistoricActivityInstanceList) {
            if (historicActivityInstanceList.stream().filter(p -> p.getActivityId().equals(historicActivityInstance.getActivityId())).count() == 0) {
                historicActivityInstanceList.add(historicActivityInstance);
            }
        }


        for (HistoricActivityInstance historicActivityInstance : historicActivityInstanceList) {
            //非当前任务
            UserTask userTask = preTaskKeyList.stream().filter(p -> p.getId().equals(historicActivityInstance.getActivityId())).findFirst().get();
            list.add(getUserTaskCandicate(null,userTask, historicActivityInstance));
        }
        return list;
    }


    private void recursionFindPreTask( BpmnModel bpmnModel, String taskDefinitionKey, List<UserTask> preTaskKeyList) {
        if(!preTaskKeyList.contains(taskDefinitionKey)) {
            FlowNode currentFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(taskDefinitionKey);
            List<SequenceFlow> sequenceFlowList = currentFlowNode.getIncomingFlows();
            if (sequenceFlowList.size() > 0) {
                for (SequenceFlow sequenceFlow : sequenceFlowList) {
                    String sourceRef = sequenceFlow.getSourceRef();
                    if (StringUtils.isNotEmpty(sourceRef)) {
                        FlowElement preElement = bpmnModel.getMainProcess().getFlowElement(sourceRef);
                        if (preElement instanceof UserTask) {
                            UserTask preUserTask = (UserTask) preElement;
                            preTaskKeyList.add(preUserTask);
                            recursionFindPreTask(bpmnModel, preUserTask.getId(), preTaskKeyList);
                        } else {
                            recursionFindPreTask(bpmnModel, preElement.getId(), preTaskKeyList);
                        }
                    }
                }
            }
        }

    }

    /**
     * 得到某个用户任务的候选人
     * @param userTask
     * @return
     */
    private  Map getUserTaskCandicate(Map variables,UserTask userTask,HistoricActivityInstance historicActivityInstance) {
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
                user.put("userName", hrEmployeeMapper.getNameByUserLogin(userLogin));
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
            user.put("userName", hrEmployeeMapper.getNameByUserLogin(assigne));
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
            Map map=Maps.newHashMap();
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

    public String startProcess(String processKey,int projectId,int businessId,Map<String,Object> variables) {

        String businessKey=processKey + "_" + projectId + "_" + businessId;
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processKey, businessKey, variables);
        return processInstance.getId();
    }

    public String startProcess(String processKey,int businessId,Map<String,Object> variables) {
        String businessKey = processKey + "_" + businessId;
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processKey, businessKey, variables);
        return processInstance.getId();
    }

    /**
     * 判断流程是否在用户那里
     * @param processInstanceId
     * @param userLogin
     * @return
     */
    public Boolean checkProcessOnUser(String processInstanceId,String userLogin){
        return taskService.createTaskQuery().processInstanceId(processInstanceId).taskAssignee(userLogin).count()>0;
    }

    /**
     * 完成任务
     * @param processInstanceId
     * @param userLogin
     * @param variables
     */
    public boolean complete(String processInstanceId,String comment,String userLogin,Map<String,Object> variables) {
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).taskCandidateOrAssigned(userLogin).list();
        Assert.state(tasks.size() == 1, "当前任务不为1");
        String taskId = tasks.get(0).getId();
        taskService.setVariableLocal(taskId,"passed","0");//打回状态
        taskService.addComment(taskId, processInstanceId, comment);
        taskService.complete(taskId, variables);
        return false;
    }



    public Map listTask(String userLogin,int pageNum,int pageSize,String taskName) {
        Map map = Maps.newHashMap();
        map.put("userLogin", userLogin);
        map.put("time", new Date());
        TaskQuery taskQuery = taskService.createTaskQuery().taskAssignee(userLogin).active().orderByTaskCreateTime().desc().includeProcessVariables();
        if (StringUtils.isNotEmpty(taskName)) {
            taskQuery = taskQuery.processDefinitionNameLike("%"+taskName+"%");
        }
        map.put("pageInfo", WebUtil.getPageInfo(pageNum, pageSize, (int) taskQuery.count()));
        List<Map> list = Lists.newArrayList();
        for (Task task : taskQuery.listPage((pageNum - 1) * pageSize, pageSize)) {

            Map item = Maps.newHashMap();
            MyProcessInstance myProcessInstance=myHistoryService.getMyProcessInstance(task.getProcessInstanceId(),userLogin);
            item.put("description", task.getProcessVariables().get("description"));
            if(task.getProcessVariables().get("processDefinitionName")!=null){
                item.put("processDefinitionName",task.getProcessVariables().get("processDefinitionName").toString());
                item.put("businessKey",task.getProcessVariables().get("businessKey").toString());
            }else{
                HistoricProcessInstance processInstance =historyService.createHistoricProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
                item.put("processDefinitionName", processInstance.getProcessDefinitionName());
                item.put("businessKey", processInstance.getBusinessKey());
                taskService.setVariable(task.getId(),"businessKey",processInstance.getBusinessKey());
                taskService.setVariable(task.getId(),"processDefinitionName",processInstance.getProcessDefinitionName());
            }
            item.put("preHandleTime",myProcessInstance.getPreHandleTime());
            item.put("name", task.getName());
            item.put("createTime", task.getCreateTime());
            list.add(item);
        }
        map.put("list", list);
        return map;
    }

    public Map listDoneTask(String userLogin,int pageNum,int pageSize,String taskName) {
        Map map = Maps.newHashMap();
        map.put("userLogin", userLogin);
        map.put("time", new Date());
        HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery().taskAssignee(userLogin).finished().orderByHistoricTaskInstanceEndTime().desc().includeProcessVariables();
        if (StringUtils.isNotEmpty(taskName)) {
            historicTaskInstanceQuery=historicTaskInstanceQuery.processDefinitionNameLike("%"+taskName+"%");
        }
        map.put("pageInfo", WebUtil.getPageInfo(pageNum, pageSize, (int) historicTaskInstanceQuery.count()));
        List<Map> list = Lists.newArrayList();
        for (HistoricTaskInstance task : historicTaskInstanceQuery.listPage((pageNum - 1) * pageSize, pageSize)) {
            try {
                MyProcessInstance processInstanceDto = myHistoryService.getMyProcessInstance(task.getProcessInstanceId(), userLogin);
                Map item = Maps.newHashMap();
                item.put("description", task.getProcessVariables().get("description"));
                item.put("processDefinitionName", processInstanceDto.getProcessDefinitionName());
                if (task.getProcessVariables().get("processDefinitionName") != null) {
                    item.put("processDefinitionName", task.getProcessVariables().get("processDefinitionName").toString());
                }
                item.put("businessKey", processInstanceDto.getBusinessKey());
                item.put("name", task.getName());
                item.put("createTime", task.getCreateTime());
                item.put("endTime", task.getEndTime());
                item.put("processName", processInstanceDto.getProcessName());
                item.put("preHandleTime", processInstanceDto.getPreHandleTime());
                list.add(item);
            }catch (Exception ex){
                System.out.println(task.getProcessInstanceId());
                ex.printStackTrace();
            }
        }
        map.put("list", list);
        return map;
    }

    public List<Map> listCadTask(String userLogin,String description) {

        TaskQuery taskQuery = taskService.createTaskQuery().taskAssignee(userLogin).active().orderByTaskCreateTime().desc().includeProcessVariables().includeTaskLocalVariables();
        taskQuery=taskQuery.processDefinitionKeyIn(Arrays.asList(EdConst.PROCESS_ED_HOUSE_REFER, EdConst.PROCESS_ED_HOUSE_VALIDATE,EdConst.PROCESS_ED_HOUSE_VALIDATE_PARALLEL,EdConst.PROCESS_ED_VALIDATE_BATCH,EdConst.EXPLORE_VALIDATE,
                EdConst.FIVE_ED_HOUSE_REFER,EdConst.FIVE_ED_VALIDATE));
        if (StringUtils.isNotEmpty(description)) {
            taskQuery = taskQuery.processVariableValueLike("description","%"+description+"%");
        }
        List<Map> list = Lists.newArrayList();
        for (Task task : taskQuery.list()) {
            MyProcessInstance processInstanceDto=myHistoryService.getMyProcessInstance(task.getProcessInstanceId(),userLogin);
            HistoricProcessInstance processInstance =historyService.createHistoricProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
            Map item = Maps.newHashMap();

            item.put("description", task.getProcessVariables().get("description"));
            item.put("processDefinitionName", processInstance.getProcessDefinitionName());
            if(task.getProcessVariables().get("processDefinitionName")!=null){
                item.put("processDefinitionName",task.getProcessVariables().get("processDefinitionName").toString());
            }
            item.put("businessKey", processInstance.getBusinessKey());
            item.put("name", task.getName());
            item.put("createTime", task.getCreateTime());
            item.put("taskId",task.getId());
            item.put("preHandleTime",processInstanceDto.getPreHandleTime());
            List<HistoricTaskInstance> taskInstances=historyService.createHistoricTaskInstanceQuery().processInstanceId(task.getProcessInstanceId()).taskAssignee(userLogin).finished().orderByHistoricTaskInstanceEndTime().desc().listPage(0,1);
            if(taskInstances.size()>0){
                item.put("preHandleTime",taskInstances.get(0).getEndTime());
            }

            int id=Integer.parseInt(StringUtils.split(processInstance.getBusinessKey(),"_")[2]);
            /*if(processInstance.getBusinessKey().contains(EdConst.PROCESS_ED_HOUSE_REFER)){
                item.put("model",edHouseReferMapper.selectByPrimaryKey(id));
            }else if(processInstance.getBusinessKey().contains(EdConst.PROCESS_ED_HOUSE_VALIDATE)) {
                item.put("model", edHouseValidateMapper.selectByPrimaryKey(id));
            }else if(processInstance.getBusinessKey().contains(EdConst.PROCESS_ED_VALIDATE_BATCH)){
                item.put("model", edValidateBatchMapper.selectByPrimaryKey(id));
            }else if(processInstance.getBusinessKey().contains(EdConst.EXPLORE_VALIDATE)){
                item.put("model",exploreValidateMapper.selectByPrimaryKey(id));
            }*/ if(processInstance.getBusinessKey().contains(EdConst.FIVE_ED_HOUSE_REFER)){
                item.put("model",fiveEdHouseReferMapper.selectByPrimaryKey(id));
            }else if(processInstance.getBusinessKey().contains(EdConst.FIVE_ED_VALIDATE)){
                item.put("model",fiveEdHouseValidateMapper.selectByPrimaryKey(id));
            }
            item.put("done",false);
            list.add(item);
        }
       return list;
    }

    public List<Map> listDoneCadTask(String userLogin,String description) {

        HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery().taskAssignee(userLogin)
                .finished().orderByHistoricTaskInstanceEndTime().desc().includeProcessVariables().includeTaskLocalVariables();
        historicTaskInstanceQuery=historicTaskInstanceQuery.processDefinitionKeyIn(Arrays.asList(EdConst.PROCESS_ED_HOUSE_REFER,EdConst.PROCESS_ED_HOUSE_VALIDATE,EdConst.PROCESS_ED_VALIDATE_BATCH,EdConst.EXPLORE_VALIDATE,
                EdConst.FIVE_ED_VALIDATE,EdConst.PROCESS_ED_HOUSE_VALIDATE_PARALLEL,EdConst.FIVE_ED_HOUSE_REFER));
        if (StringUtils.isNotEmpty(description)) {
            historicTaskInstanceQuery = historicTaskInstanceQuery.processVariableValueLike("description", "%" + description + "%");
        }

        List<String>  processInstanceIdList=Lists.newArrayList();
        List<Map> list = Lists.newArrayList();

        for (HistoricTaskInstance task : historicTaskInstanceQuery.listPage(0, 150)) {
            if(!processInstanceIdList.contains(task.getProcessInstanceId())) {
                processInstanceIdList.add(task.getProcessInstanceId());

                MyProcessInstance processInstanceDto = myHistoryService.getMyProcessInstance(task.getProcessInstanceId(), userLogin);
                Map item = Maps.newHashMap();
                item.put("description", task.getProcessVariables().get("description"));
                item.put("processDefinitionName", processInstanceDto.getProcessDefinitionName());
                if (task.getProcessVariables().get("processDefinitionName") != null) {
                    item.put("processDefinitionName", task.getProcessVariables().get("processDefinitionName").toString());
                }
                item.put("businessKey", processInstanceDto.getBusinessKey());
                item.put("name", task.getName());
                item.put("createTime", task.getCreateTime());
                item.put("endTime", task.getEndTime());
                item.put("processName", processInstanceDto.getProcessName());
                item.put("taskId", task.getId());
                item.put("done", true);
                item.put("preHandleTime", processInstanceDto.getPreHandleTime());
                int id = Integer.parseInt(StringUtils.split(processInstanceDto.getBusinessKey(), "_")[2]);
               /* if (processInstanceDto.getBusinessKey().contains(EdConst.PROCESS_ED_HOUSE_REFER)) {
                    item.put("model", edHouseReferMapper.selectByPrimaryKey(id));
                } else if (processInstanceDto.getBusinessKey().contains(EdConst.PROCESS_ED_HOUSE_VALIDATE)) {
                    item.put("model", edHouseValidateMapper.selectByPrimaryKey(id));
                } else if (processInstanceDto.getBusinessKey().contains(EdConst.PROCESS_ED_VALIDATE_BATCH)) {
                    item.put("model", edValidateBatchMapper.selectByPrimaryKey(id));
                } else if (processInstanceDto.getBusinessKey().contains(EdConst.EXPLORE_VALIDATE)) {
                    item.put("model", exploreValidateMapper.selectByPrimaryKey(id));
                }*/  if (processInstanceDto.getBusinessKey().contains(EdConst.FIVE_ED_HOUSE_REFER)) {
                    item.put("model", fiveEdHouseReferMapper.selectByPrimaryKey(id));
                } else if (processInstanceDto.getBusinessKey().contains(EdConst.FIVE_ED_VALIDATE)) {
                    item.put("model", fiveEdHouseValidateMapper.selectByPrimaryKey(id));
                }
                list.add(item);
            }
        }
        return list;
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

    public void addVariables(String processInstanceId,Map variables){
        Assert.state(taskService.createTaskQuery().processInstanceId(processInstanceId).count()>0,"流程已结束或不存在");
        String taskId=taskService.createTaskQuery().processInstanceId(processInstanceId).list().get(0).getId();
        taskService.setVariables(taskId,variables);
    }

}
