package com.cmcu.common.controller;

import com.common.model.JsonData;
import com.cmcu.common.dao.CommonEdQuestionMapper;
import com.cmcu.common.dto.UserDto;
import com.cmcu.common.entity.CommonCode;
import com.cmcu.common.entity.CommonEdQuestion;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.service.CommonEdQuestionService;
import com.cmcu.common.service.CommonUserService;
import com.cmcu.common.util.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

@RestController
@RequestMapping("/common/edQuestion")
public class CommonEdQuestionController {

    @Resource
    CommonEdQuestionService commonEdQuestionService;

    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        params.put("deleted", false);
        PageInfo<CommonEdQuestion> pageInfo = commonEdQuestionService.listPagedData(pageNum, pageSize, params);
        return JsonData.success(pageInfo);
    }

    @PostMapping("/selectAll.json")
    public JsonData selectAll(String tenetId, String majorName) {
        List<CommonEdQuestion> list = commonEdQuestionService.selectAll(tenetId,majorName);
        return JsonData.success(list);
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String enLogin) {
        CommonEdQuestion model =commonEdQuestionService.getNewModel(enLogin);
        return JsonData.success(model);
    }

    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id) {
        CommonEdQuestion model =commonEdQuestionService.getModelById(id);
        return JsonData.success(model);
    }

    @PostMapping("/remove.json")
    public JsonData remove(int id, String enLogin) {
        commonEdQuestionService.remove(id,enLogin);
        return JsonData.success();
    }

    @PostMapping("/save.json")
    public JsonData save(@RequestBody CommonEdQuestion item) {
        commonEdQuestionService.save(item);
        return JsonData.success();
    }

    @PostMapping("/insert.json")
    public JsonData insert(String majorName, String questionLv, String questionContent, String enLogin) {
        commonEdQuestionService.insert(majorName,questionLv,questionContent,enLogin);
        return JsonData.success();
    }



    @PostMapping("/upload.json")
    public JsonData upload(MultipartFile file,String enLogin) throws IOException, ParseException {
        //return JsonData.fail("该功能暂未公开");
        List<Map> data = MyPoiUtil.listTableData(file.getInputStream(), 0, 0, true);
        commonEdQuestionService.upload(data, enLogin);
        return JsonData.success();
    }


    @RequestMapping("/download.json")
    public void download(String tenetId,final HttpServletResponse response) {
        List<Map> list = commonEdQuestionService.listData(tenetId);
        MyPoiUtil.downloadExcel(list, "校审意见库-" + DateFormatUtils.format(new Date(), "yyyyMMddHHmm") + ".xls", response);
    }
}
