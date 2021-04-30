package com.cmcu.common.controller;


import com.common.model.JsonData;
import com.cmcu.common.entity.CommonEdArrangePlan;
import com.cmcu.common.service.CommonEdArrangePlanService;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/common/edArrangePlan")
public class CommonEdArrangePlanController {

    @Resource
    CommonEdArrangePlanService commonEdArrangePlanService;


    @PostMapping("/listData.json")
    public JsonData listData(String businessKey, String buildBusinessKey) {
        Map result = Maps.newHashMap();
        List<CommonEdArrangePlan> list = commonEdArrangePlanService.listData(businessKey, buildBusinessKey);
        result.put("list", list);
        result.put("buildNameList",list.stream().map(CommonEdArrangePlan::getBuildName).distinct().collect(Collectors.toList()));
        return JsonData.success(result);
    }


    @PostMapping("/save.json")
    public JsonData save(int id,String planName,String planArea,String planDesc) {
        commonEdArrangePlanService.save(id, planName, planArea, planDesc);
        return JsonData.success();
    }

    @PostMapping("/setSeq.json")
    public JsonData setSeq(int id,boolean up){
        commonEdArrangePlanService.setSeq(id,up);
        return JsonData.success();
    }

    @PostMapping("/remove.json")
    public JsonData remove(int id,String enLogin){
        commonEdArrangePlanService.remove(id,enLogin);
        return JsonData.success();
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String businessKey, String buildBusinessKey,String buildName){
        return JsonData.success( commonEdArrangePlanService.getNewModel(businessKey,buildBusinessKey,buildName));
    }


}
