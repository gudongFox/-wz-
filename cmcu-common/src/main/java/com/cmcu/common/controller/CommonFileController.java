package com.cmcu.common.controller;

import com.common.model.JsonData;
import com.cmcu.common.dto.CommonFileDto;
import com.cmcu.common.service.CommonAttachService;
import com.cmcu.common.service.CommonBaseService;
import com.cmcu.common.service.CommonFileService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/common/file")
public class CommonFileController {


    final
    CommonFileService commonFileService;

    final
    CommonAttachService commonAttachService;

    final
    CommonBaseService commonBaseService;

    public CommonFileController(CommonFileService commonFileService, CommonAttachService commonAttachService, CommonBaseService commonBaseService) {
        this.commonFileService = commonFileService;
        this.commonAttachService = commonAttachService;
        this.commonBaseService = commonBaseService;
    }



    @RequestMapping("/receive.json")
    public JsonData receive(MultipartFile file,String businessKey,Integer dirId, String fileName, Integer sourceId,Integer extractId,String remark,String enLogin) throws IOException {
        if (StringUtils.isEmpty(fileName)) fileName = file.getOriginalFilename();
        if(dirId==null) dirId=0;
        if(sourceId==null) sourceId=0;
        if(extractId==null) extractId=0;
        if(StringUtils.isEmpty(remark)) remark="";

        int id = commonFileService.insert(file, businessKey, dirId, fileName, sourceId,extractId,remark, enLogin);
        return JsonData.success(id);
    }


    @RequestMapping("/insertBySign.json")
    public JsonData insertBySign(MultipartFile file,String businessKey,Integer dirId, String fileName,String enLogin) throws IOException {
        if (StringUtils.isEmpty(fileName)) fileName = file.getOriginalFilename();
        if (dirId == null) dirId = 0;
        int attachId = commonAttachService.insert(file,fileName,"",enLogin);
        commonFileService.insertBySign(businessKey, dirId, fileName, attachId, enLogin);
        return JsonData.success();
    }

    @RequestMapping("/insertByExistAttach.json")
    public JsonData insertByExistAttach(String businessKey,int dirId, String fileName, int attachId,String enLogin)  {
        int id = commonFileService.insertByExistAttach(businessKey, dirId, fileName, attachId, 0,0, enLogin);
        return JsonData.success(id);
    }

    @RequestMapping("/listDataByExtractId.json")
    public JsonData listDataByExtractId(int fileId,int extractId,String enLogin)  {
        return JsonData.success( commonFileService.listDataByExtractId(fileId, extractId, enLogin));
    }

    @RequestMapping("/receiveDirectoryFile.json")
    public JsonData receiveDirectoryFile(MultipartFile file,String businessKey,Integer dirId,String webkitRelativePath,String enLogin) {
        try {
            int targetDirId=commonBaseService.getTargetDirId(businessKey,dirId,webkitRelativePath,enLogin);
            int id = commonFileService.insert(file, businessKey, targetDirId, file.getOriginalFilename(), 0,0, "", enLogin);
            return JsonData.success(id);
        } catch (IOException ex) {
            throw new IllegalArgumentException("上传文件失败,请重试!");
        }
    }

    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(commonFileService.getModelById(id));
    }


    @PostMapping("/transferCoToEd.json")
    public JsonData transferCoToEd(@RequestBody List<CommonFileDto> coFiles, String destBusinessKey, String enLogin){
        commonFileService.transferCoToEd(coFiles,destBusinessKey,enLogin);
        return JsonData.success();
    }


    @PostMapping("/remove.json")
    public JsonData remove(int id,String enLogin){
        commonFileService.remove(id,enLogin);
        return JsonData.success();
    }

    @PostMapping("/save.json")
    public JsonData save(int id,String fileType,String fileName,int seq,String remark,String enLogin){
        commonFileService.save(id,fileType,fileName,seq,remark,enLogin);
        return JsonData.success();
    }


    @PostMapping("/toggleLocked.json")
    public JsonData toggleLocked(int id,String enLogin){
        commonFileService.toggleLocked(id,enLogin);
        return JsonData.success();
    }


    @PostMapping("/checkUploadAble.json")
    public JsonData checkUploadAble(String businessKey,int dirId,String fileName,String md5,String enLogin){
        commonFileService.checkUploadAble(businessKey,dirId,fileName,md5,enLogin);
        return JsonData.success(true);
    }


    @PostMapping("/listData.json")
    public JsonData listData(String businessKey,int dirId,String enLogin){
        if(StringUtils.isEmpty(businessKey)) return JsonData.success(Lists.newArrayList());
        return JsonData.success(commonFileService.listData(businessKey,dirId,enLogin));
    }

    @PostMapping("/listDataCount.json")
    public JsonData listDataCount(String businessKey,int dirId){
        return JsonData.success(commonFileService.listData(businessKey,dirId,"").size());
    }


    @PostMapping("/listHistory.json")
    public JsonData listHistory(int id){
        return JsonData.success(commonFileService.listHistory(id));
    }


    @PostMapping("/checkIsInHistory.json")
    public JsonData checkIsInHistory(int id,String md5) {
        return JsonData.success(commonFileService.checkIsInHistory(id, md5));
    }



    @PostMapping("/removeHistory.json")
    public JsonData removeHistory(int id,int attachId,String enLogin){
        commonFileService.removeHistory(id,attachId,enLogin);
        return JsonData.success();
    }


    @PostMapping("/listFileType.json")
    public JsonData listFileType(String tenetId,String fileTypeCode){
        return JsonData.success(commonFileService.listFileType(tenetId,fileTypeCode));
    }




}
