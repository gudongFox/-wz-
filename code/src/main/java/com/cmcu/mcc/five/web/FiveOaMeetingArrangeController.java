package com.cmcu.mcc.five.web;

import com.cmcu.common.JsonData;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.five.dto.FiveOaMeetingArrangeDto;
import com.cmcu.mcc.five.service.FiveOaMeetingArrangeService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.Map;

@RestController
@RequestMapping("five/oa/meetingArrange")
public class FiveOaMeetingArrangeController {

    @Autowired
    FiveOaMeetingArrangeService fiveOaMeetingArrangeService;

    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin,String uiSref, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = fiveOaMeetingArrangeService.listPagedData(params, userLogin, uiSref, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }


    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id) {
        return  JsonData.success(fiveOaMeetingArrangeService.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin) {
        return JsonData.success(fiveOaMeetingArrangeService.getNewModel(userLogin));
    }

    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin) {
        fiveOaMeetingArrangeService.remove(id,userLogin);
        return JsonData.success();
    }

    @PostMapping("/update.json")
    public JsonData update(@RequestBody FiveOaMeetingArrangeDto item) throws ParseException {
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"操作人不能为空!");
        fiveOaMeetingArrangeService.update(item);
        return  JsonData.success();
    }



}
