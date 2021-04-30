package com.cmcu.common.controller;

import com.common.model.JsonData;
import com.cmcu.common.entity.CommonEdLayerExtraction;
import com.cmcu.common.service.CommonEdLayerExtractionService;
import com.cmcu.common.util.WebUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/common/edLayerExtraction")
public class CommonEdLayerExtractionController {


    @Resource
    CommonEdLayerExtractionService commonEdLayerExtractionService;

    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String enLogin, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        return JsonData.success(commonEdLayerExtractionService.listPagedData(params, enLogin,pageNum, pageSize));
    }

    @PostMapping("/selectAll.json")
    public JsonData selectAll(String tenetId,String majorName,String enLogin) {
        List<CommonEdLayerExtraction> all = commonEdLayerExtractionService.selectAll(tenetId);
        if(StringUtils.isNotEmpty(majorName)) all=all.stream().filter(p->p.getSourceMajor().contains(majorName)).collect(Collectors.toList());
        return JsonData.success(all);
    }


    @PostMapping("/update.json")
    public JsonData update(@RequestBody CommonEdLayerExtraction item){
        commonEdLayerExtractionService.update(item);
        return JsonData.success();
    }

    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(commonEdLayerExtractionService.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String enLogin) {
        JsonData jsonData =JsonData.success(commonEdLayerExtractionService.getNewModel(enLogin));
        return jsonData;
    }

    @PostMapping("/remove.json")
    public JsonData remove(int id){
        commonEdLayerExtractionService.remove(id);
        return JsonData.success();
    }


    @PostMapping("/insert.json")
    public JsonData insert(String tenetId,String enLogin,String extractName,String extractLayer,String extractDesc,String sourceMajor,String destMajor) {
        commonEdLayerExtractionService.insert(tenetId, enLogin, extractName, extractLayer, extractDesc, sourceMajor, destMajor);
        return JsonData.success();
    }

}
