package com.cmcu.mcc.five.web;

import com.cmcu.common.JsonData;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.five.service.FiveActRelevanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/five/actRelevance")
public class FiveActRelevanceController {
    @Autowired
    FiveActRelevanceService fiveActRelevanceService;

    @PostMapping("/listActRelevance.json")
    public JsonData listActRelevance(String businessId){
        return JsonData.success(fiveActRelevanceService.listActRelevance(businessId));
    }
    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        fiveActRelevanceService.remove(id,userLogin);
        return JsonData.success();
    }
    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(){
        Map params = WebUtil.getRequestParameters();
        fiveActRelevanceService.getNewModel(params);
        return JsonData.success();
    }

}
