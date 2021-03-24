package com.cmcu.mcc.comm.exportDoc;

import com.cmcu.common.dto.CommonCodeDto;
import com.cmcu.common.entity.CommonCode;
import com.cmcu.mcc.dto.ActHistoryDto;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.MiniTableRenderData;
import com.deepoove.poi.data.NumbericRenderData;
import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.data.TextRenderData;
import com.deepoove.poi.data.style.Style;
import com.deepoove.poi.data.style.TableStyle;
import org.apache.commons.lang3.StringUtils;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ProjectName: mcc
 * @Package: com.cmcu.mcc.comm.exportDoc
 * @ClassName: ExportUtils
 * @Author: Administrator
 * @Description: 导出world 工具类
 * @Date: 2019/9/6 17:21
 * @Version: 1.0
 */
public class ExportUtils {
    /**
     * 生成流程表格  word 标签{{%}}
     * @param nodes 流程历史节点
     * @param tableWidth 生成表格宽度
     * @return
     */
    public static MiniTableRenderData getFlowTable( List<ActHistoryDto> nodes,float tableWidth) {
        List<RowRenderData> rows = new ArrayList<>();

        for (int i = 0; i < nodes.size(); i++) {
            if (i % 2 == 0) {
                if(StringUtils.isEmpty(nodes.get(i+1).getActivityName())){
                    nodes.get(i+1).setActivityName("    ");
                    nodes.get(i+1).setUserName("      ");
                    nodes.get(i+1).setEndTime("       ");
                }
                RowRenderData row = RowRenderData.build(nodes.get(i).getActivityName(), nodes.get(i).getUserName() + "  " + nodes.get(i).getEndTime(),
                        nodes.get(i + 1).getActivityName(), nodes.get(i + 1).getUserName() + "  " + nodes.get(i + 1).getEndTime());
                rows.add(row);
            }
        }
        MiniTableRenderData miniTableRenderData = new MiniTableRenderData(rows);
        //表格宽度 （cm）
        miniTableRenderData.setWidth(tableWidth);
        TableStyle flowStyle = new TableStyle();
        flowStyle.setAlign(STJc.CENTER);
        miniTableRenderData.setStyle(flowStyle);

        return miniTableRenderData;
    }

    /**
     * 得到选择框（字典数据）内容
     * @param list 字典列表 \t空格，策略换行
     * @param targetText 勾选文字
     * @param flag 是否多选（true-多选）
     * @return
     */
    public static String getCheckText(List<CommonCodeDto> list, String targetText, Boolean flag) {
        List<Map> results = new ArrayList<>();
        if(flag){
            for(CommonCode dto:list){
                Map map=new HashMap();
                map.put("text",dto.getName());
                if(targetText.contains(dto.getCode())) {
                    map.put("select", " ✔ \t");
                }else{
                    map.put("select", "     \t");
                }
                results.add(map);
            }
        }else {
            for (CommonCode dto : list) {
                Map map = new HashMap();
                map.put("text", dto.getName());
                if (dto.getCode().equals(targetText)) {
                    map.put("select", " ✔   \t");
                } else {
                    map.put("select", "     \t");
                }
                results.add(map);
            }}
        String result = "";
        for (Map map : results) {
            result = result + map.get("text").toString() + map.get("select");
        }
        return result;
    }
    public static String getCheckStringText(List<String> list,String targetText,Boolean flag) {
        List<Map> results = new ArrayList<>();
        if(flag){
            for(String string:list){
                Map map=new HashMap();
                map.put("text",string);
                if(targetText.indexOf(string)>0) {
                    map.put("select", " ✔ \t");
                }else{
                    map.put("select", "     \t");
                }
                results.add(map);
            }
        }else {
            for (String string : list) {
                Map map = new HashMap();
                map.put("text", string);
                if (string.equals(targetText)) {
                    map.put("select", " ✔   \t");
                } else {
                    map.put("select", "     \t");
                }
                results.add(map);
            }}
        String result = "";
        for (Map map : results) {
            result = result + map.get("text").toString() + map.get("select");
        }
        return result;
    }

    /**
     * 得到 列表数
     * @param list 已查询数据
     * @return
     */
    public static NumbericRenderData getList(List<String> list,String type) {
        NumbericRenderData numbericRenderData = new NumbericRenderData(new ArrayList<TextRenderData>() {
            {
                for(int i=0;i<list.size();i++) {
                    add(new TextRenderData(list.get(i)));
                }
            }
        });
        //1),2),3)
        //1. 2. 3.
        if(type == "1.") {
            numbericRenderData.setNumFmt(NumbericRenderData.FMT_DECIMAL);
        }
        if(type == "1)"){
            numbericRenderData.setNumFmt(NumbericRenderData.FMT_DECIMAL_PARENTHESES);
        }
        if(type == "●"){
            numbericRenderData.setNumFmt(NumbericRenderData.FMT_BULLET);
        }
        if(type == "a."){
            numbericRenderData.setNumFmt(NumbericRenderData.FMT_LOWER_LETTER);
        }
        if(type == "A."){
            numbericRenderData.setNumFmt(NumbericRenderData.FMT_UPPER_LETTER);
        }
        if(type == "Ⅰ"){
            numbericRenderData.setNumFmt(NumbericRenderData.FMT_UPPER_ROMAN);
        }
        if(type ==""){
            numbericRenderData.setNumFmt(NumbericRenderData.FMT_BULLET);
        }
        return numbericRenderData;
    }

    public static void sendResponse(HttpServletResponse response, String fileName, XWPFTemplate xwpfTemplate) throws IOException {
        response.setContentType("application/csv");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "utf-8"));
        xwpfTemplate.write(response.getOutputStream());
        xwpfTemplate.close();
    }

    public static TextRenderData getUnderLine(String text) {
        Style textStyle = new Style();
        textStyle.setBold(false);//加粗
        textStyle.setStrike(false);//删除线
        textStyle.setItalic(false);//斜体
        textStyle.setColor("000000");//颜色
        textStyle.setUnderLine(true);//下划线
        textStyle.setFontFamily("黑体");//字体
       // textStyle.setFontSize(12);//字号
        TextRenderData textRenderData = new TextRenderData("00FF00", text);
        textRenderData.setStyle(textStyle);

        return  textRenderData;

    }
}

