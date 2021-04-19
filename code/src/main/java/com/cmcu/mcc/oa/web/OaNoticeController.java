package com.cmcu.mcc.oa.web;

import com.common.model.JsonData;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.oa.dto.OaNoticeDto;
import com.cmcu.mcc.oa.service.OaNoticeService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/oa/notice")
public class OaNoticeController {

    @Autowired
    OaNoticeService oaNoticeService;




    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(int pageNum, int pageSize,String userLogin) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = oaNoticeService.listPagedData(params,userLogin,pageNum, pageSize);
        return JsonData.success(pageInfo);
    }
    @PostMapping("/fuzzySearch.json")
    public JsonData fuzzySearch(int pageNum, int pageSize,String userLogin) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = oaNoticeService.fuzzySearch(params,userLogin,pageNum, pageSize);
        return JsonData.success(pageInfo);
    }

    @PostMapping("/listPagedDataByType.json")
    public JsonData listPagedDataByType(String type,String userLogin, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = oaNoticeService.listPagedDataByType(params,type,userLogin,pageNum, pageSize);
        return JsonData.success(pageInfo);
    }

    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id,String userLogin){
        return JsonData.success(oaNoticeService.getModelById(id,userLogin));
    }

    @PostMapping("/getModelByBusinessKey.json")
    public JsonData getModelByBusinessKey(String businessKey,String enLogin){
        return JsonData.success(oaNoticeService.getModelByBusinessKey(businessKey,enLogin));
    }



    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin){
        return JsonData.success(oaNoticeService.getNewModel(userLogin));
    }
    @PostMapping("/getNewModelByType.json")
    public JsonData getNewModelByType(String userLogin,String type){
        return JsonData.success(oaNoticeService.getNewModelByType(userLogin,type));
    }
    @PostMapping("/selectAll.json")
    public JsonData selectAll(){
        return JsonData.success(oaNoticeService.selectAll());
    }

    @PostMapping("/listFirmDateByType.json")
    public JsonData listFirmDateByType(String type,String userLogin){
        return JsonData.success(oaNoticeService.listFirmDateByType(type,userLogin));
    }

    @PostMapping("/listDateByDeptName.json")
    public JsonData listDateByDeptName(String userLogin){
        return JsonData.success(oaNoticeService.listDateByDeptName(userLogin));
    }

    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        oaNoticeService.remove(id,userLogin);
        return JsonData.success();
    }
    @PostMapping("/update.json")
    public JsonData update(@RequestBody OaNoticeDto item){
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"操作人不能为空!");
        oaNoticeService.update(item);
        return JsonData.success();
    }
    @PostMapping("/getOtherType.json")
    public JsonData getOtherType(){
        return JsonData.success(oaNoticeService.getOtherType());
    }

    @PostMapping("/getNewsPhoto.json")
    public JsonData getNewsPhoto(){
        return JsonData.success(oaNoticeService.getNewsPhoto());
    }

    @PostMapping("/listPagedDataByUserLogin.json")
    public JsonData listPagedDataByUserLogin(String deptName,String userLogin, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = oaNoticeService.listPagedDataByUserLogin(params,userLogin, deptName,pageNum, pageSize);
        return JsonData.success(pageInfo);
    }
    @PostMapping("/listPagedDataOrderType.json")
    public JsonData listPagedDataOrderType(String deptName,String userLogin,int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        List<Map> list = oaNoticeService.listPagedDataOrderType(params,userLogin, deptName,pageNum, pageSize);
        return JsonData.success(list);
    }

    @PostMapping("/getNewModelByDispatch.json")
    public JsonData getNewModelByDispatch(String businessKey,String types,String noticeLevel,String noticeSystemType){
        oaNoticeService.getNewModelByDispatch(businessKey,types,noticeLevel,noticeSystemType);
        return JsonData.success();
    }


    @PostMapping("/sendToWx.json")
    public JsonData sendToWx(int oaNoticeId){
        oaNoticeService.sendToWx(oaNoticeId);
        return JsonData.success();
    }
    @PostMapping("/sendToWxTest.json")
    public JsonData sendToWxTest(int oaNoticeId){
        oaNoticeService.sendToWxTest(oaNoticeId);
        return JsonData.success();
    }
    @PostMapping("/getAcceptUser.json")
    public JsonData getAcceptUser(int oaNoticeId,String type){
        return JsonData.success( oaNoticeService.getAcceptUser(oaNoticeId,type));
    }



}


