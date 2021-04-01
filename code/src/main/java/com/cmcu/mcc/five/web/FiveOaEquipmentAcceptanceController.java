package com.cmcu.mcc.five.web;

import com.cmcu.common.JsonData;
import com.cmcu.common.util.FileUtil;
import com.cmcu.common.util.MyPoiUtil;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.five.entity.FiveOaCardChange;
import com.cmcu.mcc.five.entity.FiveOaCardChangeDetail;
import com.cmcu.mcc.five.entity.FiveOaEquipmentAcceptance;
import com.cmcu.mcc.five.entity.FiveOaEquipmentAcceptanceDetail;
import com.cmcu.mcc.five.service.FiveOaCardChangeService;
import com.cmcu.mcc.five.service.FiveOaEquipmentAcceptanceService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/five/oa/equipmentAcceptance")
public class FiveOaEquipmentAcceptanceController {
    @Resource
    FiveOaEquipmentAcceptanceService fiveOaEquipmentAcceptanceService;


    @PostMapping(value = "/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(fiveOaEquipmentAcceptanceService.getModelById(id));
    }


    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin){
        return JsonData.success(fiveOaEquipmentAcceptanceService.getNewModel(userLogin));
    }
    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        fiveOaEquipmentAcceptanceService.remove(id,userLogin);
        return JsonData.success();
    }
    @PostMapping("/update.json")
    public JsonData update(@RequestBody FiveOaEquipmentAcceptance item){
        fiveOaEquipmentAcceptanceService.update(item);
        return JsonData.success();
    }
    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin,String uiSref, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = fiveOaEquipmentAcceptanceService.listPagedData(params, userLogin, uiSref, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }


    @PostMapping(value = "/getNewModelDetail.json")
    public JsonData getNewModelDetail(int  id,String userLogin){
        return JsonData.success(fiveOaEquipmentAcceptanceService.getNewModelDetail(id,userLogin));
    }

    @PostMapping("/getModelDetailById.json")
    public JsonData getModelDetailById(int  id){
        return JsonData.success(fiveOaEquipmentAcceptanceService.getModelDetailById(id));
    }
    @PostMapping("/removeDetail.json")
    public JsonData removeDetail(int  id){
        fiveOaEquipmentAcceptanceService.removeDetail(id);
        return JsonData.success();
    }
    @PostMapping("/updateDetail.json")
    public JsonData updateDetail(@RequestBody FiveOaEquipmentAcceptanceDetail item){
        fiveOaEquipmentAcceptanceService.updateDetail(item);
        return JsonData.success();
    }
    @PostMapping("/listDetail.json")
    public JsonData listDetail(int  id){
        return JsonData.success(fiveOaEquipmentAcceptanceService.listDetail(id));
    }

    @PostMapping("/downTempleXls.json")
    public void downTempleXls(String startTime,String endTime,String uiSref,String userLogin,final HttpServletResponse response) {
        Map params = WebUtil.getRequestParameters();
        List<Map> list = fiveOaEquipmentAcceptanceService.listMapData(params,uiSref,startTime,endTime,userLogin);
        MyPoiUtil.downloadExcel(list, FileUtil.getGoodFileName("资产管理物资验收(低值易耗)"+ DateFormatUtils.format(new Date(),"yyyyMMddHHmm")+".xls"), response);
    }

}
