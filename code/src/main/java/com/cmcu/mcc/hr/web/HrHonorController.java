package com.cmcu.mcc.hr.web;

import com.common.model.JsonData;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.hr.dto.HrHonorDto;
import com.cmcu.mcc.hr.dto.HrInventDto;
import com.cmcu.mcc.hr.entity.HrHonor;
import com.cmcu.mcc.hr.service.HrHonorService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/hr/honor")
public class HrHonorController {
    @Autowired
    HrHonorService hrHonorService;

    @PostMapping("/update.json")
    public JsonData update(@RequestBody HrHonorDto item){
        hrHonorService.update(item);
        return JsonData.success();
    }

    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(hrHonorService.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin) {
        return JsonData.success(hrHonorService.getNewModel(userLogin));
    }

    @PostMapping("/remove.json")
    public JsonData remove(int id, String userLogin){
        hrHonorService.remove(id,userLogin);
        return JsonData.success();
    }

    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(int pageNum, int pageSize,String userLogin) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = hrHonorService.listPagedData(params,userLogin, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }

}
