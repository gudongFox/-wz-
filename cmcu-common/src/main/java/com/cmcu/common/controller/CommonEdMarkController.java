package com.cmcu.common.controller;


import com.common.model.JsonData;
import com.cmcu.common.dto.CommonEdMarkDto;
import com.cmcu.common.entity.CommonFormData;
import com.cmcu.common.service.CommonEdMarkService;
import com.cmcu.common.service.CommonFormDataService;
import com.cmcu.common.util.FileUtil;
import com.cmcu.common.util.MyPoiUtil;
import com.cmcu.common.util.WebUtil;
import com.common.util.JsonMapper;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

@RestController
@RequestMapping("/common/edMark")
public class CommonEdMarkController {


    final
    CommonEdMarkService commonEdMarkService;

    @Resource
    CommonFormDataService commonFormDataService;


    public CommonEdMarkController(CommonEdMarkService commonEdMarkService) {
        this.commonEdMarkService = commonEdMarkService;
    }


    @PostMapping("/setLocation.json")
    public JsonData setLocation(int id, String drawNo, String drawName, String cloudLocation, String enLogin) {
        commonEdMarkService.setLocation(id,drawNo,drawName,cloudLocation,enLogin);
        return JsonData.success();
    }

    @PostMapping("/setPicAttachId.json")
    public JsonData setPicAttachId(int id,int picAttachId,String enLogin){
        commonEdMarkService.setPicAttachId(id,picAttachId,enLogin);
        return JsonData.success();
    }

    @PostMapping("/setColor.json")
    public JsonData setColor(int id,String questionColor,String enLogin){
        commonEdMarkService.setColor(id,questionColor,enLogin);
        return JsonData.success();
    }

    @PostMapping("/setAnswer.json")
    public JsonData setAnswer(int id,String questionAnswer,String enLogin){
        return JsonData.success(commonEdMarkService.setAnswer(id,questionAnswer,enLogin));
    }

    @PostMapping("/remove.json")
    public JsonData remove(int id,String enLogin){
        commonEdMarkService.remove(id,enLogin);
        return JsonData.success();
    }


    @PostMapping("/update.json")
    public JsonData updateMark(int id,int fileId,String drawNo,String drawName,String questionLevel,String questionContent,String enLogin) {
        return JsonData.success(commonEdMarkService.updateMark(id,fileId,drawNo,drawName,questionLevel,questionContent,enLogin));
    }

    @PostMapping("/save.json")
    public JsonData save(@RequestBody CommonEdMarkDto item) {
        commonEdMarkService.save(item);
        return JsonData.success();
    }

    @PostMapping("/insert.json")
    public JsonData insert(int fileId,String drawNo,String drawName,String questionLevel,String questionContent,String enLogin) {
        return JsonData.success(commonEdMarkService.insertMark(fileId,drawNo,drawName,questionLevel,questionContent,enLogin));
    }

    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id,String enLogin){
        return JsonData.success(commonEdMarkService.getModelById(id,enLogin));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String businessKey,String enLogin){
        return JsonData.success(commonEdMarkService.getNewModel(businessKey,enLogin));
    }

    @PostMapping("/getNewModelByFileId.json")
    public JsonData getNewModelByFileId(int fileId,String enLogin){
        return JsonData.success(commonEdMarkService.getNewModelByFileId(fileId,enLogin));
    }

    @PostMapping("/listData.json")
    public JsonData listData(String businessKey,Boolean onlyCloud,String enLogin){
        return JsonData.success(commonEdMarkService.listData(businessKey,onlyCloud,enLogin));
    }

    @PostMapping("/listDataByFileId.json")
    public JsonData listDataByFileId(int fileId,boolean onlyCloud,String enLogin){
        return JsonData.success(commonEdMarkService.listDataByFileId(fileId,onlyCloud,enLogin));
    }

    @RequestMapping("/downloadMark.json")
    public void  download(final HttpServletResponse response, String businessKey, String enLogin) throws IOException {
        List<Map> list = commonEdMarkService.listMapData(businessKey, enLogin);
        List<String> excelNames = Lists.newArrayList();
        CommonFormData commonFormData = commonFormDataService.getModelByBusinessKey(businessKey);
        if (commonFormData != null) {
            Map data = JsonMapper.string2Map(commonFormData.getFormData());
            String majorName = data.getOrDefault("majorName", "").toString();
            String creatorName = data.getOrDefault("creatorName", "").toString();
            String createTime = DateFormatUtils.format(commonFormData.getGmtCreate(), "MMdd");

            if (StringUtils.isNotEmpty(majorName)) {
                excelNames.add(majorName);
            }
            if (StringUtils.isNotEmpty(creatorName)) {
                excelNames.add(creatorName);
            }
            if (StringUtils.isNotEmpty(createTime)) {
                excelNames.add(createTime);
            }
        }
        excelNames.add("校审意见");
        excelNames.add(DateFormatUtils.format(new Date(), "yyyyMMdd"));
        MyPoiUtil.downloadExcel(list, FileUtil.getGoodFileName(StringUtils.join(excelNames, "-") + ".xls"), response);
    }


    @RequestMapping("/upload.json")
    public JsonData upload(MultipartFile file, String businessKey, String enLogin) throws IOException, ParseException {
        List<Map> data = MyPoiUtil.listTableDataNew(file.getInputStream(), 1, 0, true);
        commonEdMarkService.upload(data, businessKey, enLogin);
        return JsonData.success();
    }




}
