package com.cmcu.mcc.business.web;

import com.cmcu.common.JsonData;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.business.dto.BusinessBidAttendDto;
import com.cmcu.mcc.business.service.BusinessBidAttendService;
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
@RequestMapping("/business/bidAttend")
public class BusinessBidAttendController {
    @Autowired
    BusinessBidAttendService businessBidAttendService;


    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(businessBidAttendService.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin){
        return JsonData.success(businessBidAttendService.getNewModel(userLogin));
    }

    @PostMapping("/getNewModelByApplyId.json")
    public JsonData getNewModelByApplyId(int bidApplyId,String userLogin){
        return JsonData.success(businessBidAttendService.getNewModel(bidApplyId,userLogin));
    }

    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        businessBidAttendService.remove(id,userLogin);
        return JsonData.success();
    }

    @PostMapping("/update.json")
    public JsonData update(@RequestBody BusinessBidAttendDto item){
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"操作人不能为空!");
        businessBidAttendService.update(item);
        return JsonData.success();
    }

    @PostMapping("/updateById.json")
    public JsonData updateById(int bidApplyId,int bidAttendId){
        businessBidAttendService.update(bidApplyId,bidAttendId);
        return JsonData.success();
    }


    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin,int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = businessBidAttendService.listPagedData(params,userLogin,pageNum, pageSize);
        return JsonData.success(pageInfo);
    }


    @PostMapping("/listAllUnExistApply.json")
    public JsonData listAllUnExistApply(int applyId) {
        return JsonData.success(businessBidAttendService.listAllUnExistApply(applyId));
    }

    @PostMapping("/getPrintData.json")
    public JsonData getPrintData(int id){
        return JsonData.success(businessBidAttendService.getPrintData(id));
    }


}
