package com.cmcu.mcc.five.web;

import com.common.model.JsonData;
import com.cmcu.common.util.FileUtil;
import com.cmcu.common.util.MyPoiUtil;
import com.cmcu.common.util.WebUtil;

import com.cmcu.mcc.five.dto.FiveOaExternalResearchProjectApplyDto;
import com.cmcu.mcc.five.service.FiveOaExternalResearchProjectApplyService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/five/oa/externalResearchProjectApply")
public class FiveOaExternalResearchProjectApplyController {
    @Autowired
    FiveOaExternalResearchProjectApplyService fiveOaExternalResearchProjectApplyService;

    @PostMapping(value = "/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(fiveOaExternalResearchProjectApplyService.getModelById(id));
    }

    @PostMapping("/downTempleXls.json")
    public void downTempleXls(String startTime,String uiSref,String endTime,String userLogin,final HttpServletResponse response) {
        Map params = WebUtil.getRequestParameters();
        List<Map> list = fiveOaExternalResearchProjectApplyService.listMapData(params,uiSref,startTime,endTime,userLogin);
        MyPoiUtil.downloadExcel(list, FileUtil.getGoodFileName("外部科研项目申请"+ DateFormatUtils.format(new Date(),"yyyyMMddHHmm")+".xls"), response);
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin){
        return JsonData.success(fiveOaExternalResearchProjectApplyService.getNewModel(userLogin));
    }
    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        fiveOaExternalResearchProjectApplyService.remove(id,userLogin);
        return JsonData.success();
    }
    @PostMapping("/update.json")
    public JsonData update(@RequestBody FiveOaExternalResearchProjectApplyDto item){
        fiveOaExternalResearchProjectApplyService.update(item);
        return JsonData.success();
    }
    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin,String uiSref, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = fiveOaExternalResearchProjectApplyService.listPagedData(params, userLogin, uiSref, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }

}
