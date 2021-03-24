package com.cmcu.mcc.hr.web;

import com.cmcu.common.JsonData;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.hr.dto.HrEducationDto;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.HrEducationService;
import com.cmcu.mcc.hr.service.HrEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/hr/education")
public class HrEducationController {


    @Autowired
    HrEducationService hrEducationService;

    @PostMapping("/update.json")
    public JsonData update(@RequestBody HrEducationDto item){
        hrEducationService.update(item);
        return JsonData.success();
    }

    @PostMapping("/getModelById.json")
    public JsonData getModelById(int educationId){
        return JsonData.success(hrEducationService.getModelById(educationId));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin) {
        return JsonData.success(hrEducationService.getNewModel(userLogin));
    }

    @PostMapping("/remove.json")
    public JsonData remove(int id){
        hrEducationService.remove(id);
        return JsonData.success();
    }

    @PostMapping("/listData.json")
    public JsonData listData(String userLogin) {
        return JsonData.success(hrEducationService.listData(userLogin));
    }




}
