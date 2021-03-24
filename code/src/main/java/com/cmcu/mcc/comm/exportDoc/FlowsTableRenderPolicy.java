package com.cmcu.mcc.comm.exportDoc;

import com.deepoove.poi.NiceXWPFDocument;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.CellRenderData;
import com.deepoove.poi.data.MiniTableRenderData;
import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.data.TextRenderData;
import com.deepoove.poi.data.style.TableStyle;
import com.deepoove.poi.policy.AbstractRenderPolicy;
import com.deepoove.poi.policy.MiniTableRenderPolicy;
import com.deepoove.poi.policy.TextRenderPolicy;
import com.deepoove.poi.render.RenderContext;
import com.deepoove.poi.template.run.RunTemplate;
import com.deepoove.poi.util.ObjectUtils;
import com.deepoove.poi.util.StyleUtils;
import com.deepoove.poi.util.TableTools;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.math.BigInteger;
import java.util.List;

/**
 * @ProjectName: mcc
 * @Package: PACKAGE_NAME
 * @ClassName: flowsTableRenderPolicy
 * @Author: Administrator
 * @Description: 流程表格 策略
 * @Date: 2019/9/7 11:06
 * @Version: 1.0
 */
public class FlowsTableRenderPolicy extends AbstractRenderPolicy<MiniTableRenderData> {

    @Override
    protected boolean validate(MiniTableRenderData data) {
        if (!(data).isSetBody() && !(data).isSetHeader()) {
            logger.debug("Empty MiniTableRenderData datamodel: {}", data);
            return false;
        }
        return true;
    }

    @Override
    public void doRender(RunTemplate runTemplate, MiniTableRenderData data, XWPFTemplate template)
            throws Exception {
        NiceXWPFDocument doc = template.getXWPFDocument();
        XWPFRun run = runTemplate.getRun();

        if (!data.isSetBody()) {
            renderNoDataTable(doc, run, data);
        } else {
            renderTable(doc, run, data);
        }
    }

    @Override
    protected void afterRender(RenderContext context) {
        clearPlaceholder(context, true);
    }

    private void renderTable(NiceXWPFDocument doc, XWPFRun run, MiniTableRenderData tableData) {
        // 1.计算行和列
        int row = tableData.getDatas().size(), col = 0;
        if (!tableData.isSetHeader()) {
            col = getMaxColumFromData(tableData.getDatas());
        } else {
            row++;
            col = tableData.getHeader().size();
        }

        // 2.创建表格
        XWPFTable table = doc.insertNewTable(run, row, col);
        initBasicTable(table, col, tableData.getWidth(), tableData.getStyle());

        // 3.渲染数据
        int startRow = 0;
        if (tableData.isSetHeader())
            Helper.renderRow(table, startRow++, tableData.getHeader());
        for (RowRenderData data : tableData.getDatas()) {
            Helper.renderRow(table, startRow++, data);
        }

    }

    private void renderNoDataTable(NiceXWPFDocument doc, XWPFRun run,
                                   MiniTableRenderData tableData) {
        int row = 2, col = tableData.getHeader().size();

        XWPFTable table = doc.insertNewTable(run, row, col);
        initBasicTable(table, col, tableData.getWidth(), tableData.getStyle());

        com.deepoove.poi.policy.MiniTableRenderPolicy.Helper.renderRow(table, 0, tableData.getHeader());

        TableTools.mergeCellsHorizonal(table, 1, 0, tableData.getHeader().size() - 1);
        XWPFTableCell cell = table.getRow(1).getCell(0);
        cell.setText(tableData.getNoDatadesc());

    }

    private void initBasicTable(XWPFTable table, int col, float width, TableStyle style) {
        widthTable(table, width, col);
        TableTools.borderTable(table, 4);
        StyleUtils.styleTable(table, style);
    }
    /**
     * 表格设置宽度，每列固定分布
     *
     * @param table
     * @param widthCM
     * @param cols
     */
    public static void widthTable(XWPFTable table, float widthCM, int cols) {
        int width = (int)(widthCM/2.54*1440);
        CTTblPr tblPr = table.getCTTbl().getTblPr();
        if (null == tblPr) {
            tblPr = table.getCTTbl().addNewTblPr();
        }
        CTTblWidth tblW = tblPr.isSetTblW() ? tblPr.getTblW() : tblPr.addNewTblW();
        tblW.setType(0 == width ? STTblWidth.AUTO : STTblWidth.DXA);
        tblW.setW(BigInteger.valueOf(width));

        if (0 != width) {

            CTTblGrid tblGrid = table.getCTTbl().getTblGrid();
            if (null == tblGrid) {
                tblGrid = table.getCTTbl().addNewTblGrid();
            }

            for (int j = 0; j < cols; j++) {
                if(j==0||j==2){
                    CTTblGridCol addNewGridCol = tblGrid.addNewGridCol();
                    addNewGridCol.setW(BigInteger.valueOf(width * 15 /100));
                }
                if(j==1||j==3){
                    CTTblGridCol addNewGridCol = tblGrid.addNewGridCol();
                    addNewGridCol.setW(BigInteger.valueOf(width * 35 /100));
                }

            }
        }
    }
    private int getMaxColumFromData(List<RowRenderData> datas) {
        int maxColom = 0;
        for (RowRenderData obj : datas) {
            if (null == obj) continue;
            if (obj.size() > maxColom) maxColom = obj.size();
        }
        return maxColom;
    }

    public static class Helper {

        /**
         * 填充表格一行的数据
         *
         * @param table
         * @param row     第几行
         * @param rowData 行数据：确保行数据的大小不超过表格该行的单元格数量
         */
        public static void renderRow(XWPFTable table, int row, RowRenderData rowData) {
            CTTblBorders borders = table.getCTTbl().getTblPr().addNewTblBorders();
            CTBorder hBorder = borders.addNewTop();
            hBorder.setVal(STBorder.Enum.forString("none"));  // 线条类型--无边框
            hBorder.setSz(new BigInteger("0")); // 线条大小

            if (null == rowData || rowData.size() <= 0) return;
            XWPFTableRow tableRow = table.getRow(row);
            ObjectUtils.requireNonNull(tableRow, "Row " + row + " do not exist in the table");

            TableStyle rowStyle = rowData.getRowStyle();
            List<CellRenderData> cellList = rowData.getCellDatas();
            XWPFTableCell cell = null;

            for (int i = 0; i < cellList.size(); i++) {
                //设置每行高度
                tableRow.setHeight(400);
                //设置每列比例（列数固定为 4 ）
                cell = tableRow.getCell(i);
                if(i==0||i==2) {
                    cell.setWidthType(TableWidthType.PCT);
                    cell.setWidth(20 + "%");
                }
                if(i==1||i==3){
                    cell.setWidthType(TableWidthType.PCT);
                    cell.setWidth(30 + "%");
                }
                CTTc cttc = cell.getCTTc();
                CTTcPr ctPr = cttc.addNewTcPr();
                ctPr.addNewVAlign().setVal(STVerticalJc.CENTER);
                cttc.getPList().get(0).addNewPPr().addNewJc().setVal(STJc.CENTER);
                if (null == cell) {
                    logger.warn("Extra cell data at row {}, but no extra cell: col {}", row, cell);
                    break;
                }
                renderCell(cell, cellList.get(i), rowStyle);
            }
        }

        public static void renderCell(XWPFTableCell cell, CellRenderData cellData,
                                      TableStyle rowStyle) {
            TableStyle cellStyle = (null == cellData.getCellStyle() ? rowStyle
                    : cellData.getCellStyle());

            // 处理单元格样式
            if (null != cellStyle && null != cellStyle.getBackgroundColor()) {
                cell.setColor(cellStyle.getBackgroundColor());
            }

            TextRenderData renderData = cellData.getRenderData();
            String cellText = renderData.getText();
            if (StringUtils.isBlank(cellText)) return;

            // 处理单元格数据
            CTTc ctTc = cell.getCTTc();
            CTP ctP = (ctTc.sizeOfPArray() == 0) ? ctTc.addNewP() : ctTc.getPArray(0);
            XWPFParagraph par = new XWPFParagraph(ctP, cell);
            StyleUtils.styleTableParagraph(par, cellStyle);

            String text = renderData.getText();
            String[] fragment = text.split(TextRenderPolicy.REGEX_LINE_CHARACTOR, -1);

            if (fragment.length <= 1) {
                TextRenderPolicy.Helper.renderTextRun(par.createRun(), renderData);
            } else {
                // 单元格内换行的用不同段落来特殊处理
                XWPFRun run;
                for (int j = 0; j < fragment.length; j++) {
                    if (0 != j) {
                        par = cell.addParagraph();
                        StyleUtils.styleTableParagraph(par, cellStyle);
                    }
                    run = par.createRun();
                    StyleUtils.styleRun(run, renderData.getStyle());
                    run.setText(fragment[j]);
                }
            }
        }
    }


}
