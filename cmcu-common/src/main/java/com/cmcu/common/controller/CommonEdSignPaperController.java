package com.cmcu.common.controller;


import com.common.model.JsonData;
import com.cmcu.common.entity.CommonEdSignPaper;
import com.cmcu.common.service.CommonEdSignPaperService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

@Controller
@RequestMapping("/common/edSignPaper")
public class CommonEdSignPaperController {


    @Resource
    CommonEdSignPaperService commonEdSignPaperService;


    @ResponseBody
    @PostMapping("/insertList.json")
    public JsonData  insertList(String tenetId,int fileId,String paperData,String enLogin) {
        return JsonData.success(StringUtils.join(commonEdSignPaperService.getSignPaperIdList(tenetId, fileId, paperData, enLogin),","));
    }


    @RequestMapping("/{signId}")
    public ModelAndView paper(@PathVariable("signId") String signId){
        ModelAndView modelAndView=new ModelAndView("paper");
        CommonEdSignPaper paper=commonEdSignPaperService.getModelBySignId(signId);
        modelAndView.addObject("paper",paper);
        modelAndView.addObject("gmtCreate", DateFormatUtils.format(paper.getGmtCreate(),"yyyy-MM-dd"));
        return modelAndView;
    }



}
