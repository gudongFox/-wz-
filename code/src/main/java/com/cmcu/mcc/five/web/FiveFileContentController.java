package com.cmcu.mcc.five.web;

import com.common.model.JsonData;
import com.cmcu.common.dto.CommonFileDto;
import com.cmcu.common.dto.UserDto;
import com.cmcu.common.entity.CommonConfig;
import com.cmcu.common.service.CommonConfigService;
import com.cmcu.common.service.CommonFileService;
import com.cmcu.common.service.CommonUserService;
import com.cmcu.common.util.CookieSessionUtils;
import com.cmcu.common.util.FileUtil;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.five.dto.FiveContentFileDto;
import com.cmcu.mcc.five.entity.FiveContentFile;
import com.cmcu.mcc.five.service.FiveContentFileService;
import com.github.pagehelper.PageInfo;
import javafx.scene.control.Alert;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/wuzhou/file")
public class FiveFileContentController {


    @Autowired
    FiveContentFileService fileService;
    @Autowired
    CommonConfigService commonConfigService;
    @Resource
    CommonFileService commonFileService;

    @Resource
    FiveContentFileService fiveContentFileService;

    @Resource
    CommonUserService commonUserService;


    /*//tth2021.3.29
    @PostMapping(value = "/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(fiveContentFileService.getModelById(id));
    }*/

    @RequestMapping("/uploadContent")
    public String uploadContent(FiveContentFileDto contentFile){
        fileService.Add(contentFile);
        return  "";
    }
    @RequestMapping("/remove.json")
    public JsonData remove(int id){
        fileService.remove(id);
        return JsonData.success();
    }

    @RequestMapping(value = "/updateFile.do")
    public JsonData updateFile(MultipartFile singleUpload,String id) throws IOException {
        FiveContentFile file = fileService.getModelByBusinessKey(id,1);
        FiveContentFileDto newlyFile=new FiveContentFileDto();
        String tableNameKey=file.getTableName();
        int tKey=file.getTableKey();
        int seq=fileService.GetSeqByKey(tableNameKey,tKey);
        String fileName=file.getFileName();
        String localPath = calculateFilePath(tableNameKey,tKey,seq,file.getFileType());
        String docName=localPath+"\\"+UUID.randomUUID();
        singleUpload.transferTo(new File(docName));
        newlyFile.setTableKey(tKey);
        newlyFile.setTableName(tableNameKey);
        newlyFile.setCreator(file.getCreator());
        newlyFile.setBusinessKey(file.getBusinessKey());
        newlyFile.setFileName(fileName);
        newlyFile.setSize(String.valueOf(singleUpload.getSize()));
        newlyFile.setFileType(file.getFileType());//1 content  0 redHead
        newlyFile.setLocalPath(docName);
        int newId = fileService.Add(newlyFile);
        fileService.removeOther(file.getId());
        return  JsonData.success(fileService.getModelById(newId));
    }

    @RequestMapping(value = "/updateRedFile.do")
    public JsonData updateRedFile(MultipartFile singleUpload,String id) throws IOException {
        FiveContentFile file = fileService.getModelByBusinessKey(id, 0);
        //???????????? ???????????? ???????????????
        if (file.getId()==null){
            file = fileService.getModelByBusinessKey(id, 1);
        }
        FiveContentFileDto newlyFile=new FiveContentFileDto();
        String tableNameKey=file.getTableName();
        int tKey=file.getTableKey();
        int seq=fileService.GetSeqByKey(tableNameKey,tKey);
        String fileName= file.getFileName();
        String localPath = calculateFilePath(tableNameKey,tKey,seq,0);
        String docName=localPath+"\\"+UUID.randomUUID();
        singleUpload.transferTo(new File(docName));
        newlyFile.setTableKey(tKey);
        newlyFile.setTableName(tableNameKey);
        newlyFile.setCreator(file.getCreator());
        newlyFile.setBusinessKey(file.getBusinessKey());
        newlyFile.setFileName(fileName);
        newlyFile.setSize(String.valueOf(singleUpload.getSize()));
        newlyFile.setFileType(0);//1 content  0 redHead
        newlyFile.setLocalPath(docName);
        int newId = fileService.Add(newlyFile);
        if (file.getFileType()!=1){
            fileService.removeOther(file.getId());
        }
        return  JsonData.success(fileService.getModelById(newId));
    }

    @RequestMapping("/makeRedHeader")
    public ModelAndView stampDetail(){
        return new ModelAndView("/five/me/stampDetail");
    }

    @RequestMapping("/calculateFilePath")
    public String calculateFilePath(String tableName,int tableKey,int seq,int fileType) {
        StringBuilder sbPath = new StringBuilder();
        sbPath.append(commonConfigService.getConfig().getDirPath());
        sbPath.append("\\");
        sbPath.append(tableName);
        sbPath.append("\\");
        sbPath.append(tableKey);
        sbPath.append("\\");
        if (fileType==1){
            sbPath.append("content");
        }else {
            sbPath.append("redHead");
        }
        sbPath.append("\\");
        sbPath.append(seq);
        String directoryPath = sbPath.toString();
        if (!new File(directoryPath).exists()) {
            new File(directoryPath).mkdirs();
        }
        return directoryPath;
    }

    @RequestMapping("/getModelByBusinessKey.json")
    public JsonData getModelByBusinessKey(String businessKey){
        return  JsonData.success(fileService.getModelByBusinessKey(businessKey));
    }

    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin,String uiSref, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = fiveContentFileService.listPagedData(params, userLogin, uiSref, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }

    @RequestMapping(value = "/receive.do")
    public JsonData receives(MultipartFile singleUpload,String businessKey, String userLogin,int fileType) throws IOException {
        FiveContentFileDto newlyFile=new FiveContentFileDto();
        int newId=0;
        if(StringUtils.isNotEmpty(businessKey)){
            String[] s = businessKey.split("_");
            String tableName=s[0];
            String tableKey=s[1];
            int tKey=Integer.parseInt(tableKey);
            int seq=fileService.GetSeqByKey(tableName,tKey);
            //??????????????????????????? doc docx
            String fileName=singleUpload.getOriginalFilename();
            String ext = fileName.substring(fileName.lastIndexOf("."));
                //?????????????????????
                if(businessKey.startsWith("fiveBusinessAdvanceCollect_")){
                    if (!".xls,.xlsx".contains(ext)) {
                        return JsonData.fail("??????????????????.xls,.xlsx????????????");
                    }
                }else{
                    if (!".doc,.docx,.pdf".contains(ext)) {
                        return JsonData.fail("??????????????????doc,docx???pdf????????????");
                    }
                }

            /** ???????????????????????????MultipartFile??????transferTo(dest)????????????????????????;
                 ????????????transferTo??????????????????????????????getInputStream()??????;
                ???????????????getInputStream()??????????????????java.lang.IllegalStateException: File has been moved - cannot be read again;
            **/
            //??????????????????????????? ??????File has already been moved - cannot be transferred again??????
           /* List<CommonFileDto> commonFileDtos = commonFileService.listData(businessKey, 0, userLogin);

            for(CommonFileDto dto:commonFileDtos){
                if (dto.getFileName().equals(fileName)){
                    commonFileService.remove(dto.getId(),userLogin);
                }
            }*/

            //int commodId = commonFileService.insert(singleUpload, businessKey, 0, fileName, 0, 0, "????????????", userLogin);
           // CommonFileDto commonFileDto = commonFileService.getModelById(commodId);
            //String commPath=commonFileDto.getCommonAttach().getLocalPath();

            String localPath = calculateFilePath(tableName,tKey,seq,fileType);
            String docName=localPath+"\\"+UUID.randomUUID();

            //File comm=new File(commPath);
            //File dest=new File(docName);
            //FileChannel input=null;
           // FileChannel output=null;
           // input = new FileInputStream(comm).getChannel();
           // output = new FileOutputStream(dest).getChannel();
            //output.transferFrom(input, 0, input.size());

            singleUpload.transferTo(new File(docName));


            newlyFile.setTableKey(tKey);
            newlyFile.setTableName(tableName);
            newlyFile.setBusinessKey(businessKey);
            newlyFile.setCreator(userLogin);
            newlyFile.setFileName(fileName);
            newlyFile.setSize(String.valueOf(singleUpload.getSize()));
            newlyFile.setFileType(fileType);
            newlyFile.setLocalPath(docName);
            newId = fileService.Add(newlyFile);
            fileService.removeOther(newId);


        }
        return  JsonData.success(fileService.getModelById(newId));
    }

    /**
     * ???????????? ??????
     * @param businessKey
     * @param response
     * @throws IOException
     */
    @RequestMapping("/downloadContent/{businessKey}")
    public void  downloadContent(@PathVariable String businessKey, final HttpServletResponse response) throws IOException {
        FiveContentFileDto fiveContentFile = fileService.getModelByBusinessKey(businessKey, 1);
        String filePath = fiveContentFile.getLocalPath();
        if(filePath!=null) {
            downloadingFile(filePath, fiveContentFile.getFileName(), response);
        }
        else{
            Assert.state(StringUtils.isNotEmpty(fiveContentFile.getLocalPath()),"??????????????????");
        }
    }

    /**
     * ???????????? ????????????
     * @param businessKey
     * @param response
     * @throws IOException
     */
    @RequestMapping("/downloadRedHead/{businessKey}")
    public void  downloadRedHead(@PathVariable String businessKey, final HttpServletResponse response) throws IOException {

        FiveContentFileDto fiveContentFile = fileService.getModelByBusinessKey(businessKey, 0);
        //???????????? ???????????? ???????????????
        if (fiveContentFile.getId()==null){
            fiveContentFile = fileService.getModelByBusinessKey(businessKey, 1);
        }
        String filePath = fiveContentFile.getLocalPath();
        if(filePath!=null) {
            downloadingFile(filePath, fiveContentFile.getFileName(), response);
        }
    }


    /**
     * ????????????
     * @param id
     * @param response
     * @throws IOException
     */
    @RequestMapping("/download/{id}")
    public void  downloadContent(@PathVariable int id, final HttpServletResponse response) throws IOException {
        FiveContentFile fiveContentFile = fileService.getModelById(id);
        String filePath = fiveContentFile.getLocalPath();
        if(filePath!=null) {
            downloadingFile(filePath, fiveContentFile.getFileName(), response);
        }
    }


    @RequestMapping("/preview/{businessKey}")
    public ModelAndView previewFile(@PathVariable("businessKey") String businessKey) {
        ModelAndView modelAndView = new ModelAndView("common/previewFile");
        String enLogin = CookieSessionUtils.getSession("enLogin");
        if (StringUtils.isEmpty(enLogin)) {
            return new ModelAndView("redirect:/login");
        }

        CommonConfig commonConfig=commonConfigService.getConfig();
        UserDto userDto = commonUserService.selectByEnLogin(enLogin);

        FiveContentFile file = fiveContentFileService.getModelByBusinessKey(businessKey,0);



        modelAndView.addObject("fileName", file.getFileName());
        modelAndView.addObject("src", commonConfig.getOwaServer() + "/wuzhou/file/download/" + file.getId());
        modelAndView.addObject("sec", userDto.getCnName() + " " + enLogin);
        return modelAndView;
    }





    private void downloadingFile(String filePath,String fileName,HttpServletResponse response){
        File file = new File(filePath);
        if (file.exists() && file.length() > 0) {
            try {
               /* byte[] data = FileCopyUtils.copyToByteArray(file);
                response.reset();
                response.setHeader("Content-Disposition", "filename=" + URLEncoder.encode(fileName, "utf-8").replace("+", "%20"));
                response.addHeader("Content-Length", "" + data.length);
                OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
                outputStream.write(data);
                outputStream.flush();
                outputStream.close();*/

                //File file = new File(filePath);
                Assert.state(file.exists() && file.length() > 0, fileName + "?????????!");
                byte[] data = FileCopyUtils.copyToByteArray(file);
                response.reset();
                //inline?????????????????????????????? attachment(????????????
                //response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8").replace("+", "%20"));
                response.setHeader("Content-Disposition", "filename=" + URLEncoder.encode(fileName, "utf-8").replace("+", "%20"));
                response.addHeader("Content-Length", "" + data.length);
                response.setContentType(FileUtil.getContentType(fileName));
                OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
                outputStream.write(data);
                outputStream.flush();
                outputStream.close();
            }catch (Exception ex){
                System.out.println("????????????:"+ex.getMessage());
            }
        }
    }



}
