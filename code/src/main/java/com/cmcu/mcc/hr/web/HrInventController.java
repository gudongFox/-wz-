package com.cmcu.mcc.hr.web;

import com.common.model.JsonData;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.hr.dto.HrCertificationDto;
import com.cmcu.mcc.hr.dto.HrInventDto;
import com.cmcu.mcc.hr.service.HrCertificationService;
import com.cmcu.mcc.hr.service.HrInventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/hr/invent")
public class HrInventController {


    @Autowired
    HrInventService hrInventService;

    @PostMapping("/update.json")
    public JsonData update(@RequestBody HrInventDto item){
        hrInventService.update(item);
        return JsonData.success();
    }

    @PostMapping("/getModelById.json")
    public JsonData getModelById(int inventId){
        return JsonData.success(hrInventService.getModelById(inventId));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin) {
        return JsonData.success(hrInventService.getNewModel(userLogin));
    }

    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        hrInventService.remove(id,userLogin);
        return JsonData.success();
    }

    @PostMapping("/listData.json")
    public JsonData listData(String userLogin) {
        return JsonData.success(hrInventService.listData(userLogin));
    }

    @PostMapping(value = "/listPagedData.json")
    public JsonData loadPagedData(int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        return JsonData.success(hrInventService.listPagedData(params,pageNum, pageSize));
    }
    @PostMapping("/getPrintData.json")
    public JsonData getPrintData(int id){
        return JsonData.success(hrInventService.getPrintData(id));
    }


}
