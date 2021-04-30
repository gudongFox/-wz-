package com.cmcu.mcc.ed.web;

import com.common.model.JsonData;
import com.cmcu.mcc.ed.service.FiveEdService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/five/ed")
public class FiveEdController {

    @Resource
    FiveEdService fiveEdService;


    @PostMapping("/listInputReviewFile.json")
    public JsonData listInputReviewFile(int nodeId){
        return JsonData.success(fiveEdService.listInputReviewFile(nodeId));
    }


    @PostMapping("/initArrangeUserFromLatestEdTask.json")
    public JsonData initArrangeUserFromLatestEdTask(int arrangeFormDataId){
        fiveEdService.initArrangeUserFromLatestEdTask(arrangeFormDataId);
        return JsonData.success();
    }



}
