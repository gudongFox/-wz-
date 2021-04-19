package com.cmcu.mcc.business.web;

import com.common.model.JsonData;
import com.cmcu.common.util.JsonMapper;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.business.dto.BusinessCustomerDto;
import com.cmcu.mcc.business.entity.BusinessCustomerUsedName;
import com.cmcu.mcc.business.service.BusinessCustomerService;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/business/customer")
public class BusinessCustomerController {

    @Autowired
    BusinessCustomerService businessCustomerService;

    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id){
       return JsonData.success(businessCustomerService.getModelById(id));
    }

    @PostMapping("/checkCustomer.json")
    public JsonData checkCustomer(String name,int customerId) {
        businessCustomerService.checkCustomer(name,customerId);
        return JsonData.success();
    }
    @PostMapping("/checkTaxNo.json")
    public JsonData checkTaxNo(String taxNo,int customerId) {
        businessCustomerService.checkTaxNo(taxNo,customerId);
        return JsonData.success();
    }

    @PostMapping("/loadPagedData.json")
    public JsonData loadPagedData(String userLogin,String uiSref,int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        return JsonData.success(businessCustomerService.loadPagedData(userLogin,uiSref,params, pageNum, pageSize));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin,String uiSref){
        return JsonData.success(businessCustomerService.getNewModel(userLogin,uiSref));
    }
    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        businessCustomerService.remove(id,userLogin);
        return JsonData.success();
    }
    @PostMapping("/removeUsedName.json")
    public JsonData removeUsedName(int id,String userLogin){
        businessCustomerService.removeUsedName(id,userLogin);
        return JsonData.success();
    }

    @PostMapping("/update.json")
    public JsonData update(@RequestBody BusinessCustomerDto item){
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"操作人不能为空!");
        businessCustomerService.update(item);
        return JsonData.success();
    }
    @PostMapping("/updateUsedName.json")
    public JsonData updateUsedName(@RequestBody BusinessCustomerUsedName usedName){
        businessCustomerService.updateUsedName(usedName);
        return JsonData.success();
    }

    @PostMapping("/getNewUsedName.json")
    public JsonData getNewUsedName(String userLogin,int  customerId){
        return JsonData.success(businessCustomerService.getNewUsedName(userLogin,customerId));
    }
    @PostMapping("/listUsedNamesById.json")
    public JsonData listUsedNamesById(int customerId){
        return JsonData.success(businessCustomerService.listUsedNamesById(customerId));
    }
    @PostMapping("/getUsedNameById.json")
    public JsonData getUsedNameById(int id){
        return JsonData.success(businessCustomerService.getUsedNameById(id));
    }
    @PostMapping("/getModelByNameAndLinMan.json")
    public JsonData getModelByNameAndLinMan(String customerName,String linkMan){
        return JsonData.success(businessCustomerService.getModelByNameAndLinMan(customerName,linkMan));
    }

    @PostMapping("/selectAll.json")
    public JsonData selectAll(String userLogin,String uiSref){
        return JsonData.success(businessCustomerService.selectAll(userLogin,uiSref).stream().filter(p->StringUtils.isNotEmpty(p.getName())).collect(Collectors.toList()));
    }
    @PostMapping("/listCooperationProject.json")
    public JsonData listCooperationProject(int customerId){
        return JsonData.success(businessCustomerService.listCooperationProject(customerId));
    }


    @RequestMapping("/downloadModel.json")
    public void  downloadModel(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        String classpath = this.getClass().getResource("/").getPath().replaceFirst("/", "");
        String webappRoot = classpath.replaceAll("WEB-INF/classes/", "");
        String filePath =webappRoot+"assets/doc/导出模板/客户信息.xls";
        downloadingFile(filePath, "客户信息模板.xls", response);
    }



    private void downloadingFile(String filePath,String fileName,HttpServletResponse response) throws IOException {
        File file = new File(filePath);
        if (file.exists() && file.length() > 0) {
            byte[] data = FileCopyUtils.copyToByteArray(file);
            response.reset();
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "utf-8").replace("+","%20"));
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
     * @param multipartFile
     * @return
     * @throws IOException
     */
    @RequestMapping("/receive.json")
    public JsonData receive(MultipartFile multipartFile,String userLogin) throws IOException {
        String fileName=multipartFile.getOriginalFilename();
        String msg="";
        if(StringUtils.isEmpty(fileName)||!fileName.endsWith(".xls")){
            Assert.state(true,"请将文件格式转换为 .xls");
        }else {
            msg= businessCustomerService.insertByFile(multipartFile.getInputStream(), userLogin);
        }
        return JsonData.success("",msg);
    }

    /**
     * 返回字符串格式兼容IE9
     * @param multipartFile

     * @return
     * @throws IOException
     */
    @RequestMapping("/receive.do")
    public String receives(MultipartFile multipartFile,String userLogin) throws IOException {
        JsonData result=receive(multipartFile,userLogin);
        return JsonMapper.obj2String(result);
    }
}
