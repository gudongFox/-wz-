package com.cmcu.mcc.five.web;

import com.common.model.JsonData;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.five.dto.FiveOaResearchProjectLibraryDto;
import com.cmcu.mcc.five.service.FiveOaResearchProjectLibraryService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/five/oa/researchProjectLibrary")
public class FiveOaResearchProjectLibraryController {

    @Resource
    FiveOaResearchProjectLibraryService fiveOaResearchProjectLibraryService;

    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin, String uiSref, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = fiveOaResearchProjectLibraryService.listPagedData(uiSref,params, userLogin, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }

    @PostMapping("/listDataByType.json")
    public JsonData listDataByType(String userLogin, String uiSref) {
        return JsonData.success( fiveOaResearchProjectLibraryService.listDataByType(uiSref, userLogin ));
    }

    @PostMapping(value = "/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(fiveOaResearchProjectLibraryService.getModelById(id));
    }
    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        fiveOaResearchProjectLibraryService.remove(id,userLogin);
        return JsonData.success();
    }
    @PostMapping("/update.json")
    public JsonData update(@RequestBody FiveOaResearchProjectLibraryDto item){
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"操作人不能为空!");
        fiveOaResearchProjectLibraryService.update(item);
        return JsonData.success();
    }


}
