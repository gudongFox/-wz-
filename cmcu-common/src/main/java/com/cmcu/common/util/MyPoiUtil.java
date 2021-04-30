package com.cmcu.common.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Roy
 * Time: 2017/12/26 13:53
 * To change this template use File | Settings | File Templates.
 */
public class MyPoiUtil {


    static Logger logger= LoggerFactory.getLogger(MyPoiUtil.class);



    public static Map listSheetData(InputStream inputStream,int headRowIndex,boolean removeBlank) throws IOException {
        Map data=Maps.newLinkedHashMap();
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
        for(int s=0;s<workbook.getNumberOfSheets();s++) {
            if(!workbook.isSheetHidden(s)) {
                Sheet sheet = workbook.getSheetAt(s);
                List<Map> list = new ArrayList<>();
                int cellNum = sheet.getRow(headRowIndex).getLastCellNum();

                List<String> heads = Lists.newArrayList();
                Row headRow = sheet.getRow(headRowIndex);
                for (int i = 0; i < headRow.getLastCellNum(); i++) {
                    if (headRow.getCell(i) != null) {
                        heads.add(headRow.getCell(i).getStringCellValue());
                    }
                }

                for (int i = headRowIndex + 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (row == null) {
                        break;
                    }
                    if (row.getCell(0) == null || row.getCell(0).getCellType().equals(CellType.BLANK)) {
                        break;
                    }

                    LinkedHashMap map = Maps.newLinkedHashMap();
                    for (int j = 0; j < cellNum; j++) {

                        String key = j + "";
                        if (j < heads.size()) {
                            key = heads.get(j);
                        }

                        Cell cell = row.getCell(j);
                        if (cell == null) {
                            map.put(key, "");
                        } else {
                            CellType cellType = cell.getCellType();
                            if (cellType == CellType.STRING) {
                                String value = cell.getStringCellValue().trim();
                                if (removeBlank) {
                                    value = StringUtils.deleteWhitespace(value);
                                }
                                map.put(key, value);
                            } else if (cellType == CellType.NUMERIC) {
                                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                                    map.put(key, DateFormatUtils.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()), "yyyy-MM-dd"));
                                } else {
                                    String sValue=String.valueOf(cell.getNumericCellValue());
                                    //对于类似电话号码或手机一类的大数值读取问题,多个E
                                    if (sValue.indexOf("E") > 0) {
                                        map.put(key, new DecimalFormat("#").format(cell.getNumericCellValue()));
                                    } else {
                                        if(sValue.endsWith(".0")){
                                            map.put(key,Integer.parseInt(sValue.substring(0,sValue.length()-2)));
                                        }else {
                                            map.put(key, cell.getNumericCellValue());
                                        }
                                    }
                                }
                            } else if (cellType == CellType.BOOLEAN) {
                                map.put(j, cell.getBooleanCellValue());
                            } else if (cellType == cellType.FORMULA) {
                                try {
                                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                                        map.put(key, DateFormatUtils.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()), "yyyy-MM-dd"));
                                    } else {
                                        map.put(key, cell.getNumericCellValue());
                                    }
                                } catch (Exception ex) {
                                    map.put(key, String.valueOf(cell.getRichStringCellValue()));
                                }
                            } else {
                                map.put(key, "");
                            }
                        }
                    }
                    list.add(map);
                }
                if (list.size() > 0) {
                    data.put(sheet.getSheetName(), list);
                }
            }
        }
        return data;
    }


    /**
     * 即将废弃
     * @param inputStream
     * @param rowIndex
     * @param sheetAt
     * @param removeBlank
     * @return
     * @throws IOException
     */
    public static List<Map> listTableData(InputStream inputStream,int rowIndex,int sheetAt,boolean removeBlank) throws IOException {
        List<Map> list = new ArrayList<>();
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(sheetAt);
        int cellNum=sheet.getRow(0).getLastCellNum();

        for (int i = rowIndex; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if(row==null){
                break;
            }
            if(row.getCell(0)==null||row.getCell(0).getCellType().equals(CellType.BLANK)){
                break;
            }

            LinkedHashMap map = Maps.newLinkedHashMap();
            for (int j = 0; j < cellNum; j++) {
                Cell cell = row.getCell(j);
                if(cell==null){
                    map.put(j, "");
                }
                else{
                    CellType cellType = cell.getCellType();
                    if (cellType == CellType.STRING) {
                        String value=cell.getStringCellValue().trim();
                        if(removeBlank) {
                            value = StringUtils.deleteWhitespace(value);
                        }
                        map.put(j,value);
                    } else if (cellType == CellType.NUMERIC) {
                        if (HSSFDateUtil.isCellDateFormatted(cell)) {
                            map.put(j, DateFormatUtils.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()),"yyyy-MM-dd"));
                        } else {
                            //对于类似电话号码或手机一类的大数值读取问题,多个E
                            //new DecimalFormat("#").format(cellValue);
                            //map.put(j, cell.getNumericCellValue());
                            String result=BigDecimal.valueOf(cell.getNumericCellValue()).toPlainString();
                            map.put(j, BigDecimal.valueOf(cell.getNumericCellValue()).toPlainString());
                        }
                    } else if (cellType == CellType.BOOLEAN) {
                        map.put(j, cell.getBooleanCellValue());
                    } else if (cellType == cellType.FORMULA) {
                        try {
                            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                                map.put(j, DateFormatUtils.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()), "yyyy-MM-dd"));
                            } else {
                                map.put(j,cell.getNumericCellValue());
                            }
                        }catch (Exception ex) {
                            map.put(j,String.valueOf(cell.getRichStringCellValue()));
                        }
                    } else {
                        map.put(j, "");
                    }
                }
            }
            list.add(map);
        }
        return list;
    }


    /**
     * 读取excel转为数据列表
     * @param inputStream 文件流
     * @param rowIndex 第几行开始（0--)
     * @param sheetAt 第几个sheet
     * @return
     * @throws IOException
     */
    public static List<Map> listTableDataNew(InputStream inputStream,int rowIndex,int sheetAt,boolean removeBlank) throws IOException {
        List<Map> list = new ArrayList<>();
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(sheetAt);
        int cellNum=sheet.getRow(0).getLastCellNum();


        for (int i = rowIndex; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if(row==null){
                break;
            }
            if(row.getCell(0)==null||row.getCell(0).getCellType().equals(CellType.BLANK)){
                break;
            }

            LinkedHashMap map = Maps.newLinkedHashMap();
            for (int j = 0; j < cellNum; j++) {
                String columnName=sheet.getRow(0).getCell(j).toString();
                Cell cell = row.getCell(j);
                if(cell==null){
                    map.put(columnName, "");
                }
                else{
                    CellType cellType = cell.getCellType();
                    if (cellType == CellType.STRING) {
                        String value=cell.getStringCellValue().trim();
                        if(removeBlank) {
                            //value = StringUtils.deleteWhitespace(value);
                            value=value.trim();
                        }
                        map.put(columnName,value);
                    } else if (cellType == CellType.NUMERIC) {
                        if (HSSFDateUtil.isCellDateFormatted(cell)) {
                            map.put(columnName, DateFormatUtils.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()),"yyyy-MM-dd"));
                        } else {
                            //对于类似电话号码或手机一类的大数值读取问题,多个E
                            //new DecimalFormat("#").format(cellValue);
                            //map.put(j, cell.getNumericCellValue());
                            map.put(columnName, BigDecimal.valueOf(cell.getNumericCellValue()).toPlainString());
                        }
                    } else if (cellType == CellType.BOOLEAN) {
                        map.put(columnName, cell.getBooleanCellValue());
                    } else if (cellType == cellType.FORMULA) {
                        try {
                            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                                map.put(columnName, DateFormatUtils.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()), "yyyy-MM-dd"));
                            } else {
                                map.put(columnName,cell.getNumericCellValue());
                            }
                        }catch (Exception ex) {
                            map.put(columnName,String.valueOf(cell.getRichStringCellValue()));
                        }
                    } else {
                        map.put(columnName, "");
                    }
                }
            }
            list.add(map);
        }
        return list;
    }




    /**
     * 将map转为excel数据
     * @param list
     * @return
     * @throws IOException
     */
    public static InputStream buildInputStream(List<Map> list) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFCellStyle headCellStyle = getHeadCellStyle(workbook);
        HSSFCellStyle cellStyle = getCellStyle(workbook);

        HSSFSheet sheet = workbook.createSheet(DateFormatUtils.format(new Date(), "yyyyMMddHHmm"));

        if (list.size() > 0) {
            Map first = list.get(0);
            Object[] keys = first.keySet().toArray();
            int columnCount = keys.length+1;
            HSSFRow headerRow = sheet.createRow(0);
            for (int i = 0; i < columnCount; i++) {
                if(i==0){
                    HSSFCell cell = headerRow.createCell(i);
                    cell.setCellValue("序号");
                    cell.setCellStyle(headCellStyle);
                }else{
                    HSSFCell cell = headerRow.createCell(i);
                    cell.setCellValue(keys[i-1].toString());
                    cell.setCellStyle(headCellStyle);
                }

            }
            //表头的高度
            sheet.getRow(0).setHeight(Short.valueOf(2*300+""));

            int rowIndex = 1;
            for (int j=0;j<list.size();j++) {
                HSSFRow row = sheet.createRow(rowIndex++);
                //表单内容高度
                row.setHeight(Short.valueOf(400+""));
                for (int i = 0; i < columnCount; i++) {
                    //设置序号
                    if(i==0){
                        HSSFCell cell = row.createCell(i);
                        cell.setCellValue(j+1);
                        cell.setCellStyle(cellStyle);
                    }else{
                        Object v=list.get(j).getOrDefault(keys[i-1],"");
                        HSSFCell cell = row.createCell(i);
                        cell.setCellValue(v==null?"":v.toString());
                        cell.setCellStyle(cellStyle);
                    }
                }
            }
            //设置列宽
            for (int i = 0; i < columnCount; i++) {
                //sheet.setColumnWidth(i, 25 * 256);
                //宽度自适应
                //sheet.autoSizeColumn(i, true);
                int columnWidthMax = sheet.getColumnWidth(i) / 256;
                for (int row = 0; row < rowIndex; ++row) {
                    Cell cell = sheet.getRow(row).getCell(i);//如果有合并单元格，就会有getCell()==null的情况，需要createCell();
                    if (cell == null) {
                        cell = sheet.getRow(row).createCell(i);
                    }
                    int length  = cell.toString().getBytes("GBK").length;
                    if (columnWidthMax < length + 1) {
                        columnWidthMax = length + 1;
                    }
                }
                //如果最大超过 80
                if(columnWidthMax>80){
                    sheet.setColumnWidth(i, 80 * 256); //对中文列调整列宽
                }else {
                    sheet.setColumnWidth(i, columnWidthMax * 256+550); //对中文列调整列宽
                }
            }
        }
        workbook.write(os);
        //设置导出格式
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        os.close();
        is.close();
        return is;
    }

    /**
     * 将map转为excel数据 多sheet
     * @param data 包含 deptName list
     * @return
     * @throws IOException
     */
    public static InputStream buildInputStream2(List<Map> data) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFCellStyle headCellStyle = getHeadCellStyle(workbook);
        HSSFCellStyle cellStyle = getCellStyle(workbook);
        for(Map map:data){
            HSSFSheet sheet = workbook.createSheet(map.getOrDefault("deptName","其他").toString());
            List<Map> list = (List<Map>)map.get("list");
            if (list.size() > 0) {
                Map first = list.get(0);
                Object[] keys = first.keySet().toArray();
                int columnCount = keys.length+1;

                HSSFRow headerRow = sheet.createRow(0);
                for (int i = 0; i < columnCount; i++) {
                    if(i==0){
                        HSSFCell cell = headerRow.createCell(i);
                        cell.setCellValue("序号");
                        cell.setCellStyle(headCellStyle);
                    }else{
                        HSSFCell cell = headerRow.createCell(i);
                        cell.setCellValue(keys[i-1].toString());
                        cell.setCellStyle(headCellStyle);
                    }

                }
                //表头的高度
                sheet.getRow(0).setHeight(Short.valueOf(2*300+""));

                int rowIndex = 1;
                for (int j=0;j<list.size();j++) {
                    HSSFRow row = sheet.createRow(rowIndex++);
                    //表单内容高度
                    row.setHeight(Short.valueOf(400+""));
                    for (int i = 0; i < columnCount; i++) {
                        //设置序号
                        if(i==0){
                            HSSFCell cell = row.createCell(i);
                            cell.setCellValue(j+1);
                            cell.setCellStyle(cellStyle);
                        }else{
                            Object v=list.get(j).getOrDefault(keys[i-1],"");
                            HSSFCell cell = row.createCell(i);
                            cell.setCellValue(v==null?"":v.toString());
                            cell.setCellStyle(cellStyle);
                        }
                    }
                }
                //设置列宽
                for (int i = 0; i < columnCount; i++) {
                    //sheet.setColumnWidth(i, 25 * 256);
                    //宽度自适应
                    //sheet.autoSizeColumn(i, true);
                    int columnWidthMax = sheet.getColumnWidth(i) / 256;
                    for (int row = 0; row < rowIndex; ++row) {
                        Cell cell = sheet.getRow(row).getCell(i);//如果有合并单元格，就会有getCell()==null的情况，需要createCell();
                        if (cell == null) {
                            cell = sheet.getRow(row).createCell(i);
                        }
                        int length  = cell.toString().getBytes("GBK").length;
                        if (columnWidthMax < length + 1) {
                            columnWidthMax = length + 1;
                        }
                    }
                    //如果最大超过 80
                    if(columnWidthMax>80){
                        sheet.setColumnWidth(i, 80 * 256); //对中文列调整列宽
                    }else {
                        sheet.setColumnWidth(i, columnWidthMax * 256+550); //对中文列调整列宽
                    }
                }
            }
        }

        workbook.write(os);
        //设置导出格式
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        os.close();
        is.close();
        return is;
    }

    public  static InputStream buildMultipleSheetInputStream(Map data,boolean addIndex) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFCellStyle headCellStyle = getHeadCellStyle(workbook);
        HSSFCellStyle cellStyle = getCellStyle(workbook);
        for(Object key:data.keySet()) {
            HSSFSheet sheet = workbook.createSheet(key.toString());
            List<Map> list= (List<Map>) data.get(key);
            if (list.size() > 0) {
                Map first = list.get(0);
                Object[] keys = first.keySet().toArray();
                int columnCount = keys.length + (addIndex?1:0);

                HSSFRow headerRow = sheet.createRow(0);
                for (int i = 0; i < columnCount; i++) {
                    if (addIndex&&i == 0) {
                        HSSFCell cell = headerRow.createCell(i);
                        cell.setCellValue("#");
                        cell.setCellStyle(headCellStyle);
                    } else {
                        HSSFCell cell = headerRow.createCell(i);
                        cell.setCellValue(keys[i - (addIndex?1:0)].toString());
                        cell.setCellStyle(headCellStyle);
                    }
                }
                //表头的高度
                sheet.getRow(0).setHeight(Short.valueOf(2 * 300 + ""));

                int rowIndex = 1;
                for (int j = 0; j < list.size(); j++) {
                    HSSFRow row = sheet.createRow(rowIndex++);
                    //表单内容高度
                    row.setHeight(Short.valueOf(400 + ""));
                    for (int i = 0; i < columnCount; i++) {
                        //设置序号
                        if (addIndex&&i == 0) {
                            HSSFCell cell = row.createCell(i);
                            cell.setCellValue(j + 1);
                            cell.setCellStyle(cellStyle);
                        } else {
                            Object v = list.get(j).getOrDefault(keys[i - (addIndex?1:0)], "");
                            HSSFCell cell = row.createCell(i);
                            cell.setCellValue(v == null ? "" : v.toString());
                            cell.setCellStyle(cellStyle);
                        }
                    }
                }
                //设置列宽
                for (int i = 0; i < columnCount; i++) {
                    //sheet.setColumnWidth(i, 25 * 256);
                    //宽度自适应
                    //sheet.autoSizeColumn(i, true);
                    int columnWidthMax = sheet.getColumnWidth(i) / 256;
                    for (int row = 0; row < rowIndex; ++row) {
                        Cell cell = sheet.getRow(row).getCell(i);//如果有合并单元格，就会有getCell()==null的情况，需要createCell();
                        if (cell == null) {
                            cell = sheet.getRow(row).createCell(i);
                        }
                        int length = cell.toString().getBytes("GBK").length;
                        if (columnWidthMax < length + 1) {
                            columnWidthMax = length + 1;
                        }
                    }
                    //如果最大超过 80
                    if (columnWidthMax > 80) {
                        sheet.setColumnWidth(i, 80 * 256); //对中文列调整列宽
                    } else {
                        sheet.setColumnWidth(i, columnWidthMax * 256 + 550); //对中文列调整列宽
                    }
                }
            }
        }
        workbook.write(os);
        //设置导出格式
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        os.close();
        is.close();
        return is;

    }


    //表头样式
    public static HSSFCellStyle getHeadCellStyle(HSSFWorkbook workbook) {

        // 生成一个样式
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置这些样式
        style.setAlignment(HorizontalAlignment.CENTER);//水平居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        // 背景色
        style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
       // style.setFillBackgroundColor(IndexedColors.DARK_GREEN.getIndex());

        // 设置边框
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        // 自动换行
        style.setWrapText(false);

        // 生成一个字体
        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 10);
        //font.setColor(HSSFColor.RED.index);
        font.setBold(true);
        font.setFontName("宋体");

        // 把字体 应用到当前样式
        style.setFont(font);

        return  style;
    }
    //表单
    public static HSSFCellStyle getCellStyle(HSSFWorkbook workbook) {

        // 生成一个样式
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置这些样式
        style.setAlignment(HorizontalAlignment.CENTER);//水平居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        // 背景色
       // style.setFillForegroundColor(IndexedColors.DARK_GREEN.getIndex());
        //style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        // style.setFillBackgroundColor(IndexedColors.DARK_GREEN.getIndex());

        // 设置边框
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        // 自动换行
        style.setWrapText(false);

        // 生成一个字体
        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 10);
        //font.setColor(HSSFColor.RED.index);
        font.setBold(false);
        font.setFontName("宋体");

        // 把字体 应用到当前样式
        style.setFont(font);

        return  style;
    }



    /**
     * 导出数据
     * @param list
     * @param excelName
     * @param response
     */
    public static void downloadExcel(List<Map> list, String excelName, HttpServletResponse response) {
        try {
            InputStream inputStream = buildInputStream(list);
                response.reset();
            byte[] data = IOUtils.toByteArray(inputStream);
            String fileName = URLEncoder.encode(excelName, "utf-8");
            response.reset();
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            response.addHeader("Content-Length", "" + data.length);
            response.setContentType("application/vnd.ms-excel");
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            outputStream.write(data);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static void downloadExcel(Map sheetData,boolean addIndex,String excelName, HttpServletResponse response) {
        try {
            InputStream inputStream = buildMultipleSheetInputStream(sheetData,addIndex);
            response.reset();
            byte[] data = IOUtils.toByteArray(inputStream);
            String fileName = URLEncoder.encode(excelName, "utf-8");
            response.reset();
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            response.addHeader("Content-Length", "" + data.length);
            response.setContentType("application/vnd.ms-excel");
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            outputStream.write(data);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static Map<Integer,String> sortHashMap(Map<Integer,String> map){
        Map<Integer,String> sortedMap = new LinkedHashMap<>();
        List<Integer> list = new ArrayList<>();
        Iterator<Integer> item = map.keySet().iterator();
        while(item.hasNext()){
            list.add(item.next());
        }
        Collections.sort(list);
        Iterator<Integer> item2 = list.iterator();
        while(item2.hasNext()){
            Integer key = item2.next();
            sortedMap.put(key,map.get(key));
        }
        return sortedMap;
    }
    public static void getFileByBytes(byte[] bytes, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {// 判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath + "\\" + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
