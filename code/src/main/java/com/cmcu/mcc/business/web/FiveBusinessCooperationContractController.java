package com.cmcu.mcc.business.web;

import com.common.model.JsonData;
import com.cmcu.common.util.FileUtil;
import com.cmcu.common.util.MyPoiUtil;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.business.dto.FiveBusinessCooperationContractDto;
import com.cmcu.mcc.business.service.FiveBusinessCooperationContractService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/five/business/cooperationContract")
public class FiveBusinessCooperationContractController {
    @Autowired
    FiveBusinessCooperationContractService fiveBusinessCooperationContractService;

    @PostMapping("/downData.json")
    public void downData(String userLogin,int outAssistId,final HttpServletResponse response) {
        List<Map> list = fiveBusinessCooperationContractService.downData(userLogin,outAssistId);
        MyPoiUtil.downloadExcel(list, FileUtil.getGoodFileName("内部协作协议合同:"+ DateFormatUtils.format(new Date(),"yyyyMMddHHmm")+".xls"), response);
    }
    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id) {
        return JsonData.success(fiveBusinessCooperationContractService.getModelById(id));
    }
    @PostMapping("/getInteriorContractNo.json")
    public JsonData getInteriorContractNo(int id) {
        fiveBusinessCooperationContractService.getInteriorContractNo(id);
        return JsonData.success();
    }


    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin,String uiSref) {
        return JsonData.success(fiveBusinessCooperationContractService.getNewModel(userLogin,uiSref));
    }

    @PostMapping("/remove.json")
    public JsonData remove(int id, String userLogin) {
        fiveBusinessCooperationContractService.remove(id, userLogin);
        return JsonData.success();
    }

    @PostMapping("/update.json")
    public JsonData update(@RequestBody FiveBusinessCooperationContractDto item) {
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()), "操作人不能为空!");
        fiveBusinessCooperationContractService.update(item);
        return JsonData.success();
    }


    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin,String uiSref, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = fiveBusinessCooperationContractService.listPagedData(params, userLogin, uiSref, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }

    @PostMapping("/selectAll.json")
    public JsonData selectAll(String userLogin,String uiSref){
        return JsonData.success(fiveBusinessCooperationContractService.selectAll(userLogin,uiSref));
    }
}


