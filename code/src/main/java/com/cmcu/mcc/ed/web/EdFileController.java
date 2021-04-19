package com.cmcu.mcc.ed.web;

import com.common.model.JsonData;
import com.cmcu.common.util.JsonMapper;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.ed.dto.EdFileDto;
import com.cmcu.mcc.ed.service.EdFileService;
import com.cmcu.mcc.sys.entity.SysAttach;
import com.cmcu.mcc.sys.service.SysAttachService;
import com.cmcu.mcc.sys.service.SysConfigService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/ed/file")
public class EdFileController {

    @Autowired
    EdFileService edFileService;

    @Autowired
    SysAttachService sysAttachService;
    @Autowired
    SysConfigService sysConfigService;



    /**
     * 上传文件
     * @param multipartFile
     * @throws IOException
     */
    @RequestMapping("/receiveSign.json")
    public JsonData receiveSign(MultipartFile multipartFile,int fileId,String userLogin) throws IOException {
        String localPath = edFileService.getRandomFilePath();
        userLogin = userLogin.trim();
        multipartFile.transferTo(new File(localPath));
        String fileName = multipartFile.getOriginalFilename();
        if (fileName.endsWith(".DWG")) {
            fileName = StringUtils.replace(fileName, ".DWG", ".dwg");
        }
        SysAttach sysAttach = sysAttachService.insert("signDwg", fileName, localPath, userLogin);
        edFileService.updateSign(fileId, sysAttach.getId(), userLogin);
        return JsonData.success();
    }



    /**
     * 上传文件
     * @param multipartFile
     * @param businessId
     * @return
     * @throws IOException
     */
    @RequestMapping("/receive.json")
    public JsonData receive(MultipartFile multipartFile,String businessId,String userLogin) throws IOException {
        String localPath = edFileService.getRandomFilePath();
        businessId=businessId.trim();
        userLogin=userLogin.trim();
        multipartFile.transferTo(new File(localPath));
        String fileType="";
        if (sysConfigService.getConfig().getAppCode().contains("wuzhou")) {
             fileType=edFileService.getAIFileTypeByBusinessKey(businessId,multipartFile.getOriginalFilename());
        }else {
             fileType=edFileService.getAIFileType(businessId,multipartFile.getOriginalFilename());
        }

        String fileName=multipartFile.getOriginalFilename();
        if(fileName.endsWith(".DWG")){
            fileName= StringUtils.replace(fileName,".DWG",".dwg");
        }
        EdFileDto edFileDto=edFileService.insert(fileName,localPath,userLogin.trim(),businessId,fileType);
        return JsonData.success(edFileDto);
    }


    /**
     * 返回字符串格式兼容IE9
     * @param multipartFile
     * @param businessId
     * @param userLogin
     * @return
     * @throws IOException
     */

    @RequestMapping("/receive.do")
    public String receives(MultipartFile multipartFile,String businessId,String userLogin) throws IOException {
       JsonData result=receive(multipartFile,businessId,userLogin);
       return JsonMapper.obj2String(result);
    }





    @RequestMapping("/insertByCpFile.json")
    public JsonData insertByCpFile(String businessId,int cpFileId,String fileType,String userLogin)  {
        return JsonData.success(edFileService.insertByCpFile(businessId,cpFileId,fileType,userLogin));
    }

    @PostMapping("/listData.json")
    public JsonData listData(String businessId){
        /*return JsonData.success(edFileService.listData(businessId));*/
        return JsonData.success();
    }
    @PostMapping("/fileCheckByBusinessKey.json")
    public JsonData fileCheckByBusinessKey(String businessId){
        return JsonData.success(edFileService.fileCheckByBusinessKey(businessId));
    }

    @PostMapping("/listFileHistory.json")
    public JsonData listFileHistory(String id){
        return JsonData.success(edFileService.listFileHistory(Integer.parseInt(id)));
    }

    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        edFileService.remove(id,userLogin);
        return JsonData.success();
    }


    @PostMapping("/updateFileType.json")
    public JsonData updateFileType(int id,String fileType,String userLogin){
        edFileService.updateFileType(id,fileType,userLogin);
        return JsonData.success();
    }


    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(edFileService.getModelById(id));
    }



    @PostMapping("/listDataByStepId.json")
    public JsonData listDataByStepId(int stepId){
        return JsonData.success(edFileService.listDataByStepId(stepId));
    }


    @PostMapping("/checkIsInHistory.json")
    public JsonData checkIsInHistory(int id,String md5){
        return JsonData.success(edFileService.checkIsInHistory(id,md5));
    }
    @PostMapping("/listDeletedFile.json")
    public JsonData listDeletedFile(int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = edFileService.listDeletedFile(params, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }
    @PostMapping("/recoverFile.json")
    public JsonData recoverFile(int id){
        edFileService.recoverFile(id);
        return JsonData.success();
    }


}
