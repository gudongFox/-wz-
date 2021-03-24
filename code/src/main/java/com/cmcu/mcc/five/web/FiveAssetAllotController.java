package com.cmcu.mcc.five.web;

import com.cmcu.common.JsonData;
import com.cmcu.common.util.FileUtil;
import com.cmcu.common.util.MyPoiUtil;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.five.dto.FiveAssetAllotDto;
import com.cmcu.mcc.five.entity.FiveAssetAllotDetail;
import com.cmcu.mcc.five.service.FiveAssetAllotService;
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
@RequestMapping("/five/asset/assetAllot")
public class FiveAssetAllotController {
    @Autowired
    FiveAssetAllotService fiveAssetAllotService;


    @PostMapping("/listDate.json")
    public JsonData listDate(String userLogin){
        return JsonData.success(fiveAssetAllotService.listDate(userLogin));
    }

    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        fiveAssetAllotService.remove(id,userLogin);
        return JsonData.success();
    }

    @PostMapping("/update.json")
    public JsonData update(@RequestBody FiveAssetAllotDto item){
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"操作人不能为空!");
        fiveAssetAllotService.update(item);
        return JsonData.success();
    }

    @PostMapping(value = "/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(fiveAssetAllotService.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin){
        return JsonData.success(fiveAssetAllotService.getNewModel(userLogin));
    }

    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin,String uiSref, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = fiveAssetAllotService.listPagedData(params, userLogin, uiSref, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }

    @PostMapping(value = "/getNewModelDetail.json")
    public JsonData getNewModelDetail(int  id,String userLogin){
        return JsonData.success(fiveAssetAllotService.getNewModelDetail(id,userLogin));
    }

    @PostMapping("/getModelDetailById.json")
    public JsonData getModelDetailById(int  id){
        return JsonData.success(fiveAssetAllotService.getModelDetailById(id));
    }
    @PostMapping("/removeDetail.json")
    public JsonData removeDetail(int  id){
        fiveAssetAllotService.removeDetail(id);
        return JsonData.success();
    }
    @PostMapping("/updateDetail.json")
    public JsonData updateDetail(@RequestBody FiveAssetAllotDetail item){
        fiveAssetAllotService.updateDetail(item);
        return JsonData.success();
    }

    @PostMapping("/listDetail.json")
    public JsonData listDetail(int  id){
        return JsonData.success(fiveAssetAllotService.listDetail(id));
    }

    @PostMapping("/downTempleXls.json")
    public void downTempleXls(String startTime1,String uiSref,String endTime1,String userLogin,final HttpServletResponse response) {
        Map params = WebUtil.getRequestParameters();
        List<Map> list = fiveAssetAllotService.listMapData(params,uiSref,startTime1,endTime1,userLogin);
        MyPoiUtil.downloadExcel(list, FileUtil.getGoodFileName("资产管理固定资产调拨(低值易耗)"+ DateFormatUtils.format(new Date(),"yyyyMMddHHmm")+".xls"), response);
    }

}
