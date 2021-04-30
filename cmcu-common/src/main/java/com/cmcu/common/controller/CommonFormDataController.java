package com.cmcu.common.controller;

import com.common.model.JsonData;
import com.cmcu.common.dao.CommonFormDetailMapper;
import com.cmcu.common.dto.FastUserDto;
import com.cmcu.common.dto.TplConfigDto;
import com.cmcu.common.dto.UserDto;
import com.cmcu.common.service.CommonFormDataService;
import com.cmcu.common.service.CommonUserService;
import com.cmcu.common.service.IHandleFormService;
import com.cmcu.common.util.WebUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/common/formData")
public class CommonFormDataController {

    final
    CommonFormDataService commonFormDataService;

    final
    CommonUserService commonUserService;

    @Resource
    IHandleFormService handleFormService;



    public CommonFormDataController(CommonFormDataService commonFormDataService, CommonUserService commonUserService) {
        this.commonFormDataService = commonFormDataService;
        this.commonUserService = commonUserService;
    }


    @PostMapping("/listData.json")
    public JsonData listData(String referType,int referId,String q,String enLogin) {
        return JsonData.success(commonFormDataService.listData(referType, referId, q,enLogin));
    }

    @PostMapping("/listDataByUser.json")
    public JsonData listDataByUser(String referType,String q,String enLogin) {
        FastUserDto userDto = commonUserService.getFastByEnLogin(enLogin);
        int referId = userDto.getDeptId();
        if ("hrTrain".equalsIgnoreCase(referType)) {
            referId = 0;
        }
        return JsonData.success(commonFormDataService.listData(referType, referId, q, enLogin));
    }

    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(int pageNum,int pageSize,String referType,String enLogin,String q) {
        Map params = WebUtil.getRequestParameters();
        return JsonData.success(commonFormDataService.listPagedData(pageNum, pageSize, referType, enLogin, q, params));
    }
    @PostMapping("/listActPagedData.json")
    public JsonData listActPagedData(int pageNum,int pageSize,String referType,String enLogin,String q) {
        Map params = WebUtil.getRequestParameters();
        return JsonData.success(commonFormDataService.listActPagedData(pageNum, pageSize, referType, enLogin, q, params));
    }


    @PostMapping("/getNewModelByUser.json")
    public JsonData getNewModelByUser(String referType,String enLogin){
        FastUserDto userDto=commonUserService.getFastByEnLogin(enLogin);
        return JsonData.success(commonFormDataService.getNewModel(referType,userDto.getDeptId(),enLogin));
    }



    @Resource
    CommonFormDetailMapper commonFormDetailMapper;

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String referType,int referId,String enLogin){
        return JsonData.success(commonFormDataService.getNewModel(referType,referId,enLogin).getBusinessKey());
    }


    @PostMapping("/getFormDataById.json")
    public JsonData getFormDataById(int id){
        return JsonData.success(commonFormDataService.getFormDataById(id));
    }


    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id,String enLogin){
        return JsonData.success(commonFormDataService.getModelById(id,enLogin));
    }

    @PostMapping("/getModelByBusinessKey.json")
    public JsonData getModelByBusinessKey(String businessKey,String enLogin){
        return JsonData.success(commonFormDataService.getDataByBusinessKey(businessKey,enLogin));
    }

    @PostMapping("/save.json")
    public JsonData save(@RequestBody Map data) {
        int id = Integer.parseInt(data.get("id").toString());
        String enLogin = data.get("enLogin").toString();
        data.remove("id");
        data.remove("enLogin");
        commonFormDataService.save(id, data, enLogin);
        return JsonData.success();
    }

    @PostMapping("/remove.json")
    public JsonData remove(int id,String enLogin){
        commonFormDataService.remove(id,enLogin);
        return JsonData.success();
    }


    @PostMapping("/checkConfigMarkRole.json")
    public JsonData checkConfigMarkRole(String businessKey) {
        return JsonData.success(commonFormDataService.checkConfigMarkRole(businessKey));
    }


    @PostMapping("/getTplConfig.json")
    public JsonData getTplConfig(String processInstanceId,String businessKey,String enLogin) {
        TplConfigDto tplConfigDto=handleFormService.getTplConfigDto(processInstanceId, businessKey, enLogin);
        return JsonData.success(tplConfigDto);
    }

    @PostMapping("/listFileType.json")
    public com.common.model.JsonData listFileType(String businessKey){
        return com.common.model.JsonData.success(commonFormDataService.listFileType(businessKey));
    }




}
