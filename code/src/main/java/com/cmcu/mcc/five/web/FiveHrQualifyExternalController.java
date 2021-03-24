package com.cmcu.mcc.five.web;

import com.cmcu.common.JsonData;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.five.dto.FiveHrQualifyExternalDto;
import com.cmcu.mcc.five.entity.FiveHrQualifyExternal;
import com.cmcu.mcc.five.service.FiveHrQualifyExternalService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author hnz
 * @date 2019/10/29
 */
@RestController
@RequestMapping("/five/hr/qualifyExternal")
public class FiveHrQualifyExternalController {
    @Autowired
    FiveHrQualifyExternalService fiveHrQualifyExternalService;

    @PostMapping("/update.json")
    public JsonData update(@RequestBody FiveHrQualifyExternalDto item){
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"当前登录用户不能为空!");
        fiveHrQualifyExternalService.update(item);
        return JsonData.success();
    }

    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        fiveHrQualifyExternalService.remove(id,userLogin);
        return JsonData.success();
    }

    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(fiveHrQualifyExternalService.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String uiSref,String userLogin){
        return JsonData.success(fiveHrQualifyExternalService.getNewModel(uiSref,userLogin));
    }

    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin,String uiSref, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = fiveHrQualifyExternalService.listPagedData(params, userLogin, uiSref, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }
}
