package com.cmcu.mcc.finance.web;

import com.common.model.JsonData;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.finance.dto.FiveFinanceDeptFundDto;
import com.cmcu.mcc.finance.service.FiveFinanceDeptFundService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/five/finance/deptFund")
public class FiveFinanceDeptFundController {
    @Autowired
    FiveFinanceDeptFundService fiveFinanceDeptFundService;

    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id) {
        return JsonData.success(fiveFinanceDeptFundService.getModelById(id));
    }

    @PostMapping("/getModelByDeptId.json")
    public JsonData getModelByDeptId(int deptId) {
        return JsonData.success(fiveFinanceDeptFundService.getModelByDeptId(deptId));
    }

    @PostMapping("/refreshDept.json")
    public JsonData refreshDept() {
        fiveFinanceDeptFundService.refreshDept();
        return JsonData.success();
    }
    @PostMapping("/refreshMoney.json")
    public JsonData refreshMoney(int deptId) {
        fiveFinanceDeptFundService.refreshMoney(deptId);
        return JsonData.success();
    }

    @PostMapping("/remove.json")
    public JsonData remove(int id, String userLogin) {
        fiveFinanceDeptFundService.remove(id, userLogin);
        return JsonData.success();
    }

    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin,String uiSref, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = fiveFinanceDeptFundService.listPagedData(params, userLogin, uiSref, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }

    @PostMapping("/selectAll.json")
    public JsonData selectAll(String userLogin,String uiSref){
        return JsonData.success(fiveFinanceDeptFundService.selectAll(userLogin,uiSref));
    }

}


