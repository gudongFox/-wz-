package com.cmcu.mcc.budget.web;

import com.common.model.JsonData;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.budget.dto.FiveBudgetMaintainDto;
import com.cmcu.mcc.budget.entity.FiveBudgetMaintainDetail;
import com.cmcu.mcc.budget.service.FiveBudgetMaintainService;
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
@RequestMapping("/five/budget/business")
public class FiveBudgetBusinessController {
    @Autowired
    FiveBudgetMaintainService fiveBudgetMaintainService;

    @PostMapping(value = "/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(fiveBudgetMaintainService.getModelById(id));
    }
    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin,String uiSref){
        return JsonData.success(fiveBudgetMaintainService.getNewModel(userLogin,uiSref));
    }
    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        fiveBudgetMaintainService.remove(id,userLogin);
        return JsonData.success();
    }
    @PostMapping("/update.json")
    public JsonData update(@RequestBody FiveBudgetMaintainDto item){
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"操作人不能为空!");
        fiveBudgetMaintainService.update(item);
        return JsonData.success();
    }
    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin,String uiSref, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = fiveBudgetMaintainService.listPagedData(params, userLogin, uiSref, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }

    @PostMapping("/getNewModelDetail.json")
    public JsonData getNewModelDetail(int  id,String userLogin){
        return JsonData.success(fiveBudgetMaintainService.getNewModelDetail(id,userLogin));
    }

    @PostMapping("/getModelDetailById.json")
    public JsonData getModelDetailById(int  id){
        return JsonData.success(fiveBudgetMaintainService.getModelDetailById(id));
    }
    @PostMapping("/removeDetail.json")
    public JsonData removeDetail(int  id){
        fiveBudgetMaintainService.removeDetail(id);
        return JsonData.success();
    }
    @PostMapping("/updateDetail.json")
    public JsonData updateDetail(@RequestBody FiveBudgetMaintainDetail item){
        fiveBudgetMaintainService.updateDetail(item);
        return JsonData.success();
    }

    @PostMapping("/listDetail.json")
    public JsonData listDetail(int  id){
        return JsonData.success(fiveBudgetMaintainService.listDetail(id));
    }

    @PostMapping("/listLastYearDetail.json")
    public JsonData listLastYearDetail(int  id){
        return JsonData.success(fiveBudgetMaintainService.listLastYearDetail(id));
    }

    @PostMapping("/getTotal.json")
    public JsonData getTotal(String temp1,String temp2,String temp3){
        String temp = MyStringUtil.getNewAddMoney(temp1,temp2);
        return JsonData.success(MyStringUtil.getNewAddMoney(temp,temp3));
    }
    @PostMapping("/getPrintData.json")
    public JsonData getPrintData(int id){
        return JsonData.success(fiveBudgetMaintainService.getPrintData(id));
    }
}
