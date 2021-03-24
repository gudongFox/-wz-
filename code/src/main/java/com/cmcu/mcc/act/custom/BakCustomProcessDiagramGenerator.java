package com.cmcu.mcc.act.custom;

import com.cmcu.mcc.act.model.MyHistoryTask;
import com.google.common.collect.Lists;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.image.impl.DefaultProcessDiagramCanvas;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class BakCustomProcessDiagramGenerator extends DefaultProcessDiagramGenerator {


    private List<Map> nodes = Lists.newArrayList();

    private List<MyHistoryTask> tasks=Lists.newArrayList();

    public InputStream buildPngInputStream(BpmnModel bpmnModel, List<String> highLightedActivities, List<String> highLightedFlows,List<Map> nodes,List<MyHistoryTask> tasks) {
        String imageType = "png";
        this.nodes=nodes;
        this.tasks=tasks;
        return this.generateProcessDiagram(bpmnModel, imageType, highLightedActivities, highLightedFlows, "宋体", "宋体", "宋体", null, 1.0D).generateImage(imageType);
    }


    @Override
    protected void drawActivity(DefaultProcessDiagramCanvas processDiagramCanvas, BpmnModel bpmnModel, FlowNode flowNode, List<String> highLightedActivities, List<String> highLightedFlows, double scaleFactor) {
        ActivityDrawInstruction drawInstruction = activityDrawInstructions.get(flowNode.getClass());
        if (drawInstruction != null) {

            drawInstruction.draw(processDiagramCanvas, bpmnModel, flowNode);

            // Gather info on the multi instance marker
            boolean multiInstanceSequential = false, multiInstanceParallel = false, collapsed = false;
            if (flowNode instanceof Activity) {
                Activity activity = (Activity) flowNode;
                MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics = activity.getLoopCharacteristics();
                if (multiInstanceLoopCharacteristics != null) {
                    multiInstanceSequential = multiInstanceLoopCharacteristics.isSequential();
                    multiInstanceParallel = !multiInstanceSequential;
                }
            }

            // Gather info on the collapsed marker
            GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(flowNode.getId());
            if (flowNode instanceof SubProcess) {
                collapsed = graphicInfo.getExpanded() != null && !graphicInfo.getExpanded();
            } else if (flowNode instanceof CallActivity) {
                collapsed = true;
            }

            if (scaleFactor == 1.0) {
                // Actually draw the markers
                processDiagramCanvas.drawActivityMarkers((int) graphicInfo.getX(), (int) graphicInfo.getY(),(int) graphicInfo.getWidth(), (int) graphicInfo.getHeight(),
                        multiInstanceSequential, multiInstanceParallel, collapsed);
            }

            // Draw highlighted activities
            if (highLightedActivities.contains(flowNode.getId())) {
                boolean current=false;
                for(Map node:nodes){
                    if(node.get("activityId").toString().equals(flowNode.getId())){
                        current=Boolean.parseBoolean(node.get("current").toString());
                        if(current){
                            break;
                        }
                    }
                }

                ((BakCustomProcessDiagramCanvas)processDiagramCanvas).drawHighLight((int) graphicInfo.getX(), (int) graphicInfo.getY(), (int) graphicInfo.getWidth(), (int) graphicInfo.getHeight(),current);
            }

        }

        // Outgoing transitions of activity
        for (SequenceFlow sequenceFlow : flowNode.getOutgoingFlows()) {
            boolean highLighted = (highLightedFlows.contains(sequenceFlow.getId()));
            String defaultFlow = null;
            if (flowNode instanceof Activity) {
                defaultFlow = ((Activity) flowNode).getDefaultFlow();
            } else if (flowNode instanceof Gateway) {
                defaultFlow = ((Gateway) flowNode).getDefaultFlow();
            }

            boolean isDefault = false;
            if (defaultFlow != null && defaultFlow.equalsIgnoreCase(sequenceFlow.getId())) {
                isDefault = true;
            }
            boolean drawConditionalIndicator = sequenceFlow.getConditionExpression() != null && !(flowNode instanceof Gateway);

            String sourceRef = sequenceFlow.getSourceRef();
            String targetRef = sequenceFlow.getTargetRef();
            FlowElement sourceElement = bpmnModel.getFlowElement(sourceRef);
            FlowElement targetElement = bpmnModel.getFlowElement(targetRef);
            List<GraphicInfo> graphicInfoList = bpmnModel.getFlowLocationGraphicInfo(sequenceFlow.getId());
            if (graphicInfoList != null && graphicInfoList.size() > 0) {
                graphicInfoList = connectionPerfectionizer(processDiagramCanvas, bpmnModel, sourceElement, targetElement, graphicInfoList);
                int xPoints[]= new int[graphicInfoList.size()];
                int yPoints[]= new int[graphicInfoList.size()];

                for (int i=1; i<graphicInfoList.size(); i++) {
                    GraphicInfo graphicInfo = graphicInfoList.get(i);
                    GraphicInfo previousGraphicInfo = graphicInfoList.get(i-1);

                    if (i == 1) {
                        xPoints[0] = (int) previousGraphicInfo.getX();
                        yPoints[0] = (int) previousGraphicInfo.getY();
                    }
                    xPoints[i] = (int) graphicInfo.getX();
                    yPoints[i] = (int) graphicInfo.getY();

                }

                List<Integer> numbers= Lists.newArrayList();
                if(!(targetElement instanceof Gateway)){
                    for(int i=1;i<=nodes.size();i++){
                        Map node=nodes.get(i-1);
                        if(node.get("activityId").equals(targetRef)&&!Boolean.parseBoolean(node.get("backed").toString())){
                            numbers.add(i);
                        }
                    }
                }
                ((BakCustomProcessDiagramCanvas)processDiagramCanvas).drawConnection(xPoints, yPoints, drawConditionalIndicator, isDefault, "sequenceFlow",  AssociationDirection.ONE, highLighted,scaleFactor, StringUtils.join(numbers,","),false);


                numbers=Lists.newArrayList();
                for(int i=1;i<=nodes.size();i++){
                    Map node=nodes.get(i-1);
                    if (node.get("activityId").equals(targetRef) && Boolean.parseBoolean(node.get("backed").toString())) {
                        String preId = node.get("preId").toString();

                        String gateWayId=((FlowNode)bpmnModel.getFlowElement(preId)).getIncomingFlows().get(0).getSourceRef();
                        FlowNode gateWay=(FlowNode)bpmnModel.getFlowElement(gateWayId);
                        boolean parallelTask=false;
                        if(gateWay instanceof Gateway){
                            parallelTask=gateWay.getOutgoingFlows().size()>1;
                        }
                        numbers.add(i);
                        drawBackFlowSequence((BakCustomProcessDiagramCanvas)processDiagramCanvas, bpmnModel.getGraphicInfo(preId), bpmnModel.getGraphicInfo(targetRef),parallelTask,StringUtils.join(numbers,","));
                    }
                }



                // Draw sequenceflow label
                GraphicInfo labelGraphicInfo = bpmnModel.getLabelGraphicInfo(sequenceFlow.getId());
                if (labelGraphicInfo != null) {
                    processDiagramCanvas.drawLabel(sequenceFlow.getName(), labelGraphicInfo, false);
                }
            }
        }

        // Nested elements
        if (flowNode instanceof FlowElementsContainer) {
            for (FlowElement nestedFlowElement : ((FlowElementsContainer) flowNode).getFlowElements()) {
                if (nestedFlowElement instanceof FlowNode) {
                    drawActivity(processDiagramCanvas, bpmnModel, (FlowNode) nestedFlowElement,
                            highLightedActivities, highLightedFlows, scaleFactor);
                }
            }
        }
    }

    private void drawBackFlowSequence(BakCustomProcessDiagramCanvas processDiagramCanvas, GraphicInfo sourceGraphicInfo, GraphicInfo graphicInfo1, boolean parallelTask, String numbers) {
        int[] xPoints = new int[4];
        int[] yPoints = new int[4];


        if (parallelTask) {

            int gap = (int) Math.abs(graphicInfo1.getY() - sourceGraphicInfo.getY()) / 2;
            if (sourceGraphicInfo.getY() < graphicInfo1.getY()) {
                xPoints[0] = (int) sourceGraphicInfo.getX() + (int) sourceGraphicInfo.getWidth() / 2;
                yPoints[0] = (int) sourceGraphicInfo.getY();

                xPoints[1] = xPoints[0];
                yPoints[1] = yPoints[0] - gap;

                xPoints[2] = (int) graphicInfo1.getX() + (int) graphicInfo1.getWidth() / 2 + 10;
                yPoints[2] = yPoints[1];

                xPoints[3] = (int) graphicInfo1.getX() + (int) sourceGraphicInfo.getWidth() / 2 + 10;
                yPoints[3] = (int) graphicInfo1.getY();

            } else if (sourceGraphicInfo.getY() > graphicInfo1.getY()) {

                xPoints[0] = (int) sourceGraphicInfo.getX() + (int) sourceGraphicInfo.getWidth() / 2;
                yPoints[0] = (int) sourceGraphicInfo.getY() + (int) sourceGraphicInfo.getHeight();

                xPoints[1] = xPoints[0];
                yPoints[1] = yPoints[0] + gap;

                xPoints[2] = (int) graphicInfo1.getX() + (int) graphicInfo1.getWidth() / 2;
                yPoints[2] = yPoints[1];

                xPoints[3] = (int) graphicInfo1.getX() + (int) graphicInfo1.getWidth() / 2;
                yPoints[3] = (int) graphicInfo1.getY() + (int) sourceGraphicInfo.getHeight();

            }

        } else {

            int gap = (int) Math.abs(graphicInfo1.getY() - sourceGraphicInfo.getY());
            gap = gap + gap / 2;

            if (sourceGraphicInfo.getY() >= graphicInfo1.getY()) {
                xPoints[0] = (int) sourceGraphicInfo.getX() + (int) sourceGraphicInfo.getWidth() / 2;
                yPoints[0] = (int) sourceGraphicInfo.getY();

                xPoints[1] = xPoints[0];
                yPoints[1] = yPoints[0] - gap;

                xPoints[2] = (int) graphicInfo1.getX() + (int) graphicInfo1.getWidth() / 2 + 10;
                yPoints[2] = yPoints[1];

                xPoints[3] = (int) graphicInfo1.getX() + (int) sourceGraphicInfo.getWidth() / 2 + 10;
                yPoints[3] = (int) graphicInfo1.getY();

            } else if (sourceGraphicInfo.getY() < graphicInfo1.getY()) {

                xPoints[0] = (int) sourceGraphicInfo.getX() + (int) sourceGraphicInfo.getWidth() / 2;
                yPoints[0] = (int) sourceGraphicInfo.getY() + (int) sourceGraphicInfo.getHeight();

                xPoints[1] = xPoints[0];
                yPoints[1] = yPoints[0] + gap;

                xPoints[2] = (int) graphicInfo1.getX() + (int) graphicInfo1.getWidth() / 2;
                yPoints[2] = yPoints[1];

                xPoints[3] = (int) graphicInfo1.getX() + (int) graphicInfo1.getWidth() / 2;
                yPoints[3] = (int) graphicInfo1.getY() + (int) sourceGraphicInfo.getHeight();

            }
        }
        processDiagramCanvas.drawConnection(xPoints, yPoints, false, false, "sequenceFlow", AssociationDirection.ONE, true, 1.0, numbers,true);
    }

    //重置canvas
    @Override
    protected DefaultProcessDiagramCanvas generateProcessDiagram(BpmnModel bpmnModel, String imageType, List<String> highLightedActivities, List<String> highLightedFlows, String activityFontName, String labelFontName, String annotationFontName, ClassLoader customClassLoader, double scaleFactor) {
        prepareBpmnModel(bpmnModel);

        BakCustomProcessDiagramCanvas processDiagramCanvas = initProcessDiagramCanvas(bpmnModel, imageType, activityFontName, labelFontName, annotationFontName, customClassLoader);

        // Draw pool shape, if process is participant in collaboration
        for (Pool pool : bpmnModel.getPools()) {
            GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(pool.getId());
            processDiagramCanvas.drawPoolOrLane(pool.getName(), graphicInfo);
        }

        // Draw lanes
        for (org.activiti.bpmn.model.Process process : bpmnModel.getProcesses()) {
            for (Lane lane : process.getLanes()) {
                GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(lane.getId());
                processDiagramCanvas.drawPoolOrLane(lane.getName(), graphicInfo);
            }
        }

        // Draw activities and their sequence-flows
        for (FlowNode flowNode : bpmnModel.getProcesses().get(0).findFlowElementsOfType(FlowNode.class)) {
            drawActivity(processDiagramCanvas, bpmnModel, flowNode, highLightedActivities, highLightedFlows, scaleFactor);
        }

        for (org.activiti.bpmn.model.Process process: bpmnModel.getProcesses()) {
            for (FlowNode flowNode : process.findFlowElementsOfType(FlowNode.class)) {
                drawActivity(processDiagramCanvas, bpmnModel, flowNode, highLightedActivities, highLightedFlows, scaleFactor);
            }
        }

        // Draw artifacts
        for (org.activiti.bpmn.model.Process process : bpmnModel.getProcesses()) {

            for (Artifact artifact : process.getArtifacts()) {
                drawArtifact(processDiagramCanvas, bpmnModel, artifact);
            }

            List<SubProcess> subProcesses = process.findFlowElementsOfType(SubProcess.class, true);
            if (subProcesses != null) {
                for (SubProcess subProcess : subProcesses) {
                    for (Artifact subProcessArtifact : subProcess.getArtifacts()) {
                        drawArtifact(processDiagramCanvas, bpmnModel, subProcessArtifact);
                    }
                }
            }
        }

        return processDiagramCanvas;
    }

    protected static BakCustomProcessDiagramCanvas initProcessDiagramCanvas(BpmnModel bpmnModel, String imageType,
                                                                            String activityFontName, String labelFontName, String annotationFontName, ClassLoader customClassLoader) {

        // We need to calculate maximum values to know how big the image will be in its entirety
        double minX = Double.MAX_VALUE;
        double maxX = 0;
        double minY = Double.MAX_VALUE;
        double maxY = 0;

        for (Pool pool : bpmnModel.getPools()) {
            GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(pool.getId());
            minX = graphicInfo.getX();
            maxX = graphicInfo.getX() + graphicInfo.getWidth();
            minY = graphicInfo.getY();
            maxY = graphicInfo.getY() + graphicInfo.getHeight();
        }

        List<FlowNode> flowNodes = gatherAllFlowNodes(bpmnModel);
        for (FlowNode flowNode : flowNodes) {

            GraphicInfo flowNodeGraphicInfo = bpmnModel.getGraphicInfo(flowNode.getId());

            // width
            if (flowNodeGraphicInfo.getX() + flowNodeGraphicInfo.getWidth() > maxX) {
                maxX = flowNodeGraphicInfo.getX() + flowNodeGraphicInfo.getWidth();
            }
            if (flowNodeGraphicInfo.getX() < minX) {
                minX = flowNodeGraphicInfo.getX();
            }
            // height
            if (flowNodeGraphicInfo.getY() + flowNodeGraphicInfo.getHeight() > maxY) {
                maxY = flowNodeGraphicInfo.getY() + flowNodeGraphicInfo.getHeight();
            }
            if (flowNodeGraphicInfo.getY() < minY) {
                minY = flowNodeGraphicInfo.getY();
            }

            for (SequenceFlow sequenceFlow : flowNode.getOutgoingFlows()) {
                List<GraphicInfo> graphicInfoList = bpmnModel.getFlowLocationGraphicInfo(sequenceFlow.getId());
                if (graphicInfoList != null) {
                    for (GraphicInfo graphicInfo : graphicInfoList) {
                        // width
                        if (graphicInfo.getX() > maxX) {
                            maxX = graphicInfo.getX();
                        }
                        if (graphicInfo.getX() < minX) {
                            minX = graphicInfo.getX();
                        }
                        // height
                        if (graphicInfo.getY() > maxY) {
                            maxY = graphicInfo.getY();
                        }
                        if (graphicInfo.getY()< minY) {
                            minY = graphicInfo.getY();
                        }
                    }
                }
            }
        }

        List<Artifact> artifacts = gatherAllArtifacts(bpmnModel);
        for (Artifact artifact : artifacts) {

            GraphicInfo artifactGraphicInfo = bpmnModel.getGraphicInfo(artifact.getId());

            if (artifactGraphicInfo != null) {
                // width
                if (artifactGraphicInfo.getX() + artifactGraphicInfo.getWidth() > maxX) {
                    maxX = artifactGraphicInfo.getX() + artifactGraphicInfo.getWidth();
                }
                if (artifactGraphicInfo.getX() < minX) {
                    minX = artifactGraphicInfo.getX();
                }
                // height
                if (artifactGraphicInfo.getY() + artifactGraphicInfo.getHeight() > maxY) {
                    maxY = artifactGraphicInfo.getY() + artifactGraphicInfo.getHeight();
                }
                if (artifactGraphicInfo.getY() < minY) {
                    minY = artifactGraphicInfo.getY();
                }
            }

            List<GraphicInfo> graphicInfoList = bpmnModel.getFlowLocationGraphicInfo(artifact.getId());
            if (graphicInfoList != null) {
                for (GraphicInfo graphicInfo : graphicInfoList) {
                    // width
                    if (graphicInfo.getX() > maxX) {
                        maxX = graphicInfo.getX();
                    }
                    if (graphicInfo.getX() < minX) {
                        minX = graphicInfo.getX();
                    }
                    // height
                    if (graphicInfo.getY() > maxY) {
                        maxY = graphicInfo.getY();
                    }
                    if (graphicInfo.getY()< minY) {
                        minY = graphicInfo.getY();
                    }
                }
            }
        }

        int nrOfLanes = 0;
        for (Process process : bpmnModel.getProcesses()) {
            for (Lane l : process.getLanes()) {

                nrOfLanes++;

                GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(l.getId());
                // // width
                if (graphicInfo.getX() + graphicInfo.getWidth() > maxX) {
                    maxX = graphicInfo.getX() + graphicInfo.getWidth();
                }
                if (graphicInfo.getX() < minX) {
                    minX = graphicInfo.getX();
                }
                // height
                if (graphicInfo.getY() + graphicInfo.getHeight() > maxY) {
                    maxY = graphicInfo.getY() + graphicInfo.getHeight();
                }
                if (graphicInfo.getY() < minY) {
                    minY = graphicInfo.getY();
                }
            }
        }

        // Special case, see https://activiti.atlassian.net/browse/ACT-1431
        if (flowNodes.isEmpty() && bpmnModel.getPools().isEmpty() && nrOfLanes == 0) {
            // Nothing to show
            minX = 0;
            minY = 0;
        }

        return new BakCustomProcessDiagramCanvas((int) maxX + 10,(int) maxY + 10, (int) minX, (int) minY,
                imageType, activityFontName, labelFontName, annotationFontName, customClassLoader);
    }



}
