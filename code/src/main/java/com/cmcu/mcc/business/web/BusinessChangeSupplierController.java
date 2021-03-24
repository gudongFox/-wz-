package com.cmcu.mcc.business.web;

import com.cmcu.common.JsonData;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.business.dto.BusinessChangeSupplierDto;
import com.cmcu.mcc.business.service.BusinessChangeSupplierService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/business/changeSupplier")
public class BusinessChangeSupplierController {

    @Autowired
    BusinessChangeSupplierService businessChangeSupplierService;

    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id){
       return JsonData.success(businessChangeSupplierService.getModelById(id));
    }

    @PostMapping("/loadPagedData.json")
    public JsonData loadPagedData(String userLogin,String uiSref,int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        return JsonData.success(businessChangeSupplierService.loadPagedData(userLogin,uiSref,params, pageNum, pageSize));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin,String uiSref){
        return JsonData.success(businessChangeSupplierService.getNewModel(userLogin,uiSref));
    }
    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        businessChangeSupplierService.remove(id,userLogin);
        return JsonData.success();
    }
    @PostMapping("/update.json")
    public JsonData update(@RequestBody BusinessChangeSupplierDto item){
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"操作人不能为空!");
        businessChangeSupplierService.update(item);
        return JsonData.success();
    }



}
