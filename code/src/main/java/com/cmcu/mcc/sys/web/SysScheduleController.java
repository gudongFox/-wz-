package com.cmcu.mcc.sys.web;

import com.cmcu.common.JsonData;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.sys.dto.SysScheduleDto;
import com.cmcu.mcc.sys.service.SysScheduleService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

/**
 * @author hnz
 * @date 2019/11/5
 */
@RestController
@RequestMapping("/sys/schedule")
public class SysScheduleController {
    @Autowired
    SysScheduleService sysScheduleService;

    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(sysScheduleService.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin){
        return JsonData.success(sysScheduleService.getNewModel(userLogin));
    }
    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        sysScheduleService.remove(id,userLogin);
        return JsonData.success();
    }
    @PostMapping("/insert.json")
    public JsonData insert(String start,String end,String type,String title,String userLogin){
        return JsonData.success(sysScheduleService.insert(start,end,type,title,userLogin));
    }
    @PostMapping("/update.json")
    public JsonData update(@RequestBody SysScheduleDto item){
        sysScheduleService.update(item);
        return JsonData.success();
    }
    @PostMapping("/selectAll.json")
    public JsonData selectAll(String userLogin){
        return JsonData.success( sysScheduleService.selectAll(userLogin));
    }
}
