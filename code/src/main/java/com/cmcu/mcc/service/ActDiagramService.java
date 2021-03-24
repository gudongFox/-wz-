package com.cmcu.mcc.service;

import com.cmcu.mcc.act.custom.CustomProcessDiagramGenerator;
import com.google.common.collect.Lists;
import org.activiti.bpmn.model.*;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.persistence.entity.CommentEntityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Comment;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ActDiagramService {


    @Resource
    ProcessEngine processEngine;





    public InputStream getDefaultInstanceStream(String processInstanceId){


        List<HistoricActivityInstance> oHistoricActivityInstanceList = processEngine.getHistoryService()
                .createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByHistoricActivityInstanceStartTime().asc()
                .list();


        List<HistoricActivityInstance> historicActivityInstanceList = Lists.newArrayList();
        oHistoricActivityInstanceList.forEach(p -> {
            List<Comment> comments = processEngine.getTaskService().getTaskComments(p.getTaskId());
            if (comments.size() == 0 || !((CommentEntityImpl) comments.get(0)).getMessage().equals("系统自动完成")) {
                historicActivityInstanceList.add(p);
            }
        });


        String processDefinitionId = processEngine.getHistoryService().createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).list().get(0).getProcessDefinitionId();
        BpmnModel bpmnModel = processEngine.getRepositoryService().getBpmnModel(processDefinitionId);

        List<String> highLightedActivities = Lists.newArrayList();
        List<String> highLightedFlows = Lists.newArrayList();
        for (int i = 0; i < historicActivityInstanceList.size(); i++) {

            HistoricActivityInstance historicActivityInstance = historicActivityInstanceList.get(i);
            FlowNode flowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(historicActivityInstance.getActivityId());
            List<SequenceFlow> incomingFlows = flowNode.getIncomingFlows();
            if (historicActivityInstance.getActivityType().equals("userTask")) {
                highLightedActivities.add(historicActivityInstance.getActivityId());
            }
            incomingFlows.forEach(p -> highLightedFlows.add(p.getId()));
        }

        InputStream imageStream = new DefaultProcessDiagramGenerator().generateDiagram(bpmnModel, "png", highLightedActivities,
                highLightedFlows,"宋体", "宋体", "宋体", null, 1.0D);
        return imageStream;
    }

    /**
     * 根据流程定义 得到流程的图片
     * @return
     */
    public InputStream getCustomProcessInstanceStream(String processDefinitionKey) {
        List<ProcessDefinition> list = processEngine.getRepositoryService().createProcessDefinitionQuery().processDefinitionKey(processDefinitionKey).processDefinitionTenantIdLike("mcc")
                .orderByProcessDefinitionVersion()  //使用流程定义的版本降序排列
                .desc().list();
        BpmnModel bpmnModel = processEngine.getRepositoryService().getBpmnModel(list.get(0).getId());
        CustomProcessDiagramGenerator diagramGenerator = (CustomProcessDiagramGenerator)processEngine.getProcessEngineConfiguration().setProcessDiagramGenerator(new CustomProcessDiagramGenerator()).getProcessDiagramGenerator();
        InputStream inputStream = diagramGenerator.buildPngInputStream(bpmnModel,new ArrayList<>());
        return inputStream;
    }
    /**
     * 根据流程定义 得到第一个节点
     * @return
     */
    public String getFirstUserTaskNameByKey(String processDefinitionKey) {
        List<ProcessDefinition> list = processEngine.getRepositoryService().createProcessDefinitionQuery().processDefinitionKey(processDefinitionKey).processDefinitionTenantIdLike("mcc")
                .orderByProcessDefinitionVersion()  //使用流程定义的版本降序排列
                .desc().list();
        BpmnModel bpmnModel = processEngine.getRepositoryService().getBpmnModel(list.get(0).getId());
        StartEvent startEvent=bpmnModel.getMainProcess().findFlowElementsOfType(StartEvent.class).get(0);
        List<SequenceFlow> startOutgoingFlows = startEvent.getOutgoingFlows();
        String firstTaskDefinitionKey=startOutgoingFlows.get(0).getTargetRef();
        FlowElement firstTask = bpmnModel.getMainProcess().getFlowElement(firstTaskDefinitionKey);
        return firstTask.getName();
    }

}
