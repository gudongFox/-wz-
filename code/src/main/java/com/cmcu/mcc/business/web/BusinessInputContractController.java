package com.cmcu.mcc.business.web;

import com.cmcu.common.JsonData;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.business.dto.BusinessInputContractDto;
import com.cmcu.mcc.business.service.BusinessInputContractService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Map;

@RestController
@RequestMapping("/business/inputContract")
public class BusinessInputContractController {


    @Autowired
    BusinessInputContractService businessInputContractService;

    @PostMapping("/loadPagedData.json")
    public JsonData loadPagedData(String userLogin,String uiSref,int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        return JsonData.success(businessInputContractService.listPagedData(params,userLogin,uiSref,pageNum, pageSize));
    }

    @PostMapping("/update.json")
    public JsonData update(@RequestBody BusinessInputContractDto item) {
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()), "操作人不能为空!");
        businessInputContractService.update(item);
        return JsonData.success();
    }

    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id) {
        return JsonData.success(businessInputContractService.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin,String uiSref) {
        return JsonData.success(businessInputContractService.getNewModel(userLogin,uiSref));
    }

    @PostMapping("/remove.json")
    public JsonData remove(int id, String userLogin) {
        businessInputContractService.remove(id, userLogin);
        return JsonData.success();
    }

    @PostMapping("/getContractNo.json")
    public JsonData getContractNo(int id) {
        return JsonData.success(businessInputContractService.getContractNo(id));
    }

    @PostMapping("/getProjectNo.json")
    public JsonData getProjectNo(int id) {
        return JsonData.success(businessInputContractService.getProjectNo(id));
    }

    @PostMapping("/insertContractLibrary.json")
    public JsonData insertContractLibrary(int inputContractId){
        return JsonData.success(businessInputContractService.insertContractLibrary(inputContractId));
    }

}
