package com.cmcu.mcc.business.web;

import com.common.model.JsonData;
import com.cmcu.common.util.JsonMapper;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.business.dto.BusinessContractDto;
import com.cmcu.mcc.business.service.BusinessContractService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Map;

@RestController
@RequestMapping("/business/contract")
public class BusinessContractController {


    @Autowired
    BusinessContractService businessContractService;

    @PostMapping("/loadPagedData.json")
    public JsonData loadPagedData(String userLogin,String uiSref,int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        return JsonData.success(businessContractService.listPagedData(params,userLogin,uiSref,pageNum, pageSize));
    }

    @PostMapping("/update.json")
    public JsonData update(@RequestBody BusinessContractDto item) {
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()), "操作人不能为空!");
        businessContractService.update(item);
        return JsonData.success();
    }

    @PostMapping("/saveCustomer.json")
    public JsonData saveCustomer(@RequestBody BusinessContractDto item, int id) {
        businessContractService.saveCustomer(item, id);
        return JsonData.success();
    }

    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id) {
        return JsonData.success(businessContractService.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin,String uiSref) {
        return JsonData.success(businessContractService.getNewModel(userLogin,uiSref));
    }

    @PostMapping("/remove.json")
    public JsonData remove(int id, String userLogin) {
        businessContractService.remove(id, userLogin);
        return JsonData.success();
    }


    @PostMapping("/getNewContractNo.json")
    public JsonData getNewContractNo(int id) {
        String contractNo = businessContractService.getNewContractNo(id);
        return JsonData.success(contractNo);
    }

    @PostMapping("/listBusinessBidContract.json")
    public JsonData listBusinessBidContract(int id) {
        return JsonData.success(businessContractService.listBusinessBidContract(id));
    }

    @PostMapping("/listAttendPagedData.json")
    public JsonData listAttendPagedData(String userLogin, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        return JsonData.success(businessContractService.listAttendPagedData(params, userLogin, pageNum, pageSize));
    }
    @PostMapping("/listAllPagedData.json")
    public JsonData listAllPagedData( int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        return JsonData.success(businessContractService.listAllPagedData(params, pageNum, pageSize));
    }
    @PostMapping("/getNewModelById.json")
    public JsonData getNewModelById(String userLogin, int bidContractId) {
        return JsonData.success(businessContractService.getNewModelById(userLogin,bidContractId));
    }

    @PostMapping("/statisticByUserLogin.json")
    public JsonData statisticByUserLogin(String userLogin, String searchTime) {
        return JsonData.success(businessContractService.statisticByUserLogin(userLogin,searchTime));
    }

    @RequestMapping("/downloadModel.json")
    public void downloadModel(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        String classpath = this.getClass().getResource("/").getPath().replaceFirst("/", "");
        String webappRoot = classpath.replaceAll("WEB-INF/classes/", "");
        String filePath = webappRoot + "assets/doc/导出模板/合同台账.xls";
        downloadingFile(filePath, "合同台账模板.xls", response);
    }

    private void downloadingFile(String filePath, String fileName, HttpServletResponse response) throws IOException {
        File file = new File(filePath);
        if (file.exists() && file.length() > 0) {
            byte[] data = FileCopyUtils.copyToByteArray(file);
            response.reset();
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "utf-8").replace("+", "%20"));
            response.addHeader("Content-Length", "" + data.length);
            response.setContentType("multipart/form-data;charset=UTF-8");
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            outputStream.write(data);
            outputStream.flush();
            outputStream.close();
        }
    }

    /**
     * 上传文件
     *
     * @param multipartFile
     * @return
     * @throws IOException
     */
    @RequestMapping("/receive.json")
    public JsonData receive(MultipartFile multipartFile, String userLogin) throws IOException {
        String fileName = multipartFile.getOriginalFilename();
        String msg="";
        if (!fileName.endsWith(".xls")) {
            Assert.state(false, "请将文件格式转换为 .xls");
        } else {
            msg=businessContractService.insertByFile(multipartFile.getInputStream(),userLogin);
        }
        return JsonData.success("",msg);
    }

    /**
     * 返回字符串格式兼容IE9
     *
     * @param multipartFile
     * @return
     * @throws IOException
     */
    @RequestMapping("/receive.do")
    public String receives(MultipartFile multipartFile, String userLogin) throws IOException {
        JsonData result = receive(multipartFile, userLogin);
        return JsonMapper.obj2String(result);
    }


    @RequestMapping("/downloadModelExcel.json")
    public void  downloadModelExcel(final HttpServletRequest request, final HttpServletResponse response,String userLogin) throws IOException {
        String fileName="合同台账.xls";
        HSSFWorkbook hssfWorkbook = businessContractService.downloadExcel(userLogin);
        response.reset();
        response.setContentType("application/csv");
        response.setHeader("Content-Disposition", "attachment; filename=" +  URLEncoder.encode(fileName, "utf-8"));
        hssfWorkbook.write(response.getOutputStream());
    }

    @RequestMapping("/downloadModelSimpleExcel.json")
    public void  downloadModelSimpleExcel(final HttpServletRequest request, final HttpServletResponse response,String userLogin) throws IOException {
        String fileName="合同台账(精简).xls";
        HSSFWorkbook hssfWorkbook = businessContractService.downloadSimpleExcel(userLogin);
        response.reset();
        response.setContentType("application/csv");
        response.setHeader("Content-Disposition", "attachment; filename=" +  URLEncoder.encode(fileName, "utf-8"));
        hssfWorkbook.write(response.getOutputStream());
    }
}
