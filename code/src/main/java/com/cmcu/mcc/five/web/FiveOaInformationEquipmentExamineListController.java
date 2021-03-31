package com.cmcu.mcc.five.web;

import com.cmcu.common.JsonData;
import com.cmcu.common.util.FileUtil;
import com.cmcu.common.util.MyPoiUtil;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.five.dto.FiveOaInformationEquipmentExamineListDto;
import com.cmcu.mcc.five.entity.FiveOaInformationEquipmentExamineListDetail;
import com.cmcu.mcc.five.service.FiveOaInformationEquipmentExamineListService;
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
@RequestMapping("/five/oa/informationEquipmentExamineList")
public class FiveOaInformationEquipmentExamineListController {

    @Autowired
    FiveOaInformationEquipmentExamineListService fiveOaInformationEquipmentExamineListService;

    @PostMapping(value = "/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(fiveOaInformationEquipmentExamineListService.getModelById(id));
    }


    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin){
        return JsonData.success(fiveOaInformationEquipmentExamineListService.getNewModel(userLogin));
    }
    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        fiveOaInformationEquipmentExamineListService.remove(id,userLogin);
        return JsonData.success();
    }
    @PostMapping("/update.json")
    public JsonData update(@RequestBody FiveOaInformationEquipmentExamineListDto item){
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"操作人不能为空!");
        fiveOaInformationEquipmentExamineListService.update(item);
        return JsonData.success();
    }
    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin,String uiSref, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = fiveOaInformationEquipmentExamineListService.listPagedData(params, userLogin, uiSref, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }

    @PostMapping(value = "/getNewModelDetail.json")
    public JsonData getNewModelDetail(int  id ){
        return JsonData.success(fiveOaInformationEquipmentExamineListService.getNewModelDetail(id));
    }

    @PostMapping("/getModelDetailById.json")
    public JsonData getModelDetailById(int  id){
        return JsonData.success(fiveOaInformationEquipmentExamineListService.getModelDetailById(id));
    }
    @PostMapping("/removeDetail.json")
    public JsonData removeDetail(int  id){
        fiveOaInformationEquipmentExamineListService.removeDetail(id);
        return JsonData.success();
    }
    @PostMapping("/updateDetail.json")
    public JsonData updateDetail(@RequestBody FiveOaInformationEquipmentExamineListDetail item){
        fiveOaInformationEquipmentExamineListService.updateDetail(item);
        return JsonData.success();
    }
    @PostMapping("/listDetail.json")
    public JsonData listDetail(int  id){
        return JsonData.success(fiveOaInformationEquipmentExamineListService.listDetail(id));
    }
    @PostMapping("/getPrintData.json")
    public JsonData getPrintData(int id){
        return JsonData.success(fiveOaInformationEquipmentExamineListService.getPrintData(id));
    }
    @PostMapping("/getNewDetailByApplyDetail.json")
    public JsonData getNewDetailByApplyDetail(int  detailId,int listId ,String userLogin){
        fiveOaInformationEquipmentExamineListService.getNewDetailByApplyDetail(detailId,listId,userLogin);
        return JsonData.success();
    }
    @PostMapping("/getEquipmentExamineNo.json")
    public JsonData getEquipmentExamineNo(int  id){
        fiveOaInformationEquipmentExamineListService.getEquipmentExamineNo(id);
        return JsonData.success();
    }

    @PostMapping("/downTempleXls.json")
    public void downTempleXls(String startTime1,String uiSref,String endTime1,String userLogin,final HttpServletResponse response) {
        Map params = WebUtil.getRequestParameters();
        List<Map> list = fiveOaInformationEquipmentExamineListService.listMapData(params,uiSref,startTime1,endTime1,userLogin);
        MyPoiUtil.downloadExcel(list, FileUtil.getGoodFileName("资产管理信息化设备验收审批(多台)"+ DateFormatUtils.format(new Date(),"yyyyMMddHHmm")+".xls"), response);
    }
}
