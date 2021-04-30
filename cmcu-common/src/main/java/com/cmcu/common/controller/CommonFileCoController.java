package com.cmcu.common.controller;

import com.common.model.JsonData;
import com.cmcu.common.service.CommonFileCoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/common/file/co")
public class CommonFileCoController {

    @Resource
    CommonFileCoService commonFileCoService;

    @PostMapping("/listSelectDir.json")
    public JsonData listSelectDir(String businessKey,int coParentId, String enLogin)  {
        return JsonData.success(commonFileCoService.listSelectDir(businessKey,coParentId, enLogin));
    }

    @PostMapping("/listSelectFile.json")
    public JsonData listSelectFile(String businessKey, int coParentId, String enLogin) {
        return JsonData.success(commonFileCoService.listSelectFile(businessKey, coParentId, enLogin));
    }


    @PostMapping("/insertSelect.json")
    public JsonData insertSelect(String businessKey, int coParentId,String coDirIds,String coFileIds, String enLogin) {
        commonFileCoService.insertSelect(businessKey, coParentId,coDirIds,coFileIds,enLogin);
        return JsonData.success();
    }

    @PostMapping("/listBreadcrumb.json")
    public JsonData listBreadcrumb(String businessKey,int coParentId, String enLogin)  {
        return JsonData.success(commonFileCoService.listBreadcrumb(businessKey,coParentId, enLogin));
    }


}
