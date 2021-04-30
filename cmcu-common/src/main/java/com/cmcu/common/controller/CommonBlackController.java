package com.cmcu.common.controller;

import com.common.model.JsonData;
import com.cmcu.common.entity.CommonBlack;
import com.cmcu.common.service.CommonBlackService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/common/black")
public class CommonBlackController {

    final
    CommonBlackService commonBlackService;

    public CommonBlackController(CommonBlackService commonBlackService) {
        this.commonBlackService = commonBlackService;
    }

    @PostMapping("/selectAll.json")
    public JsonData selectAll(String enLogin){
        return JsonData.success(commonBlackService.selectAll(enLogin));
    }

    @PostMapping("/save.json")
    public JsonData save(@RequestBody CommonBlack item){
        commonBlackService.save(item);
        return JsonData.success();
    }

    @PostMapping("/remove.json")
    public JsonData remove(int id,String enLogin){
        commonBlackService.remove(id,enLogin);
        return JsonData.success();
    }

    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success( commonBlackService.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String enLogin){
        return JsonData.success( commonBlackService.getNewModel(enLogin));
    }

}
