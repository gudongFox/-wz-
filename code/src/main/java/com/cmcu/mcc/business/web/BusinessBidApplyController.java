package com.cmcu.mcc.business.web;

import com.cmcu.common.JsonData;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.business.dto.BusinessBidApplyDto;

import com.cmcu.mcc.business.service.BusinessBidApplyService;
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
@RequestMapping("/business/bigApply")
public class BusinessBidApplyController {
    @Autowired
    BusinessBidApplyService businessBidApplyService;

    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(businessBidApplyService.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin){
        return JsonData.success(businessBidApplyService.getNewModel(userLogin));
    }
    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        businessBidApplyService.remove(id,userLogin);
        return JsonData.success();
    }
    @PostMapping("/update.json")
    public JsonData update(@RequestBody BusinessBidApplyDto item){
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"操作人不能为空!");
        businessBidApplyService.update(item);
        return JsonData.success();
    }
    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin,int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = businessBidApplyService.listPagedData(params,userLogin,pageNum, pageSize);
        return JsonData.success(pageInfo);
    }
    @PostMapping("/getPrintData.json")
    public JsonData getPrintData(int id){
        return JsonData.success(businessBidApplyService.getPrintData(id));
    }

    @PostMapping("/listAgent.json")
    public JsonData selectAgent(){
    return JsonData.success(businessBidApplyService.listAgent());
}

    @PostMapping("/getNewModelById.json")
    public JsonData getNewModelById(int recordId,String userLogin){
        return JsonData.success(businessBidApplyService.getNewModelById(recordId,userLogin));
    }
    @PostMapping("/listAllUnExistRecord.json")
    public JsonData listAllUnExistRecord(int id) {
        return JsonData.success(businessBidApplyService.listAllUnExistRecord(id));
    }

}
