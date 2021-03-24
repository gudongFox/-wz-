package com.cmcu.mcc.act.service;

import com.cmcu.mcc.act.custom.BakCustomProcessDiagramGenerator;
import com.cmcu.mcc.act.model.MyHistoryTask;
import com.cmcu.mcc.act.custom.CustomProcessDiagramGenerator;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.activiti.bpmn.model.*;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.persistence.entity.CommentEntityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Comment;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MyDiagramService {

    @Resource
    ProcessEngine processEngine;

    @Autowired
    MyHistoryService myHistoryService;

    /**
     * 得到流程的图片
     *
     * @param processInstanceId
     * @return
     */
    public InputStream getCustomProcessInstanceStream2(String processInstanceId) {


        List<MyHistoryTask> tasks=myHistoryService.listHistoryTask(processInstanceId);


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
        List<Map> nodes = Lists.newArrayList();
        for (int i = 0; i < historicActivityInstanceList.size(); i++) {

            HistoricActivityInstance historicActivityInstance = historicActivityInstanceList.get(i);
            FlowNode flowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(historicActivityInstance.getActivityId());
            List<SequenceFlow> incomingFlows = flowNode.getIncomingFlows();
            if (historicActivityInstance.getActivityType().equals("userTask")) {
                highLightedActivities.add(historicActivityInstance.getActivityId());

                Map node = Maps.newHashMap();
                node.put("activityId", historicActivityInstance.getActivityId());
                node.put("backed", false);
                node.put("current", historicActivityInstance.getEndTime() == null);
                if (i > 0) {
                    SequenceFlow incomingFlow = incomingFlows.get(0);
                    FlowNode incomingNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(incomingFlow.getSourceRef());
                    if (incomingNode instanceof UserTask || incomingNode instanceof StartEvent) {
                        if (!historicActivityInstanceList.get(i - 1).getActivityId().equals(incomingNode.getId())) {
                            node.put("backed", true);
                            node.put("preId", historicActivityInstanceList.get(i - 1).getActivityId());
                        }
                    } else if (incomingNode instanceof Gateway) {

                        //定义的前置节点
                        List<String> definePreNodeIds = Lists.newArrayList();
                        for (SequenceFlow sequenceFlow : incomingNode.getIncomingFlows()) {
                            definePreNodeIds.add(sequenceFlow.getSourceRef());
                        }


                        List<String> childNodeIds = Lists.newArrayList();
                        for (SequenceFlow sequenceFlow : incomingNode.getOutgoingFlows()) {
                            childNodeIds.add((bpmnModel.getMainProcess().getFlowElement(sequenceFlow.getTargetRef())).getId());
                        }

                        FlowNode findPreNode = null;


                        //找到历史表中的上级,非兄弟元素
                        for (int j = i; j >= 0; j--) {
                            FlowNode currentFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(historicActivityInstanceList.get(j).getActivityId());
                            if (currentFlowNode instanceof UserTask) {
                                if (!childNodeIds.contains(currentFlowNode.getId())) {
                                    findPreNode = currentFlowNode;
                                    break;
                                }

                            }
                        }
                        boolean backed = !definePreNodeIds.contains(findPreNode.getId());
                        node.put("backed", backed);
                        node.put("preId", findPreNode.getId());
                    }

                }


                nodes.add(node);
            }

            incomingFlows.forEach(p -> highLightedFlows.add(p.getId()));
        }
        BakCustomProcessDiagramGenerator diagramGenerator = (BakCustomProcessDiagramGenerator) processEngine.getProcessEngineConfiguration().setProcessDiagramGenerator(new BakCustomProcessDiagramGenerator())
                .getProcessDiagramGenerator();

        InputStream inputStream = diagramGenerator.buildPngInputStream(bpmnModel, highLightedActivities, highLightedFlows, nodes,tasks);

        return inputStream;
    }


    /**
     * 得到流程的图片
     *
     * @param processInstanceId
     * @return
     */
    public InputStream getCustomProcessInstanceStream(String processInstanceId) {
        List<MyHistoryTask> tasks=myHistoryService.listHistoryTask(processInstanceId);
        String processDefinitionId = processEngine.getHistoryService().createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).list().get(0).getProcessDefinitionId();
        BpmnModel bpmnModel = processEngine.getRepositoryService().getBpmnModel(processDefinitionId);
        CustomProcessDiagramGenerator diagramGenerator = (CustomProcessDiagramGenerator)processEngine.getProcessEngineConfiguration().setProcessDiagramGenerator(new CustomProcessDiagramGenerator()).getProcessDiagramGenerator();
        InputStream inputStream = diagramGenerator.buildPngInputStream(bpmnModel,tasks);
        return inputStream;
    }
    /**
     * 根据流程定义 得到流程的图片
     * @return
     */
    public InputStream getCustomProcessInstanceStreamByKey(String processDefinitionKey) {
        List<ProcessDefinition> list = processEngine.getRepositoryService().createProcessDefinitionQuery().processDefinitionKey(processDefinitionKey).processDefinitionTenantIdLike("mcc")
                .orderByProcessDefinitionVersion()  //使用流程定义的版本降序排列
                .desc().list();
        BpmnModel bpmnModel = processEngine.getRepositoryService().getBpmnModel(list.get(0).getId());
        CustomProcessDiagramGenerator diagramGenerator = (CustomProcessDiagramGenerator)processEngine.getProcessEngineConfiguration().setProcessDiagramGenerator(new CustomProcessDiagramGenerator()).getProcessDiagramGenerator();
        InputStream inputStream = diagramGenerator.buildPngInputStream(bpmnModel,new ArrayList<>());
        return inputStream;
    }


    public InputStream getDefaultInstanceStream(String processInstanceId){
        List<MyHistoryTask> tasks=myHistoryService.listHistoryTask(processInstanceId);
        List<String> highLightedActivities=tasks.stream().filter(p->"发送".equalsIgnoreCase(p.getOpt())).map(MyHistoryTask::getTaskDefinitionKey).distinct().collect(Collectors.toList());
        String processDefinitionId = processEngine.getHistoryService().createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).list().get(0).getProcessDefinitionId();
        BpmnModel bpmnModel = processEngine.getRepositoryService().getBpmnModel(processDefinitionId);
        InputStream imageStream = new DefaultProcessDiagramGenerator().generateDiagram(bpmnModel, "png", highLightedActivities,
                Lists.newArrayList(),"宋体", "宋体", "宋体", null, 1.0D);
        return imageStream;
    }

}
