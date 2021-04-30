package com.cmcu.common.controller;

import com.common.model.JsonData;
import com.cmcu.common.entity.CommonExport;
import com.cmcu.common.service.CommonExportService;
import com.cmcu.common.util.FileUtil;
import com.cmcu.common.util.MyPoiUtil;
import com.cmcu.common.util.WebUtil;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/common/export")
public class CommonExportController {


    @Resource
    CommonExportService commonExportService;

    @PostMapping("/listData.json")
    public JsonData listData(String enLogin){
        return JsonData.success(commonExportService.listData(enLogin));
    }

    @PostMapping("/save.json")
    public JsonData save(@RequestBody CommonExport item){
        commonExportService.save(item);
        return JsonData.success();
    }

    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(commonExportService.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String enLogin) {
        JsonData jsonData =JsonData.success(commonExportService.getNewModel(enLogin));
        return jsonData;
    }

    @PostMapping("/remove.json")
    public JsonData remove(int id,String enLogin){
        commonExportService.remove(id,enLogin);
        return JsonData.success();
    }

    @RequestMapping("/download/{id}")
    public void  download(@PathVariable int id, final HttpServletResponse response) {
        Map params = WebUtil.getRequestParameters();
        CommonExport item = commonExportService.getModelById(id);
        List<Map> list = commonExportService.listSelectData(id,params);
        MyPoiUtil.downloadExcel(list, FileUtil.getGoodFileName(item.getName() +"-"+ DateFormatUtils.format(new Date(),"yyyyMMddHHmm")+".xls"), response);
    }


    @PostMapping("/listSelectData.json")
    public JsonData listSelectData(int id) {
        Map params = WebUtil.getRequestParameters();
        JsonData jsonData =JsonData.success(commonExportService.listSelectData(id,params));
        return jsonData;
    }
}
