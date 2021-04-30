package com.cmcu.common.controller;

import com.common.model.JsonData;
import com.cmcu.common.service.CommonPrintTableService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/common/printTable")
public class CommonPrintTableController {
    @Resource
    CommonPrintTableService commonPrintTableService;


    @PostMapping("/getPrintDate.json")
    public JsonData getPrintDate(String businessKey, String userLogin) {
        return JsonData.success(commonPrintTableService.getPrintDate(businessKey, userLogin));
    }
}
