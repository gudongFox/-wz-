package com.cmcu.common.controller;

import com.common.model.JsonData;
import com.cmcu.common.service.CommonEdArrangeUserService;
import com.cmcu.common.util.MyPoiUtil;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/common/edArrangeUser")
public class CommonEdArrangeUserController {

    @Resource
    CommonEdArrangeUserService commonEdArrangeUserService;

    @PostMapping("/getDefaultArrangeBusinessKey.json")
    public JsonData getDefaultArrangeBusinessKey(int stepId){
        String businessKey=commonEdArrangeUserService.getDefaultArrangeBusinessKey(stepId);
        return JsonData.success(businessKey);
    }



    @PostMapping("/listData.json")
    public JsonData listData(String businessKey,String buildBusinessKey) {
        Map result = Maps.newHashMap();
        List<Map> users = commonEdArrangeUserService.listData(businessKey, buildBusinessKey);
        result.put("majorNameList", users.stream().filter(p-> StringUtils.isNotEmpty(p.getOrDefault("majorName","").toString())).map(p -> p.get("majorName").toString()).distinct().collect(Collectors.toList()));
        result.put("buildNameList", users.stream().filter(p-> StringUtils.isNotEmpty(p.getOrDefault("buildName","").toString())).map(p -> p.get("buildName").toString()).distinct().collect(Collectors.toList()));
        result.put("list", users);
        return JsonData.success(result);
    }


    @PostMapping("/setUser.json")
    public JsonData setUser(int arrangeId,String roleName,String roleCode,String allUser){
        commonEdArrangeUserService.setUser(arrangeId,roleName,roleCode,allUser);
        return JsonData.success();
    }



    @PostMapping("/buildCoDirData.json")
    public JsonData buildCoDirData(int stepId,String buildBusinessKey){
        commonEdArrangeUserService.buildCoDirData(stepId,buildBusinessKey);
        return JsonData.success();
    }


    @RequestMapping("/download.json")
    public void download(String businessKey,String buildBusinessKey,HttpServletResponse response) {
        List<Map> list = commonEdArrangeUserService.listExcelTemplateData(businessKey, buildBusinessKey);
        MyPoiUtil.downloadExcel(list, "人员任命安排数据-" + DateFormatUtils.format(new Date(), "yyyyMMdd") + ".xls", response);
    }

    @RequestMapping("/upload.json")
    public JsonData upload(MultipartFile file, String businessKey,String buildBusinessKey,String enLogin) throws IOException, ParseException {
        List<Map> data = MyPoiUtil.listTableData(file.getInputStream(), 0, 0, true);
        commonEdArrangeUserService.uploadExcelData(data,businessKey,buildBusinessKey,enLogin);
        return JsonData.success();
    }


}
