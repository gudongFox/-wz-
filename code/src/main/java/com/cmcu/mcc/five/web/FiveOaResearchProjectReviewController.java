package com.cmcu.mcc.five.web;

import com.common.model.JsonData;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.five.dto.FiveOaResearchProjectReviewDto;
import com.cmcu.mcc.five.entity.FiveOaResearchProjectReviewDetail;
import com.cmcu.mcc.five.service.FiveOaResearchProjectReviewSevice;
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
@RequestMapping("/five/oa/oaResearchProjectReview")
public class FiveOaResearchProjectReviewController {
    @Autowired
    FiveOaResearchProjectReviewSevice fiveOaResearchProjectReviewSevice;

    @PostMapping(value = "/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(fiveOaResearchProjectReviewSevice.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin){
        return JsonData.success(fiveOaResearchProjectReviewSevice.getNewModel(userLogin));
    }
    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        fiveOaResearchProjectReviewSevice.remove(id,userLogin);
        return JsonData.success();
    }
    @PostMapping("/update.json")
    public JsonData update(@RequestBody FiveOaResearchProjectReviewDto item){
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"操作人不能为空!");
        fiveOaResearchProjectReviewSevice.update(item);
        return JsonData.success();
    }
    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin,String uiSref, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = fiveOaResearchProjectReviewSevice.listPagedData(params, userLogin, uiSref, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }


    @PostMapping(value = "/getNewModelDetail.json")
    public JsonData getNewModelDetail(int researchReviewId,String userLogin){
        return JsonData.success(fiveOaResearchProjectReviewSevice.getNewModelDetail(researchReviewId,userLogin));
    }

    @PostMapping("/getModelDetailById.json")
    public JsonData getModelDetailById(int  id){
        return JsonData.success(fiveOaResearchProjectReviewSevice.getModelDetailById(id));
    }
    @PostMapping("/removeDetail.json")
    public JsonData removeDetail(int  id){
        fiveOaResearchProjectReviewSevice.removeDetail(id);
        return JsonData.success();
    }
    @PostMapping("/updateDetail.json")
    public JsonData updateDetail(@RequestBody FiveOaResearchProjectReviewDetail item){
        fiveOaResearchProjectReviewSevice.updateDetail(item);
        return JsonData.success();
    }
    @PostMapping("/listDetail.json")
    public JsonData listDetail(int  researchReviewId){
        return JsonData.success(fiveOaResearchProjectReviewSevice.listDetail(researchReviewId));
    }
//    @PostMapping("/getPrintData.json")
//    public JsonData getPrintData(int id){
//        return JsonData.success(fiveOaResearchProjectReviewSevice.getPrintData(id));
//    }
}
