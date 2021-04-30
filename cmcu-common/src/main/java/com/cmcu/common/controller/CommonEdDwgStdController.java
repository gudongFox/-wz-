package com.cmcu.common.controller;

import com.common.model.JsonData;
import com.cmcu.common.entity.CommonEdDwgStd;
import com.cmcu.common.service.CommonEdDwgStdService;
import com.cmcu.common.util.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/common/edDwgStd")
public class CommonEdDwgStdController {
    @Autowired
    CommonEdDwgStdService commonEdDwgStdService;


    @PostMapping("/selectAll.json")
    public JsonData selectAll(String enLogin) {
        return JsonData.success(commonEdDwgStdService.selectAll(enLogin));
    }

    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String enLogin,int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        return JsonData.success(commonEdDwgStdService.listPagedData(params, enLogin,pageNum, pageSize));
    }

    @PostMapping("/update.json")
    public JsonData update(@RequestBody CommonEdDwgStd item){
        commonEdDwgStdService.update(item);
        return JsonData.success();
    }

    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(commonEdDwgStdService.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String enLogin) {
        JsonData jsonData =JsonData.success(commonEdDwgStdService.getNewModel(enLogin));
        return jsonData;
    }

    @PostMapping("/remove.json")
    public JsonData remove(int id){
        commonEdDwgStdService.remove(id);
        return JsonData.success();
    }
}
