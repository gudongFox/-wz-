package com.cmcu.mcc.comm.exportDoc;

import com.deepoove.poi.policy.DynamicTableRenderPolicy;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc;

import java.math.BigInteger;
import java.util.List;

/**
 * @ProjectName: mcc
 * @Package: com.cmcu.mcc.comm.exportDoc
 * @ClassName: AddColTablePolicy
 * @Author: Administrator
 * @Description: 新建策略 动态添加行
 * @Date: 2019/9/6 10:17
 * @Version: 1.0
 */
public class AddRowTablePolicy extends DynamicTableRenderPolicy {

    // 填充数据所在行数
    int startRow ;
    // 填充每行的列数
    int colNum ;
    //最小填充数量
    int minNum ;
    //在行头高度基础增加量
    int rowHeight = 70;

    public AddRowTablePolicy(int startRow, int colNum, int minNum ,int rowHeight){
        this.colNum = colNum;
        this.startRow = startRow;
        this.minNum = minNum;
        this.rowHeight = rowHeight;
    }
    public AddRowTablePolicy(int startRow, int colNum, int minNum){
        this.colNum = colNum;
        this.startRow = startRow;
        this.minNum = minNum;
    }
    @Override
    public void render(XWPFTable table, Object data) {
        if (null == data) return;

        // 填充明细
        List<List<String>> details = (List<List<String>>) data;
        //原表格行高度
        int height = 500;
        if(startRow!=0){
            height =table.getRow(startRow-1).getHeight();
        }

        if (null != details) {
            //数据大于最少显示
            if(details.size()>minNum) {
                for (int i = 0; i < details.size(); i++) {
                    XWPFTableRow insertNewTableRow = table.insertNewTableRow(startRow+i);
                    //新行高度
                    insertNewTableRow.getCtRow().addNewTrPr().addNewTrHeight().setVal(BigInteger.valueOf(height + rowHeight));
                    for (int j = 0; j < colNum; j++) {
                        XWPFTableCell cell = insertNewTableRow.createCell();
                        //存在 \t 换行
                        XWPFParagraph para = cell.getParagraphs().get(0);//对某个单元格设置段落，
                        para.setAlignment(ParagraphAlignment.LEFT);//需要设置，否则中文换行会很生硬很难看
                        String[] strings = details.get(i).get(j).split("\t");
                        for (int k=0;k<strings.length;k++) {
                            XWPFRun run = para.createRun();//对某个段落设置格式
                            run.setText(strings[k].trim());
                            if(strings.length>0&&k!=strings.length-1) {
                                run.addBreak();//换行
                            }
                        }
                        //单元格内容居中
                        CTTc cttc = cell.getCTTc();
                        CTTcPr ctPr = cttc.addNewTcPr();
                        ctPr.addNewVAlign().setVal(STVerticalJc.CENTER);
                        cttc.getPList().get(0).addNewPPr().addNewJc().setVal(STJc.CENTER);
                    }
                }
            }
            else{
                for (int i = 0; i < details.size(); i++) {
                    XWPFTableRow insertNewTableRow = table.insertNewTableRow(startRow+i);
                    //新行高度
                    insertNewTableRow.getCtRow().addNewTrPr().addNewTrHeight().setVal(BigInteger.valueOf(height + rowHeight));
                    for (int j = 0; j < colNum; j++) {
                        XWPFTableCell cell = insertNewTableRow.createCell();
                        //存在\t换行
                        XWPFParagraph para = cell.getParagraphs().get(0);//对某个单元格设置段落，
                        para.setAlignment(ParagraphAlignment.LEFT);//需要设置，否则中文换行会很生硬很难看
                        String[] strings = details.get(i).get(j).split("\t");
                        for (int k=0;k<strings.length;k++) {
                            XWPFRun run = para.createRun();//对某个段落设置格式
                            run.setText(strings[k].trim());
                            if(strings.length>0&&k!=strings.length-1) {
                                run.addBreak();//换行
                            }
                        }
                        //单元格内容居中
                        CTTc cttc = cell.getCTTc();
                        CTTcPr ctPr = cttc.addNewTcPr();
                        ctPr.addNewVAlign().setVal(STVerticalJc.CENTER);
                        cttc.getPList().get(0).addNewPPr().addNewJc().setVal(STJc.CENTER);
                    }
                }
                //补充空数据
                for(int i=0;i<minNum-details.size();i++){
                    XWPFTableRow insertNewTableRow = table.insertNewTableRow(startRow+details.size()+i);
                    //新行高度
                    insertNewTableRow.getCtRow().addNewTrPr().addNewTrHeight().setVal(BigInteger.valueOf(height + rowHeight));
                    for (int j = 0; j < colNum; j++) {
                        XWPFTableCell cell = insertNewTableRow.createCell();
                    }
                }
            }
        }
    }
}
