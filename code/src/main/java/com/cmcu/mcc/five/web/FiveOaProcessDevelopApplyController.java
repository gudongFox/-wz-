package com.cmcu.mcc.five.web;

import com.cmcu.common.JsonData;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.five.dto.FiveOaProcessDevelopApplyDto;
import com.cmcu.mcc.five.service.FiveOaProcessDevelopApplyService;
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
@RequestMapping("/five/oa/processDevelopApply")
public class FiveOaProcessDevelopApplyController {

    @Autowired
    FiveOaProcessDevelopApplyService fiveOaProcessDevelopApplyService;

    @PostMapping(value = "/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(fiveOaProcessDevelopApplyService.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin){
        return JsonData.success(fiveOaProcessDevelopApplyService.getNewModel(userLogin));
    }
    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        fiveOaProcessDevelopApplyService.remove(id,userLogin);
        return JsonData.success();
    }
    @PostMapping("/update.json")
    public JsonData update(@RequestBody FiveOaProcessDevelopApplyDto item){
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"操作人不能为空!");
        fiveOaProcessDevelopApplyService.update(item);
        return JsonData.success();
    }
    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin,String uiSref, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = fiveOaProcessDevelopApplyService.listPagedData(params, userLogin, uiSref, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }

    @PostMapping("/getPrintData.json")
    public JsonData getPrintData(int id){
        return JsonData.success(fiveOaProcessDevelopApplyService.getPrintData(id));
    }
}