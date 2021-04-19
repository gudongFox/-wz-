package com.cmcu.mcc.five.web;

import com.common.model.JsonData;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.five.dto.FiveHrQualifyApplyDto;
import com.cmcu.mcc.five.dto.FiveHrQualifyChiefDto;
import com.cmcu.mcc.five.entity.FiveHrQualifyApplyDetail;
import com.cmcu.mcc.five.service.FiveHrQualifyApplyService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/five/hr/qualifyApply")
public class FiveHrQualifyApplyController {


    @Autowired
    FiveHrQualifyApplyService fiveHrQualifyApplyService;


    @PostMapping("/update.json")
    public JsonData update(@RequestBody FiveHrQualifyApplyDto item){
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"当前登录用户不能为空!");
        fiveHrQualifyApplyService.update(item);
        return JsonData.success();
    }

    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        fiveHrQualifyApplyService.remove(id,userLogin);
        return JsonData.success();
    }

    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(fiveHrQualifyApplyService.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String uiSref,String userLogin){
        return JsonData.success(fiveHrQualifyApplyService.getNewModel(uiSref,userLogin));
    }

    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin,String uiSref, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = fiveHrQualifyApplyService.listPagedData(params, userLogin, uiSref, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }



    @PostMapping("/listDetail.json")
    public JsonData  listDetail(int qualifyApplyId){
        return JsonData.success(fiveHrQualifyApplyService.listDetail(qualifyApplyId));
    }

    @PostMapping("/clearDetail.json")
    public JsonData clearDetail(int qualifyApplyId){
        fiveHrQualifyApplyService.clearDetail(qualifyApplyId);
        return JsonData.success();
    }

    @PostMapping("/removeDetail.json")
    public JsonData removeDetail(int id){
        fiveHrQualifyApplyService.removeDetail(id);
        return JsonData.success();
    }

    @PostMapping("/initDetailList.json")
    public JsonData initDetailList(int qualifyApplyId){
        fiveHrQualifyApplyService.initDetailList(qualifyApplyId);
        return JsonData.success();
    }

    @PostMapping("/insertDetail.json")
    public JsonData insertDetail(int qualifyApplyId,String userLoginList){
        fiveHrQualifyApplyService.insertDetail(qualifyApplyId,userLoginList);
        return JsonData.success();
    }

    @PostMapping("/toggleDetail.json")
    public JsonData toggleDetail(int id,String optType){
        fiveHrQualifyApplyService.toggleDetail(id,optType);
        return JsonData.success();
    }

    @PostMapping("/copyDetail.json")
    public JsonData copyDetail(int id){
        fiveHrQualifyApplyService.copyDetail(id);
        return JsonData.success();
    }

    @PostMapping("/updateDetail.json")
    public JsonData updateDetail(int id,String majorName,String projectType){
        fiveHrQualifyApplyService.updateDetail(id,majorName,projectType);
        return JsonData.success();
    }
}
