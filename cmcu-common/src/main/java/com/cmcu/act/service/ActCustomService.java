package com.cmcu.act.service;

import com.cmcu.act.dto.ActBpmnModel;
import com.cmcu.act.extend.IPropertyConstants;
import com.cmcu.common.util.MyStringUtil;
import com.google.common.collect.Lists;
import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.util.SimpleContext;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.DelegationState;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.el.ExpressionFactory;
import java.util.Comparator;
import java.util.List;
import java.util.Map;


@Service
public class ActCustomService implements IPropertyConstants {


    @Resource
    ActCacheService actCacheService;

    @Resource
    TaskService taskService;

    @Resource
    RuntimeService runtimeService;

    @Resource
    HistoryService historyService;

    /**
     * 增加抄送任务
     * @param taskId
     * @param
     */
    public void  addCcUserTask(String taskId){
        if(StringUtils.isNotEmpty(taskId)) {
            Task task = taskService.createTaskQuery().includeProcessVariables().includeTaskLocalVariables().taskId(taskId).singleResult();
            String taskKey = task.getTaskDefinitionKey();
            ActBpmnModel actBpmnModel = actCacheService.getActBpmnModel(task.getProcessDefinitionId());
            if (actBpmnModel.getCopyUserTaskList().stream().anyMatch(p -> p.containsKey(taskKey))) {

                Map variables = task.getProcessVariables();
                variables.putAll(task.getTaskLocalVariables());
                String variableValue = variables.getOrDefault(CC_USER, "").toString();
                if (variableValue.startsWith("[")) {
                    variableValue = variableValue.substring(1, variableValue.length() - 1);
                }
                List<String> ccUsers = MyStringUtil.getStringList(variableValue);
                if (ccUsers.size() == 0) {

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


                List<IdentityLink> links = runtimeService.getIdentityLinksForProcessInstance(task.getProcessInstanceId());

                List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
                        .processInstanceId(task.getProcessInstanceId())
                        .taskDefinitionKey(task.getTaskDefinitionKey())
                        .taskAssignee(task.getAssignee()).list();


                for (String user : ccUsers) {

                    boolean alreadyCcRunning = false;
                    for (HistoricTaskInstance historicTaskInstance : historicTaskInstances) {
                       if(!alreadyCcRunning) {
                           List<HistoricTaskInstance> ccList = historyService.createHistoricTaskInstanceQuery().taskAssignee(user).taskParentTaskId(historicTaskInstance.getId()).list();
                           alreadyCcRunning = ccList.stream().anyMatch(p -> p.getEndTime() == null);
                       }
                    }

                    if (!alreadyCcRunning) {
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
                        taskService.setVariables(ccTask.getId(), variables);
                        if (links.stream().noneMatch(p -> p.getUserId().equalsIgnoreCase(user))) {
                            runtimeService.addParticipantUser(task.getProcessInstanceId(), user);
                        }
                    }
                }
            }
        }
    }

}
