package com.cmcu.mcc.business.web;

import com.cmcu.mcc.business.entity.FiveBusinessAdvanceCollectDetail;
import com.common.model.JsonData;
import com.cmcu.common.util.MyPoiUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.business.dto.FiveBusinessAdvanceCollectDto;

import com.cmcu.mcc.business.service.FiveBusinessAdvanceCollectService;

import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
@RequestMapping("/business/AdvanceCollect")
public class FiveBusinessAdvanceCollectController {
    @Autowired
    FiveBusinessAdvanceCollectService fiveOaAdvanceCollectSevice;

    @PostMapping(value = "/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(fiveOaAdvanceCollectSevice.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin){
        return JsonData.success(fiveOaAdvanceCollectSevice.getNewModel(userLogin));
    }
    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        fiveOaAdvanceCollectSevice.remove(id,userLogin);
        return JsonData.success();
    }
    @PostMapping("/downCollect.json")
    public void downCollect(int collectId,String collectMonth ,final HttpServletResponse response) throws IOException {
        FiveBusinessAdvanceCollectDto advanceCollectDto = fiveOaAdvanceCollectSevice.getModelById(collectId);
        String fileName = collectMonth+" "+advanceCollectDto.getDeclareType()+" ?????????.xls";
        HSSFWorkbook hssfWorkbook = fiveOaAdvanceCollectSevice.downCollect(collectId);
        response.reset();
        response.setContentType("application/csv");
        response.setHeader("Content-Disposition", "attachment; filename=" +  URLEncoder.encode(fileName, "utf-8"));
        hssfWorkbook.write(response.getOutputStream());
    }
    @PostMapping("/update.json")
    public JsonData update(@RequestBody FiveBusinessAdvanceCollectDto item){
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"?????????????????????!");
        fiveOaAdvanceCollectSevice.update(item);
        return JsonData.success();
    }
    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin,String uiSref, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = fiveOaAdvanceCollectSevice.listPagedData(params, userLogin, uiSref, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }
    @PostMapping("/downData.json")
    public void downData(int collectId,String userLogin,final HttpServletResponse response) throws IOException {
        List<Map> list = fiveOaAdvanceCollectSevice.downData(collectId,userLogin);
        InputStream inputStream = MyPoiUtil.buildInputStream2(list);
        response.reset();
        byte[] data = IOUtils.toByteArray(inputStream);
        String fileName = URLEncoder.encode("??????????????????"+ DateFormatUtils.format(new Date(),"yyyyMMdd"), "utf-8");
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/vnd.ms-excel");
        OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
        outputStream.write(data);
        outputStream.flush();
        outputStream.close();
    }

    @PostMapping(value = "/getNewModelDetail.json")
    public JsonData getNewModelDetail(int collectId){
        fiveOaAdvanceCollectSevice.getNewModelDetail(collectId);
        return JsonData.success();
    }

    @PostMapping("/listDetail.json")
    public JsonData listDetail(int  collectId){
        return JsonData.success(fiveOaAdvanceCollectSevice.listDetail(collectId));
    }

    @PostMapping("/updateDetails.json")
    public  JsonData updateDetails(@RequestBody FiveBusinessAdvanceCollectDto item){
        fiveOaAdvanceCollectSevice.updateDetails(item);
        return JsonData.success();
    }

    @PostMapping("/updateDetail.json")
    public  JsonData updateDetail(@RequestBody FiveBusinessAdvanceCollectDetail detail){
        fiveOaAdvanceCollectSevice.updateDetail(detail);
        return JsonData.success();
    }

    @PostMapping("/getModelDetailById.json")
    public JsonData getModelDetailById(int id){
        return JsonData.success(fiveOaAdvanceCollectSevice.getModelDetailById(id));
    }
    @PostMapping("/removeDetail.json")
    public JsonData removeDetail(int  id){
        fiveOaAdvanceCollectSevice.removeDetail(id);
        return JsonData.success();
    }

    @PostMapping("/updateExcl.json")
    public JsonData updateExcl(MultipartFile file, int id, String userLogin) throws IOException, ParseException {
        List<Map> data = MyPoiUtil.listTableData(file.getInputStream(), 0, 0, true);
        fiveOaAdvanceCollectSevice.uploadExcl(data,id,userLogin);
        return JsonData.success();
    }
}
