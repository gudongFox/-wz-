package com.cmcu.common.controller;

import com.common.model.JsonData;
import com.cmcu.common.entity.CommonCadPlugin;
import com.cmcu.common.service.CommonCadPluginService;
import com.cmcu.common.util.WebUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/common/cadPlugin")
public class CommonCadPluginController {


    @Resource
    CommonCadPluginService commonCadPluginService;

    @RequestMapping("/getLatest.json")
    public JsonData getLatest(String tenetId,String versionType) {
        if(StringUtils.isEmpty(versionType)) versionType="app";
        CommonCadPlugin item = commonCadPluginService.getLatest(tenetId, versionType);
        return JsonData.success(item);
    }


    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String enLogin, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        return JsonData.success(commonCadPluginService.listPagedData(params, enLogin,pageNum, pageSize));
    }


    @PostMapping("/update.json")
    public JsonData update(@RequestBody CommonCadPlugin item){
        commonCadPluginService.update(item);
        return JsonData.success();
    }
    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(commonCadPluginService.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String enLogin) {
        JsonData jsonData =JsonData.success(commonCadPluginService.getNewModel(enLogin));
        return jsonData;
    }

    @PostMapping("/remove.json")
    public JsonData remove(int id){
        commonCadPluginService.remove(id);
        return JsonData.success();
    }
}
