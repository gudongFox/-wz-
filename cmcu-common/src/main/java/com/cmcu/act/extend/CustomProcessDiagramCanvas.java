package com.cmcu.act.extend;

import org.activiti.bpmn.model.GraphicInfo;
import org.activiti.image.impl.DefaultProcessDiagramCanvas;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.List;

public class CustomProcessDiagramCanvas  extends DefaultProcessDiagramCanvas {



    public CustomProcessDiagramCanvas(int width, int height, int minX, int minY, String imageType, String activityFontName, String labelFontName, String annotationFontName, ClassLoader customClassLoader) {
        super(width, height, minX, minY, imageType, activityFontName, labelFontName, annotationFontName, customClassLoader);
    }


    public void drawHighLight(int x, int y, int width, int height,String nodeType) {
        Paint originalPaint = g.getPaint();
        Stroke originalStroke = g.getStroke();
        if("current".equalsIgnoreCase(nodeType)){
            g.setStroke(THICK_TASK_BORDER_STROKE);
            g.setColor(Color.blue);
        } else if("backed".equalsIgnoreCase(nodeType)) {
            g.setPaint(HIGHLIGHT_COLOR);
            g.setStroke(THICK_TASK_BORDER_STROKE);
        }else if("passed".equalsIgnoreCase(nodeType)) {
            g.setPaint(Color.BLACK);
            g.setStroke(THICK_TASK_BORDER_STROKE);
        }else{
            //不会过来
            g.setPaint(Color.black);
            g.setStroke(new BasicStroke(1.0f));
        }
        RoundRectangle2D rect = new RoundRectangle2D.Double(x, y, width, height, 20, 20);
        g.draw(rect);

        g.setPaint(originalPaint);
        g.setStroke(originalStroke);
    }



    public void drawRecord(int x, int y, int width, int height, String nodeType, List<String> handleInfoList) {
        Paint originalPaint = g.getPaint();
        Font originalFont = g.getFont();
        for (int i=0;i<handleInfoList.size();i++) {
            if ("current".equalsIgnoreCase(nodeType)) {
                g.setColor(Color.blue);
            } else if ("backed".equalsIgnoreCase(nodeType)) {
                g.setColor(HIGHLIGHT_COLOR);
            } else {
                g.setColor(Color.BLACK);
            }
            g.setFont(new Font("微软雅黑", Font.PLAIN, originalFont.getSize() - 1));
            g.drawString(handleInfoList.get(i), x, y + (int) (height * 1.2) + 16 * i);
        }
        // restore originals
        g.setFont(originalFont);
        g.setPaint(originalPaint);

    }


    @Override
    public void drawLabel(String text, GraphicInfo graphicInfo, boolean centered){
        float interline = 1.0f;

        // text
        if (text != null && text.length()>0) {
            Paint originalPaint = g.getPaint();
            Font originalFont = g.getFont();

            g.setPaint(Color.BLACK);
            g.setFont(new Font("微软雅黑", Font.PLAIN, originalFont.getSize() - 2));

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
                tl.draw(g, (float) tX, textY+3);
                textY += tl.getDescent() + tl.getLeading() + (interline - 1.0f) * tl.getAscent();
            }

            // restore originals
            g.setFont(originalFont);
            g.setPaint(originalPaint);
        }
    }


}
