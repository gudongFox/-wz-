package com.cmcu.mcc.oa.web;

import com.common.model.JsonData;
import com.cmcu.common.util.JsonMapper;
import com.cmcu.common.web.RequestHolder;
import com.cmcu.mcc.oa.entity.OaArchiveDetail;
import com.cmcu.mcc.oa.service.OaArchiveDetailService;
import com.cmcu.mcc.sys.entity.SysAttach;
import com.cmcu.mcc.sys.service.SysAttachService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;


@RestController
@RequestMapping("/oa/archiveDetail")
public class OaArchiveDetailController {
    @Autowired
    OaArchiveDetailService oaArchiveDetailService;
    @Autowired
    SysAttachService sysAttachService;

    @PostMapping("/selectAll.json")
    public JsonData selectAll(String attachId) {

        return JsonData.success(oaArchiveDetailService.selectAll(attachId));
    }

    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(oaArchiveDetailService.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(int archiveId,String userLogin){
        return JsonData.success(oaArchiveDetailService.getNewModel(archiveId,userLogin));
    }
    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        oaArchiveDetailService.remove(id,userLogin);
        return JsonData.success();
    }
    @PostMapping("/update.json")
    public JsonData update(@RequestBody OaArchiveDetail item){
        oaArchiveDetailService.update(item);
        return JsonData.success();
    }

    @PostMapping("/listDetail.json")
    public JsonData listDetail(String archiveId,String detailIds){
        return JsonData.success(oaArchiveDetailService.listDetail(archiveId,detailIds));
    }



    /**
     * 上传文件
     * @param multipartFile
     * @param source 来源
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/receive.json",produces="text/html;charset=UTF-8;")
    public JsonData receive(MultipartFile multipartFile, String source, String creator,int id) throws IOException {
        source=source.trim();
        if(StringUtils.isNotEmpty(creator)) {
            creator = creator.trim();
        }else{
            creator = RequestHolder.getUserName();
        }
        String localPath = sysAttachService.getRandomFilePath(source);
        multipartFile.transferTo(new File(localPath));
        SysAttach sysAttach=sysAttachService.insert(source,multipartFile.getOriginalFilename(),localPath,creator);

        OaArchiveDetail modelById = oaArchiveDetailService.getModelById(id);

        modelById.setAttachId(sysAttach.getId());
       /* int dot=  sysAttach.getName().lastIndexOf(".");
        modelById.setFileName(sysAttach.getName().substring(0,dot));*/
        oaArchiveDetailService.update(modelById);

        return  JsonData.success(sysAttach);
    }


    /**
     * 文件上传 兼容IE9
     * @param multipartFile
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/receive.do",produces="text/html;charset=UTF-8;")
    public String receives(MultipartFile multipartFile,String creator,int id) throws IOException {
        String source="oa";
        JsonData result=receive(multipartFile,source,creator,id);
        return JsonMapper.obj2String(result);
    }


}
