package com.cmcu.mcc.finance.web;

import com.common.model.JsonData;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.finance.dto.FiveFinanceReimburseDto;
import com.cmcu.mcc.finance.entity.FiveFinanceReimburseDetail;
import com.cmcu.mcc.finance.service.FiveFinanceReimburseService;
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
@RequestMapping("/five/finance/reimburse")
public class FiveFinanceReimburseController {
    @Autowired
    FiveFinanceReimburseService fiveFinanceReimburseService;

    @PostMapping(value = "/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(fiveFinanceReimburseService.getModelById(id));
    }


    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin,String uiSref){
        return JsonData.success(fiveFinanceReimburseService.getNewModel(userLogin,uiSref));
    }
    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        fiveFinanceReimburseService.remove(id,userLogin);
        return JsonData.success();
    }
    @PostMapping("/update.json")
    public JsonData update(@RequestBody FiveFinanceReimburseDto item){
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"操作人不能为空!");
        fiveFinanceReimburseService.update(item);
        return JsonData.success();
    }
    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin,String uiSref, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = fiveFinanceReimburseService.listPagedData(params, userLogin, uiSref, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }


    @PostMapping(value = "/getNewReplenishRefund.json")
    public JsonData getNewReplenishRefund(int  reimburseId,String userLogin){
        fiveFinanceReimburseService.getNewReplenishRefund(reimburseId,userLogin);
        return JsonData.success();
    }


    @PostMapping(value = "/getNewModelDetail.json")
    public JsonData getNewModelDetail(int  id,String userLogin,String uiSref){
        return JsonData.success(fiveFinanceReimburseService.getNewModelDetail(id,userLogin,uiSref));
    }
    @PostMapping(value = "/saveSelectedDeduction.json")
    public JsonData saveSelectedDeduction(int reimburseId,int  id,String type){
        fiveFinanceReimburseService.saveSelectedDeduction(reimburseId,id,type);
        return JsonData.success();
    }
    @PostMapping(value = "/removeDeduction.json")
    public JsonData removeDeduction(int  id,String userLogin){
        fiveFinanceReimburseService.removeDeduction(id,userLogin);
        return JsonData.success();
    }



    @PostMapping("/getModelDetailById.json")
    public JsonData getModelDetailById(int  id){
        return JsonData.success(fiveFinanceReimburseService.getModelDetailById(id));
    }
    @PostMapping("/removeDetail.json")
    public JsonData removeDetail(int  id){
        fiveFinanceReimburseService.removeDetail(id);
        return JsonData.success();
    }
    @PostMapping("/updateDetail.json")
    public JsonData updateDetail(@RequestBody FiveFinanceReimburseDetail item){
        fiveFinanceReimburseService.updateDetail(item);
        return JsonData.success();
    }

    @PostMapping("/listDetail.json")
    public JsonData listDetail(int  id){
        return JsonData.success(fiveFinanceReimburseService.listDetail(id));
    }
    @PostMapping("/listDeduction.json")
    public JsonData listDeduction(int  id){
        return JsonData.success(fiveFinanceReimburseService.listDeduction(id));
    }

    @PostMapping("/getPrintData.json")
    public JsonData getPrintData(int id){
        return JsonData.success(fiveFinanceReimburseService.getPrintData(id));
    }

    @PostMapping("/getReceiptsNumber")
    public  JsonData getReceiptsNumber(int id){return  JsonData.success(fiveFinanceReimburseService.getReceiptsNumber(id));}

}
