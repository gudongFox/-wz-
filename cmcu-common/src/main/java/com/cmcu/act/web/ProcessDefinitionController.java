package com.cmcu.act.web;

import com.cmcu.act.service.ProcessDefinitionService;
import com.common.model.JsonData;
import com.cmcu.common.util.WebUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Map;

@RestController
@RequestMapping("/act/processDefinition")
public class ProcessDefinitionController {

    final
    ProcessDefinitionService processDefinitionService;



    public ProcessDefinitionController(ProcessDefinitionService processDefinitionService) {
        this.processDefinitionService = processDefinitionService;
    }


    @PostMapping("/listPagedDefinition.json")
    public JsonData listPagedDefinition(int pageNum, int pageSize){
        Map params = WebUtil.getRequestParameters();
        return JsonData.success(processDefinitionService.listPagedData(pageNum, pageSize, params));
    }

    @PostMapping("/removeDefinition.json")
    public JsonData removeDefinition(String deploymentId){
        processDefinitionService.remove(deploymentId);
        return JsonData.success();
    }

    @RequestMapping("/image/{deploymentId}")
    public void  definitionImage(@PathVariable String deploymentId, final HttpServletResponse response) throws IOException {
        if (StringUtils.isNotEmpty(deploymentId)) {
            try {
                byte[] data = FileCopyUtils.copyToByteArray(processDefinitionService.getPngInputStream(deploymentId));
                response.reset();
                response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(deploymentId + ".png", "utf-8").replace("+", "%20"));
                response.addHeader("Content-Length", "" + data.length);
                response.setContentType("multipart/form-data");
                OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
                outputStream.write(data);
                outputStream.flush();
                outputStream.close();
            } catch (Exception ex) {

            }
        }
    }


}

