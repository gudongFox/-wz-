package com.cmcu.mcc.act.service;

import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.act.model.MyHistoryTask;
import com.cmcu.mcc.act.model.MyProcessInstance;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.dto.ActHistoryDto;
import com.cmcu.mcc.dto.ProcessInstanceDto;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.entity.HrEmployeeSys;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.activiti.bpmn.model.*;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.*;
import org.activiti.engine.impl.persistence.entity.CommentEntityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskInfo;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MyHistoryService {




    @Resource
    HistoryService historyService;

    @Resource
    TaskService taskService;

    @Resource
    RepositoryService repositoryService;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    SelectEmployeeService selectEmployeeService;


    public  List<Map> listTask(String userLogin,String processDefinitionName,String description) {
        List<Map> list = Lists.newArrayList();
        TaskQuery taskQuery = taskService.createTaskQuery().taskAssignee(userLogin).active().orderByTaskCreateTime().desc().includeProcessVariables();
        if (StringUtils.isNotEmpty(processDefinitionName)) {
            taskQuery = taskQuery.processDefinitionNameLike("%" + processDefinitionName + "%");
        }
        if (StringUtils.isNotEmpty(description)) {
            taskQuery = taskQuery.processVariableValueLikeIgnoreCase("description", "%" + description + "%");
        }
        for (Task task : taskQuery.list()) {
            try {
                Map item = Maps.newHashMap();
                if(StringUtils.isNotEmpty(task.getParentTaskId())){
                    HistoricTaskInstance historicTaskInstance=historyService.createHistoricTaskInstanceQuery().taskId(task.getParentTaskId()).singleResult();
                    item.put("description", historicTaskInstance.getProcessVariables().getOrDefault("description", ""));
                    String initiator = historicTaskInstance.getProcessVariables().getOrDefault("userLogin", "").toString();
                    if (StringUtils.isNotEmpty(initiator)) {
                        item.put("initiator", selectEmployeeService.getNameByUserLogin(initiator));
                    }
                    if (task.getProcessVariables().get("processDefinitionName") != null) {
                        item.put("processDefinitionName", historicTaskInstance.getProcessVariables().get("processDefinitionName").toString());
                        item.put("businessKey", historicTaskInstance.getProcessVariables().getOrDefault("businessKey", "").toString());
                    } else {
                        HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(historicTaskInstance.getProcessInstanceId()).singleResult();
                        item.put("processDefinitionName", processInstance.getProcessDefinitionName());
                        item.put("businessKey", processInstance.getBusinessKey());
                    }
                    item.put("processInstanceId", historicTaskInstance.getProcessInstanceId());
                }else {

                    item.put("description", task.getProcessVariables().getOrDefault("description", ""));
                    String initiator = task.getProcessVariables().getOrDefault("userLogin", "").toString();
                    if (StringUtils.isNotEmpty(initiator)) {
                        item.put("initiator", selectEmployeeService.getNameByUserLogin(initiator));
                    }
                    if (task.getProcessVariables().get("processDefinitionName") != null&&task.getProcessVariables().get("businessKey") != null) {
                        item.put("processDefinitionName", task.getProcessVariables().get("processDefinitionName").toString());
                        item.put("businessKey", task.getProcessVariables().getOrDefault("businessKey", "").toString());
                    } else {
                        HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
                        item.put("processDefinitionName", processInstance.getProcessDefinitionName());
                        item.put("businessKey", processInstance.getBusinessKey());
                        taskService.setVariable(task.getId(), "businessKey", processInstance.getBusinessKey());
                        taskService.setVariable(task.getId(), "processDefinitionName", processInstance.getProcessDefinitionName());
                    }
                    item.put("processInstanceId", task.getProcessInstanceId());
                }
                item.put("name", task.getName());
                item.put("createTime", task.getCreateTime());
                list.add(item);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return list;
    }

    public Map listDoneTask(String userLogin,int pageNum,int pageSize,String processDefinitionName,String description) {
        Map map = Maps.newHashMap();
        map.put("userLogin", userLogin);
        map.put("time", new Date());
        HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(userLogin).finished().orderByHistoricTaskInstanceEndTime().desc().includeProcessVariables();
        if (StringUtils.isNotEmpty(processDefinitionName)) {
            historicTaskInstanceQuery = historicTaskInstanceQuery.processDefinitionNameLike("%" + processDefinitionName + "%");
        }
        if (StringUtils.isNotEmpty(description)) {
            historicTaskInstanceQuery = historicTaskInstanceQuery.processVariableValueLikeIgnoreCase("processDescription", "%" + description + "%");
        }
        List<String> processInstanceIdList=Lists.newArrayList();
        List<Map> list = Lists.newArrayList();
        for (HistoricTaskInstance task : historicTaskInstanceQuery.list()) {
            if(StringUtils.isNotEmpty(task.getProcessInstanceId())) {
                if(!processInstanceIdList.contains(task.getProcessInstanceId())) {
                    processInstanceIdList.add(task.getProcessInstanceId());
                    try {
                        int beginSize=(pageNum-1)*pageSize;
                        int endSize=pageNum*pageSize;
                        if(processInstanceIdList.size()>beginSize&&processInstanceIdList.size()<=endSize) {
                            Map item = Maps.newHashMap();
                            item.put("processDescription", task.getProcessVariables().getOrDefault("processDescription", ""));
                            String initiator = task.getProcessVariables().getOrDefault("userLogin", "").toString();
                            if (StringUtils.isNotEmpty(initiator)) {
                                item.put("initiator", selectEmployeeService.getNameByUserLogin(initiator));
                            }
                            if (task.getProcessVariables().containsKey("processDefinitionName")) {
                                item.put("processDefinitionName", task.getProcessVariables().getOrDefault("processDefinitionName", ""));
                                item.put("businessKey", task.getProcessVariables().getOrDefault("businessKey", ""));
                            } else {
                                HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
                                item.put("processDefinitionName", processInstance.getProcessDefinitionName());
                                item.put("businessKey", processInstance.getBusinessKey());
                            }
                            item.put("processInstanceId", task.getProcessInstanceId());
                            item.put("name", task.getName());
                            item.put("createTime", task.getCreateTime());
                            item.put("endTime", task.getEndTime());
                            item.put("deleted", true);
                            list.add(item);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
            map.put("pageInfo", WebUtil.getPageInfo(pageNum, pageSize, processInstanceIdList.size()));
        }
        map.put("list", list);
        return map;
    }

    public Map listMyProcess(String userLogin,int pageNum,int pageSize,String finish,String processDefinitionName,String description) {
        Map map = Maps.newHashMap();
        map.put("userLogin", userLogin);
        map.put("time", new Date());

        HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery().includeProcessVariables().variableValueEqualsIgnoreCase("userLogin", userLogin).notDeleted().orderByProcessInstanceStartTime().desc();
        if("1".equalsIgnoreCase(finish)){
            historicProcessInstanceQuery=historicProcessInstanceQuery.finished();
        }else{
            historicProcessInstanceQuery=historicProcessInstanceQuery.unfinished();
        }
        if (StringUtils.isNotEmpty(processDefinitionName)) {
            historicProcessInstanceQuery = historicProcessInstanceQuery.variableValueLike("processDefinitionName","%" + processDefinitionName + "%");
        }
        if (StringUtils.isNotEmpty(description)) {
            historicProcessInstanceQuery = historicProcessInstanceQuery.variableValueLike("description","%" + description + "%");
        }

        map.put("pageInfo", WebUtil.getPageInfo(pageNum, pageSize, (int) historicProcessInstanceQuery.count()));
        List<Map> list = Lists.newArrayList();
        for (HistoricProcessInstance processInstance : historicProcessInstanceQuery.listPage((pageNum - 1) * pageSize, pageSize)) {
            try {
                Map item = Maps.newHashMap();
                item.put("description", processInstance.getProcessVariables().getOrDefault("description",""));
                item.put("processDefinitionName",processInstance.getProcessDefinitionName());
                item.put("businessKey",processInstance.getBusinessKey());
                item.put("processInstanceId",processInstance.getId());
                item.put("createTime", processInstance.getStartTime());
                item.put("endTime", processInstance.getEndTime());
                StringBuilder sb=new StringBuilder();
                List<Task> tasks=taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
                if(tasks.size()>0) {
                    for (Task task : tasks) {
                        sb.append(task.getName());
                        if (StringUtils.isNotEmpty(task.getAssignee())) {
                            sb.append("(");
                            sb.append(selectEmployeeService.getNameByUserLogin(task.getAssignee()));
                            sb.append(")");
                            sb.append(" ");
                        }
                    }
                    item.put("currentName", sb.toString());
                }else{
                    item.put("currentName","已完成");
                }
                list.add(item);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        map.put("list", list);
        return map;
    }


    public Map listDoneEndTask(String userLogin,int pageNum,int pageSize,String processDefinitionName,String description) {
        Map map = Maps.newHashMap();
        map.put("userLogin", userLogin);
        map.put("time", new Date());
        HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(userLogin).processFinished().orderByHistoricTaskInstanceEndTime().desc().includeProcessVariables();
        if (StringUtils.isNotEmpty(processDefinitionName)) {
            historicTaskInstanceQuery = historicTaskInstanceQuery.processDefinitionNameLike("%" + processDefinitionName + "%");
        }
        if (StringUtils.isNotEmpty(description)) {
            historicTaskInstanceQuery = historicTaskInstanceQuery.processVariableValueLikeIgnoreCase("processDescription", "%" + description + "%");
        }
        List<String> processInstanceIdList=Lists.newArrayList();
        List<Map> list = Lists.newArrayList();
        for (HistoricTaskInstance task : historicTaskInstanceQuery.list()) {
            if(StringUtils.isNotEmpty(task.getProcessInstanceId())) {
                if(!processInstanceIdList.contains(task.getProcessInstanceId())) {
                    processInstanceIdList.add(task.getProcessInstanceId());
                    try {
                        int beginSize=(pageNum-1)*pageSize;
                        int endSize=pageNum*pageSize;
                        if(processInstanceIdList.size()>beginSize&&processInstanceIdList.size()<=endSize) {
                            Map item = Maps.newHashMap();
                            item.put("processDescription", task.getProcessVariables().getOrDefault("processDescription", ""));
                            String initiator = task.getProcessVariables().getOrDefault("userLogin", "").toString();
                            if (StringUtils.isNotEmpty(initiator)) {
                                item.put("initiator", selectEmployeeService.getNameByUserLogin(initiator));
                            }
                            if (task.getProcessVariables().containsKey("processDefinitionName")) {
                                item.put("processDefinitionName", task.getProcessVariables().getOrDefault("processDefinitionName", ""));
                                item.put("businessKey", task.getProcessVariables().getOrDefault("businessKey", ""));
                            } else {
                                HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
                                item.put("processDefinitionName", processInstance.getProcessDefinitionName());
                                item.put("businessKey", processInstance.getBusinessKey());
                            }
                            item.put("processInstanceId", task.getProcessInstanceId());
                            item.put("name", task.getName());
                            item.put("createTime", task.getCreateTime());
                            item.put("endTime", task.getEndTime());
                            item.put("deleted", true);
                            list.add(item);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
            map.put("pageInfo", WebUtil.getPageInfo(pageNum, pageSize, processInstanceIdList.size()));
        }
        map.put("list", list);
        return map;
    }

    /**
     * 更具流程ID 查询当前流程基本信息
     * @param processInstanceId
     * @return
     */
    public Map getTask(String processInstanceId){
        Map item = Maps.newHashMap();
       HistoricTaskInstance task = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId).orderByHistoricTaskInstanceStartTime().asc().includeProcessVariables().list().get(0);

        item.put("processDescription", task.getProcessVariables().getOrDefault("processDescription", ""));

        String initiator = task.getProcessVariables().getOrDefault("userLogin", "").toString();
        if (StringUtils.isNotEmpty(initiator)) {
            item.put("initiator", selectEmployeeService.getNameByUserLogin(initiator));
        }
        if (task.getProcessVariables().containsKey("processDefinitionName")) {
            item.put("processDefinitionName", task.getProcessVariables().getOrDefault("processDefinitionName", ""));
            item.put("businessKey", task.getProcessVariables().getOrDefault("businessKey", ""));
        } else {
            HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
            item.put("processDefinitionName", processInstance.getProcessDefinitionName());
            item.put("businessKey", processInstance.getBusinessKey());
        }
        item.put("processInstanceId", task.getProcessInstanceId());
        item.put("name", task.getName());
        item.put("createTime", task.getCreateTime());
        item.put("endTime", task.getEndTime());
        item.put("deleted", true);

        return item;
    }

    /**
     * 查看某个流程的处理记录
     * @param processInstanceId
     * @return
     */
    public List<MyHistoryTask> listHistoryTask(String processInstanceId) {
        List<MyHistoryTask> list = Lists.newArrayList();
        List<HistoricTaskInstance> tasks = historyService.createHistoricTaskInstanceQuery().includeTaskLocalVariables().processInstanceId(processInstanceId)
                .orderByHistoricTaskInstanceStartTime().asc()
                .list();
        for (HistoricTaskInstance task : tasks) {
            MyHistoryTask model = new MyHistoryTask();
            model.setId(task.getId());
            model.setName(task.getName());
            model.setTaskDefinitionKey(task.getTaskDefinitionKey());
            model.setStartTime(task.getStartTime());
            model.setEndTime(task.getEndTime());
            model.setClaimTime(task.getClaimTime());
            model.setAssignee(task.getAssignee());
            HrEmployeeDto hrEmployeeDto=selectEmployeeService.selectByUserLogin(task.getAssignee());
            if(hrEmployeeDto!=null){
                model.setAssigneeName(hrEmployeeDto.getUserName());
                model.setHeadAttachId(hrEmployeeDto.getHeadAttachId());
            }


            Map variables = task.getTaskLocalVariables();
            model.setOpt(variables.getOrDefault("opt", "").toString());
            model.setComment(variables.getOrDefault("comment", "").toString());
            list.add(model);
        }
        return list;
    }


    public List<MyHistoryTask> listPassedHistoryDto(String processInstanceId){


        List<MyHistoryTask> list=Lists.newArrayList();
        List<MyHistoryTask> tasks=listHistoryTask(processInstanceId).stream().filter(p->"发送".equalsIgnoreCase(p.getOpt())).sorted(Comparator.comparing(MyHistoryTask::getEndTime).reversed()).collect(Collectors.toList());
        tasks.forEach(p->{
            if(list.stream().anyMatch(o->o.getTaskDefinitionKey().equalsIgnoreCase(p.getTaskDefinitionKey())&&o.getAssignee().equalsIgnoreCase(p.getAssignee()))){



            }
        });



        return list;



//        //流程信息
//        List<ActHistoryDto> nodes=Lists.newArrayList();
//        List<ActHistoryDto> historicInstances=listHistoryDto(processInstanceId).stream().sorted(Comparator.comparing(ActHistoryDto::getStart).reversed()).collect(Collectors.toList());
//        for(ActHistoryDto actHistoryDto:historicInstances.stream().filter(p->p.isFinished()).filter(p->p.isPassed()).collect(Collectors.toList())) {
//            if (actHistoryDto.getActivityName().contains("结束") || actHistoryDto.getActivityName().contains("开始")) {
//            } else if (nodes.stream().noneMatch(p -> (p.getActivityName().equalsIgnoreCase(actHistoryDto.getActivityName()) && p.getUserLogin().equalsIgnoreCase(actHistoryDto.getUserLogin())))) {
//                nodes.add(actHistoryDto);
//            }
//        }
//        nodes.sort(Comparator.comparing(ActHistoryDto::getEndTime));
//        if(nodes.size()%2>0){
//            nodes.add(new ActHistoryDto());
//        }
//        return nodes;
    }



    /**
     * 得到某个流程用户的状态
     * @param processInstanceId
     * @param userLogin
     * @return
     */
    public MyProcessInstance getMyProcessInstance(String processInstanceId, String userLogin) {
        Assert.state(StringUtils.isNotEmpty(processInstanceId), "该任务并未发起流程!");
        List<MyHistoryTask> allTasks = listHistoryTask(processInstanceId);
        List<MyHistoryTask> doneTasks = allTasks.stream().filter(p -> p.getEndTime() != null).collect(Collectors.toList());
        List<MyHistoryTask> doingTasks = allTasks.stream().filter(p -> p.getEndTime() == null).collect(Collectors.toList());
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        MyProcessInstance item = new MyProcessInstance();
        item.setProcessInstanceId(processInstanceId);
        if(historicProcessInstance!=null) {
            item.setProcessDefinitionName(historicProcessInstance.getProcessDefinitionName());
            item.setBusinessKey(historicProcessInstance.getBusinessKey());
        }
        item.setPassAble(false);
        item.setBackAble(false);
        item.setFetchAble(false);
        item.setMyTaskFirst(false);
        item.setPrintAble(true);
        item.setProcessEnd(doingTasks.size() == 0);
        item.setProcessName(StringUtils.join(doingTasks.stream().map(p -> p.getName() + "(" + p.getAssigneeName() + ")").collect(Collectors.toList()), ","));
        item.setTaskUsers(StringUtils.join(doingTasks.stream().map(p -> p.getAssignee()).distinct().collect(Collectors.toList()), ","));
        item.setPreHandleTime(DateUtils.addDays(new Date(),-365));
        List<Map> taskList = Lists.newArrayList();
        if (doingTasks.size() > 0) {
            item.setProcessTime(doingTasks.stream().map(MyHistoryTask::getStartTime).findFirst().get());
            doingTasks.forEach(task -> {
                Map tMap = Maps.newHashMap();
                tMap.put("name", task.getName());
                tMap.put("assigne", task.getAssignee());
                tMap.put("assigneName", task.getAssigneeName());
                tMap.put("createTime", task.getStartTime());
                taskList.add(tMap);
            });
            if (StringUtils.isNotEmpty(userLogin)) {

                String userName = selectEmployeeService.getNameByUserLogin(userLogin);
                Optional<Date> preHandleTime = allTasks.stream().filter(p -> userLogin.equalsIgnoreCase(p.getAssignee())).filter(p -> p.getEndTime() != null).sorted(Comparator.comparing(MyHistoryTask::getEndTime).reversed()).map(MyHistoryTask::getEndTime).findFirst();
                preHandleTime.ifPresent(item::setPreHandleTime);
                List<MyHistoryTask> myDoingTasks = doingTasks.stream().filter(p -> userLogin.equalsIgnoreCase(p.getAssignee())).collect(Collectors.toList());
                if (myDoingTasks.size() > 0) {
                    item.setPassAble(true);
                    //item.setMyTaskName(StringUtils.join(StringUtils.join(myDoingTasks.stream().map(MyHistoryTask::getName),",")));
                    //item.setMyTaskId(StringUtils.join(StringUtils.join(myDoingTasks.stream().map(MyHistoryTask::getId),",")));
                    item.setMyTaskName(myDoingTasks.get(0).getName());
                    item.setMyTaskId(myDoingTasks.get(0).getId());
                    item.setMyTaskTime(myDoingTasks.get(0).getStartTime());
                    boolean backAble = false;
                    if(historicProcessInstance!=null) {
                        BpmnModel bpmnModel = repositoryService.getBpmnModel(historicProcessInstance.getProcessDefinitionId());
                        for (MyHistoryTask myHistoryTask : myDoingTasks) {
                            if (!backAble) {

                                FlowNode currentFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(myHistoryTask.getTaskDefinitionKey());
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
                            }
                        }
                    }
                    item.setBackAble(backAble);
                    for (MyHistoryTask task : doingTasks) {
                        if (task.getClaimTime() == null && userLogin.equalsIgnoreCase(task.getAssignee())) {
                            new Thread(()-> {
                                taskService.claim(task.getId(), userLogin);
                            }).start();
                            task.setClaimTime(new Date());
                        }
                    }
                }

                //所有进行中的任务都未接收、且有完成了的任务
                if (doingTasks.stream().noneMatch(p -> userLogin.equalsIgnoreCase(p.getAssignee())) && doingTasks.stream().noneMatch(p -> p.getClaimTime() != null) && doneTasks.size() > 0) {
                    MyHistoryTask firstDoneTask = doneTasks.get(0);
                    MyHistoryTask latestDoneTask = doneTasks.stream().sorted(Comparator.comparing(MyHistoryTask::getEndTime).reversed()).findFirst().get();
                    //最近完成的任务是第一个任务
                    if (firstDoneTask.getTaskDefinitionKey().equalsIgnoreCase(latestDoneTask.getTaskDefinitionKey())&&firstDoneTask.getAssignee().equalsIgnoreCase(userLogin)) {
                        item.setFetchAble(true);
                    }
                }
                item.setUserLogin(userLogin);
                item.setUserName(userName);
            }
        } else {
            item.setProcessName("已完成");
        }
        item.setTaskList(taskList);

        if( MccConst.APP_CODE.equalsIgnoreCase("wuzhou")){
            item.setPrintAble(false);
        }
        ModelUtil.setNotNullFields(item);
        return item;
    }


    public String getAttendUser(String processInstanceId){
        List<HistoricTaskInstance> tasks = historyService.createHistoricTaskInstanceQuery().includeTaskLocalVariables().processInstanceId(processInstanceId)
                .orderByHistoricTaskInstanceStartTime().asc()
                .list();
        String attendUser=",";
        if (tasks.size()>0){
           for (HistoricTaskInstance task:tasks){
               if (task.getAssignee()!=null){
                   if (!attendUser.contains(task.getAssignee())){
                       attendUser=attendUser+task.getAssignee()+",";
                   }
               }
           }
        }

    return attendUser;
}


    

}
