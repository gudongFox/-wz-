package com.cmcu.act.service;

import com.cmcu.act.extend.IPropertyConstants;
import com.cmcu.act.extend.IVariableConstants;
import com.cmcu.common.service.CommonActActionService;
import com.cmcu.common.service.CommonAttachService;
import com.cmcu.common.service.IHandleFormService;
import com.cmcu.common.service.CommonUserService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.DelegationState;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ProcessHandleService implements IVariableConstants, IPropertyConstants {


    @Resource
    HistoryService historyService;

    @Resource
    RuntimeService runtimeService;

    @Resource
    CommonUserService commonUserService;

    @Resource
    ActCacheService actCacheService;

    @Resource
    TaskService taskService;

    @Resource
    CommonActActionService commonActActionService;

    @Resource
    IHandleFormService handleFormService;



    public void toggleStar(String processInstanceId,String enLogin){
        HistoricProcessInstance historicProcessInstance=historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        commonActActionService.toggleStar(historicProcessInstance.getTenantId(),historicProcessInstance.getId(),enLogin);
    }



    public void suspendProcessInstanceById(String processInstanceId) {
        Assert.state(StringUtils.isNotEmpty(processInstanceId), "processInstanceId不能为空!");
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        Assert.state(processInstance!=null, "流程已结束!");
        Assert.state(!processInstance.isSuspended(), "流程已挂起!");
        runtimeService.suspendProcessInstanceById(processInstanceId);
    }

    public void activateProcessInstanceById(String processInstanceId) {
        Assert.state(StringUtils.isNotEmpty(processInstanceId), "processInstanceId不能为空!");
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        Assert.state(processInstance.isSuspended(), "流程未挂起!");
        runtimeService.activateProcessInstanceById(processInstanceId);
    }

    /**
     * 删除流程
     * @param processInstanceId
     * @param enLogin 如果为空,则直接删除，否则验证是否发起人以及任务是否仅在他那里
     * @param deleteReason
     */
    public void deleteProcessInstanceById(String processInstanceId,String enLogin,String deleteReason) {
        if(StringUtils.isNotEmpty(processInstanceId)) {
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            if (StringUtils.isNotEmpty(enLogin)) {
                Assert.state(enLogin.equalsIgnoreCase(processInstance.getStartUserId()), "发起人才能删除!");
                Assert.state(taskService.createTaskQuery().processInstanceId(processInstanceId).count() == taskService.createTaskQuery().processInstanceId(processInstanceId).taskCandidateOrAssigned(enLogin).count(), "其他用户处理中!");
            }

            List<HistoricTaskInstance> historicTaskInstances=historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).list();
            for(HistoricTaskInstance historicTaskInstance:historicTaskInstances) {
                List<HistoricTaskInstance> ccTasks = historyService.createHistoricTaskInstanceQuery().taskParentTaskId(historicTaskInstance.getId()).list();
                for (HistoricTaskInstance ccTask : ccTasks) {
                    historyService.deleteHistoricTaskInstance(ccTask.getId());
                }
            }

            runtimeService.deleteProcessInstance(processInstanceId, StringUtils.isNotEmpty(deleteReason) ? deleteReason : "no why");
            historyService.deleteHistoricProcessInstance(processInstanceId);
            handleFormService.deleteFormData(processInstance.getBusinessKey(), enLogin);
        }
    }

    /**
     * 删除流程
     * @param businessKey 业务Id
     * @param enLogin 不传则不验证
     * @param deleteReason
     */
    public void deleteProcessInstanceByBusinessKey(String businessKey,String enLogin,String deleteReason) {
        if(StringUtils.isNotEmpty(businessKey)) {
            if( runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(businessKey).count()==1) {
                ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(businessKey).singleResult();
                deleteProcessInstanceById(processInstance.getProcessInstanceId(),enLogin,deleteReason);
            }
        }
    }

    private void setAuthenticatedUserId(String enLogin){
        if(!enLogin.equalsIgnoreCase(Authentication.getAuthenticatedUserId())){
            Authentication.setAuthenticatedUserId(enLogin);
        }
    }


    /**
     * 增加抄送任务
     * @param processInstanceId
     * @param ccUsers
     */
    public void  addCcUserTask(String processInstanceId, List<String> ccUsers){
        if(StringUtils.isNotEmpty(processInstanceId)&&ccUsers.size()>0) {

            HistoricProcessInstance historicProcessInstance=historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).list();
            if (historicTaskInstances.size() > 0) {
                HistoricTaskInstance latestTask = historicTaskInstances.stream().sorted(Comparator.comparing(HistoricTaskInstance::getStartTime).reversed()).findFirst().get();
                List<IdentityLink> links = Lists.newArrayList();
                if(historicProcessInstance.getEndTime()==null) {
                    //流程未结束增加抄送任务
                    links=runtimeService.getIdentityLinksForProcessInstance(processInstanceId);
                }

                for (String user : ccUsers) {
                    Task task = taskService.newTask();
                    task.setDescription(latestTask.getDescription());
                    task.setTenantId(latestTask.getTenantId());
                    task.setOwner(user);
                    task.setAssignee(user);
                       task.setCategory(latestTask.getCategory());
                    task.setDelegationState(DelegationState.RESOLVED);
                    task.setName("[抄送]" + latestTask.getName());
                    task.setParentTaskId(latestTask.getId());//父任务id
                    taskService.saveTask(task);

                    if(historicProcessInstance.getEndTime()==null) {
                        if (links.stream().noneMatch(p -> p.getUserId().equalsIgnoreCase(user))) {
                            runtimeService.addParticipantUser(latestTask.getProcessInstanceId(), user);
                        }
                    }
                }
            }
        }
    }

}


