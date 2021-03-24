package com.cmcu.mcc.controller;

import com.cmcu.common.JsonData;
import com.cmcu.mcc.service.impl.FiveCommonCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fiveCommonCode")
public class FiveCommonCodeController {
    @Autowired
    FiveCommonCodeService fiveCommonCodeService;


    @RequestMapping("/listDataByCodeAndCodeCatalog.json")
    public JsonData listDataByCodeAndCodeCatalog(String code, String codeCatalog){
        return JsonData.success(fiveCommonCodeService.listDataByCodeAndCodeCatalog(code,codeCatalog));
    }

    @RequestMapping("/listAllMajorName.json")
    public JsonData listAllMajorName(){
        return JsonData.success(fiveCommonCodeService.listAllMajorName());
    }
}
