package com.cmcu.mcc.act.custom;

import org.activiti.bpmn.model.AssociationDirection;
import org.activiti.image.impl.DefaultProcessDiagramCanvas;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.RoundRectangle2D;

public class BakCustomProcessDiagramCanvas extends DefaultProcessDiagramCanvas {

    protected static Color FINISHED_COLOR = new Color(112, 146, 190);


    public BakCustomProcessDiagramCanvas(int width, int height, int minX, int minY, String imageType, String activityFontName, String labelFontName, String annotationFontName, ClassLoader customClassLoader) {
        super(width, height, minX, minY, imageType, activityFontName, labelFontName, annotationFontName, customClassLoader);
    }


    public void drawConnection(int[] xPoints, int[] yPoints, boolean conditional, boolean isDefault, String connectionType,
                                   AssociationDirection associationDirection, boolean highLighted, double scaleFactor,String stepNumbers,Boolean backed) {

        Paint originalPaint = g.getPaint();
        Stroke originalStroke = g.getStroke();

        g.setPaint(CONNECTION_COLOR);
        if (connectionType.equals("association")) {
            g.setStroke(ASSOCIATION_STROKE);
        } else if (highLighted) {
            g.setPaint(HIGHLIGHT_COLOR);
            g.setStroke(HIGHLIGHT_FLOW_STROKE);
        }

        if(backed){
            g.setPaint(HIGHLIGHT_COLOR);
            g.setStroke(HIGHLIGHT_FLOW_STROKE);
        }else if(highLighted){
            g.setPaint(LABEL_COLOR);
        }else{
            g.setPaint(originalPaint);
            g.setStroke(originalStroke);
        }


        for (int i=1; i<xPoints.length; i++) {
            Integer sourceX = xPoints[i - 1];
            Integer sourceY = yPoints[i - 1];
            Integer targetX = xPoints[i];
            Integer targetY = yPoints[i];
            Line2D.Double line = new Line2D.Double(sourceX, sourceY, targetX, targetY);
            g.draw(line);


            if(StringUtils.isNotEmpty(stepNumbers)) {
                //增加画数字
                if (i == xPoints.length - 1&&!backed) {
                    g.drawString(stepNumbers, sourceX + (targetX - sourceX) / 5, sourceY);
                }else if(i==xPoints.length-2&&backed){
                    g.drawString(stepNumbers, sourceX + (targetX - sourceX) / 5, sourceY);
                }
            }
        }


        if (isDefault){
            Line2D.Double line = new Line2D.Double(xPoints[0], yPoints[0], xPoints[1], yPoints[1]);
            drawDefaultSequenceFlowIndicator(line, scaleFactor);
        }

        if (conditional) {
            Line2D.Double line = new Line2D.Double(xPoints[0], yPoints[0], xPoints[1], yPoints[1]);
            drawConditionalSequenceFlowIndicator(line, scaleFactor);
        }

        if (associationDirection.equals(AssociationDirection.ONE) || associationDirection.equals(AssociationDirection.BOTH)) {
            Line2D.Double line = new Line2D.Double(xPoints[xPoints.length-2], yPoints[xPoints.length-2], xPoints[xPoints.length-1], yPoints[xPoints.length-1]);
            drawArrowHead(line, scaleFactor);
        }
        if (associationDirection.equals(AssociationDirection.BOTH)) {
            Line2D.Double line = new Line2D.Double(xPoints[1], yPoints[1], xPoints[0], yPoints[0]);
            drawArrowHead(line, scaleFactor);
        }
        g.setPaint(originalPaint);
        g.setStroke(originalStroke);
    }

    public void drawHighLight(int x, int y, int width, int height,boolean current) {
        Paint originalPaint = g.getPaint();
        Stroke originalStroke = g.getStroke();
        if(current){
            g.setStroke(THICK_TASK_BORDER_STROKE);
            g.setColor(Color.blue);

        }else {
            g.setPaint(FINISHED_COLOR);
        }
        RoundRectangle2D rect = new RoundRectangle2D.Double(x, y, width, height, 20, 20);
        g.draw(rect);
        g.setPaint(originalPaint);
        g.setStroke(originalStroke);
    }
}
