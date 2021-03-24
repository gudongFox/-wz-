package com.cmcu.mcc.five.web;

import com.cmcu.common.JsonData;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.five.dto.FiveOaDispatchCpcaAcademyDto;

import com.cmcu.mcc.five.service.FiveOaDispatchCpcaAcademyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/five/oa/dispatchCpcaAcademy")
public class FiveOaDispatchCpcaAcademyController {
    @Autowired
    FiveOaDispatchCpcaAcademyService fiveOaDispatchCpcaAcademyService;

    @PostMapping(value = "/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(fiveOaDispatchCpcaAcademyService.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin){
        return JsonData.success(fiveOaDispatchCpcaAcademyService.getNewModel(userLogin));
    }
    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        fiveOaDispatchCpcaAcademyService.remove(id,userLogin);
        return JsonData.success();
    }
    @PostMapping("/update.json")
    public JsonData update(@RequestBody FiveOaDispatchCpcaAcademyDto item){
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"操作人不能为空!");
        fiveOaDispatchCpcaAcademyService.update(item);
        return JsonData.success();
    }
    @PostMapping(value = "/listPagedData.json")
    public JsonData loadPagedData(int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        return JsonData.success(fiveOaDispatchCpcaAcademyService.listPagedData(params,pageNum, pageSize));
    }
}
