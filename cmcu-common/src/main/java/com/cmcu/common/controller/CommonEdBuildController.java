package com.cmcu.common.controller;

import com.common.model.JsonData;
import com.cmcu.common.entity.CommonEdBuild;
import com.cmcu.common.service.CommonEdBuildService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/common/edBuild")
public class CommonEdBuildController {

    @Resource
    CommonEdBuildService commonEdBuildService;

    @PostMapping("/listData.json")
    public JsonData listData(String businessKey) {
        return JsonData.success(commonEdBuildService.listData(businessKey));
    }

    @PostMapping("/save.json")
    public JsonData save(@RequestBody CommonEdBuild item){
        commonEdBuildService.save(item);
        return JsonData.success();
    }

    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(commonEdBuildService.getModelById(id));
    }


    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String businessKey,String enLogin){
        return JsonData.success(commonEdBuildService.getNewModel(businessKey));
    }

    @PostMapping("/remove.json")
    public JsonData remove(int id,String enLogin){
        commonEdBuildService.remove(id,enLogin);
        return JsonData.success();
    }

}
