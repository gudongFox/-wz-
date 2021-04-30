package com.cmcu.act.dto;

import com.cmcu.act.extend.IPropertyConstants;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import org.activiti.bpmn.model.*;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Getter
@Setter
public class ActBpmnModel implements IPropertyConstants {

    private String tenetId;

    private String name;

    private BpmnModel bpmnModel;

    private List<String> firstUserTaskList;

    private List<String> lastUserTaskList;

    private List<Map<String,String>> copyUserTaskList;

    private List<String> backUserTaskList;

    private List<String> acceptBackUserTaskList;

    private List<String> delegateUserTaskList;

    private List<String> transferUserTaskList;

    private List<List<FlowNode>> pathList;

    private List<FlowNode> simplestPath;

    public List<String> variables;




    public ActBpmnModel(BpmnModel bpmnModel) {
        this.bpmnModel = bpmnModel;
        pathList = Lists.newArrayList();
        simplestPath = Lists.newArrayList();
        firstUserTaskList = Lists.newArrayList();
        lastUserTaskList = Lists.newArrayList();
        copyUserTaskList=Lists.newArrayList();
        backUserTaskList=Lists.newArrayList();
        acceptBackUserTaskList=Lists.newArrayList();
        delegateUserTaskList=Lists.newArrayList();
        transferUserTaskList=Lists.newArrayList();
        variables=Lists.newArrayList();

        List<StartEvent> startEvents = bpmnModel.getMainProcess().findFlowElementsOfType(StartEvent.class);
        startEvents.forEach(this::recursiveFindFirstUserTask);

        List<EndEvent> endEvents = bpmnModel.getMainProcess().findFlowElementsOfType(EndEvent.class);
        endEvents.forEach(this::recursiveFindLastUserTask);

        List<UserTask> userTasks = bpmnModel.getMainProcess().findFlowElementsOfType(UserTask.class);


        bpmnModel.getMainProcess().findFlowElementsOfType(SequenceFlow.class).forEach(sequenceFlow -> {
            if(StringUtils.isNotEmpty(sequenceFlow.getConditionExpression())){
                if(sequenceFlow.getConditionExpression().startsWith("${")&&sequenceFlow.getConditionExpression().endsWith("}")){
                    String variable=sequenceFlow.getConditionExpression().substring(2,sequenceFlow.getConditionExpression().length()-1);
                    if(variable.contains("==")){
                        for(String v:StringUtils.split(variable,"==")){
                            if(!v.contains("'")){
                                variables.add(v.replace("(","").replace(")","").replace("!",""));
                            }
                        }
                    }else{
                        variables.add(variable.replace("(","").replace(")","").replace("!",""));
                    }
                }
            }
        });


        variables.add("useStamp");

        userTasks.forEach(userTask->{
           if(userTask.getLoopCharacteristics()!=null&&StringUtils.isNotEmpty(userTask.getLoopCharacteristics().getInputDataItem())){
               String inputDataItem=userTask.getLoopCharacteristics().getInputDataItem();
               if(inputDataItem.startsWith("${")&&inputDataItem.endsWith("}")){
                   variables.add(inputDataItem.substring(2,inputDataItem.length()-1));
               }
           } else if(userTask.getCandidateUsers().size()>0) {
               for (String candidateUser : userTask.getCandidateUsers()) {
                   if (candidateUser.startsWith("${")&&candidateUser.endsWith("}")) {
                       variables.add(candidateUser.substring(2,candidateUser.length()-1));
                   }
               }
           }else if(userTask.getCandidateGroups().size()==1){
               for (String candidateGroup : userTask.getCandidateGroups()) {
                   if (candidateGroup.startsWith("${")&&candidateGroup.endsWith("}")) {
                       variables.add(candidateGroup.substring(2,candidateGroup.length()-1));
                   }
               }
           }else if(StringUtils.isNotEmpty(userTask.getAssignee())&&userTask.getAssignee().startsWith("${")&&userTask.getAssignee().endsWith("}")) {
                variables.add(userTask.getAssignee().substring(2,userTask.getAssignee().length()-1));
           }
        });

        variables=variables.stream().distinct().collect(Collectors.toList());

        for(UserTask userTask:userTasks) {
            ExtensionElement ccEnable = getExtensionElement(userTask.getId(), CC_ENABLE);
            if (ccEnable != null && Boolean.parseBoolean(ccEnable.getElementText())) {
                ExtensionElement ccUser = getExtensionElement(userTask.getId(), CC_USER);
                Map cc= Maps.newHashMap();
                cc.put(userTask.getId(),ccUser==null?"":ccUser.getElementText());
                copyUserTaskList.add(cc);
            }
            ExtensionElement backAble = getExtensionElement(userTask.getId(), BACK_ENABLE);
            if (backAble == null || Boolean.parseBoolean(backAble.getElementText())) {
                backUserTaskList.add(userTask.getId());
            }

            ExtensionElement acceptBack = getExtensionElement(userTask.getId(), ACCEPT_BACK);
            if (acceptBack == null || Boolean.parseBoolean(acceptBack.getElementText())) {
                acceptBackUserTaskList.add(userTask.getId());
            }

            ExtensionElement delegateAble = getExtensionElement(userTask.getId(), DELEGATE_ENABLE);
            if (delegateAble == null || Boolean.parseBoolean(delegateAble.getElementText())) {
                delegateUserTaskList.add(userTask.getId());
            }

            ExtensionElement transferAble = getExtensionElement(userTask.getId(), TRANSFER_ENABLE);
            if (transferAble == null || Boolean.parseBoolean(transferAble.getElementText())) {
                transferUserTaskList.add(userTask.getId());
            }
        }



    }

    //遍历开始的用户节点
    private void recursiveFindFirstUserTask(FlowNode firstNode){
        List<SequenceFlow> startOutgoingFlows = firstNode.getOutgoingFlows();
        for(SequenceFlow sequenceFlow:startOutgoingFlows) {
            String tempFirstKey = sequenceFlow.getTargetRef();
            FlowElement tempFirstFlowElement = bpmnModel.getFlowElement(tempFirstKey);
            if (tempFirstFlowElement instanceof UserTask) {
                firstUserTaskList.add(tempFirstKey);
                return;
            } else if (tempFirstFlowElement instanceof FlowNode) {
                recursiveFindFirstUserTask((FlowNode) tempFirstFlowElement);
            }
        }

    }

    //遍历最后的用户节点
    private void recursiveFindLastUserTask(FlowNode lastNode){
        List<SequenceFlow> endIncomingFlows=lastNode.getIncomingFlows();
        for(SequenceFlow sequenceFlow:endIncomingFlows) {
            String tempLastKey = sequenceFlow.getSourceRef();
            FlowElement tempLastFlowElement = bpmnModel.getFlowElement(tempLastKey);
            if (tempLastFlowElement instanceof UserTask) {
                lastUserTaskList.add(tempLastKey);
                return;
            } else if (tempLastFlowElement instanceof FlowNode) {
                recursiveFindLastUserTask((FlowNode) tempLastFlowElement);
            }
        }
    }

    //得到扩展属性
    public ExtensionElement getExtensionElement(String taskKey,String key) {
        UserTask userTask = (UserTask) bpmnModel.getFlowElement(taskKey);
        if (userTask.getExtensionElements() != null) {
            List<ExtensionElement> extensionElements = userTask.getExtensionElements().get(key);
            if (extensionElements!=null&&extensionElements.size() > 0) return extensionElements.get(0);
        }
        return null;
    }





    public List<String>  listAllParallelTask(String sourceKey){
        List<String> paths=Lists.newArrayList();
        //直接在最简线上
        if(simplestPath.stream().anyMatch(p->p.getId().equalsIgnoreCase(sourceKey))){
            return paths;
        }
        Gateway gateway= (Gateway) pathList.stream().filter(l->l.stream().anyMatch(n->n.getId().equalsIgnoreCase(sourceKey))).findFirst().get().get(0);
        List<List<FlowNode>> otherPathList=
                pathList.stream().filter(l->l.stream().anyMatch(n->n.getId().equalsIgnoreCase(gateway.getId())))
                        .filter(l->l.stream().noneMatch(n->n.getId().equalsIgnoreCase(sourceKey))).collect(Collectors.toList());
        recursiveUserTask(paths,gateway.getId(),otherPathList);
        return paths;
    }

    private  void recursiveUserTask(List<String> paths,String beginGateWayId,List<List<FlowNode>> otherPathList){
        for(List<FlowNode> nodes:otherPathList) {
            for (FlowNode flowNode : nodes) {
                if (flowNode instanceof UserTask) {
                    paths.add(flowNode.getId());
                } else if (flowNode instanceof Gateway && !flowNode.getId().equalsIgnoreCase(beginGateWayId) && flowNode.getId().indexOf("begin_") == 0) {
                    recursiveUserTask(paths, flowNode.getId(), pathList.stream().filter(l -> l.stream().anyMatch(n -> n.getId().equalsIgnoreCase(flowNode.getId()))).collect(Collectors.toList()));
                }
            }
        }
    }


    public List<String>  listAllPreTask(String sourceKey){
        List<String> paths=Lists.newArrayList();
        if(simplestPath.stream().anyMatch(p->p.getId().equalsIgnoreCase(sourceKey))){
            for(FlowNode node :simplestPath){
                if(node.getId().equalsIgnoreCase(sourceKey)){
                    break;
                }else if(node instanceof UserTask){
                    paths.add(node.getId());
                }else if(node instanceof Gateway){
                    if(node.getId().contains("begin_")) {
                        recursiveAllTask((Gateway) node, paths);
                    }
                }
            }
            return paths;
        }else {
            List<String> allPaths=Lists.newArrayList();
            List<FlowNode> myLink = pathList.stream().filter(l -> l.stream().anyMatch(n -> n.getId().equalsIgnoreCase(sourceKey))).findFirst().get();
            for(FlowNode flowNode:myLink){
                if(flowNode.getId().equalsIgnoreCase(sourceKey)){
                    break;
                }
                allPaths.add(0,flowNode.getId());
            }
            Gateway gateway= (Gateway) myLink.get(0);
            recursivePreTaskByGateway(gateway,allPaths);
            return allPaths;
        }
    }

    private void recursiveAllTask(Gateway gateway,List<String> paths){
        List<List<FlowNode>> otherPathList=
                pathList.stream().filter(l->l.get(0).getId().equalsIgnoreCase(gateway.getId()))
                       .collect(Collectors.toList());
        for(List<FlowNode> flowNodes:otherPathList) {
            for(FlowNode node:flowNodes) {
                if (node instanceof UserTask) {
                    paths.add(node.getId());
                } else if (node instanceof Gateway) {
                    if(node!=gateway&&node.getId().contains("begin_")) {
                        recursiveAllTask((Gateway) node, paths);
                    }
                }
            }
        }
    }

    private void recursivePreTaskByGateway(Gateway gateway,List<String> allPaths) {

        boolean parallelExist=false;
        for(List<FlowNode> pathList:pathList){
            if(!pathList.get(0).getId().equalsIgnoreCase(gateway.getId())&&pathList.contains(gateway)){
                parallelExist=true;
                List<String> path=Lists.newArrayList();
                for(FlowNode flowNode:pathList){
                    if(flowNode.getId().equalsIgnoreCase(gateway.getId())){
                        break;
                    }
                    path.add(flowNode.getId());
                }
                allPaths.addAll(0,path);
                Gateway newGateWay= (Gateway) pathList.get(0);
                recursivePreTaskByGateway(newGateWay,allPaths);
                break;
            }
        }
        if(!parallelExist){
            List<String> path=Lists.newArrayList();
            for(FlowNode flowNode:simplestPath){
                if(flowNode.getId().equalsIgnoreCase(gateway.getId())){
                    break;
                }
                path.add(flowNode.getId());
            }
            allPaths.addAll(0,path);
        }


    }


}



