package com.cmcu.common.controller;


import com.cmcu.common.dto.CommonEdMarkMoreDto;
import com.cmcu.common.service.CommonEdMarkMoreService;
import com.common.model.JsonData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/common/edMarkMore")
public class CommonEdMarkMoreController {


    @Resource
    CommonEdMarkMoreService commonEdMarkMoreService;


    @PostMapping("/remove.json")
    public JsonData remove(int id,String enLogin){
        commonEdMarkMoreService.remove(id,enLogin);
        return JsonData.success();
    }

    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id,String enLogin) {
        CommonEdMarkMoreDto dto = commonEdMarkMoreService.getModelById(id, enLogin);
        return JsonData.success(dto);
    }


    @PostMapping("/save.json")
    public JsonData save(int id,int markId,String commentContent,String enLogin) {
        commonEdMarkMoreService.save(id,markId,commentContent,enLogin);
        return JsonData.success();
    }


    @PostMapping("/listData.json")
    public JsonData listData(int markId,String enLogin) {
        return JsonData.success(commonEdMarkMoreService.listData(markId, enLogin));
    }

}
