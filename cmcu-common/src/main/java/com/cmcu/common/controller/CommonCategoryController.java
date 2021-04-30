package com.cmcu.common.controller;

import com.common.model.JsonData;
import com.cmcu.common.service.CommonCategoryService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/common/category")
public class CommonCategoryController {

    @Resource
    CommonCategoryService commonCategoryService;

    @PostMapping("/selectAll.json")
    public JsonData selectAll(String tenetId, String businessKey) {
        return JsonData.success(commonCategoryService.selectAll(tenetId, businessKey));
    }

}
