package com.cmcu.mcc.business.web;

import com.cmcu.common.JsonData;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.business.dto.FiveBusinessAdvanceCollectDto;

import com.cmcu.mcc.business.service.FiveBusinessAdvanceCollectService;

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
import java.util.Map;

@RestController
@RequestMapping("/business/AdvanceCollect")
public class FiveBusinessAdvanceCollectController {
    @Autowired
    FiveBusinessAdvanceCollectService fiveOaAdvanceCollectSevice;

    @PostMapping(value = "/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(fiveOaAdvanceCollectSevice.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin){
        return JsonData.success(fiveOaAdvanceCollectSevice.getNewModel(userLogin));
    }
    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        fiveOaAdvanceCollectSevice.remove(id,userLogin);
        return JsonData.success();
    }
    @PostMapping("/downCollect.json")
    public void downCollect(int collectId,String collectMonth ,final HttpServletResponse response) throws IOException {
        String fileName = collectMonth+"预支奖金签发单.xls";
        HSSFWorkbook hssfWorkbook = fiveOaAdvanceCollectSevice.downCollect(collectId);
        response.reset();
        response.setContentType("application/csv");
        response.setHeader("Content-Disposition", "attachment; filename=" +  URLEncoder.encode(fileName, "utf-8"));
        hssfWorkbook.write(response.getOutputStream());
    }
    @PostMapping("/update.json")
    public JsonData update(@RequestBody FiveBusinessAdvanceCollectDto item){
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"操作人不能为空!");
        fiveOaAdvanceCollectSevice.update(item);
        return JsonData.success();
    }
    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin,String uiSref, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = fiveOaAdvanceCollectSevice.listPagedData(params, userLogin, uiSref, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }
}
