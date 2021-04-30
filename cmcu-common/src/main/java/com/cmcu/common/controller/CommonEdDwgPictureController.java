package com.cmcu.common.controller;

import com.common.model.JsonData;
import com.cmcu.common.entity.CommonEdDwgPicture;
import com.cmcu.common.service.CommonAttachService;
import com.cmcu.common.service.CommonEdDwgPictureService;
import com.cmcu.common.service.CommonUserService;
import com.cmcu.common.util.WebUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/common/edDwgPicture")
public class CommonEdDwgPictureController {


    @Resource
    CommonEdDwgPictureService commonEdDwgPictureService;
    @Resource
    CommonAttachService commonAttachService;
    @Resource
    CommonUserService commonUserService;


    @PostMapping("/selectAll.json")
    public JsonData selectAll(String tenetId){
        return JsonData.success(commonEdDwgPictureService.listData(tenetId));
    }



    @PostMapping("/update.json")
    public JsonData update(@RequestBody CommonEdDwgPicture item){
        commonEdDwgPictureService.update(item);
        return JsonData.success();
    }

    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(commonEdDwgPictureService.getModelById(id));
    }



    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String enLogin ) {
        String tenetId=commonUserService.getTenetId(enLogin);
        return JsonData.success(commonEdDwgPictureService.getNewModel(tenetId));
    }

    @PostMapping("/getNewModelLast.json")
    public JsonData getNewModelLast(String enLogin ) {
        return JsonData.success(commonEdDwgPictureService.getNewModelLast(enLogin));
    }


    @PostMapping("/remove.json")
    public JsonData remove(int id){
        commonEdDwgPictureService.remove(id);
        return JsonData.success();
    }

    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(int pageNum, int pageSize,String enLogin) {
        Map params = WebUtil.getRequestParameters();
        String tenetId=commonUserService.getTenetId(enLogin);
        return JsonData.success(commonEdDwgPictureService.listPagedData(params,tenetId,pageNum,pageSize));
    }

    /**
     * 上传文件
     * @param multipartFile
     * @return
     * @throws IOException
     */
    @RequestMapping("/receiveDwg.do")
    public JsonData receiveDwg(MultipartFile multipartFile, int id, String enLogin) throws IOException {
        String tenetId=commonUserService.getTenetId(enLogin);

        int attachId = commonAttachService.insert(multipartFile, multipartFile.getOriginalFilename(), "", enLogin);

        return JsonData.success(commonEdDwgPictureService.updateAttach(tenetId,id, attachId));
    }


    /**
     * 上传文件
     * @param multipartFile
     * @return
     * @throws IOException
     */
    @RequestMapping("/receiveDwgList.do")
    public JsonData receiveDwgList(MultipartFile multipartFile,String enLogin) throws IOException {
        String tenetId=commonUserService.getTenetId(enLogin);
        int attachId = commonAttachService.insert(multipartFile, multipartFile.getOriginalFilename(), "批量图框插入", enLogin);
        return JsonData.success(commonEdDwgPictureService.getNewModel(tenetId, attachId));
    }
}
