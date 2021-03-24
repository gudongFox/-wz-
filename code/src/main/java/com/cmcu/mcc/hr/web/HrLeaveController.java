package com.cmcu.mcc.hr.web;

import com.cmcu.common.JsonData;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.hr.dto.HrLeaveDto;
import com.cmcu.mcc.hr.service.HrLeaveService;
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
@RequestMapping("/hr/leave")
public class HrLeaveController {

    @Autowired
    HrLeaveService hrLeaveService;

    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(hrLeaveService.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(int foreignKey,String userLogin){
        return JsonData.success(hrLeaveService.getNewModel(foreignKey,userLogin));
    }
    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        hrLeaveService.remove(id,userLogin);
        return JsonData.success();
    }
    @PostMapping("/update.json")
    public JsonData update(@RequestBody HrLeaveDto item){
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"操作人不能为空!");
        hrLeaveService.update(item);
        return JsonData.success();
    }

    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = hrLeaveService.listPagedData(params, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }
}
