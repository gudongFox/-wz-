package com.cmcu.mcc.sys.service;

import com.cmcu.common.util.MyDateUtil;
import com.cmcu.mcc.business.entity.BusinessCustomer;
import com.cmcu.mcc.sys.dao.SysRequestMapper;
import com.cmcu.mcc.sys.entity.SysRequest;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created with IntelliJ IDEA.
 * User: Roy
 * Time: 2019/2/22 16:27
 * To change this template use File | Settings | File Templates.
 */
@Service
public class SysRequestService {

    @Resource
    SysRequestMapper sysRequestMapper;

    public PageInfo<SysRequest> listPagedData(String q,int pageNum,int pageSize){

        Map<String,Object> params= Maps.newHashMap();
        params.put("q",q);
        PageInfo<SysRequest> pageInfo= PageHelper.startPage(pageNum,pageSize).doSelectPageInfo(()->{
            sysRequestMapper.selectAll(params);
        });

        return pageInfo;
    }

    /**
     * 获取系统管理员 操作日志
     * @param q
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo<SysRequest> listSysAdminData(String q,int pageNum,int pageSize){

        Map<String,Object> params= Maps.newHashMap();
        params.put("userLogin","admin");
        params.put("q",q);
        PageInfo<SysRequest> pageInfo= PageHelper.startPage(pageNum,pageSize).doSelectPageInfo(()->{
            sysRequestMapper.selectAll(params);
        });

        return pageInfo;
    }
    /**
     * 获取系统安全员 操作日志
     * @param q
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo<SysRequest> listSafetyData(String q,int pageNum,int pageSize){

        Map<String,Object> params= Maps.newHashMap();
        params.put("userLogin","safety");
        params.put("q",q);
        PageInfo<SysRequest> pageInfo= PageHelper.startPage(pageNum,pageSize).doSelectPageInfo(()->{
            sysRequestMapper.selectAll(params);
        });

        return pageInfo;
    }
    /**
     * 获取系统审计员 操作日志
     * @param q
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo<SysRequest> listAuditData(String q,int pageNum,int pageSize){
        Map<String,Object> params= Maps.newHashMap();
        params.put("userLogin","audit");
        params.put("q",q);

        PageInfo<SysRequest> pageInfo= PageHelper.startPage(pageNum,pageSize).doSelectPageInfo(()->{
            sysRequestMapper.selectAll(params);
        });

        return pageInfo;
    }

    /**
     * 获取用户审计日志 操作日志
     * @param q
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo<SysRequest> listUserData(String q,int pageNum,int pageSize){
        Map<String,Object> params= Maps.newHashMap();
        params.put("q",q);
        params.put("request","five");
        PageInfo<SysRequest> pageInfo= PageHelper.startPage(pageNum,pageSize).doSelectPageInfo(()->{
            sysRequestMapper.selectAll(params);
        });

        return pageInfo;
    }

    //导出数据
    public HSSFWorkbook downloadExcel(String key) throws IOException {
        Map<String,Object> params= Maps.newHashMap();
        if ("系统管理员".equals(key)){
            params.put("userLogin","admin");
        }else if ("系统安全员".equals(key)){
            params.put("userLogin","safety");
        }else if ("系统审计员".equals(key)){
            params.put("userLogin","audit");
        }
        List<SysRequest> sysRequests = sysRequestMapper.selectAll(params);

        String classpath = this.getClass().getResource("/").getPath().replaceFirst("/", "");
        String webappRoot = classpath.replaceAll("WEB-INF/classes/", "");
        String filePath = webappRoot + "assets/doc/导出模板/系统日志.xls";
        InputStream in = new FileInputStream(filePath);
        HSSFWorkbook workbook = new HSSFWorkbook(in);
        HSSFSheet sheet = workbook.getSheetAt(0);
        HSSFCellStyle cellStyle = sheet.getRow(0).getCell(0).getCellStyle();
        List<Map> list = new ArrayList<>();
        int rowIndex = 1;//起始行
        for (int i=0;i<sysRequests.size();i++){
            HSSFRow row = sheet.createRow(rowIndex++);

            HSSFCell cell = row.createCell(0);
            cell.setCellValue(sysRequests.get(i).getRequestUrl());
            row.setRowStyle(cellStyle);

            HSSFCell cell1 = row.createCell(1);
            cell1.setCellValue(sysRequests.get(i).getRequestMethod());
            row.setRowStyle(cellStyle);

            HSSFCell cell2 = row.createCell(2);
            cell2.setCellValue(MyDateUtil.dateToStrLong(sysRequests.get(i).getFinishTime()));
            row.setRowStyle(cellStyle);

            HSSFCell cell3 = row.createCell(3);
            cell3.setCellValue(sysRequests.get(i).getRequestSecond()+"s");
            row.setRowStyle(cellStyle);

            HSSFCell cell4 = row.createCell(4);
            cell4.setCellValue(sysRequests.get(i).getRequestParameter());
            row.setRowStyle(cellStyle);

            HSSFCell cell5 = row.createCell(5);
            cell5.setCellValue(sysRequests.get(i).getRequestIp());
            row.setRowStyle(cellStyle);

            HSSFCell cell6 = row.createCell(6);
            cell6.setCellValue(sysRequests.get(i).getRequestLogin());
            row.setRowStyle(cellStyle);

        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        workbook.write(os);
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        byte[] data = IOUtils.toByteArray(is);
        return workbook;
    }



}
