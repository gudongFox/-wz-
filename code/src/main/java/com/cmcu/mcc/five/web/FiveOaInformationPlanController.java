package com.cmcu.mcc.five.web;

import com.cmcu.common.JsonData;
import com.cmcu.common.util.FileUtil;
import com.cmcu.common.util.MyPoiUtil;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.five.dto.FiveOaInformationPlanDto;
import com.cmcu.mcc.five.entity.FiveOaInformationPlanDetail;
import com.cmcu.mcc.five.service.FiveOaInformationPlanService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/five/oa/informationPlan")
public class FiveOaInformationPlanController {
    @Autowired
    FiveOaInformationPlanService fiveOaInformationPlanService;


    @PostMapping(value = "/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(fiveOaInformationPlanService.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin){
        return JsonData.success(fiveOaInformationPlanService.getNewModel(userLogin));
    }
    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        fiveOaInformationPlanService.remove(id,userLogin);
        return JsonData.success();
    }
    @PostMapping("/update.json")
    public JsonData update(@RequestBody FiveOaInformationPlanDto item){
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"操作人不能为空!");
        fiveOaInformationPlanService.update(item);
        return JsonData.success();
    }
    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin,String uiSref, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = fiveOaInformationPlanService.listPagedData(params, userLogin, uiSref, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }



    @PostMapping(value = "/getNewModelDetail.json")
    public JsonData getNewModelDetail(int planId){
        return JsonData.success(fiveOaInformationPlanService.getNewModelDetail(planId));
    }

    @PostMapping("/getModelDetailById.json")
    public JsonData getModelDetailById(int  id){
        return JsonData.success(fiveOaInformationPlanService.getModelDetailById(id));
    }
    @PostMapping("/removeDetail.json")
    public JsonData removeDetail(int  id){
        fiveOaInformationPlanService.removeDetail(id);
        return JsonData.success();
    }
    @PostMapping("/updateDetail.json")
    public JsonData updateDetail(@RequestBody FiveOaInformationPlanDetail item){
        fiveOaInformationPlanService.updateDetail(item);
        return JsonData.success();
    }
    @PostMapping("/listDetail.json")
    public JsonData listDetail(int  planId){
        return JsonData.success(fiveOaInformationPlanService.listDetail(planId));
    }
    @PostMapping("/getPrintData.json")
    public JsonData getPrintData(int id){
        return JsonData.success(fiveOaInformationPlanService.getPrintData(id));
    }
    @PostMapping("/downTempleXls.json")
    public void downTempleXls(String startTime1,String uiSref,String endTime1,String userLogin,final HttpServletResponse response) {
        Map params = WebUtil.getRequestParameters();
        List<Map> list = fiveOaInformationPlanService.listMapData(params,uiSref,startTime1,endTime1,userLogin);
        MyPoiUtil.downloadExcel(list, FileUtil.getGoodFileName("资产管理年度软件采购预算"+ DateFormatUtils.format(new Date(),"yyyyMMddHHmm")+".xls"), response);
    }

}
