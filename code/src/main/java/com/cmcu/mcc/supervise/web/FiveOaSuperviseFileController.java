package com.cmcu.mcc.supervise.web;

import com.common.model.JsonData;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.supervise.dto.FiveOaSuperviseFileDto;
import com.cmcu.mcc.supervise.service.FiveOaSuperviseFileService;
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
@RequestMapping("/five/superviseFile")
public class FiveOaSuperviseFileController {
    @Autowired
    FiveOaSuperviseFileService fiveOaSuperviseFileService;
    @PostMapping(value = "/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(fiveOaSuperviseFileService.getModelById(id));
    }
    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin){
        return JsonData.success(fiveOaSuperviseFileService.getNewModel(userLogin));
    }
    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        fiveOaSuperviseFileService.remove(id,userLogin);
        return JsonData.success();
    }
    @PostMapping("/update.json")
    public JsonData update(@RequestBody FiveOaSuperviseFileDto item){
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"操作人不能为空!");
        fiveOaSuperviseFileService.update(item);
        return JsonData.success();
    }
    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin,String uiSref, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = fiveOaSuperviseFileService.listPagedData(params, userLogin, uiSref, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }

    @PostMapping("/getNewModelByType.json")
    public JsonData getNewModelByType(String userLogin,String superviseType,String companyLeader,String businessId,String fileTitle,String view){
        return JsonData.success(fiveOaSuperviseFileService.getNewModelByType(userLogin,superviseType,companyLeader,businessId,fileTitle,view));
    }
}
