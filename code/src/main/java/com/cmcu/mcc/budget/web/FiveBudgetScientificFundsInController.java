package com.cmcu.mcc.budget.web;

import com.common.model.JsonData;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.budget.dto.FiveBudgetScientificFundsInDto;
import com.cmcu.mcc.budget.entity.FiveBudgetScientificFundsInDetail;
import com.cmcu.mcc.budget.service.FiveBudgetScientificFundsInService;
import com.github.pagehelper.PageInfo;
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
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Map;

@RestController
@RequestMapping("/five/budget/scientificFundsIn")
public class FiveBudgetScientificFundsInController {
    @Autowired
    FiveBudgetScientificFundsInService fiveBudgetScientificFundsInService;

    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id) {
        return JsonData.success(fiveBudgetScientificFundsInService.getModelById(id));
    }
    @PostMapping("/getDetailById.json")
    public JsonData getDetailById(int id) {
        return JsonData.success(fiveBudgetScientificFundsInService.getDetailById(id));
    }
    @PostMapping("/getLastYearDetailById.json")
    public JsonData getLastYearDetailById(int id) {
        return JsonData.success(fiveBudgetScientificFundsInService.getLastYearDetailById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin,String uiSref) {
        return JsonData.success(fiveBudgetScientificFundsInService.getNewModel(userLogin,uiSref));
    }

    @PostMapping("/remove.json")
    public JsonData remove(int id, String userLogin) {
        fiveBudgetScientificFundsInService.remove(id, userLogin);
        return JsonData.success();
    }

    @PostMapping("/update.json")
    public JsonData update(@RequestBody FiveBudgetScientificFundsInDto item) {
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()), "操作人不能为空!");
        fiveBudgetScientificFundsInService.update(item);
        return JsonData.success();
    }


    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin,String uiSref, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = fiveBudgetScientificFundsInService.listPagedData(params, userLogin, uiSref, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }

    @PostMapping("/selectAll.json")
    public JsonData selectAll(String userLogin,String uiSref){
        return JsonData.success(fiveBudgetScientificFundsInService.selectAll(userLogin,uiSref));
    }


    @PostMapping("/moneyTurnCapital.json")
    public JsonData moneyTurnCapital(Double money){
        return JsonData.success(fiveBudgetScientificFundsInService.moneyTurnCapital(money));
    }

    @PostMapping(value = "/getNewModelDetail.json")
    public JsonData getNewModelDetail(int  id,int detailId,String userLogin){
        return JsonData.success(fiveBudgetScientificFundsInService.getNewModelDetail(id,detailId,userLogin));
    }

    @PostMapping("/getModelDetailById.json")
    public JsonData getModelDetailById(int  id){
        return JsonData.success(fiveBudgetScientificFundsInService.getModelDetailById(id));
    }
    @PostMapping("/removeDetail.json")
    public JsonData removeDetail(int  id){
        fiveBudgetScientificFundsInService.removeDetail(id);
        return JsonData.success();
    }
    @PostMapping("/updateDetail.json")
    public JsonData updateDetail(@RequestBody FiveBudgetScientificFundsInDetail item){
        fiveBudgetScientificFundsInService.updateDetail(item);
        return JsonData.success();
    }
}


