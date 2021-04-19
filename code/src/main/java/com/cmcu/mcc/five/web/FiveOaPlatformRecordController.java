package com.cmcu.mcc.five.web;

import com.common.model.JsonData;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.five.dto.FiveOaPlatformRecordDto;
import com.cmcu.mcc.five.dto.FiveOaReportDto;
import com.cmcu.mcc.five.entity.FiveOaPlatformRecord;
import com.cmcu.mcc.five.entity.FiveOaPlatformRecordDetail;
import com.cmcu.mcc.five.service.FiveOaPlatformRecordService;
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
@RequestMapping("/five/oa/platformRecord")
public class FiveOaPlatformRecordController {
    @Autowired
    FiveOaPlatformRecordService fiveOaPlatformRecordService;



    @PostMapping(value = "/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(fiveOaPlatformRecordService.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin){
        return JsonData.success(fiveOaPlatformRecordService.getNewModel(userLogin));
    }
    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        fiveOaPlatformRecordService.remove(id,userLogin);
        return JsonData.success();
    }
    @PostMapping("/listRecordByUserLogin.json")
    public JsonData listRecordByUserLogin(String userLogin,int recordId) {
        return JsonData.success(fiveOaPlatformRecordService.listRecordByUserLogin(userLogin,recordId));
    }
    @PostMapping("/update.json")
    public JsonData update(@RequestBody FiveOaPlatformRecordDto item){
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"操作人不能为空!");
        fiveOaPlatformRecordService.update(item);
        return JsonData.success();
    }
    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin,String uiSref, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = fiveOaPlatformRecordService.listPagedData(params, userLogin, uiSref, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }
    @PostMapping(value = "/getNewModelDetail.json")
    public JsonData getNewModelDetail(int recordId){
        return JsonData.success(fiveOaPlatformRecordService.getNewModelDetail(recordId));
    }

    @PostMapping("/getModelDetailById.json")
    public JsonData getModelDetailById(int  id){
        return JsonData.success(fiveOaPlatformRecordService.getModelDetailById(id));
    }
    @PostMapping("/removeDetail.json")
    public JsonData removeDetail(int  id){
        fiveOaPlatformRecordService.removeDetail(id);
        return JsonData.success();
    }
    @PostMapping("/updateDetail.json")
    public JsonData updateDetail(@RequestBody FiveOaPlatformRecordDetail  item){
        fiveOaPlatformRecordService.updateDetail(item);
        return JsonData.success();
    }
    @PostMapping("/listDetail.json")
    public JsonData listDetail(int  recordId){
        return JsonData.success(fiveOaPlatformRecordService.listDetail(recordId));
    }
    @PostMapping("/getPrintData.json")
    public JsonData getPrintData(int id){
        return JsonData.success(fiveOaPlatformRecordService.getPrintData(id));
    }
}
