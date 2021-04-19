package com.cmcu.mcc.ed.web;

import com.common.model.JsonData;
import com.cmcu.mcc.ed.dto.EdStepBuildDto;
import com.cmcu.mcc.ed.entity.EdStepBuild;
import com.cmcu.mcc.ed.service.EdStepBuildService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ed/stepBuild")
public class EdStepBuildController {


    @Autowired
    EdStepBuildService edStepBuildService;


    @PostMapping("/update.json")
    public JsonData update(@RequestBody EdStepBuild item){
        edStepBuildService.update(item);
        return JsonData.success();
    }

    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(edStepBuildService.getModelById(id));
    }

    @PostMapping("/listDataByStepId.json")
    public JsonData listDataByStepId(int stepId) {
        return JsonData.success(edStepBuildService.listDataByStepId(stepId));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(int stepId){
        return JsonData.success(edStepBuildService.getNewModel(stepId));
    }

    @PostMapping("/remove.json")
    public JsonData remove(int id){
        edStepBuildService.remove(id);
        return JsonData.success();
    }

}
