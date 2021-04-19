package com.cmcu.mcc.business.web;

import com.common.model.JsonData;
import com.cmcu.common.util.JsonMapper;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.business.dto.BusinessChangeCustomerDto;
import com.cmcu.mcc.business.service.BusinessChangeCustomerService;
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
@RequestMapping("/business/changeCustomer")
public class BusinessChangeCustomerController {

    @Autowired
    BusinessChangeCustomerService businessChangeCustomerService;

    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id){
       return JsonData.success(businessChangeCustomerService.getModelById(id));
    }

    @PostMapping("/loadPagedData.json")
    public JsonData loadPagedData(String userLogin,String uiSref,int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        return JsonData.success(businessChangeCustomerService.loadPagedData(userLogin,uiSref,params, pageNum, pageSize));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin,String uiSref){
        return JsonData.success(businessChangeCustomerService.getNewModel(userLogin,uiSref));
    }
    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        businessChangeCustomerService.remove(id,userLogin);
        return JsonData.success();
    }
    @PostMapping("/update.json")
    public JsonData update(@RequestBody BusinessChangeCustomerDto item){
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"操作人不能为空!");
        businessChangeCustomerService.update(item);
        return JsonData.success();
    }

    @PostMapping("/listCooperationProject.json")
    public JsonData listCooperationProject(int changeCustomerId){
        return JsonData.success(businessChangeCustomerService.listCooperationProject(changeCustomerId));
    }

}
