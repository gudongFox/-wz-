package com.cmcu.mcc.hr.web;

import com.common.model.JsonData;
import com.cmcu.mcc.hr.dto.HrCertificationDto;
import com.cmcu.mcc.hr.dto.HrEducationDto;
import com.cmcu.mcc.hr.service.HrCertificationService;
import com.cmcu.mcc.hr.service.HrEducationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hr/certification")
public class HrCertificationController {


    @Autowired
    HrCertificationService hrCertificationService;

    @PostMapping("/update.json")
    public JsonData update(@RequestBody HrCertificationDto item){
        hrCertificationService.update(item);
        return JsonData.success();
    }

    @PostMapping("/getModelById.json")
    public JsonData getModelById(int certificationId){
        return JsonData.success(hrCertificationService.getModelById(certificationId));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin) {
        return JsonData.success(hrCertificationService.getNewModel(userLogin));
    }

    @PostMapping("/remove.json")
    public JsonData remove(int id){
        hrCertificationService.remove(id);
        return JsonData.success();
    }

    @PostMapping("/listData.json")
    public JsonData listData(String userLogin) {
        return JsonData.success(hrCertificationService.listData(userLogin));
    }




}
