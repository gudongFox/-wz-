package com.cmcu.common.controller;


import com.common.model.JsonData;
import com.cmcu.common.entity.CommonBlack;
import com.cmcu.common.entity.CommonCode;
import com.cmcu.common.entity.CommonMessage;
import com.cmcu.common.service.CommonMessageService;
import com.cmcu.common.util.WebUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/common/message")
public class CommonMessageController {


    @Resource
    CommonMessageService commonMessageService;


    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(int pageNum,int pageSize){
        Map params= WebUtil.getRequestParameters();
        return JsonData.success( commonMessageService.listPagedData(pageNum,pageSize,params));
    }


    @PostMapping("/listLastFiveNoReceived.json")
    public JsonData listLastFiveNoReceived(String receiver) {
        List<CommonMessage> list = commonMessageService.listLastFiveNoReceived(receiver);
        return JsonData.success(list);
    }


}
