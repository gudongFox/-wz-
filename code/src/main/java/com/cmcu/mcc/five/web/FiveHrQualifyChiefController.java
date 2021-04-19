package com.cmcu.mcc.five.web;


import com.common.model.JsonData;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.five.dto.FiveEdStampDto;
import com.cmcu.mcc.five.dto.FiveHrQualifyChiefDto;
import com.cmcu.mcc.five.entity.FiveHrQualifyChief;
import com.cmcu.mcc.five.service.FiveHrQualifyChiefService;
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
@RequestMapping("/five/hr/qualifyChief")
public class FiveHrQualifyChiefController {

    @Autowired
    FiveHrQualifyChiefService fiveHrQualifyChiefService;

    @PostMapping("/update.json")
    public JsonData update(@RequestBody FiveHrQualifyChiefDto item){
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"当前登录用户不能为空!");
        fiveHrQualifyChiefService.update(item);
        return JsonData.success();
    }

    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        fiveHrQualifyChiefService.remove(id,userLogin);
        return JsonData.success();
    }

    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(fiveHrQualifyChiefService.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String uiSref,String userLogin){
        return JsonData.success(fiveHrQualifyChiefService.getNewModel(uiSref,userLogin));
    }

    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin,String uiSref, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = fiveHrQualifyChiefService.listPagedData(params, userLogin, uiSref, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }

}
