package com.cmcu.common.controller;


import com.common.model.JsonData;
import com.cmcu.common.entity.CommonEdStamp;
import com.cmcu.common.service.CommonEdStampService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/common/edStamp")
public class CommonEdStampController {

    @Resource
    CommonEdStampService commonEdStampService;

    @PostMapping("/selectAll.json")
    public JsonData selectAll(String tenetId) {
        return JsonData.success(commonEdStampService.selectAll(tenetId));
    }

    @PostMapping("/save.json")
    public JsonData save(@RequestBody CommonEdStamp item) {
        return JsonData.success(commonEdStampService.save(item));
    }

    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(commonEdStampService.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String tenetId) {
        JsonData jsonData =JsonData.success(commonEdStampService.getNewModel(tenetId));
        return jsonData;
    }

    @PostMapping("/remove.json")
    public JsonData remove(int id){
        commonEdStampService.remove(id);
        return JsonData.success();
    }
}
