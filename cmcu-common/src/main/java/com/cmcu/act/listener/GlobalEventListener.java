package com.cmcu.act.listener;

import com.cmcu.act.dto.ActBpmnModel;
import com.cmcu.act.dto.CustomProcessInstance;
import com.cmcu.act.extend.IPropertyConstants;
import com.cmcu.act.service.ActCacheService;
import com.cmcu.act.service.ProcessHandleService;
import com.cmcu.common.dto.FastUserDto;
import com.cmcu.common.dto.UserDto;
import com.cmcu.common.service.CommonActActionService;
import com.cmcu.common.service.CommonFormDataService;
import com.cmcu.common.service.IWxMessageService;
import com.cmcu.common.web.ApplicationContextHelper;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.common.service.CommonUserService;
import com.common.wx.model.User;
import com.common.wx.service.MessageService;
import com.google.common.collect.Lists;
import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.util.SimpleContext;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.*;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.delegate.event.impl.ActivitiEntityEventImpl;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.persistence.entity.TaskEntityImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.DelegationState;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.task.TaskExecutor;

import javax.el.ExpressionFactory;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
public class GlobalEventListener implements ActivitiEventListener, IPropertyConstants,Serializable {

    ActCacheService actCacheService;

    RuntimeService runtimeService;

    TaskService taskService;

    HistoryService historyService;

    CommonUserService commonUserService;

    TaskExecutor taskExecutor;

    CommonActActionService commonActActionService;

    CommonFormDataService commonFormDataService;

    IWxMessageService wxMessageService;


    ProcessHandleService processHandleService;


    @Override
    public void onEvent(ActivitiEvent event) {

        try {
            if (runtimeService == null) {
                runtimeService = ApplicationContextHelper.popBean(RuntimeService.class);
                actCacheService = ApplicationContextHelper.popBean(ActCacheService.class);
                taskService = ApplicationContextHelper.popBean(TaskService.class);
                historyService = ApplicationContextHelper.popBean(HistoryService.class);
            }
            if (commonUserService == null)
                commonUserService = ApplicationContextHelper.popBean(CommonUserService.class);
            if (commonActActionService == null)
                commonActActionService = ApplicationContextHelper.popBean(CommonActActionService.class);
            if (taskExecutor == null) taskExecutor = ApplicationContextHelper.popBean(TaskExecutor.class);
            if (commonFormDataService == null)
                commonFormDataService = ApplicationContextHelper.popBean(CommonFormDataService.class);
            if (wxMessageService == null) wxMessageService = ApplicationContextHelper.popBean(IWxMessageService.class);

            if(processHandleService==null) processHandleService=ApplicationContextHelper.popBean(ProcessHandleService.class);
        }catch (Exception ex){

        }

        if(taskExecutor!=null) {
            if (event.getType() == ActivitiEventType.TASK_COMPLETED) {
                taskExecutor.execute(() -> {
                    try {
                        Thread.sleep(3 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //sendCcTask(event);
                    sendStarMsg(event, false);
                });
            }
            else if (event.getType() == ActivitiEventType.TASK_CREATED) {
                taskExecutor.execute(() -> handleTaskWhenCreated(event));
            }
            else if (event.getType() == ActivitiEventType.PROCESS_COMPLETED) {
                taskExecutor.execute(() -> {
                    try {
                        Thread.sleep(5 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    sendStarMsg(event, true);
                });
            }
        }
    }




    @Override
    public boolean isFailOnException() {
        return false;
    }


    /**
     * 检查抄送任务
     * @param event
     */
    private void sendCcTask(ActivitiEvent event) {
        if (StringUtils.isNotEmpty(event.getProcessDefinitionId())) {
            ActBpmnModel actBpmnModel = actCacheService.getActBpmnModel(event.getProcessDefinitionId());
            if (actBpmnModel.getBpmnModel() == null) return;

            TaskEntityImpl doneTask = (TaskEntityImpl) ((ActivitiEntityEventImpl) event).getEntity();
            String taskKey=doneTask.getTaskDefinitionKey();
            if (actBpmnModel.getCopyUserTaskList().stream().anyMatch(p -> p.containsKey(taskKey))) {
                HistoricTaskInstance historicTaskInstance=historyService.createHistoricTaskInstanceQuery().includeTaskLocalVariables().taskId(doneTask.getId()).singleResult();
                Map variables=historicTaskInstance.getTaskLocalVariables();
                if ((boolean) variables.getOrDefault(PASSED_BACKED_STATE, false)) {
                    String variableValue=variables.getOrDefault(CC_USER, "").toString();
                    if (variableValue.startsWith("[")) {
                        variableValue=variableValue.substring(1, variableValue.length() - 1);
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


                    processHandleService.addCcUserTask(doneTask.getProcessInstanceId(),ccUsers);
                }
            }
        }
    }


    private void sendStarMsg(ActivitiEvent event,boolean containStartUser) {
        String processInstanceId = event.getProcessInstanceId();
        if(StringUtils.isNotEmpty(processInstanceId)) {
            HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            String tenetId = processInstance.getTenantId();
            List<String> enLoginList = commonActActionService.listStaredUser(processInstanceId);
            if (containStartUser) enLoginList.add(processInstance.getStartUserId());

            List<FastUserDto> userList = commonUserService.listFastUserByEnLoginList(tenetId, enLoginList);
            if (userList.stream().anyMatch(p -> StringUtils.isNotEmpty(p.getWxId()))) {
                wxMessageService.sendCommonFormDataMessage(processInstance.getBusinessKey(),userList);
            }
        }
    }


    private void handleTaskWhenCreated(ActivitiEvent event){
        try {
            if (StringUtils.isNotEmpty(event.getProcessDefinitionId())) {

                Thread.sleep(1000);

                TaskEntityImpl task=(TaskEntityImpl)((ActivitiEntityEventImpl)event).getEntity();
                log.error(task.getTaskDefinitionKey()+"_create new Task for "+task.getAssignee());

                if(StringUtils.isEmpty(task.getAssignee())) {
                    ActBpmnModel actBpmnModel = actCacheService.getActBpmnModel(event.getProcessDefinitionId());
                    BpmnModel bpmnModel = actBpmnModel.getBpmnModel();
                    UserTask userTask = (UserTask) bpmnModel.getFlowElement(task.getTaskDefinitionKey());
                    List<String> candidateUsers = userTask.getCandidateUsers();
                    Map variables = taskService.getVariables(task.getId());
                    boolean needUpdate = false;
                    if (variables.containsKey(CANDIDATE_USER)) {
                        List<String> oList = Arrays.stream((String[]) variables.get(CANDIDATE_USER)).collect(Collectors.toList());
                        List<String> users = Lists.newArrayList();
                        //兼容以前的,对用户分隔符号做了替换
                        oList.forEach(p -> users.add(StringUtils.replace(p, "_", "|")));
                        candidateUsers = users.stream().filter(p -> p.startsWith(userTask.getId()))
                                .filter(p -> StringUtils.split(p, "|").length == 2)
                                .map(p -> StringUtils.split(p, "|")[1]).distinct()
                                .collect(Collectors.toList());
                    }
                    if(candidateUsers.size()==0) {
                        if (userTask.getCandidateUsers().size() > 0) {
                            candidateUsers = Lists.newArrayList();

                            ExpressionFactory factory = new ExpressionFactoryImpl();
                            SimpleContext context = new SimpleContext();
                            for (Object key : variables.keySet()) {
                                if (variables.get(key) != null) {
                                    context.setVariable(key.toString(), factory.createValueExpression(variables.get(key), variables.get(key).getClass()));
                                }
                            }


                            for (String pr_ : userTask.getCandidateUsers()) {
                                if (pr_.startsWith("${") && pr_.endsWith("}")) {
                                    try {
                                        needUpdate = true;
                                        Object exeValue = factory.createValueExpression(context, pr_, Object.class).getValue(context);
                                        if (exeValue instanceof String) {
                                            candidateUsers.add(exeValue.toString());
                                        }else if(exeValue instanceof ArrayList){
                                            candidateUsers=(ArrayList)exeValue;
                                        }
                                    } catch (Exception ex) {
                                        log.warn(ex.getMessage(), ex);
                                    }
                                } else {
                                    candidateUsers.add(pr_);
                                }
                            }
                        } else if (userTask.getCandidateGroups().size() > 0) {
                            needUpdate = true;
                            candidateUsers = commonUserService.listUserByPR(actBpmnModel.getTenetId(), userTask.getCandidateGroups());
                        }
                    }
                    if (needUpdate && candidateUsers.size() > 0) {
                        for (String pr : userTask.getCandidateGroups()) {
                            taskService.deleteCandidateGroup(task.getId(), pr);
                        }
                        for (IdentityLink identityLink : taskService.getIdentityLinksForTask(task.getId())) {
                            taskService.deleteCandidateUser(task.getId(), identityLink.getUserId());
                        }
                        if (candidateUsers.size()>0) {
                            for (String candidateUser : candidateUsers) {
                                taskService.addCandidateUser(task.getId(), candidateUser);
                            }
                        }
                    }
                    if(candidateUsers.size()==1){
                        taskService.setAssignee(task.getId(), candidateUsers.get(0));
                    }

                    ProcessInstance processInstance=runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
                    for(String enLogin:candidateUsers){
                        FastUserDto userDto = commonUserService.getFastByEnLogin(enLogin);
                        wxMessageService.sendCommonFormDataMessage(processInstance.getBusinessKey(),Lists.newArrayList(userDto));
                    }

                }else {
                    boolean sendMessage=false;
                    ProcessInstance processInstance=runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
                    if(!processInstance.getStartUserId().equalsIgnoreCase(task.getAssignee())) {
                        sendMessage=true;
                    }else {
                        long count = historyService.createHistoricTaskInstanceQuery().processInstanceId(task.getProcessInstanceId()).count();
                        if (count > 1) {
                            sendMessage=true;
                        }
                    }
                    if(sendMessage) {
                        FastUserDto userDto = commonUserService.getFastByEnLogin(task.getAssignee());
                        wxMessageService.sendCommonFormDataMessage(processInstance.getBusinessKey(), Lists.newArrayList(userDto));
                    }
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    

    private Object getExtensionValue( UserTask userTask,String key) {
        List<ExtensionElement> extensionElements = userTask.getExtensionElements().get(key);
        if (extensionElements != null && extensionElements.size() > 0) {
            return extensionElements.get(0).getElementText();
        }
        return null;
    }




}
