package com.cmcu.mcc.sys.web;

import com.cmcu.common.JsonData;
import com.cmcu.common.entity.CommonAttach;
import com.cmcu.common.service.CommonAttachService;
import com.cmcu.common.util.JsonMapper;
import com.cmcu.common.util.WebUtil;
import com.cmcu.common.web.RequestHolder;
import com.cmcu.mcc.ed.dto.EdFileDto;
import com.cmcu.mcc.ed.entity.EdFile;
import com.cmcu.mcc.ed.service.EdFileService;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.HrEmployeeService;
import com.cmcu.mcc.sys.dto.SysAttachDto;
import com.cmcu.mcc.sys.entity.SysAttach;
import com.cmcu.mcc.sys.service.SysAttachService;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/sys/attach")
public class SysAttachController {


    @Autowired
    SysAttachService sysAttachService;
    @Autowired
    HrEmployeeService hrEmployeeService;

    @Autowired
    EdFileService edFileService;
    @Autowired
    CommonAttachService commonAttachService;

    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id) {
        return JsonData.success(sysAttachService.getModelById(id));
    }

    @PostMapping("/getModelByName.json")
    public JsonData getModelByName(String name) {
        return JsonData.success(sysAttachService.getModelByName(name));
    }

    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo<Object> pageInfo = sysAttachService.listPagedData(params, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }

    /**
     * 下载图片签名文件
     * @param userLogin
     * @param response
     * @throws IOException
     */
    @RequestMapping("/downloadPic/{userLogin}")
    public void  downloadPic(@PathVariable String userLogin, final HttpServletResponse response) throws IOException {
        HrEmployeeDto hrEmployeeDto = hrEmployeeService.getModelByUserLogin(userLogin);
        if (!"".equals(hrEmployeeDto.getSignPicAttachId())){
            CommonAttach attach = commonAttachService.getModelById(Integer.parseInt(hrEmployeeDto.getSignPicAttachId()));
            if(attach!=null) {
                downloadingFile(attach.getLocalPath(), attach.getName(), response);
            }
        }
    }


    /**
     * 下载文件
     * @param id
     * @param response
     * @throws IOException
     */
    @RequestMapping("/download/{id}")
    public void  download(@PathVariable int id, final HttpServletResponse response) throws IOException {
        CommonAttach attach = commonAttachService.getModelById(id);
        if(attach!=null) {
            downloadingFile(attach.getLocalPath(), attach.getName(), response);
        }

    }



    /**
     * 上传文件
     * @param multipartFile
     * @param source 来源
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/receive.json",produces="text/html;charset=UTF-8;")
    public JsonData receive(MultipartFile multipartFile,String source,String creator) throws IOException {
        source=source.trim();
        if(StringUtils.isNotEmpty(creator)) {
            creator = creator.trim();
        }else{
            creator = RequestHolder.getUserName();
        }
        String localPath = sysAttachService.getRandomFilePath(source);
        multipartFile.transferTo(new File(localPath));
        SysAttach sysAttach=sysAttachService.insert(source,multipartFile.getOriginalFilename(),localPath,creator);
        return  JsonData.success(sysAttach);
    }


    /**
     * 文件上传 兼容IE9
     * @param multipartFile
     * @param source
     * @return
     * @throws IOException
     */
    @RequestMapping( value = "/receive.do",produces="text/html;charset=UTF-8;" )
    public String receives(MultipartFile multipartFile,String source,String creator) throws IOException {
      /*  JsonData result=receive(multipartFile,source,creator);
        return JsonMapper.obj2String(result);*/
        int id= commonAttachService.insert(multipartFile,multipartFile.getOriginalFilename(),"",creator);
        JsonData success = JsonData.success(commonAttachService.getModelById(id));
        return JsonMapper.obj2String(success);
    }


    /**
     * 下载文件及配套的XML
     * @param id
     * @param response
     * @throws IOException
     */
    @RequestMapping("/downloadEdFile/{id}")
    public void  downloadEdFile(@PathVariable int id, final HttpServletResponse response) throws IOException {
        EdFileDto edFileDto = edFileService.getModelById(id);
        int attachId=edFileDto.getAttachId();
        if(edFileDto.getSignId()>0) attachId=edFileDto.getSignId();
        SysAttachDto attach = sysAttachService.getModelById(attachId);
        if (attach != null) {
            downloadingFile(attach.getLocalPath(), edFileDto.getFileName(), response);
        }
    }
    /**
     * 下载文件及配套的XML
     * @param id
     * @param response
     * @throws IOException
     */
    @RequestMapping("/downloadEdFileXml/{id}")
    public void  downloadEdFileXml(@PathVariable int id, final HttpServletResponse response) throws IOException {
        EdFileDto edFileDto = edFileService.getModelById(id);
        SysAttachDto attach = sysAttachService.getModelById(edFileDto.getAttachId());
        if (attach != null) {
            String xmlPath=attach.getLocalPath()+".xml";
            createXml(id,xmlPath);
            downloadingFile(xmlPath,edFileDto.getFileName()+".xml" , response);
        }
    }

    /**
     * 生成xml方法
     */
    public static void createXml(int edFileId,String xmlPath){
        try {
            // 创建解析器工厂
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = factory.newDocumentBuilder();
            Document document = db.newDocument();
            // 不显示standalone="no"
            document.setXmlStandalone(true);
            Element DWGS = document.createElement("DWGS");
            // 向bookstore根节点中添加子节点book
            Element DwgFile = document.createElement("DwgFile");


            // 为book节点添加属性
            DwgFile.setAttribute("fileId", edFileId+"");
            // 将book节点添加到bookstore根节点中
            DWGS.appendChild(DwgFile);
            // 将bookstore节点（已包含book）添加到dom树中
            document.appendChild(DWGS);

            // 创建TransformerFactory对象
            TransformerFactory tff = TransformerFactory.newInstance();
            // 创建 Transformer对象
            Transformer tf = tff.newTransformer();
            // 输出内容是否使用换行
            tf.setOutputProperty(OutputKeys.INDENT, "yes");
            // 创建xml文件并写入内容
            tf.transform(new DOMSource(document), new StreamResult(new File(xmlPath)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void downloadingFile(String filePath,String fileName,HttpServletResponse response) throws IOException {
        File file = new File(filePath);
        if (file.exists() && file.length() > 0) {
            try {
                byte[] data = FileCopyUtils.copyToByteArray(file);
                response.reset();
                response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "utf-8").replace("+", "%20"));
                response.addHeader("Content-Length", "" + data.length);
                response.setContentType("multipart/form-data");
                OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
                outputStream.write(data);
                outputStream.flush();
                outputStream.close();
            }catch (Exception ex){

            }
        }
    }

}
