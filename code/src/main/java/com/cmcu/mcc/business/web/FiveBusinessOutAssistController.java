package com.cmcu.mcc.business.web;

import com.common.model.JsonData;
import com.cmcu.common.util.FileUtil;
import com.cmcu.common.util.MyPoiUtil;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.business.dto.FiveBusinessOutAssistDto;
import com.cmcu.mcc.business.entity.FiveBusinessOutAssist;
import com.cmcu.mcc.business.entity.FiveBusinessOutAssistDetail;
import com.cmcu.mcc.business.service.FiveBusinessOutAssistService;
import com.cmcu.mcc.finance.dto.FiveFinanceStampTaxDto;
import com.cmcu.mcc.finance.entity.FiveFinanceStampTaxDetail;
import com.cmcu.mcc.finance.service.FiveFinanceStampTaxService;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/five/business/outAssist")
public class FiveBusinessOutAssistController {
    @Autowired
    FiveBusinessOutAssistService fiveBusinessOutAssistService;

    @PostMapping("/downData.json")
    public void downData(String userLogin,int outAssistId,final HttpServletResponse response) {
        List<Map> list = fiveBusinessOutAssistService.downData(userLogin,outAssistId);
        MyPoiUtil.downloadExcel(list, FileUtil.getGoodFileName("月度外协费用支出"+ DateFormatUtils.format(new Date(),"yyyyMMddHHmm")+".xls"), response);
    }
    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id) {
        return JsonData.success(fiveBusinessOutAssistService.getModelById(id));
    }
    @PostMapping("/getDetailById.json")
    public JsonData getDetailById(int id) {
        return JsonData.success(fiveBusinessOutAssistService.getDetailById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin,String uiSref) {
        return JsonData.success(fiveBusinessOutAssistService.getNewModel(userLogin,uiSref));
    }

    @PostMapping("/remove.json")
    public JsonData remove(int id, String userLogin) {
        fiveBusinessOutAssistService.remove(id, userLogin);
        return JsonData.success();
    }

    @PostMapping("/update.json")
    public JsonData update(@RequestBody FiveBusinessOutAssistDto item) {
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()), "操作人不能为空!");
        fiveBusinessOutAssistService.update(item);
        return JsonData.success();
    }


    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin,String uiSref, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = fiveBusinessOutAssistService.listPagedData(params, userLogin, uiSref, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }

    @PostMapping("/selectAll.json")
    public JsonData selectAll(String userLogin,String uiSref){
        return JsonData.success(fiveBusinessOutAssistService.selectAll(userLogin,uiSref));
    }






    @PostMapping(value = "/getNewModelDetail.json")
    public JsonData getNewModelDetail(int  id,String userLogin){
        return JsonData.success(fiveBusinessOutAssistService.getNewModelDetail(id,userLogin));
    }

    @PostMapping("/getModelDetailById.json")
    public JsonData getModelDetailById(int  id){
        return JsonData.success(fiveBusinessOutAssistService.getModelDetailById(id));
    }
    @PostMapping("/removeDetail.json")
    public JsonData removeDetail(int  id){
        fiveBusinessOutAssistService.removeDetail(id);
        return JsonData.success();
    }
    @PostMapping("/updateDetail.json")
    public JsonData updateDetail(@RequestBody FiveBusinessOutAssistDetail item, int stampTaxId){
        fiveBusinessOutAssistService.updateDetail(item,stampTaxId);
        return JsonData.success();
    }


}


