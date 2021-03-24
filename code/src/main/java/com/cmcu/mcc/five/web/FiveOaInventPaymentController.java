package com.cmcu.mcc.five.web;

import com.cmcu.common.JsonData;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.five.dto.FiveOaInventPaymentDto;
import com.cmcu.mcc.five.entity.FiveOaInventPaymentDetail;
import com.cmcu.mcc.five.service.FiveOaInventPaymentService;
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
@RequestMapping("/five/oa/inventPayment")
public class FiveOaInventPaymentController {
    @Autowired
    FiveOaInventPaymentService fiveOaInventPaymentService;


    @PostMapping(value = "/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(fiveOaInventPaymentService.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin){
        return JsonData.success(fiveOaInventPaymentService.getNewModel(userLogin));
    }
    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        fiveOaInventPaymentService.remove(id,userLogin);
        return JsonData.success();
    }
    @PostMapping("/update.json")
    public JsonData update(@RequestBody FiveOaInventPaymentDto item){
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"操作人不能为空!");
        fiveOaInventPaymentService.update(item);
        return JsonData.success();
    }
    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin,String uiSref, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = fiveOaInventPaymentService.listPagedData(params, userLogin, uiSref, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }



    @PostMapping(value = "/getNewModelDetail.json")
    public JsonData getNewModelDetail(int recordId){
        return JsonData.success(fiveOaInventPaymentService.getNewModelDetail(recordId));
    }

    @PostMapping("/getModelDetailById.json")
    public JsonData getModelDetailById(int  id){
        return JsonData.success(fiveOaInventPaymentService.getModelDetailById(id));
    }
    @PostMapping("/removeDetail.json")
    public JsonData removeDetail(int  id){
        fiveOaInventPaymentService.removeDetail(id);
        return JsonData.success();
    }
    @PostMapping("/updateDetail.json")
    public JsonData updateDetail(@RequestBody FiveOaInventPaymentDetail item){
        fiveOaInventPaymentService.updateDetail(item);
        return JsonData.success();
    }
    @PostMapping("/listDetail.json")
    public JsonData listDetail(int  recordId){
        return JsonData.success(fiveOaInventPaymentService.listDetail(recordId));
    }
    @PostMapping("/getPrintData.json")
    public JsonData getPrintData(int id){
        return JsonData.success(fiveOaInventPaymentService.getPrintData(id));
    }


}
