package com.cmcu.mcc.five.web;

import com.common.model.JsonData;
import com.cmcu.common.util.FileUtil;
import com.cmcu.common.util.MyPoiUtil;
import com.cmcu.mcc.five.dto.FiveEdMajorDrawingCheckDto;
import com.cmcu.mcc.five.entity.FiveEdMajorDrawingCheckDetail;
import com.cmcu.mcc.five.service.FiveEdMajorDrawingCheckService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/five/majorDrawingCheck")
public class FiveEdMajorDrawingCheckController {
    @Autowired
    FiveEdMajorDrawingCheckService fiveEdMajorDrawingCheckService;

    @PostMapping(value = "/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(fiveEdMajorDrawingCheckService.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(int stepId,String userLogin){
        return JsonData.success(fiveEdMajorDrawingCheckService.getNewModel(stepId,userLogin).getId());
    }
    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        fiveEdMajorDrawingCheckService.remove(id,userLogin);
        return JsonData.success();
    }
    @PostMapping("/update.json")
    public JsonData update(@RequestBody FiveEdMajorDrawingCheckDto item){
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"操作人不能为空!");
        fiveEdMajorDrawingCheckService.update(item);
        return JsonData.success();
    }
    @PostMapping("/listDataByStepId.json")
    public JsonData listDataByStepId(int stepId,String userLogin) {
        return JsonData.success( fiveEdMajorDrawingCheckService.listDataByStepId(stepId, userLogin));
    }

    @PostMapping("/getBuildList.json")
    public JsonData getBuildList(String stepId) {
        return JsonData.success( fiveEdMajorDrawingCheckService.getBuildList(stepId));
    }

    @PostMapping("/getMajorList.json")
    public JsonData getMajorList(int stepId,String userLogin) {
        return JsonData.success( fiveEdMajorDrawingCheckService.getMajorList(stepId, userLogin));
    }

    @PostMapping(value = "/getNewModelDetail.json")
    public JsonData getNewModelDetail(int checkId){
        return JsonData.success(fiveEdMajorDrawingCheckService.getNewModelDetail(checkId));
    }

    @PostMapping("/getModelDetailById.json")
    public JsonData getModelDetailById(int  id){
        return JsonData.success(fiveEdMajorDrawingCheckService.getModelDetailById(id));
    }
    @PostMapping("/removeDetail.json")
    public JsonData removeDetail(int  id){
        fiveEdMajorDrawingCheckService.removeDetail(id);
        return JsonData.success();
    }
    @PostMapping("/updateDetail.json")
    public JsonData updateDetail(@RequestBody FiveEdMajorDrawingCheckDetail item){
        fiveEdMajorDrawingCheckService.updateDetail(item);
        return JsonData.success();
    }
    @PostMapping("/listDetail.json")
    public JsonData listDetail(int checkId){
        return JsonData.success(fiveEdMajorDrawingCheckService.listDetail(checkId));
    }
    @PostMapping("/getPrintData.json")
    public JsonData getPrintData(int id){
        return JsonData.success(fiveEdMajorDrawingCheckService.getPrintData(id));
    }
    @PostMapping("/downTempleXls.json")
    public void downTempleXls(int stepId,final HttpServletResponse response) {
        List<Map> list = fiveEdMajorDrawingCheckService.listMapData(stepId);
        MyPoiUtil.downloadExcel(list, FileUtil.getGoodFileName("专业图纸验收清单"+ DateFormatUtils.format(new Date(),"yyyyMMddHHmm")+".xls"), response);
    }

}
