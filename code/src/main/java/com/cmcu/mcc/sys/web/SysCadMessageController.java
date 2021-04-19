package com.cmcu.mcc.sys.web;

import com.common.model.JsonData;
import com.cmcu.mcc.sys.service.SysCadMessageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/sys/cadMessage")
public class SysCadMessageController {


    @Autowired
    SysCadMessageService sysCadMessageService;

    @RequestMapping("/listReceiveMessage.json")
    public JsonData listReceiveMessage(String receiver){
        return JsonData.success(sysCadMessageService.listReceiveMessage(receiver));
    }


    @PostMapping("/markReceived.json")
    public JsonData markReceived(String ids){
        for(String id:StringUtils.split(ids,",")) {
            sysCadMessageService.markReceived(Integer.parseInt(id));
        }
        return JsonData.success();
    }

}
