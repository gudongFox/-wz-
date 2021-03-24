package com.cmcu.mcc.business.web;

import com.cmcu.common.JsonData;
import com.cmcu.common.util.JsonMapper;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.business.dto.BusinessRecordDto;
import com.cmcu.mcc.business.service.BusinessRecordService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
@RequestMapping("/business/record")
public class BusinessRecordController {
    @Autowired
    BusinessRecordService businessRecordService;

    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id) {
        return JsonData.success(businessRecordService.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin,String uiSref) {
        return JsonData.success(businessRecordService.getNewModel(userLogin,uiSref));
    }

    @PostMapping("/remove.json")
    public JsonData remove(int id, String userLogin) {
        businessRecordService.remove(id, userLogin);
        return JsonData.success();
    }
    @PostMapping("/selectReview.json")
    public JsonData selectReview(String userLogin,int recordId) {
        return JsonData.success(  businessRecordService.selectReview(recordId, userLogin));
    }
    @PostMapping("/selectPre.json")
    public JsonData selectPre(String userLogin,int recordId) {
        return JsonData.success(  businessRecordService.selectPre(recordId, userLogin));
    }


    @PostMapping("/update.json")
    public JsonData update(@RequestBody BusinessRecordDto item) {
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()), "操作人不能为空!");
        businessRecordService.update(item);
        return JsonData.success();
    }
    @PostMapping("/addCustomer.json")
    public JsonData addCustomer(String userLogin,String recordId) {
        return JsonData.success( businessRecordService.addCustomer(recordId,userLogin));
    }

    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin,String uiSref, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = businessRecordService.listPagedData(params, userLogin, uiSref, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }
    @PostMapping("/listPagedDataCommon.json")
    public JsonData listPagedDataCommon(String userLogin,String uiSref, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = businessRecordService.listPagedDataCommon(params, userLogin, uiSref, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }


    @PostMapping("/selectAll.json")
    public JsonData selectAll(String userLogin,String uiSref){
        return JsonData.success(businessRecordService.selectAll(userLogin,uiSref));
    }

    @PostMapping("/getPrintData.json")
    public JsonData getPrintData(int id) {
        return JsonData.success(businessRecordService.getPrintData(id));
    }
    @PostMapping("/listRecordByUserLogin.json")
    public JsonData listRecordByUserLogin(String userLogin,int recordId) {
        return JsonData.success(businessRecordService.listRecordByUserLogin(userLogin,recordId));
    }


    @RequestMapping("/downloadModel.json")
    public void downloadModel(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        String classpath = this.getClass().getResource("/").getPath().replaceFirst("/", "");
        String webappRoot = classpath.replaceAll("WEB-INF/classes/", "");
        String filePath = webappRoot + "assets/doc/导出模板/项目信息备案.xls";
        downloadingFile(filePath, "项目信息备案模板.xls", response);
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
            msg=   businessRecordService.insertByFile(multipartFile.getInputStream(),userLogin);
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
    @PostMapping("/getProjectNo.json")
    public JsonData getProjectNo(int id) {
        return JsonData.success(businessRecordService.getProjectNo(id));
    }



}


