package com.cmcu.act.web;

import com.cmcu.act.service.ProcessHandleService;
import com.cmcu.act.service.ProcessQueryService;
import com.common.model.JsonData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/act/processHandle")
public class ProcessHandleController {


    final
    ProcessHandleService processHandleService;


    final
    ProcessQueryService processQueryService;

    public ProcessHandleController(ProcessHandleService processHandleService, ProcessQueryService processQueryService) {
        this.processHandleService = processHandleService;
        this.processQueryService = processQueryService;
    }

    @PostMapping("/toggleStar.json")
    public JsonData toggleStar(String processInstanceId,String enLogin){
        processHandleService.toggleStar(processInstanceId,enLogin);
        return JsonData.success();
    }


    @PostMapping("/suspendProcessInstanceById.json")
    public JsonData suspendProcessInstanceById(String processInstanceId){
        processHandleService.suspendProcessInstanceById(processInstanceId);
        return JsonData.success();
    }

    @PostMapping("/activateProcessInstanceById.json")
    public JsonData activateProcessInstanceById(String processInstanceId){
        processHandleService.activateProcessInstanceById(processInstanceId);
        return JsonData.success();
    }

    @PostMapping("/deleteProcessInstanceById.json")
    public JsonData deleteProcessInstanceById(String processInstanceId,String enLogin,String deleteReason){
        processHandleService.deleteProcessInstanceById(processInstanceId,enLogin,deleteReason);
        return JsonData.success();
    }


}
