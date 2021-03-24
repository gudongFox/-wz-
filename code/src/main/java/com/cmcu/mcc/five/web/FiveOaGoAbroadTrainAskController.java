package com.cmcu.mcc.five.web;

import com.cmcu.common.JsonData;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.five.dto.FiveOaGoAbroadTrainAskDto;
import com.cmcu.mcc.five.entity.FiveOaGoAbroadTrainAskDetail;
import com.cmcu.mcc.five.service.FiveOaGoAbroadTrainAskService;
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
@RequestMapping("/five/oa/goAbroadTrainAsk")
public class FiveOaGoAbroadTrainAskController {
    @Autowired
    FiveOaGoAbroadTrainAskService fiveOaGoAbroadTrainAskService;

    @PostMapping(value = "/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(fiveOaGoAbroadTrainAskService.getModelById(id));
    }


    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin){
        return JsonData.success(fiveOaGoAbroadTrainAskService.getNewModel(userLogin));
    }
    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        fiveOaGoAbroadTrainAskService.remove(id,userLogin);
        return JsonData.success();
    }
    @PostMapping("/update.json")
    public JsonData update(@RequestBody FiveOaGoAbroadTrainAskDto item){
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"操作人不能为空!");
        fiveOaGoAbroadTrainAskService.update(item);
        return JsonData.success();
    }
    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin,String uiSref, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = fiveOaGoAbroadTrainAskService.listPagedData(params, userLogin, uiSref, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }


    @PostMapping(value = "/getNewModelDetail.json")
    public JsonData getNewModelDetail(int  id ){
        return JsonData.success(fiveOaGoAbroadTrainAskService.getNewModelDetail(id));
    }

    @PostMapping("/getModelDetailById.json")
    public JsonData getModelDetailById(int  id){
        return JsonData.success(fiveOaGoAbroadTrainAskService.getModelDetailById(id));
    }
    @PostMapping("/removeDetail.json")
    public JsonData removeDetail(int  id){
        fiveOaGoAbroadTrainAskService.removeDetail(id);
        return JsonData.success();
    }
    @PostMapping("/updateDetail.json")
    public JsonData updateDetail(@RequestBody FiveOaGoAbroadTrainAskDetail item){
        fiveOaGoAbroadTrainAskService.updateDetail(item);
        return JsonData.success();
    }
    @PostMapping("/listDetail.json")
    public JsonData listDetail(int  id){
        return JsonData.success(fiveOaGoAbroadTrainAskService.listDetail(id));
    }
    @PostMapping("/listAssociation.json")
    public JsonData listAssociation(){
        return JsonData.success(fiveOaGoAbroadTrainAskService.listAssociation());
    }
    @PostMapping("/getPrintData.json")
    public JsonData getPrintData(int id){
        return JsonData.success(fiveOaGoAbroadTrainAskService.getPrintData(id));
    }


}
