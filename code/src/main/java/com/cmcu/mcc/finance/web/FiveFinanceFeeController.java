package com.cmcu.mcc.finance.web;

import com.cmcu.common.JsonData;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.finance.dto.FiveFinanceTransferAccountsDto;
import com.cmcu.mcc.finance.entity.FiveFinanceTransferAccountsDetail;
import com.cmcu.mcc.finance.service.FiveFinanceTransferAccountsService;
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
@RequestMapping("/five/finance/transferFee")
public class FiveFinanceFeeController {
    @Autowired
    FiveFinanceTransferAccountsService fiveFinanceTransferAccountsService;

    @PostMapping(value = "/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(fiveFinanceTransferAccountsService.getModelById(id));
    }


    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin,String uiSref){
        return JsonData.success(fiveFinanceTransferAccountsService.getNewModel(userLogin,uiSref));
    }
    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        fiveFinanceTransferAccountsService.remove(id,userLogin);
        return JsonData.success();
    }
    @PostMapping("/update.json")
    public JsonData update(@RequestBody FiveFinanceTransferAccountsDto item){
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"操作人不能为空!");
        fiveFinanceTransferAccountsService.update(item);
        return JsonData.success();
    }
    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin,String uiSref, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = fiveFinanceTransferAccountsService.listPagedData(params, userLogin, uiSref, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }

    @PostMapping(value = "/getNewModelDetail.json")
    public JsonData getNewModelDetail(int  id){
        return JsonData.success(fiveFinanceTransferAccountsService.getNewModelDetail(id));
    }

    @PostMapping("/getModelDetailById.json")
    public JsonData getModelDetailById(int  id){
        return JsonData.success(fiveFinanceTransferAccountsService.getModelDetailById(id));
    }
    @PostMapping("/removeDetail.json")
    public JsonData removeDetail(int  id){
        fiveFinanceTransferAccountsService.removeDetail(id);
        return JsonData.success();
    }
    @PostMapping("/updateDetail.json")
    public JsonData updateDetail(@RequestBody FiveFinanceTransferAccountsDetail item){
        fiveFinanceTransferAccountsService.updateDetail(item);
        return JsonData.success();
    }

    @PostMapping("/listDetail.json")
    public JsonData listDetail(int  id){
        return JsonData.success(fiveFinanceTransferAccountsService.listDetail(id));
    }
    @PostMapping("/getPrintData.json")
    public JsonData getPrintData(int id){
        return JsonData.success(fiveFinanceTransferAccountsService.getPrintData(id));
    }
}
