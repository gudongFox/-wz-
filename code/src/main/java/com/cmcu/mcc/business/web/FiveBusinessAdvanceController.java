package com.cmcu.mcc.business.web;

import com.common.model.JsonData;
import com.cmcu.common.util.FileUtil;
import com.cmcu.common.util.MyPoiUtil;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.business.dto.FiveBusinessAdvanceDto;
import com.cmcu.mcc.business.entity.FiveBusinessAdvanceDetail;
import com.cmcu.mcc.business.service.FiveBusinessAdvanceSevice;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/business/advance")
public class FiveBusinessAdvanceController {
    @Autowired
    FiveBusinessAdvanceSevice fiveOaAdvanceSevice;

    @PostMapping(value = "/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(fiveOaAdvanceSevice.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin){
        return JsonData.success(fiveOaAdvanceSevice.getNewModel(userLogin));
    }
    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        fiveOaAdvanceSevice.remove(id,userLogin);
        return JsonData.success();
    }
    @PostMapping("/update.json")
    public JsonData update(@RequestBody FiveBusinessAdvanceDto item){
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"操作人不能为空!");
        fiveOaAdvanceSevice.update(item);
        return JsonData.success();
    }
    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin,String uiSref, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = fiveOaAdvanceSevice.listPagedData(params, userLogin, uiSref, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }


    @PostMapping(value = "/getNewModelDetail.json")
    public JsonData getNewModelDetail(int advanceId){
        return JsonData.success(fiveOaAdvanceSevice.getNewModelDetail(advanceId));
    }

    @PostMapping("/getModelDetailById.json")
    public JsonData getModelDetailById(int  id){
        return JsonData.success(fiveOaAdvanceSevice.getModelDetailById(id));
    }
    @PostMapping("/removeDetail.json")
    public JsonData removeDetail(int  id){
        fiveOaAdvanceSevice.removeDetail(id);
        return JsonData.success();
    }
    @PostMapping("/updateDetail.json")
    public JsonData updateDetail(@RequestBody FiveBusinessAdvanceDetail item){
        fiveOaAdvanceSevice.updateDetail(item);
        return JsonData.success();
    }
    @PostMapping("/listDetail.json")
    public JsonData listDetail(int  advanceId){
        return JsonData.success(fiveOaAdvanceSevice.listDetail(advanceId));
    }


    @PostMapping("/downTempleXls.json")
    public void downTempleXls(String uiSref,String userLogin,final HttpServletResponse response,int advanceId) {
        Map params = WebUtil.getRequestParameters();
        List<Map> list = fiveOaAdvanceSevice.listMapData(params,uiSref,userLogin,advanceId);
        FiveBusinessAdvanceDto dto = fiveOaAdvanceSevice.getModelById(advanceId);
        MyPoiUtil.downloadExcel(list, FileUtil.getGoodFileName(dto.getDeclareType()+"明细表"+ DateFormatUtils.format(new Date(),"yyyyMMddHHmm")+".xls"), response);
    }
    @PostMapping("/updateExcl.json")
    public JsonData updateExcl(MultipartFile multipartFile, int advanceId, String userLogin) throws IOException, ParseException {
        List<Map> data = MyPoiUtil.listTableData(multipartFile.getInputStream(), 0, 0, true);
        fiveOaAdvanceSevice.uploadExcl(data,advanceId,userLogin);
        return JsonData.success();
    }

}
