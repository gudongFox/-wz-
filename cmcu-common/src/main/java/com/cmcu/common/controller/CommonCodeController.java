package com.cmcu.common.controller;

import com.common.model.JsonData;
import com.cmcu.common.entity.CommonCode;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.MyPoiUtil;
import com.cmcu.common.util.WebUtil;
import com.cmcu.common.service.CommonUserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/common/code")
public class CommonCodeController {
    final
    CommonCodeService commonCodeService;
    final
    CommonUserService commonUserService;

    public CommonCodeController(CommonCodeService commonCodeService, CommonUserService commonUserService) {
        this.commonCodeService = commonCodeService;
        this.commonUserService = commonUserService;
    }

    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(int pageNum, int pageSize,String enLogin) {
        String tenetId=commonUserService.getTenetId(enLogin);
        Map params = WebUtil.getRequestParameters();
        params.put("deleted",false);
        params.put("tenetId",tenetId);
        return JsonData.success(commonCodeService.listPagedData(pageNum, pageSize,params));
    }

    @PostMapping("/save.json")
    public JsonData save(@RequestBody CommonCode item){
        commonCodeService.save(item);
        return JsonData.success();
    }


    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(commonCodeService.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String enLogin) {
        JsonData jsonData =JsonData.success(commonCodeService.getNewModel(enLogin));
        return jsonData;
    }
    @RequestMapping("/listSelectTypeByCatalog.json")
    public List<Map> listSelectTypeByCatalog(String enLogin,String codeCatalog) {
        String tenetId=commonUserService.getTenetId(enLogin);
        return commonCodeService.listSelectTypeByCatalog(tenetId,codeCatalog);
    }

    @PostMapping("/remove.json")
    public JsonData remove(int id,String enLogin){
        commonCodeService.remove(id,enLogin);
        return JsonData.success();
    }


    @PostMapping("/listCodeCategory.json")
    public JsonData listCodeCategory(String enLogin) {
        String tenetId=commonUserService.getTenetId(enLogin);
        JsonData jsonData =JsonData.success(commonCodeService.listCodeCategory(tenetId));
        return jsonData;
    }
    @PostMapping("/listDataByCatalog.json")
    public JsonData listDataByCatalog(String enLogin,String codeCatalog) {
        String tenetId=commonUserService.getTenetId(enLogin);
        JsonData jsonData =JsonData.success(commonCodeService.listDataByCatalog(tenetId,codeCatalog));
        return jsonData;
    }


    @PostMapping("/listData.json")
    public JsonData listData(String tenetId,String codeCatalog) {
        JsonData jsonData =JsonData.success(commonCodeService.listDataByCatalog(tenetId,codeCatalog));
        return jsonData;
    }


    @RequestMapping("/receive.json")
    public JsonData receive(MultipartFile file, String enLogin) throws IOException {
        String tenetId=commonUserService.getTenetId(enLogin);
        List<Map> list= MyPoiUtil.listTableData(file.getInputStream(),0,0,true);
        commonCodeService.uploadData(list,tenetId);
        return JsonData.success();
    }

    @RequestMapping("/selectAll.json")
    public JsonData selectAll(String tenetId) throws IOException {
        JsonData jsonData =JsonData.success(commonCodeService.selectAll(tenetId));
        return jsonData;
    }



}
