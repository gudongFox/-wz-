package com.cmcu.mcc.sys.web;


import com.cmcu.common.JsonData;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.sys.entity.SysPlugin;
import com.cmcu.mcc.sys.service.SysPluginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@RestController
@RequestMapping("/sys/plugin")
public class SysPluginController {

    @Autowired
    SysPluginService sysPluginService;


    @PostMapping("/getLatest.json")
    public JsonData getLatest(){
        return JsonData.success(sysPluginService.getLatest());
    }

    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        return JsonData.success(sysPluginService.listPagedData(params,pageNum,pageSize));
    }

    @PostMapping("/update.json")
    public JsonData update(@RequestBody SysPlugin sysPlugin){
        sysPluginService.update(sysPlugin);
        return JsonData.success();
    }
    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(sysPluginService.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin) {
        return JsonData.success(sysPluginService.getNewModel(userLogin));
    }

    @PostMapping("/remove.json")
    public JsonData remove(int id){
        sysPluginService.remove(id);
        return JsonData.success();
    }

}
