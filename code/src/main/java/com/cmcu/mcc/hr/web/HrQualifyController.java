package com.cmcu.mcc.hr.web;

import com.cmcu.common.JsonData;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.hr.dto.HrContractDto;
import com.cmcu.mcc.hr.dto.HrQualifyDto;
import com.cmcu.mcc.hr.entity.HrQualify;
import com.cmcu.mcc.hr.service.HrContractService;
import com.cmcu.mcc.hr.service.HrQualifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/hr/qualify")
public class HrQualifyController {


    @Autowired
    HrQualifyService hrQualifyService;

    @PostMapping("/update.json")
    public JsonData update(@RequestBody HrQualifyDto item){
        hrQualifyService.update(item);
        return JsonData.success();
    }

    @PostMapping("/initData.json")
    public JsonData initData(){
        hrQualifyService.initData();
        return JsonData.success();
    }

    @PostMapping("/toggleEnable.json")
    public JsonData toggleEnable(int id,String role){
        hrQualifyService.toggleEnable(id,role);
        return JsonData.success();
    }

    @PostMapping("/updateMajor.json")
    public JsonData updateMajor(int id,String majorName){
        hrQualifyService.updateMajor(id,majorName);
        return JsonData.success();
    }

    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(hrQualifyService.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin) {
        return JsonData.success(hrQualifyService.getNewModel(userLogin));
    }

    @PostMapping("/remove.json")
    public JsonData remove(int id){
        hrQualifyService.remove(id);
        return JsonData.success();
    }

    @PostMapping("/copy.json")
    public JsonData copy(int id){
        hrQualifyService.copy(id);
        return JsonData.success();
    }

    @PostMapping("/listData.json")
    public JsonData listData(String userLogin) {
        return JsonData.success(hrQualifyService.listData(userLogin));
    }


    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        return JsonData.success(hrQualifyService.loadPagedData(params, pageNum, pageSize));
    }

    @PostMapping("/successApply.json")
    public JsonData remove(){

        return JsonData.success();
    }


}
