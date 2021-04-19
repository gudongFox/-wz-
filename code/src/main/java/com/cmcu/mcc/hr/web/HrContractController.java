package com.cmcu.mcc.hr.web;

import com.common.model.JsonData;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.hr.dto.HrContractDto;
import com.cmcu.mcc.hr.dto.HrEducationDto;
import com.cmcu.mcc.hr.entity.HrContract;
import com.cmcu.mcc.hr.service.HrContractService;
import com.cmcu.mcc.hr.service.HrEducationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/hr/contract")
public class HrContractController {


    @Autowired
    HrContractService hrContractService;

    @PostMapping("/update.json")
    public JsonData update(@RequestBody HrContractDto item){
        hrContractService.update(item);
        return JsonData.success();
    }

    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(hrContractService.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin) {
        return JsonData.success(hrContractService.getNewModel(userLogin));
    }

    @PostMapping("/remove.json")
    public JsonData remove(int id){
        hrContractService.remove(id);
        return JsonData.success();
    }
    @PostMapping("/listData.json")
    public JsonData listData(String userLogin) {
        return JsonData.success(hrContractService.listData(userLogin));
    }

}
