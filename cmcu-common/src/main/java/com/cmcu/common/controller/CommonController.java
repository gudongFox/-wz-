package com.cmcu.common.controller;


import com.common.model.JsonData;
import com.cmcu.common.dto.FastUserDto;
import com.cmcu.common.dto.UserDto;
import com.cmcu.common.entity.CommonConfig;
import com.cmcu.common.entity.CommonFile;
import com.cmcu.common.service.CommonConfigService;
import com.cmcu.common.service.CommonFileService;
import com.cmcu.common.service.CommonUserService;
import com.cmcu.common.util.CookieSessionUtils;
import com.cmcu.common.util.GuavaCacheService;
import com.cmcu.common.util.MyStringUtil;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

@Controller
@RequestMapping("/common")
public class CommonController {


    @Resource
    GuavaCacheService guavaCacheService;

    @Resource
    CommonFileService commonFileService;

    @Resource
    CommonUserService commonUserService;

    @Resource
    CommonConfigService commonConfigService;

    @ResponseBody
    @RequestMapping("/getServerStatus.json")
    public JsonData getServerStatus(){
        return JsonData.success("request server good");
    }


    @ResponseBody
    @RequestMapping("/getOwaStatus.json")
    public JsonData getOwaStatus() {
        Map result = Maps.newHashMap();
        CommonConfig commonConfig=commonConfigService.getConfig();
        result.put("owa", commonConfig.getEnableOwa());
        result.put("owaServer", commonConfig.getOwaServer());
        result.put("owaFileType", MyStringUtil.getStringList(commonConfig.getOwaFileType()));
        return JsonData.success(result);
    }


    @ResponseBody
    @RequestMapping("/getAppConfig.json")
    public JsonData getAppConfig(String tenetId) {
        Map result = Maps.newHashMap();
        return JsonData.success(result);
    }


    @ResponseBody
    @RequestMapping("/invalidateCache.json")
    public JsonData invalidateAll(){
        guavaCacheService.invalidateAll();
        return JsonData.success("clear all cache success");
    };


    @RequestMapping("/previewFile/{fileId}")
    public ModelAndView previewFile(@PathVariable("fileId") int fileId) {
        ModelAndView modelAndView = new ModelAndView("common/previewFile");
        String enLogin = CookieSessionUtils.getSession("enLogin");
        if (StringUtils.isEmpty(enLogin)) {
            return new ModelAndView("redirect:/login");
        }
        CommonConfig commonConfig = commonConfigService.getConfig();
        FastUserDto userDto = commonUserService.getFastByEnLogin(enLogin);
        CommonFile file = commonFileService.getModelById(fileId);
        modelAndView.addObject("fileName", file.getFileName());
        modelAndView.addObject("src", commonConfig.getOwaServer() + "/common/attach/download/" + file.getAttachId());
        modelAndView.addObject("sec", userDto.getCnName() + " " + enLogin);
        return modelAndView;
    }


}


