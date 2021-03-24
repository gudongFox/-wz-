package com.cmcu.mcc.five.web;

import com.cmcu.common.JsonData;
import com.cmcu.common.util.MyPoiUtil;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.five.dto.FiveOaProjectFundPlanDto;
import com.cmcu.mcc.five.entity.FiveOaPlatformRecordDetail;
import com.cmcu.mcc.five.entity.FiveOaProjectfundplanDetail;
import com.cmcu.mcc.five.service.FiveOaProjectFundPlanService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/five/oa/projectFundPlan")
public class FiveOaProjectFundPlanController {
    @Autowired
    FiveOaProjectFundPlanService fiveOaProjectFundPlanService;

    @PostMapping(value = "/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(fiveOaProjectFundPlanService.getModelById(id));
    }


    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin){
        return JsonData.success(fiveOaProjectFundPlanService.getNewModel(userLogin));
    }
    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        fiveOaProjectFundPlanService.remove(id,userLogin);
        return JsonData.success();
    }
    @PostMapping("/update.json")
    public JsonData update(@RequestBody FiveOaProjectFundPlanDto item){
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"操作人不能为空!");
        fiveOaProjectFundPlanService.update(item);
        return JsonData.success();
    }
    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin,String uiSref, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = fiveOaProjectFundPlanService.listPagedData(params, userLogin, uiSref, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }


    @PostMapping(value = "/getNewModelDetail.json")
    public JsonData getNewModelDetail(int  id,String type){
        return JsonData.success(fiveOaProjectFundPlanService.getNewModelDetail(id,type));
    }

    @PostMapping("/getModelDetailById.json")
    public JsonData getModelDetailById(int  id){
        return JsonData.success(fiveOaProjectFundPlanService.getModelDetailById(id));
    }
    @PostMapping("/removeDetail.json")
    public JsonData removeDetail(int  id){
        fiveOaProjectFundPlanService.removeDetail(id);
        return JsonData.success();
    }
    @PostMapping("/updateDetail.json")
    public JsonData updateDetail(@RequestBody FiveOaProjectfundplanDetail item){
        fiveOaProjectFundPlanService.updateDetail(item);
        return JsonData.success();
    }
    @PostMapping("/listDetail.json")
    public JsonData listDetail(int  id){
        return JsonData.success(fiveOaProjectFundPlanService.listDetail(id));
    }

    @PostMapping("/getPrintData.json")
    public JsonData getPrintData(int id){
        return JsonData.success(fiveOaProjectFundPlanService.getPrintData(id));
    }

    @PostMapping("/updateExcl.json")
    public JsonData updateExcl(MultipartFile file, int id,String userLogin) throws IOException, ParseException  {
        List<Map> data = MyPoiUtil.listTableData(file.getInputStream(), 0, 0, true);
        fiveOaProjectFundPlanService.uploadExcl(data,id,userLogin);
        return JsonData.success();
    }
    @PostMapping("/getUpMonthsDate.json")
    public JsonData getUpMonthsDate(int planId){
        fiveOaProjectFundPlanService.getUpMonthsDate(planId);
        return JsonData.success();
    }

}
