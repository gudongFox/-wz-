package com.cmcu.mcc.ed.web;

import com.common.model.JsonData;
import com.cmcu.mcc.ed.dto.FiveEdDesignDrawingCheckDto;
import com.cmcu.mcc.ed.service.FiveEdDesignDrawingCheckService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/five/designDrawingCheck")
public class FiveEdDesignDrawingCheckController {
    @Autowired
    FiveEdDesignDrawingCheckService fiveEdDesignDrawingCheckService;

    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(fiveEdDesignDrawingCheckService.getModelById(id));
    }

    @PostMapping("/listDataByStepId.json")
    public JsonData listDataByStepId(int stepId,String userLogin){
        return JsonData.success(fiveEdDesignDrawingCheckService.listDataByStepId(stepId,userLogin));
    }
    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(int stepId,String userLogin){
        return JsonData.success(fiveEdDesignDrawingCheckService.getNewModel(stepId,userLogin).getId());
    }
    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        fiveEdDesignDrawingCheckService.remove(id,userLogin);
        return JsonData.success();
    }
    @PostMapping("/update.json")
    public JsonData update(@RequestBody FiveEdDesignDrawingCheckDto item){
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"操作人不能为空!");
        fiveEdDesignDrawingCheckService.update(item);
        return JsonData.success();
    }

}
