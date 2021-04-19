package com.cmcu.mcc.finance.web;

import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.finance.dto.FiveFinanceSubpackagePaymentDto;
import com.cmcu.mcc.finance.entity.FiveFinanceSubpackagePaymentDetail;
import com.cmcu.mcc.finance.service.FiveFinanceSubpackagePaymentService;
import com.common.model.JsonData;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/five/finance/subpackagePayment")
public class FiveFinanceSubpackagePaymentController {
    @Autowired
    FiveFinanceSubpackagePaymentService fiveFinanceSubpackagePaymentService;

    @PostMapping(value = "/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(fiveFinanceSubpackagePaymentService.getModelById(id));
    }


    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin,String uiSref){
        return JsonData.success(fiveFinanceSubpackagePaymentService.getNewModel(userLogin,uiSref));
    }
    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        fiveFinanceSubpackagePaymentService.remove(id,userLogin);
        return JsonData.success();
    }
    @PostMapping("/update.json")
    public JsonData update(@RequestBody FiveFinanceSubpackagePaymentDto item){
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"操作人不能为空!");
        fiveFinanceSubpackagePaymentService.update(item);
        return JsonData.success();
    }
    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin,String uiSref, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = fiveFinanceSubpackagePaymentService.listPagedData(params, userLogin, uiSref, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }


    @PostMapping(value = "/getNewModelDetail.json")
    public JsonData getNewModelDetail(int  id,String userLogin){
        return JsonData.success(fiveFinanceSubpackagePaymentService.getNewModelDetail(id,userLogin));
    }

    @PostMapping("/getModelDetailById.json")
    public JsonData getModelDetailById(int  id){
        return JsonData.success(fiveFinanceSubpackagePaymentService.getModelDetailById(id));
    }
    @PostMapping("/removeDetail.json")
    public JsonData removeDetail(int  id){
        fiveFinanceSubpackagePaymentService.removeDetail(id);
        return JsonData.success();
    }
    @PostMapping("/updateDetail.json")
    public JsonData updateDetail(@RequestBody FiveFinanceSubpackagePaymentDetail item){
        fiveFinanceSubpackagePaymentService.updateDetail(item);
        return JsonData.success();
    }

    @PostMapping("/listDetail.json")
    public JsonData listDetail(int  id){
        return JsonData.success(fiveFinanceSubpackagePaymentService.listDetail(id));
    }
    @PostMapping("/getPrintData.json")
    public JsonData getPrintData(int id){
        return JsonData.success(fiveFinanceSubpackagePaymentService.getPrintData(id));
    }


    @PostMapping("/getReceiptsNumber")
    public  JsonData getReceiptsNumber(int id){return  JsonData.success(fiveFinanceSubpackagePaymentService.getReceiptsNumber(id));}
}
