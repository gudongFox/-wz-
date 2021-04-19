package com.cmcu.mcc.business.web;

import com.common.model.JsonData;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.business.dto.BusinessBidProjectChargeDto;
import com.cmcu.mcc.business.service.BusinessBidProjectChargeService;
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
@RequestMapping("/business/bidProjectCharge")
public class BusinessBidProjectChargeController {
    @Autowired
    BusinessBidProjectChargeService businessBidProjectChargeService;

    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(businessBidProjectChargeService.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin){
        return JsonData.success(businessBidProjectChargeService.getNewModel(userLogin));
    }

    @PostMapping("/getNewModelId.json")
    public JsonData getNewModelById(int attendId,String userLogin){
        return JsonData.success(businessBidProjectChargeService.getNewModel(attendId,userLogin));
    }

    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        businessBidProjectChargeService.remove(id,userLogin);
        return JsonData.success();
    }
    @PostMapping("/update.json")
    public JsonData update(@RequestBody BusinessBidProjectChargeDto item){
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"操作人不能为空!");
        businessBidProjectChargeService.update(item);
        return JsonData.success();
    }
    @PostMapping("/updateById.json")
    public JsonData updateById(int attendId,int id){
        businessBidProjectChargeService.update(attendId,id);
        return JsonData.success();
    }
    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(int pageNum,String userLogin, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = businessBidProjectChargeService.listPagedData(params,userLogin, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }

    @PostMapping("/listAllUnExistAttend.json")
    public JsonData listAllUnExistAttend(int attendId) {
        return JsonData.success(businessBidProjectChargeService.listAllUnExistAttend(attendId));
    }

    @PostMapping("/getPrintData.json")
    public JsonData getPrintData(int id){
        return JsonData.success(businessBidProjectChargeService.getPrintData(id));
    }
}
