package com.cmcu.mcc.controller;

import com.cmcu.common.JsonData;
import com.cmcu.mcc.service.H5Service;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/h5")
public class H5Controller {

    @Resource
    H5Service h5Service;

    @PostMapping("/listMenu.json")
    public JsonData listMenu(String enLogin) {
        List<Map> menus = Lists.newArrayList();
        Map menu = Maps.newHashMap();
        menu.put("icon", "mui-icon mui-icon-compose");
        menu.put("url", "wz/stamp.html");
        menu.put("text", "用印申请");
        menus.add(menu);
        return JsonData.success(menus);
    }



    @PostMapping("/listStampProcessInstance.json")
    public JsonData listStampProcessInstance(String initiator, String involvedUser,String enLogin,int firstResult, int maxResults) {
        return JsonData.success(h5Service.listStampProcessInstance(initiator,involvedUser,enLogin,firstResult,maxResults));
    }

}
