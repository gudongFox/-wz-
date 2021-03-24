package com.cmcu.mcc.hr.web;

import com.cmcu.common.JsonData;
import com.cmcu.common.util.WebUtil;

import com.cmcu.mcc.hr.dto.HrEmployeeProofDto;
import com.cmcu.mcc.hr.service.HrEmployeeProofService;
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
@RequestMapping("/hr/employeeProof")
public class HrEmployeeProofController {
    @Autowired
    HrEmployeeProofService hrEmployeeProofService;

    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(hrEmployeeProofService.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin){
        return JsonData.success(hrEmployeeProofService.getNewModel(userLogin));
    }
    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        hrEmployeeProofService.remove(id,userLogin);
        return JsonData.success();
    }
    @PostMapping("/update.json")
    public JsonData update(@RequestBody HrEmployeeProofDto item){
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"操作人不能为空!");
        hrEmployeeProofService.update(item);
        return JsonData.success();
    }

    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = hrEmployeeProofService.listPagedData(params, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }
    @PostMapping("/getPrintData.json")
    public JsonData getPrintData(int id){
        return JsonData.success(hrEmployeeProofService.getPrintData(id));
    }

}
