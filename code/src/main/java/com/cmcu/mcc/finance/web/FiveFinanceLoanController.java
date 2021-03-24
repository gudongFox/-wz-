package com.cmcu.mcc.finance.web;

import com.cmcu.common.JsonData;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.finance.dto.FiveFinanceLoanDto;
import com.cmcu.mcc.finance.entity.FiveFinanceLoanDetail;
import com.cmcu.mcc.finance.service.FiveFinanceLoanService;
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
@RequestMapping("/five/finance/loan")
public class FiveFinanceLoanController {
    @Autowired
    FiveFinanceLoanService fiveFinanceLoanService;

    @PostMapping(value = "/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(fiveFinanceLoanService.getModelById(id));
    }


    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin, String uiSref){
        return JsonData.success(fiveFinanceLoanService.getNewModel(userLogin,uiSref));
    }
    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        fiveFinanceLoanService.remove(id,userLogin);
        return JsonData.success();
    }
    @PostMapping("/listLoanByUserLogin.json")
    public JsonData listLoanByUserLogin(String userLogin){
        return JsonData.success( fiveFinanceLoanService.listLoanByUserLogin(userLogin));
    }
    @PostMapping("/listLoanByDeptId.json")
    public JsonData listLoanByDeptId(String userLogin,int deptId){
        return JsonData.success( fiveFinanceLoanService.listLoanByDeptId(userLogin,deptId));
    }

    @PostMapping("/update.json")
    public JsonData update(@RequestBody FiveFinanceLoanDto item){
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"操作人不能为空!");
        fiveFinanceLoanService.update(item);
        return JsonData.success();
    }
    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin,String uiSref, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = fiveFinanceLoanService.listPagedData(params, userLogin, uiSref, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }


    @PostMapping(value = "/getNewModelDetail.json")
    public JsonData getNewModelDetail(int  id){
        return JsonData.success(fiveFinanceLoanService.getNewModelDetail(id));
    }

    @PostMapping("/getModelDetailById.json")
    public JsonData getModelDetailById(int  id){
        return JsonData.success(fiveFinanceLoanService.getModelDetailById(id));
    }
    @PostMapping("/removeDetail.json")
    public JsonData removeDetail(int  id){
        fiveFinanceLoanService.removeDetail(id);
        return JsonData.success();
    }
    @PostMapping("/updateDetail.json")
    public JsonData updateDetail(@RequestBody FiveFinanceLoanDetail item){
        fiveFinanceLoanService.updateDetail(item);
        return JsonData.success();
    }

    @PostMapping("/listDetail.json")
    public JsonData listDetail(int  id){
        return JsonData.success(fiveFinanceLoanService.listDetail(id));
    }
    @PostMapping("/getPrintData.json")
    public JsonData getPrintData(int id){
        return JsonData.success(fiveFinanceLoanService.getPrintData(id));
    }
    @PostMapping("/getReceiptsNumber")
    public  JsonData getReceiptsNumber(int id){return  JsonData.success(fiveFinanceLoanService.getReceiptsNumber(id));}
}
