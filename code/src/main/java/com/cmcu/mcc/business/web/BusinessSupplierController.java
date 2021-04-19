package com.cmcu.mcc.business.web;

import com.common.model.JsonData;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.business.dto.BusinessSupplierDto;
import com.cmcu.mcc.business.dto.FiveBusinessContractReviewDto;
import com.cmcu.mcc.business.entity.*;
import com.cmcu.mcc.business.service.BusinessSupplierService;
import com.cmcu.mcc.business.service.FiveBusinessContractReviewService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("business/supplier")
public class BusinessSupplierController {
    @Autowired
    BusinessSupplierService businessSupplierService;

    @PostMapping(value = "/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(businessSupplierService.getModelById(id));
    }


    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin){
        return JsonData.success(businessSupplierService.getNewModel(userLogin));
    }
    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        businessSupplierService.remove(id,userLogin);
        return JsonData.success();
    }
    @PostMapping("/update.json")
    public JsonData update(@RequestBody BusinessSupplierDto item){
        businessSupplierService.update(item);
        return JsonData.success();
    }
    @PostMapping(value = "/listPagedData.json")
    public JsonData loadPagedData(int pageNum,String uiSref, int pageSize ) {
        Map params = WebUtil.getRequestParameters();
        return JsonData.success(businessSupplierService.listPagedData(params,pageNum,pageSize,uiSref));
    }

    @PostMapping(value = "/listDetailById.json")
    public JsonData listDetailById(int supplierId){
        return JsonData.success(businessSupplierService.listDetailById(supplierId));
    }
    @PostMapping(value = "/getDetailModelById.json")
    public JsonData getDetailModelById(int id){
        return JsonData.success(businessSupplierService.getDetailModelById(id));
    }

    @PostMapping("/getNewDetailModel.json")
    public JsonData getNewDetailModel(int supplierId){
        return JsonData.success(businessSupplierService.getNewDetailModel(supplierId));
    }
    @PostMapping("/removeDetail.json")
    public JsonData removeDetail(int id,String userLogin){
        businessSupplierService.removeDetail(id,userLogin);
        return JsonData.success();
    }
    @PostMapping("/updateDetail.json")
    public JsonData updateDetail(@RequestBody BusinessSupplierAptitude item){
        businessSupplierService.updateDetail(item);
        return JsonData.success();
    }

    @PostMapping("/selectAll.json")
    public JsonData selectAll(String userLogin,String uiSref){
        return JsonData.success(businessSupplierService.selectAll(userLogin,uiSref).stream().filter(p->StringUtils.isNotEmpty(p.getName())).collect(Collectors.toList()));
    }

    @PostMapping("/listCooperationProject.json")
    public JsonData listCooperationProject(int supplierId){
        return JsonData.success(businessSupplierService.listCooperationProject(supplierId));
    }

    @PostMapping("/checkSupplier.json")
    public JsonData checkSupplier(String name,int supplierId) {
        businessSupplierService.checkSupplier(name,supplierId);
        return JsonData.success();
    }

    @PostMapping("/checkTaxNo.json")
    public JsonData checkTaxNo(String taxNo,int supplierId) {
        businessSupplierService.checkTaxNo(taxNo,supplierId);
        return JsonData.success();
    }

    @PostMapping("/updateUsedName.json")
    public JsonData updateUsedName(@RequestBody BusinessSupplierUsedName usedName){
        businessSupplierService.updateUsedName(usedName);
        return JsonData.success();
    }

    @PostMapping("/getNewUsedName.json")
    public JsonData getNewUsedName(String userLogin,int  supplierId){
        return JsonData.success(businessSupplierService.getNewUsedName(userLogin,supplierId));
    }
    @PostMapping("/listUsedNamesById.json")
    public JsonData listUsedNamesById(int supplierId){
        return JsonData.success(businessSupplierService.listUsedNamesById(supplierId));
    }
    @PostMapping("/getUsedNameById.json")
    public JsonData getUsedNameById(int id){
        return JsonData.success(businessSupplierService.getUsedNameById(id));
    }
    @PostMapping("/removeUsedName.json")
    public JsonData removeUsedName(int id,String userLogin){
        businessSupplierService.removeUsedName(id,userLogin);
        return JsonData.success();
    }

}
