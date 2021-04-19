package com.cmcu.mcc.sys.web;

import com.common.model.JsonData;
import com.cmcu.mcc.sys.entity.CoApp;
import com.cmcu.mcc.sys.service.CoAppService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/sys/app")
public class SysAppController {

    @Resource
    CoAppService coAppService;

    @PostMapping("/getLatest.json")
    public JsonData getLatest() {
        CoApp coApp = coAppService.getLatest();
        if (coApp == null) return JsonData.fail("未发现新版本");
        return JsonData.success(coApp);
    }
}
