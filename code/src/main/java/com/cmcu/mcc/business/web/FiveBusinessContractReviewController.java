package com.cmcu.mcc.business.web;

import com.common.model.JsonData;
import com.cmcu.common.util.WebUtil;

import com.cmcu.mcc.business.dto.FiveBusinessContractReviewDto;
import com.cmcu.mcc.business.entity.FiveBusinessContractReviewDetail;
import com.cmcu.mcc.business.service.FiveBusinessContractReviewService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.Map;

@RestController
@RequestMapping("business/contractReview")
public class FiveBusinessContractReviewController {
    @Autowired
    FiveBusinessContractReviewService fiveBusinessContractReviewService;

    @PostMapping(value = "/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(fiveBusinessContractReviewService.getModelById(id));
    }
    @PostMapping("/addCustomer.json")
    public JsonData addCustomer(String userLogin,String recordId) {
        return JsonData.success( fiveBusinessContractReviewService.addCustomer(recordId,userLogin));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin){
        return JsonData.success(fiveBusinessContractReviewService.getNewModel(userLogin));
    }
    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        fiveBusinessContractReviewService.remove(id,userLogin);
        return JsonData.success();
    }
    @PostMapping("/changeOpen.json")
    public JsonData changeOpen(int id,String userLogin){
        fiveBusinessContractReviewService.changeOpen(id,userLogin);
        return JsonData.success();
    }
    @PostMapping("/changeOpenStamp.json")
    public JsonData changeOpenStamp(int id,String userLogin){
        fiveBusinessContractReviewService.changeOpenStamp(id,userLogin);
        return JsonData.success();
    }

    @PostMapping("/update.json")
    public JsonData update(@RequestBody FiveBusinessContractReviewDto item) throws ParseException {
        //Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"操作人不能为空!");
        fiveBusinessContractReviewService.update(item);
        return JsonData.success();
    }
    @PostMapping(value = "/listPagedData.json")
    public JsonData loadPagedData(int pageNum,String uiSref, int pageSize ) {
        Map params = WebUtil.getRequestParameters();
        return JsonData.success(fiveBusinessContractReviewService.listPagedData(params,pageNum,pageSize,uiSref));
    }

    @PostMapping(value = "/listDetailById.json")
    public JsonData listDetailById(int contractReviewId){
        return JsonData.success(fiveBusinessContractReviewService.listDetailById(contractReviewId));
    }
    @PostMapping(value = "/getDetailModelById.json")
    public JsonData getDetailModelById(int id){
        return JsonData.success(fiveBusinessContractReviewService.getDetailModelById(id));
    }

    @PostMapping("/getNewDetailModel.json")
    public JsonData getNewDetailModel(int contractReviewId){
        return JsonData.success(fiveBusinessContractReviewService.getNewDetailModel(contractReviewId));
    }
    @PostMapping("/removeDetail.json")
    public JsonData removeDetail(int id,String userLogin){
        fiveBusinessContractReviewService.removeDetail(id,userLogin);
        return JsonData.success();
    }
    @PostMapping("/updateDetail.json")
    public JsonData updateDetail(@RequestBody FiveBusinessContractReviewDetail item){
        fiveBusinessContractReviewService.updateDetail(item);
        return JsonData.success();
    }

    @PostMapping("/insertContractLibrary.json")
    public JsonData insertContractLibrary(int contractReviewId){
        return JsonData.success(fiveBusinessContractReviewService.insertContractLibrary(contractReviewId));
    }


    @PostMapping("/selectNotHaveContract.json")
    public JsonData selectNotHaveContract(int  contractReviewId){
        return JsonData.success(fiveBusinessContractReviewService.selectNotHaveContract(contractReviewId));
    }
    @PostMapping("/getContractNo.json")
    public JsonData getContractNo(int  contractReviewId){
        return JsonData.success(fiveBusinessContractReviewService.getContractNo(contractReviewId));
    }
    @PostMapping("/getMainContractNo.json")
    public JsonData getMainContractNo(int  contractReviewId){
        return JsonData.success(fiveBusinessContractReviewService.getMainContractNo(contractReviewId));
    }
    @PostMapping("/addSuccessContractDir.json")
    public JsonData addSuccessContractDir(String  businessKey,int  attachId,String userLogin){
        fiveBusinessContractReviewService.addSuccessContractDir(businessKey,attachId,userLogin);
        return JsonData.success();
    }



}
