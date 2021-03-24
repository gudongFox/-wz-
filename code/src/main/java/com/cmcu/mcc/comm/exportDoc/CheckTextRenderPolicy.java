package com.cmcu.mcc.comm.exportDoc;

import com.deepoove.poi.NiceXWPFDocument;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.HyperLinkTextRenderData;
import com.deepoove.poi.data.TextRenderData;
import com.deepoove.poi.policy.AbstractRenderPolicy;
import com.deepoove.poi.policy.TextRenderPolicy;
import com.deepoove.poi.template.run.RunTemplate;
import com.deepoove.poi.util.StyleUtils;
import org.apache.poi.xwpf.usermodel.XWPFRun;

/**
 * @ProjectName: mcc
 * @Package: com.cmcu.mcc.comm.exportDoc
 * @ClassName: CheckTextRenderPolicy
 * @Author: Administrator
 * @Description: 选择列表（字典取值）
 * @Date: 2019/9/9 8:39
 * @Version: 1.0
 */
public class CheckTextRenderPolicy extends AbstractRenderPolicy<Object> {
    //每多少数据 换一次行
    int breakNum = 1;

    public CheckTextRenderPolicy(int breakNum) {
        this.breakNum = breakNum;
    }

    @Override
    public void doRender(RunTemplate runTemplate, Object renderData, XWPFTemplate template)
            throws Exception {
        XWPFRun run = runTemplate.getRun();
        Helper.renderTextRun(run, renderData, breakNum);
    }

    public static class Helper {
        public static void renderTextRun(XWPFRun run, Object renderData, int breakNum) {
            // create hyper link run
            if (renderData instanceof HyperLinkTextRenderData) {
                XWPFRun hyperLinkRun = NiceXWPFDocument.insertNewHyperLinkRun(run,
                        ((HyperLinkTextRenderData) renderData).getUrl());
                run.setText("", 0);
                run = hyperLinkRun;
            }
            // text
            TextRenderData textRenderData = renderData instanceof TextRenderData
                    ? (TextRenderData) renderData
                    : new TextRenderData(renderData.toString());

            String data = null == textRenderData.getText() ? "" : textRenderData.getText();

            StyleUtils.styleRun(run, textRenderData.getStyle());

            String[] split = data.split("\t", -1);
            if (null != split && split.length > 0) {
                run.setText(split[0], 0);
                for (int i = 1; i < split.length; i++) {
                    if(i%breakNum==0&&i!=split.length-1) {
                        run.addBreak();//换行
                        run.setText(split[i]);
                    }else {
                        run.setText(split[i]);
                    }
                }
            }
        }

    }
}
