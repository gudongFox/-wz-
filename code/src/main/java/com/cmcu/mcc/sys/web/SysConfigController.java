package com.cmcu.mcc.sys.web;


import com.common.model.JsonData;
import com.cmcu.common.entity.CommonConfig;
import com.cmcu.common.service.CommonConfigService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/sys/config")
public class SysConfigController {

    @Resource
    CommonConfigService commonConfigService;

    @PostMapping("/update.json")
    public JsonData update(@RequestBody CommonConfig item){
        commonConfigService.save(item);
        return JsonData.success();
    }

    @PostMapping("/getConfig.json")
    public JsonData getConfig(){
        return JsonData.success(commonConfigService.getConfig());
    }

}
