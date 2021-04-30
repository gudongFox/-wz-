package com.cmcu.act.web;

import com.cmcu.act.service.ProcessQueryService;
import com.common.model.JsonData;
import com.cmcu.common.util.WebUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Map;


@RestController
@RequestMapping("/act/processQuery")
public class ProcessQueryController {

    @Resource
    ProcessQueryService processQueryService;


    @PostMapping("/listPagedProcessInstance.json")
    public JsonData listPagedProcessInstance(int pageNum,int pageSize){
        Map params = WebUtil.getRequestParameters();
        return JsonData.success(processQueryService.listPagedProcessInstance(pageNum, pageSize, params));
    }


    @PostMapping("/listProcessInstanceByH5.json")
    public JsonData listProcessInstanceByH5(String initiator,String  involvedUser,String enLogin,int firstResult, int maxResults) {
        Map params = WebUtil.getRequestParameters();
        return JsonData.success(processQueryService.listProcessInstanceByH5(initiator, involvedUser,enLogin,params, firstResult, maxResults));
    }


    @PostMapping("/listProcessInstanceByCad.json")
    public JsonData listProcessInstanceByCad(String processDefinitionName,String enLogin,int firstResult, int maxResults) {
        return JsonData.success(processQueryService.listProcessInstanceByCad(processDefinitionName,enLogin,firstResult, maxResults));
    }

    @PostMapping("/listProcessInstanceGroupByCad.json")
    public JsonData listProcessInstanceGroupByCad(String tenetId,String enLogin) {
        return JsonData.success(processQueryService.listProcessInstanceGroupByCad(tenetId, enLogin));
    }

    @RequestMapping("/image/{processInstanceId}")
    public void  instanceImage(@PathVariable String processInstanceId, final HttpServletResponse response) throws IOException {
        if (StringUtils.isNotEmpty(processInstanceId)) {
            try {
                byte[] data = FileCopyUtils.copyToByteArray(processQueryService.getPngInputStream(processInstanceId));
                response.reset();
                response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(processInstanceId + ".png", "utf-8").replace("+", "%20"));
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

    @PostMapping("/getCustomProcessInstance.json")
    public JsonData getCustomProcessInstance(String processInstanceId,String businessKey,String enLogin){
        return JsonData.success(processQueryService.getCustomProcessInstance(processInstanceId,businessKey,enLogin));
    }


    @PostMapping("/getAdminCustomProcessInstance.json")
    public JsonData getAdminCustomProcessInstance(String processInstanceId,String businessKey) {
        return JsonData.success(processQueryService.getAdminCustomProcessInstance(processInstanceId,businessKey));
    }


    @RequestMapping("/listProcessCategoryTree.json")
    public JsonData listProcessCategoryTree(String tenetId,String enLogin) {
        return JsonData.success(processQueryService.listProcessCategoryTree(tenetId, enLogin));
    }

}
