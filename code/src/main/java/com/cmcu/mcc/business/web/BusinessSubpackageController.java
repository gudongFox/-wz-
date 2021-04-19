package com.cmcu.mcc.business.web;

import com.common.model.JsonData;
import com.cmcu.common.util.JsonMapper;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.business.dto.BusinessSubpackageDto;
import com.cmcu.mcc.business.service.BusinessSubpackageService;
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
@RequestMapping("/business/subpackage")
public class BusinessSubpackageController {
    @Autowired
    BusinessSubpackageService businessSubpackageService;

    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id) {
        return JsonData.success(businessSubpackageService.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin,String uiSref) {
        return JsonData.success(businessSubpackageService.getNewModel(userLogin,uiSref));
    }
    @PostMapping("/changeOpenStamp.json")
    public JsonData changeOpenStamp(int id,String userLogin){
        businessSubpackageService.changeOpenStamp(id,userLogin);
        return JsonData.success();
    }

    @PostMapping("/remove.json")
    public JsonData remove(int id, String userLogin) {
        businessSubpackageService.remove(id, userLogin);
        return JsonData.success();
    }

    @PostMapping("/update.json")
    public JsonData update(@RequestBody BusinessSubpackageDto item) {
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()), "操作人不能为空!");
        businessSubpackageService.update(item);
        return JsonData.success();
    }
    @PostMapping("/addSupplier.json")
    public JsonData addSupplier(String userLogin,String subpackageId) {
        return JsonData.success( businessSubpackageService.addSupplier(subpackageId,userLogin));
    }

    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin,String uiSref, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = businessSubpackageService.listPagedData(params, userLogin, uiSref, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }

    @PostMapping("/selectAll.json")
    public JsonData selectAll(String userLogin,String uiSref){
        return JsonData.success(businessSubpackageService.selectAll(userLogin,uiSref));
    }

    @PostMapping("/getPrintData.json")
    public JsonData getPrintData(int id) {
        return JsonData.success(businessSubpackageService.getPrintData(id));
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

    @PostMapping("/insertContractLibrary.json")
    public JsonData insertContractLibrary(int subpackageId){
        return JsonData.success(businessSubpackageService.insertContractLibrary(subpackageId));
    }
    @PostMapping("/insertContractReview.json")
    public JsonData insertContractReview(int subpackageId){
        return JsonData.success(businessSubpackageService.insertContractReview(subpackageId));
    }
    @PostMapping("/getSubContractNo.json")
    public JsonData getSubContractNo(int  subpackageId){
        return JsonData.success(businessSubpackageService.getSubContractNo(subpackageId));
    }
}


