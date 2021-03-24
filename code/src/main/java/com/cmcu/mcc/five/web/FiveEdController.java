package com.cmcu.mcc.five.web;

import com.cmcu.common.JsonData;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.five.service.FiveEdService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

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
