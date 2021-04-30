package com.cmcu.common.controller;


import com.common.model.JsonData;
import com.cmcu.common.service.CommonWxMessageService;
import com.cmcu.common.util.WebUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/common/wxMessage")
public class CommonWxMessageController {
    @Resource
    CommonWxMessageService commonWxMessageService;

    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(int pageNum, int pageSize){
        Map params= WebUtil.getRequestParameters();
        return JsonData.success( commonWxMessageService.listPagedData(params,pageNum,pageSize));
    }
    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success( commonWxMessageService.getModelById(id));
    }
}
