package com.cmcu.mcc.business.web;

import com.cmcu.common.JsonData;
import com.cmcu.common.util.MyPoiUtil;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.business.dto.FiveBusinessTenderDocumentReviewDto;
import com.cmcu.mcc.business.service.FiveBusinessTenderDocumentReviewService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/business/tenderDocumentReview")
public class FiveBusinessTenderDocumentReviewController {
    @Autowired
    FiveBusinessTenderDocumentReviewService fiveBusinessTenderDocumentReviewService;

    @PostMapping(value = "/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(fiveBusinessTenderDocumentReviewService.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin){
        return JsonData.success(fiveBusinessTenderDocumentReviewService.getNewModel(userLogin));
    }
    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        fiveBusinessTenderDocumentReviewService.remove(id,userLogin);
        return JsonData.success();
    }
    @PostMapping("/update.json")
    public JsonData update(@RequestBody FiveBusinessTenderDocumentReviewDto item){
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"操作人不能为空!");
        fiveBusinessTenderDocumentReviewService.update(item);
        return JsonData.success();
    }
    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin,String uiSref, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = fiveBusinessTenderDocumentReviewService.listPagedData(params, userLogin, uiSref, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }

    /*@PostMapping("/updateExcl.json")
    public JsonData updateExcl(MultipartFile multipartFile, int advanceId, String userLogin) throws IOException, ParseException {
        List<Map> data = MyPoiUtil.listTableData(multipartFile.getInputStream(), 0, 0, true);
        fiveBusinessTenderDocumentReviewService.uploadExcl(data,advanceId,userLogin);
        return JsonData.success();
    }*/

}
