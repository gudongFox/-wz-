package com.cmcu.mcc.oa.web;

import com.cmcu.common.JsonData;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.oa.dto.OaNoticeDto;
import com.cmcu.mcc.oa.dto.OaTechnologyDto;
import com.cmcu.mcc.oa.entity.OaTechnology;
import com.cmcu.mcc.oa.service.OaTechnologyService;
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
@RequestMapping("/oa/technology")
public class OaTechnologyController {
    @Autowired
    OaTechnologyService oaTechnologyService;

    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = oaTechnologyService.listPagedData(params,userLogin,pageNum, pageSize);
        return JsonData.success(pageInfo);
    }
    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(oaTechnologyService.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin,String type){
        return JsonData.success(oaTechnologyService.getNewModel(userLogin,type));
    }
    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        oaTechnologyService.remove(id,userLogin);
        return JsonData.success();
    }
    @PostMapping("/update.json")
    public JsonData update(@RequestBody OaTechnologyDto item){
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"操作人不能为空!");
        oaTechnologyService.update(item);
        return JsonData.success();
    }
}
