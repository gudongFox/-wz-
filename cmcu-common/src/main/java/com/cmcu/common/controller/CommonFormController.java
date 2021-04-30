package com.cmcu.common.controller;

import com.common.model.JsonData;
import com.cmcu.common.entity.CommonEdDwgPicture;
import com.cmcu.common.entity.CommonForm;
import com.cmcu.common.entity.CommonFormDetail;
import com.cmcu.common.service.CommonFormService;
import com.cmcu.common.util.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/common/form")
public class CommonFormController {
    @Autowired
    CommonFormService commonFormService;


    @PostMapping("/update.json")
    public JsonData update(@RequestBody CommonForm item){
        commonFormService.update(item);
        return JsonData.success();
    }
    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(commonFormService.getModelById(id));
    }


    @PostMapping("/getModelByKey.json")
    public JsonData getModelByKey(String tenetId,String formKey) {
        return JsonData.success(commonFormService.getModel(tenetId, formKey, 0));
    }



    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String enLogin ) {
        return JsonData.success(commonFormService.getNewModel(enLogin));
    }
    @PostMapping("/remove.json")
    public JsonData remove(int id,String enLogin){
        commonFormService.remove(id,enLogin);
        return JsonData.success();
    }
    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(int pageNum, int pageSize,String tenetId) {
        Map params = WebUtil.getRequestParameters();
        return JsonData.success(commonFormService.listPagedData(params,tenetId,pageNum,pageSize));
    }
    @PostMapping("/removeDetail.json")
    public JsonData removeDetail(int id,String enLogin){
        commonFormService.removeDetail(id,enLogin);
        return JsonData.success();
    }
    @PostMapping("/getModelDetailById.json")
    public JsonData getModelDetailById(int id){
        return JsonData.success(commonFormService.getModelDetailById(id));
    }

    @PostMapping("/getNewModelDetail.json")
    public JsonData getNewModelDetail(int formId ) {
        return JsonData.success(commonFormService.getNewModelDetail(formId));
    }
    @PostMapping("/updateDetail.json")
    public JsonData updateDetail(@RequestBody CommonFormDetail detail){
        commonFormService.updateDetail(detail);
        return JsonData.success();
    }
    @PostMapping("/listDetail.json")
    public JsonData listDetail(int formId){
        return JsonData.success(commonFormService.listDetail(formId));
    }
}
