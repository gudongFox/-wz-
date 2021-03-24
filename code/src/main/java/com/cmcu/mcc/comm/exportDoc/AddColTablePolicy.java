package com.cmcu.mcc.comm.exportDoc;

import com.deepoove.poi.policy.DynamicTableRenderPolicy;
import org.apache.poi.xwpf.usermodel.TableWidthType;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc;

import java.util.List;

/**
 * @ProjectName: mcc
 * @Package: com.cmcu.mcc.comm.exportDoc
 * @ClassName: AddColTablePolicy
 * @Author: Administrator
 * @Description: 新建策略 动态添加列
 * @Date: 2019/9/6 10:17
 * @Version: 1.0
 */
public class AddColTablePolicy extends DynamicTableRenderPolicy {

    // 填充数据行数
    int colNum ;
    //行头宽度 百分比
    List<Integer> rowHeadWidths ;

    public AddColTablePolicy(List<Integer> rowHeadWidths){
        this.rowHeadWidths = rowHeadWidths;
    }

    @Override
    public void render(XWPFTable table, Object data) {
        if (null == data) return;

        // 填充数据
        List<List<String>> details = (List<List<String>>) data;
        colNum=details.get(0).size();

        //设置 填充列 宽度（均分）
        int rowHeadWidth=0;
        for(Integer i:rowHeadWidths){
            rowHeadWidth=rowHeadWidth+i;
        }
        int cellWidth=(100-rowHeadWidth)/details.get(0).size();

        if (null != details) {
            for (int i = 0; i < details.size(); i++) {
                for( int j = 0;j < colNum;j++) {
                    XWPFTableCell cell = table.getRow(i).createCell();
                    cell.setText(details.get(i).get(j));
                    cell.setWidthType(TableWidthType.PCT);
                    cell.setWidth(cellWidth+"%");
                    //单元格内容居中
                    CTTc cttc = cell.getCTTc();
                    CTTcPr ctPr = cttc.addNewTcPr();
                    ctPr.addNewVAlign().setVal(STVerticalJc.CENTER);
                    cttc.getPList().get(0).addNewPPr().addNewJc().setVal(STJc.CENTER);
                }
            }
            //设置行头宽度
            for(int i=0;i<rowHeadWidths.size();i++){
                XWPFTableCell cell = table.getRow(0).getCell(i);
                cell.setWidthType(TableWidthType.PCT);
                cell.setWidth(rowHeadWidths.get(i)+"%");
            }
        }
    }
}
