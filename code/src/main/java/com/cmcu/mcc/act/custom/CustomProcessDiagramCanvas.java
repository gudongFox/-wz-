package com.cmcu.mcc.act.custom;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.activiti.bpmn.model.AssociationDirection;
import org.activiti.bpmn.model.GraphicInfo;
import org.activiti.image.impl.DefaultProcessDiagramCanvas;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

public class CustomProcessDiagramCanvas extends DefaultProcessDiagramCanvas {

    protected static Color FINISHED_COLOR = new Color(112, 146, 190);

    public CustomProcessDiagramCanvas(int width, int height, int minX, int minY, String imageType, String activityFontName, String labelFontName, String annotationFontName, ClassLoader customClassLoader) {
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

    public void drawHighLight(int x, int y, int width, int height,String nodeType) {
        Paint originalPaint = g.getPaint();
        Stroke originalStroke = g.getStroke();
        if("current".equalsIgnoreCase(nodeType)){
            g.setStroke(THICK_TASK_BORDER_STROKE);
            g.setColor(Color.blue);
        } else if("backed".equalsIgnoreCase(nodeType)) {
            g.setPaint(Color.PINK);
        }else if("passed".equalsIgnoreCase(nodeType)) {
            g.setStroke(HIGHLIGHT_FLOW_STROKE);
        }
        RoundRectangle2D rect = new RoundRectangle2D.Double(x, y, width, height, 20, 20);
        g.draw(rect);
        g.setPaint(originalPaint);
        g.setStroke(originalStroke);
    }

    public void drawLabel(String text, GraphicInfo graphicInfo, boolean centered){
        float interline = 1.0f;

        // text
        if (text != null && text.length()>0) {
            Paint originalPaint = g.getPaint();
            Font originalFont = g.getFont();

            g.setPaint(Color.BLACK);
            g.setFont(LABEL_FONT);

            int wrapWidth = 100;
            int textY = (int) graphicInfo.getY();

            // TODO: use drawMultilineText()
            AttributedString as = new AttributedString(text);
            as.addAttribute(TextAttribute.FOREGROUND, g.getPaint());
            as.addAttribute(TextAttribute.FONT, g.getFont());
            AttributedCharacterIterator aci = as.getIterator();
            FontRenderContext frc = new FontRenderContext(null, true, false);
            LineBreakMeasurer lbm = new LineBreakMeasurer(aci, frc);

            while (lbm.getPosition() < text.length()) {
                TextLayout tl = lbm.nextLayout(wrapWidth);
                textY += tl.getAscent();
                Rectangle2D bb = tl.getBounds();
                double tX = graphicInfo.getX();
                if (centered) {
                    tX += (int) (graphicInfo.getWidth() / 2 - bb.getWidth() / 2);
                }
                tl.draw(g, (float) tX, textY);
                textY += tl.getDescent() + tl.getLeading() + (interline - 1.0f) * tl.getAscent();
            }

            // restore originals
            g.setFont(originalFont);
            g.setPaint(originalPaint);
        }
    }


}
