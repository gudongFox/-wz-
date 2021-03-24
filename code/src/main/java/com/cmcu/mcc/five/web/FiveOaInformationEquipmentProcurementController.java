package com.cmcu.mcc.five.web;

import com.cmcu.common.JsonData;
import com.cmcu.common.util.FileUtil;
import com.cmcu.common.util.MyPoiUtil;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.five.dto.FiveOaInformationEquipmentProcurementDto;
import com.cmcu.mcc.five.entity.FiveOaInformationEquipmentProcurementDetail;
import com.cmcu.mcc.five.service.FiveOaInformationEquipmentProcurementService;
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
@RequestMapping("/five/oa/informationEquipmentProcurement")
public class FiveOaInformationEquipmentProcurementController {
    @Autowired
    FiveOaInformationEquipmentProcurementService fiveOaInformationEquipmentProcurementService;

    @PostMapping(value = "/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(fiveOaInformationEquipmentProcurementService.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin){
        return JsonData.success(fiveOaInformationEquipmentProcurementService.getNewModel(userLogin));
    }
    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        fiveOaInformationEquipmentProcurementService.remove(id,userLogin);
        return JsonData.success();
    }
    @PostMapping("/update.json")
    public JsonData update(@RequestBody FiveOaInformationEquipmentProcurementDto item){
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"操作人不能为空!");
        fiveOaInformationEquipmentProcurementService.update(item);
        return JsonData.success();
    }
    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin,String uiSref, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = fiveOaInformationEquipmentProcurementService.listPagedData(params, userLogin, uiSref, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }


    @PostMapping(value = "/getNewModelDetail.json")
    public JsonData getNewModelDetail(int  id ){
        return JsonData.success(fiveOaInformationEquipmentProcurementService.getNewModelDetail(id));
    }

    @PostMapping("/getModelDetailById.json")
    public JsonData getModelDetailById(int  id){
        return JsonData.success(fiveOaInformationEquipmentProcurementService.getModelDetailById(id));
    }
    @PostMapping("/removeDetail.json")
    public JsonData removeDetail(int  id){
        fiveOaInformationEquipmentProcurementService.removeDetail(id);
        return JsonData.success();
    }
    @PostMapping("/updateDetail.json")
    public JsonData updateDetail(@RequestBody FiveOaInformationEquipmentProcurementDetail item){
        fiveOaInformationEquipmentProcurementService.updateDetail(item);
        return JsonData.success();
    }
    @PostMapping("/listDetail.json")
    public JsonData listDetail(int  id){
        return JsonData.success(fiveOaInformationEquipmentProcurementService.listDetail(id));
    }
    @PostMapping("/getPrintData.json")
    public JsonData getPrintData(int id){
        return JsonData.success(fiveOaInformationEquipmentProcurementService.getPrintData(id));
    }

    @PostMapping("/downTempleXls.json")
    public void downTempleXls(String startTime1,String uiSref,String endTime1,String userLogin,String userName , final HttpServletResponse response) {
        Map params = WebUtil.getRequestParameters();
        List<Map> list = fiveOaInformationEquipmentProcurementService.listMapData(params,uiSref,startTime1,endTime1,userLogin,userName);
        MyPoiUtil.downloadExcel(list, FileUtil.getGoodFileName("资产管理年度信息化设备采购预算表"+ DateFormatUtils.format(new Date(),"yyyyMMddHHmm")+".xls"), response);
    }
}
