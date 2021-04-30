package com.cmcu.common.controller;


import com.cmcu.common.dto.CommonFileDto;
import com.cmcu.common.entity.CommonAttach;
import com.cmcu.common.service.CommonAttachService;
import com.common.model.JsonData;
import com.cmcu.common.service.CommonFileService;
import com.cmcu.common.util.FileUtil;
import com.cmcu.common.util.WebUtil;
import com.cmcu.common.service.CommonUserService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import sun.management.resources.agent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Controller
@RequestMapping("/common/attach")
public class CommonAttachController {



    final CommonAttachService commonAttachService;

    final CommonUserService commonUserService;

    final
    CommonFileService commonFileService;

    @Autowired
    public CommonAttachController(CommonAttachService commonAttachService, CommonUserService commonUserService, CommonFileService commonFileService){
        this.commonAttachService = commonAttachService;
        this.commonUserService = commonUserService;
        this.commonFileService = commonFileService;
    }

    @ResponseBody
    @RequestMapping("/receive.json")
    public JsonData receive(MultipartFile file, String creator) throws IOException {
        int id= commonAttachService.insert(file,file.getOriginalFilename(),"",creator);
        return JsonData.success(id);
    }

    @ResponseBody
    @RequestMapping("/editorUpload")
    public Map editorUpload(HttpServletRequest request) throws IOException {
        MultipartHttpServletRequest mureq = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> files = mureq.getFileMap();
        MultipartFile file =null;
        if (files != null &&files.size()> 0) {

            Map.Entry<String, MultipartFile> f = files.entrySet().iterator().next();
            file = f.getValue();
        }
        String fname=file.getOriginalFilename();
        int id= commonAttachService.insert(file,fname,"","ckeditor");
        Map map=new HashMap();
        map.put("fileName", fname);
        map.put("uploaded", 1);
        map.put("url", "/common/attach/download/" + id);
        return map;
    }

    @ResponseBody
    @RequestMapping("/selectByMd5.json")
    public JsonData selectByMd5(String tenetId,String md5,String enLogin) {
        CommonAttach commonAttach=commonAttachService.selectByMd5(md5,enLogin);
        if(commonAttach==null) return JsonData.fail("不存在");
        return JsonData.success(commonAttach.getId());
    }


    @ResponseBody
    @RequestMapping("/getMd5ById.json")
    public JsonData getMd5ById(int id) {
        String md5= commonAttachService.getMd5ById(id);
        return JsonData.success(md5);
    }



    @RequestMapping("/download/{id}")
    public void  download(@PathVariable int id, final HttpServletResponse response,HttpServletRequest request) throws IOException {
        CommonAttach attach = commonAttachService.getModelById(id);
        downloadingFile(attach.getLocalPath(), attach.getName(), response);
    }

    @RequestMapping("/download/file/{id}")
    public void  downloadFile(@PathVariable int id, final HttpServletResponse response,HttpServletRequest request) throws IOException {

        CommonFileDto commonFile = commonFileService.getModelById(id);
        int attachId = commonFile.getAttachId();
        if (commonFile.getSignId() > 0) {
            attachId = commonFile.getSignId();
        }
        CommonAttach attach = commonAttachService.getModelById(attachId);
        downloadingFile(attach.getLocalPath(), commonFile.getFileName(), response);
    }

    @RequestMapping("/download/dir/{dirId}")
    public void downloadDir(@PathVariable int dirId,final HttpServletResponse response) throws IOException {
        String zipPath = commonFileService.getTempZipResult(dirId);
        downloadingFile(zipPath, new File(zipPath).getName(), response);
    }

    @RequestMapping("/downloadMore")
    public void  downloadMore(String ids,String enLogin,final HttpServletResponse response) throws IOException {
        String zipPath = commonFileService.getTempZipResult(ids);
        downloadingFile(zipPath, new File(zipPath).getName(), response);
    }

    @ResponseBody
    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(int pageNum,int pageSize) {
        Map params = WebUtil.getRequestParameters();
        return JsonData.success(commonAttachService.listPagedData(pageNum, pageSize, params));
    }


    @RequestMapping("/preview/{fileId}")
    public ModelAndView preview(@PathVariable("fileId") int fileId){
        ModelAndView modelAndView=new ModelAndView("common/preview");
        return modelAndView;
    }



    @ResponseBody
    @PostMapping("/listLatestData.json")
    public JsonData listLatestData(int size,String enLogin) {
        return JsonData.success(commonAttachService.listLatestData(size, enLogin));
    }

    private void downloadingFile(String filePath,String fileName,HttpServletResponse response) throws IOException {
        File file = new File(filePath);
        Assert.state(file.exists() && file.length() > 0, fileName + "不存在!");
        byte[] data = FileCopyUtils.copyToByteArray(file);
        response.reset();
        //inline为直接浏览器打开模式 attachment(为下载）
        //response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8").replace("+", "%20"));
        response.setHeader("Content-Disposition", "filename=" + URLEncoder.encode(fileName, "utf-8").replace("+", "%20"));
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType(FileUtil.getContentType(fileName));
        OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
        outputStream.write(data);
        outputStream.flush();
        outputStream.close();
    }

    private String getDownloadFileName(String fileName) throws UnsupportedEncodingException {
        fileName= URLEncoder.encode(fileName, "utf-8");
        Map<String,String> replace= Maps.newHashMap();
        replace.put("%23","#");
        replace.put("%2B","+");
        replace.put("%26","&");
        replace.put("%7E","~");
        for(String key:replace.keySet()){
            fileName= StringUtils.replace(fileName,key,replace.get(key));
        }
        return fileName;
    }



}
