package com.cmcu.mcc.oa.web;

import com.cmcu.common.JsonData;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.oa.dto.OaNoticeApplyDto;
import com.cmcu.mcc.oa.service.OaNoticeApplyService;
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
@RequestMapping("/oa/noticeApply")
public class OaNoticeApplyController {
    @Autowired
    OaNoticeApplyService oaNoticeApplyService;

    @PostMapping(value = "/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(oaNoticeApplyService.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin){
        return JsonData.success(oaNoticeApplyService.getNewModel(userLogin));
    }

    @PostMapping("/getNewModelByType.json")
    public JsonData getNewModelByType(String userLogin,String type){
        return JsonData.success(oaNoticeApplyService.getNewModelByType(userLogin,type));
    }
    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        oaNoticeApplyService.remove(id,userLogin);
        return JsonData.success();
    }
    @PostMapping("/update.json")
    public JsonData update(@RequestBody OaNoticeApplyDto item){
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"操作人不能为空!");
        oaNoticeApplyService.update(item);
        return JsonData.success();
    }
    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin,String uiSref, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = oaNoticeApplyService.listPagedData(params, userLogin, uiSref, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }

    @PostMapping("/manualTurnNotice.json")
    public JsonData manualTurnNotice(int id,String types){
        oaNoticeApplyService.manualTurnNotice(id,types);
        return JsonData.success();
    }
}
