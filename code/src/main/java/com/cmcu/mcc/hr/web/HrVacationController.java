package com.cmcu.mcc.hr.web;

import com.cmcu.common.JsonData;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.hr.dto.HrVacationDto;
import com.cmcu.mcc.hr.service.HrVacationService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/hr/vacation")
public class HrVacationController {
    @Autowired
    HrVacationService hrVacationService;

    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(hrVacationService.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(int foreignKey,String userLogin){
        return JsonData.success(hrVacationService.getNewModel(foreignKey,userLogin));
    }
    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        hrVacationService.remove(id,userLogin);
        return JsonData.success();
    }
    @PostMapping("/update.json")
    public JsonData update(@RequestBody HrVacationDto item){
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"操作人不能为空!");
        hrVacationService.update(item);
        return JsonData.success();
    }

    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = hrVacationService.listPagedData(params, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }

}
