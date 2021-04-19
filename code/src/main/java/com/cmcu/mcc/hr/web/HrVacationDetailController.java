package com.cmcu.mcc.hr.web;

import com.common.model.JsonData;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.hr.dto.HrVacationDetailDto;
import com.cmcu.mcc.hr.entity.HrVacationDetail;
import com.cmcu.mcc.hr.service.HrVacationDetailService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/hr/vacationDetail")
public class HrVacationDetailController {
    @Autowired
    HrVacationDetailService hrVacationDetailService;


    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(hrVacationDetailService.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(int vacationId){
        return JsonData.success(hrVacationDetailService.getNewModel(vacationId));
    }
    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        hrVacationDetailService.remove(id,userLogin);
        return JsonData.success();
    }
    @PostMapping("/update.json")
    public JsonData update(@RequestBody HrVacationDetailDto item){

        return JsonData.success(hrVacationDetailService.update(item));
    }
    @PostMapping("/listDataByForeignKey.json")
    public JsonData listDataByForeignKey(int vacationId) {
        List<HrVacationDetail> hrVacationDetails = hrVacationDetailService.listDataByForeignKey(vacationId);
        return JsonData.success(hrVacationDetails);
    }
}
