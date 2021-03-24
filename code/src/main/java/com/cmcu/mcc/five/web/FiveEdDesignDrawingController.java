package com.cmcu.mcc.five.web;

import com.cmcu.common.JsonData;
import com.cmcu.mcc.five.service.FiveEdDesignDrawingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/five/designDrawing")
public class FiveEdDesignDrawingController {
    @Autowired
    FiveEdDesignDrawingService fiveEdDesignDrawingService;

    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(fiveEdDesignDrawingService.getModelById(id));
    }

    @PostMapping("/listDataByStepId.json")
    public JsonData listDataByStepId(int stepId,String userLogin){
        return JsonData.success(fiveEdDesignDrawingService.listDataByStepId(stepId,userLogin));
    }

    @PostMapping("/listDetail.json")
    public JsonData listDetail(int drawingId){
        return JsonData.success(fiveEdDesignDrawingService.listDetail(drawingId));
    }

    @PostMapping("/getModelDetailById.json")
    public JsonData getModelDetailById(int id){
        return JsonData.success(fiveEdDesignDrawingService.getModelDetailById(id));
    }
}
