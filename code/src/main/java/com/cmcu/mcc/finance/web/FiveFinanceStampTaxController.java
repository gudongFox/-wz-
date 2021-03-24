package com.cmcu.mcc.finance.web;

import com.cmcu.common.JsonData;
import com.cmcu.common.util.FileUtil;
import com.cmcu.common.util.MyPoiUtil;
import com.cmcu.common.util.WebUtil;
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
@RequestMapping("/five/finance/stampTax")
public class FiveFinanceStampTaxController {
    @Autowired
    FiveFinanceStampTaxService fiveFinanceStampTaxService;

    @PostMapping("/downData.json")
    public void downData(String userLogin,final HttpServletResponse response) {
        List<Map> list = fiveFinanceStampTaxService.downData(userLogin);
        MyPoiUtil.downloadExcel(list, FileUtil.getGoodFileName("印花税台账"+ DateFormatUtils.format(new Date(),"yyyyMMddHHmm")+".xls"), response);
    }
    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id) {
        return JsonData.success(fiveFinanceStampTaxService.getModelById(id));
    }
    @PostMapping("/getDetailById.json")
    public JsonData getDetailById(int id) {
        return JsonData.success(fiveFinanceStampTaxService.getDetailById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin,String uiSref) {
        return JsonData.success(fiveFinanceStampTaxService.getNewModel(userLogin,uiSref));
    }

    @PostMapping("/remove.json")
    public JsonData remove(int id, String userLogin) {
        fiveFinanceStampTaxService.remove(id, userLogin);
        return JsonData.success();
    }

    @PostMapping("/update.json")
    public JsonData update(@RequestBody FiveFinanceStampTaxDto item) {
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()), "操作人不能为空!");
        fiveFinanceStampTaxService.update(item);
        return JsonData.success();
    }


    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin,String uiSref, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = fiveFinanceStampTaxService.listPagedData(params, userLogin, uiSref, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }

    @PostMapping("/selectAll.json")
    public JsonData selectAll(String userLogin,String uiSref){
        return JsonData.success(fiveFinanceStampTaxService.selectAll(userLogin,uiSref));
    }

    @RequestMapping("/downloadModel.json")
    public void downloadModel(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        String classpath = this.getClass().getResource("/").getPath().replaceFirst("/", "");
        String webappRoot = classpath.replaceAll("WEB-INF/classes/", "");
        String filePath = webappRoot + "assets/doc/导出模板/项目信息备案.xls";
        downloadingFile(filePath, "项目信息备案模板.xls", response);
    }

    private void downloadingFile(String filePath, String fileName, HttpServletResponse response) throws IOException {
        File file = new File(filePath);
        if (file.exists() && file.length() > 0) {
            byte[] data = FileCopyUtils.copyToByteArray(file);
            response.reset();
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "utf-8").replace("+", "%20"));
            response.addHeader("Content-Length", "" + data.length);
            response.setContentType("multipart/form-data;charset=UTF-8");
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            outputStream.write(data);
            outputStream.flush();
            outputStream.close();
        }
    }

    @PostMapping("/moneyTurnCapital.json")
    public JsonData moneyTurnCapital(Double money){
        return JsonData.success(fiveFinanceStampTaxService.moneyTurnCapital(money));
    }

    @PostMapping(value = "/getNewModelDetail.json")
    public JsonData getNewModelDetail(int  id,String userLogin){
        return JsonData.success(fiveFinanceStampTaxService.getNewModelDetail(id,userLogin));
    }

    @PostMapping("/getModelDetailById.json")
    public JsonData getModelDetailById(int  id){
        return JsonData.success(fiveFinanceStampTaxService.getModelDetailById(id));
    }
    @PostMapping("/removeDetail.json")
    public JsonData removeDetail(int  id){
        fiveFinanceStampTaxService.removeDetail(id);
        return JsonData.success();
    }
    @PostMapping("/updateDetail.json")
    public JsonData updateDetail(@RequestBody FiveFinanceStampTaxDetail item,int stampTaxId){
        fiveFinanceStampTaxService.updateDetail(item,stampTaxId);
        return JsonData.success();
    }

    @RequestMapping("/downloadModelExcel.json")
    public void  downloadModelExcel(final HttpServletRequest request, final HttpServletResponse response,String userLogin) throws IOException {
        Map params = WebUtil.getRequestParameters();
        List<Map> list = fiveFinanceStampTaxService.listMapData(params);
        MyPoiUtil.downloadExcel(list, FileUtil.getGoodFileName( "印花税明细-"+ DateFormatUtils.format(new Date(),"yyyyMMddHHmm")+".xls"), response);
    }
    @PostMapping("/changeOpenStamp.json")
    public JsonData changeOpenStamp(int id,String userLogin){
        fiveFinanceStampTaxService.changeOpenStamp(id,userLogin);
        return JsonData.success();
    }
}


