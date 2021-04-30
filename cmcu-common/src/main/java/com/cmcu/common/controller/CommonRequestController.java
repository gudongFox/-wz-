package com.cmcu.common.controller;


import com.common.model.JsonData;
import com.cmcu.common.service.CommonRequestService;
import com.cmcu.common.util.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/common/request")
public class CommonRequestController {

    private CommonRequestService commonRequestService;

    @Autowired
    public CommonRequestController(CommonRequestService commonRequestService){
        this.commonRequestService=commonRequestService;
    }

    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        return JsonData.success(commonRequestService.listPagedData(pageNum, pageSize, params));
    }


    @PostMapping("/listRequestName.json")
    public JsonData listRequestName(String enLogin) {
        return JsonData.success(commonRequestService.listRequestName(enLogin));
    }
}
