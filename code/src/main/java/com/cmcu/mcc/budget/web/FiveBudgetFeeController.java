package com.cmcu.mcc.budget.web;

import com.cmcu.common.JsonData;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.budget.dto.FiveBudgetFeeDto;
import com.cmcu.mcc.budget.entity.FiveBudgetFeeDetail;
import com.cmcu.mcc.budget.service.FiveBudgetFeeService;
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
@RequestMapping("/five/budget/fee")
public class FiveBudgetFeeController {
    @Autowired
    FiveBudgetFeeService fiveBudgetFeeService;

    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id) {
        return JsonData.success(fiveBudgetFeeService.getModelById(id));
    }
    @PostMapping("/getDetailById.json")
    public JsonData getDetailById(int id) {
        return JsonData.success(fiveBudgetFeeService.getDetailById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin,String uiSref) {
        return JsonData.success(fiveBudgetFeeService.getNewModel(userLogin,uiSref));
    }
    @PostMapping("/getLastYearDetailById.json")
    public JsonData getLastYearDetailById(int id) {
        return JsonData.success(fiveBudgetFeeService.getLastYearDetailById(id,""));
    }

    @PostMapping("/remove.json")
    public JsonData remove(int id, String userLogin) {
        fiveBudgetFeeService.remove(id, userLogin);
        return JsonData.success();
    }

    @PostMapping("/update.json")
    public JsonData update(@RequestBody FiveBudgetFeeDto item) {
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()), "操作人不能为空!");
        fiveBudgetFeeService.update(item);
        return JsonData.success();
    }


    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin,String uiSref, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = fiveBudgetFeeService.listPagedData(params, userLogin, uiSref, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }

    @PostMapping("/selectAll.json")
    public JsonData selectAll(String userLogin,String uiSref){
        return JsonData.success(fiveBudgetFeeService.selectAll(userLogin,uiSref));
    }


    @PostMapping("/moneyTurnCapital.json")
    public JsonData moneyTurnCapital(Double money){
        return JsonData.success(fiveBudgetFeeService.moneyTurnCapital(money));
    }

    @PostMapping(value = "/getNewModelDetail.json")
    public JsonData getNewModelDetail(int  id,int detailId,String userLogin){
        return JsonData.success(fiveBudgetFeeService.getNewModelDetail(id,detailId,userLogin));
    }

    @PostMapping("/getModelDetailById.json")
    public JsonData getModelDetailById(int  id){
        return JsonData.success(fiveBudgetFeeService.getModelDetailById(id));
    }
    @PostMapping("/removeDetail.json")
    public JsonData removeDetail(int  id){
        fiveBudgetFeeService.removeDetail(id);
        return JsonData.success();
    }
    @PostMapping("/updateDetail.json")
    public JsonData updateDetail(@RequestBody FiveBudgetFeeDetail item){
        fiveBudgetFeeService.updateDetail(item);
        return JsonData.success();
    }
}


