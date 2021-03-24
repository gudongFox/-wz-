package com.cmcu.mcc.finance.web;

import com.cmcu.common.JsonData;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.finance.dto.FiveFinanceRefundDto;
import com.cmcu.mcc.finance.entity.FiveFinanceRefundDetail;
import com.cmcu.mcc.finance.service.FiveFinanceRefundService;
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
@RequestMapping("/five/finance/refund")
public class FiveFinanceRefundController {
    @Autowired
    FiveFinanceRefundService fiveFinanceRefundService;

    @PostMapping(value = "/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(fiveFinanceRefundService.getModelById(id));
    }


    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin){
        return JsonData.success(fiveFinanceRefundService.getNewModel(userLogin));
    }
    @PostMapping("/listRefundByUserLogin.json")
    public JsonData listRefundByUserLogin(String userLogin){
        return JsonData.success(fiveFinanceRefundService.listRefundByUserLogin(userLogin));
    }
    @PostMapping("/listRefundByDeptId.json")
    public JsonData listRefundByDeptId(String userLogin,int deptId){
        return JsonData.success(fiveFinanceRefundService.listRefundByDeptId(userLogin,deptId));
    }



    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        fiveFinanceRefundService.remove(id,userLogin);
        return JsonData.success();
    }
    @PostMapping("/update.json")
    public JsonData update(@RequestBody FiveFinanceRefundDto item){
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"操作人不能为空!");
        fiveFinanceRefundService.update(item);
        return JsonData.success();
    }
    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin,String uiSref, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = fiveFinanceRefundService.listPagedData(params, userLogin, uiSref, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }


    @PostMapping(value = "/getNewModelDetail.json")
    public JsonData getNewModelDetail(int  id){
        return JsonData.success(fiveFinanceRefundService.getNewModelDetail(id));
    }

    @PostMapping("/getModelDetailById.json")
    public JsonData getModelDetailById(int  id){
        return JsonData.success(fiveFinanceRefundService.getModelDetailById(id));
    }
    @PostMapping("/removeDetail.json")
    public JsonData removeDetail(int  id){
        fiveFinanceRefundService.removeDetail(id);
        return JsonData.success();
    }
    @PostMapping("/updateDetail.json")
    public JsonData updateDetail(@RequestBody FiveFinanceRefundDetail item){
        fiveFinanceRefundService.updateDetail(item);
        return JsonData.success();
    }

    @PostMapping("/listDetail.json")
    public JsonData listDetail(int  id){
        return JsonData.success(fiveFinanceRefundService.listDetail(id));
    }
    @PostMapping("/getPrintData.json")
    public JsonData getPrintData(int id){
        return JsonData.success(fiveFinanceRefundService.getPrintData(id));
    }
}
