package com.cmcu.mcc.business.web;

import com.cmcu.common.JsonData;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.business.dto.BusinessBidContractDto;
import com.cmcu.mcc.business.service.BusinessBidContractService;
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
@RequestMapping("/business/bidContract")
public class BusinessBidContractController {
    @Autowired
    BusinessBidContractService businessBidContractService;


    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(businessBidContractService.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin){
        return JsonData.success(businessBidContractService.getNewModel(userLogin));
    }

    @PostMapping("/getNewModelById.json")
    public JsonData getNewModelByApplyId(int projectChargeId,String userLogin){
        return JsonData.success(businessBidContractService.getNewModel(projectChargeId,userLogin));
    }

    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        businessBidContractService.remove(id,userLogin);
        return JsonData.success();
    }

    @PostMapping("/update.json")
    public JsonData update(@RequestBody BusinessBidContractDto item){
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"操作人不能为空!");
        businessBidContractService.update(item);
        return JsonData.success();
    }

    @PostMapping("/updateById.json")
    public JsonData updateById(int projectChargeId,int id){
        businessBidContractService.update(projectChargeId,id);
        return JsonData.success();
    }

    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(int pageNum, String userLogin,int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = businessBidContractService.listPagedData(params,userLogin, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }

    @PostMapping("/listAllUnExistProjectCharge.json")
    public JsonData listAllUnExistApply(int chargeId) {
        return JsonData.success(businessBidContractService.listAllUnExistProjectCharge(chargeId));
    }

    @PostMapping("/getPrintData.json")
    public JsonData getPrintData(int id){
        return JsonData.success(businessBidContractService.getPrintData(id));
    }
}
