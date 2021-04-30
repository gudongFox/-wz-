package com.cmcu.mcc.finance.web;

import com.cmcu.mcc.finance.entity.FiveFinanceTravelExpenseUser;
import com.common.model.JsonData;
import com.cmcu.common.util.WebUtil;

import com.cmcu.mcc.finance.dto.FiveFinanceTravelExpenseDto;

import com.cmcu.mcc.finance.entity.FiveFinanceTravelExpenseDetail;
import com.cmcu.mcc.finance.service.FiveFinanceTravelExpenseService;
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
@RequestMapping("/five/finance/travelExpense")
public class FiveFinanceTravelExpenseController {
    @Autowired
    FiveFinanceTravelExpenseService fiveFinanceTravelExpenseService;

    @PostMapping(value = "/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(fiveFinanceTravelExpenseService.getModelById(id));
    }


    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin,String uiSref){
        return JsonData.success(fiveFinanceTravelExpenseService.getNewModel(userLogin,uiSref));
    }
    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        fiveFinanceTravelExpenseService.remove(id,userLogin);
        return JsonData.success();
    }
    @PostMapping("/update.json")
    public JsonData update(@RequestBody FiveFinanceTravelExpenseDto item){
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"操作人不能为空!");
        fiveFinanceTravelExpenseService.update(item);
        return JsonData.success();
    }
    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin,String uiSref, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = fiveFinanceTravelExpenseService.listPagedData(params, userLogin, uiSref, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }


    @PostMapping(value = "/getNewModelDetail.json")
    public JsonData getNewModelDetail(int  id,String userLogin){
        return JsonData.success(fiveFinanceTravelExpenseService.getNewModelDetail(id,userLogin));
    }
    @PostMapping(value = "/getNewModelUserDetail.json")
    public JsonData getNewModelUserDetail(int  id,String userLogin){
        return JsonData.success(fiveFinanceTravelExpenseService.getNewModelUserDetail(id,userLogin));
    }





    @PostMapping("/getModelDetailById.json")
    public JsonData getModelDetailById(int  id){
        return JsonData.success(fiveFinanceTravelExpenseService.getModelDetailById(id));
    }

    @PostMapping("/getModelUserDetailById.json")
    public JsonData getModelUserDetailById(int  id){
        return JsonData.success(fiveFinanceTravelExpenseService.getModelUserDetailById(id));
    }


    @PostMapping("/removeDetail.json")
    public JsonData removeDetail(int  id){
        fiveFinanceTravelExpenseService.removeDetail(id);
        return JsonData.success();
    }
    @PostMapping("/removeUserDetail.json")
    public JsonData removeUserDetail(int  id){
        fiveFinanceTravelExpenseService.removeUserDetail(id);
        return JsonData.success();
    }

    @PostMapping("/updateDetail.json")
    public JsonData updateDetail(@RequestBody FiveFinanceTravelExpenseDetail item){
        fiveFinanceTravelExpenseService.updateDetail(item);
        return JsonData.success();
    }
    @PostMapping("/getApplyStandard.json")
    public JsonData getApplyStandard(@RequestBody FiveFinanceTravelExpenseDetail  detail){
        return JsonData.success(fiveFinanceTravelExpenseService.getApplyStandard(detail));
    }


    @PostMapping("/updateUserDetail.json")
    public JsonData updateUserDetail(@RequestBody FiveFinanceTravelExpenseUser item){
        fiveFinanceTravelExpenseService.updateUserDetail(item);
        return JsonData.success();
    }



    @PostMapping("/listDetail.json")
    public JsonData listDetail(int  id){
        return JsonData.success(fiveFinanceTravelExpenseService.listDetail(id));
    }
    @PostMapping("/listUserDetail.json")
    public JsonData listUserDetail(int  id){
        return JsonData.success(fiveFinanceTravelExpenseService.listUserDetail(id));
    }


    @PostMapping("/getPrintData.json")
    public JsonData getPrintData(int id){
        return JsonData.success(fiveFinanceTravelExpenseService.getPrintData(id));
    }

    @PostMapping(value = "/saveSelectedDeduction.json")
    public JsonData saveSelectedDeduction(int reimburseId,int  id,String type){
        fiveFinanceTravelExpenseService.saveSelectedDeduction(reimburseId,id,type);
        return JsonData.success();
    }
    @PostMapping(value = "/removeDeduction.json")
    public JsonData removeDeduction(int  id,String userLogin){
        fiveFinanceTravelExpenseService.removeDeduction(id,userLogin);
        return JsonData.success();
    }
    @PostMapping("/listDeduction.json")
    public JsonData listDeduction(int  id){
        return JsonData.success(fiveFinanceTravelExpenseService.listDeduction(id));
    }
    @PostMapping("/getReceiptsNumber")
    public  JsonData getReceiptsNumber(int id){return  JsonData.success(fiveFinanceTravelExpenseService.getReceiptsNumber(id));}
}
