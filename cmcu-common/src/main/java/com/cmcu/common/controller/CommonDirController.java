package com.cmcu.common.controller;

import com.common.model.JsonData;
import com.cmcu.common.service.CommonCoDirService;
import com.cmcu.common.service.CommonDirService;
import com.cmcu.common.util.FileUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/common/dir")
public class CommonDirController {


    @Resource
    CommonDirService commonDirService;

    @Resource
    CommonCoDirService commonCoDirService;



    @PostMapping("/listData.json")
    public JsonData listData(String businessKey, int parentId,String enLogin){
        if(StringUtils.isEmpty(businessKey)) return JsonData.success(Lists.newArrayList());
        return JsonData.success(commonDirService.listData(businessKey,parentId,enLogin));
    }

    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(commonDirService.getModelById(id));
    }


    @PostMapping("/remove.json")
    public JsonData remove(int id,String enLogin){
        commonDirService.remove(id,enLogin);
        return JsonData.success();
    }

    @PostMapping("/removeMore.json")
    public JsonData removeMore(String ids,String enLogin){
        commonDirService.removeMore(ids,enLogin);
        return JsonData.success();
    }

    @PostMapping("/newDir.json")
    public JsonData newDir(String businessKey,String dirName,int parentId,String enLogin){
        Assert.state(FileUtil.getGoodName(dirName).equalsIgnoreCase(dirName),"文件夹名称包含非法字符!");
        commonDirService.newDir(businessKey,dirName,parentId,enLogin);
        return JsonData.success();
    }

    /**
     * 创建
     * @param businessKey
     * @param dirName
     * @param parentId
     * @param enLogin
     * @return
     */
    @PostMapping("/getDir.json")
    public JsonData getDir(String businessKey,String dirName,int parentId,String enLogin) {
        Assert.state(FileUtil.getGoodName(dirName).equalsIgnoreCase(dirName), "文件夹名称包含非法字符!");
        return JsonData.success(commonDirService.getDir(businessKey, dirName, parentId, enLogin));
    }


    @PostMapping("/save.json")
    public JsonData save(int id,String dirName,int seq,String remark,String enLogin){
        Assert.state(FileUtil.getGoodName(dirName).equalsIgnoreCase(dirName),"文件夹名称包含非法字符!");
        commonDirService.save(id,dirName,seq,remark,enLogin);
        return JsonData.success();
    }

    @PostMapping("/listBreadcrumb.json")
    public JsonData listBreadcrumb(int dirId){
        return JsonData.success(commonDirService.listBreadcrumb(dirId));
    }


    @PostMapping("/getCoMirrorDir.json")
    public JsonData getCoMirrorDir(int dirId,boolean referDir) {
        int targetDirId=commonCoDirService.getCoMirrorDir(dirId, referDir);
        return JsonData.success(commonDirService.getModelById(targetDirId));
    }


}
