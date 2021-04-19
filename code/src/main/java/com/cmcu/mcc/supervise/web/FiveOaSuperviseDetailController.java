package com.cmcu.mcc.supervise.web;

import com.common.model.JsonData;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.supervise.dto.FiveOaSuperviseDetailDto;
import com.cmcu.mcc.supervise.service.FiveOaSuperviseDetailService;

import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/five/superviseDetail")
public class FiveOaSuperviseDetailController {
    @Autowired
    FiveOaSuperviseDetailService fiveOaSuperviseDetailService;
    @PostMapping(value = "/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(fiveOaSuperviseDetailService.getModelById(id));
    }
    @PostMapping("/getNewModelById.json")
    public JsonData getNewModelById(String userLogin,int superviseId){
        return JsonData.success(fiveOaSuperviseDetailService.getNewModelById(userLogin,superviseId));
    }
    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        fiveOaSuperviseDetailService.remove(id,userLogin);
        return JsonData.success();
    }
    @PostMapping("/update.json")
    public JsonData update(@RequestBody FiveOaSuperviseDetailDto item){
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"操作人不能为空!");
        fiveOaSuperviseDetailService.update(item);
        return JsonData.success();
    }
    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin,String uiSref, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = fiveOaSuperviseDetailService.listPagedData(params, userLogin, uiSref, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }
    @PostMapping("/listDate.json")
    public JsonData listDate(int superviseId){

        return JsonData.success(fiveOaSuperviseDetailService.listDate(superviseId));
    }

}
