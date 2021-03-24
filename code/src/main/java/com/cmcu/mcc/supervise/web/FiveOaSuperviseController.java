package com.cmcu.mcc.supervise.web;

import com.cmcu.common.JsonData;
import com.cmcu.common.util.WebUtil;

import com.cmcu.mcc.supervise.dto.FiveOaSuperviseDto;
import com.cmcu.mcc.supervise.entity.FiveOaSuperviseDetail;
import com.cmcu.mcc.supervise.service.FiveOaSuperviseService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/five/supervise")
public class FiveOaSuperviseController {
    @Autowired
    FiveOaSuperviseService fiveOaSuperviseService;
    @PostMapping(value = "/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(fiveOaSuperviseService.getModelById(id));
    }
    @PostMapping("/getNewModel.json")


    public JsonData getNewModel(String userLogin,String superviseType){
        return JsonData.success(fiveOaSuperviseService.getNewModel(userLogin,superviseType));
    }
    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        fiveOaSuperviseService.remove(id,userLogin);
        return JsonData.success();
    }
    @PostMapping("/update.json")
    public JsonData update(@RequestBody FiveOaSuperviseDto item){
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"操作人不能为空!");
        fiveOaSuperviseService.update(item);
        return JsonData.success();
    }
    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin,String uiSref, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = fiveOaSuperviseService.listPagedData(params, userLogin, uiSref, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }
    @PostMapping(value = "/getNewModelDetail.json")
    public JsonData getNewModelDetail(int  id ){
        return JsonData.success(fiveOaSuperviseService.getNewModelDetail(id));
    }
    @PostMapping("/getModelDetailById.json")
    public JsonData getModelDetailById(int  id){
        return JsonData.success(fiveOaSuperviseService.getModelDetailById(id));
    }
   
    @PostMapping("/removeDetail.json")
    public JsonData removeDetail(int  id){
        fiveOaSuperviseService.removeDetail(id);
        return JsonData.success();
    }
    @PostMapping("/updateDetail.json")
    public JsonData updateDetail(@RequestBody FiveOaSuperviseDetail item){
        fiveOaSuperviseService.updateDetail(item);
        return JsonData.success();
    }
    @PostMapping("/listDetail.json")
    public JsonData listDetail(int  id){
        return JsonData.success(fiveOaSuperviseService.listDetail(id));
    }


    @PostMapping("/getNewModelByType.json")
    public JsonData getNewModelByType(String userLogin,String superviseType,String companyLeader,String businessId,String fileTitle,String view){
        return JsonData.success(fiveOaSuperviseService.getNewModelByType(userLogin,superviseType,companyLeader,businessId,fileTitle,view));
    }

    @PostMapping("/listPageTask.json")
    public JsonData listPageTask(String  userLogin){
        return JsonData.success(fiveOaSuperviseService.listPageTask(userLogin));
    }


}
