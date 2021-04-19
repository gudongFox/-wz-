package com.cmcu.mcc.finance.web;

import com.common.model.JsonData;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.finance.dto.FiveFinanceIncomeConfirmDto;
import com.cmcu.mcc.finance.service.FiveFinanceIncomeConfirmService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
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
import java.text.ParseException;
import java.util.Map;

@RestController
@RequestMapping("/five/finance/incomeConfirm")
public class FiveFinanceIncomeConfirmController {
    @Autowired
    FiveFinanceIncomeConfirmService fiveFinanceIncomeConfirmService;

    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id) {
        return JsonData.success(fiveFinanceIncomeConfirmService.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin,String uiSref) {
        return JsonData.success(fiveFinanceIncomeConfirmService.getNewModel(userLogin,uiSref));
    }
    @PostMapping("/getIncomeList.json")
    public JsonData getIncomeList(int incomeConfirmId,String userLogin,String uiSref) throws ParseException {
        return JsonData.success(fiveFinanceIncomeConfirmService.getIncomeList(incomeConfirmId,userLogin,uiSref));
    }

    @PostMapping("/remove.json")
    public JsonData remove(int id, String userLogin) {
        fiveFinanceIncomeConfirmService.remove(id, userLogin);
        return JsonData.success();
    }

    @PostMapping("/update.json")
    public JsonData update(@RequestBody FiveFinanceIncomeConfirmDto item) {
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()), "操作人不能为空!");
        return JsonData.success(  fiveFinanceIncomeConfirmService.update(item));
    }


    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin,String uiSref, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = fiveFinanceIncomeConfirmService.listPagedData(params, userLogin, uiSref, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }

    @PostMapping("/selectAll.json")
    public JsonData selectAll(String userLogin,String uiSref){
        return JsonData.success(fiveFinanceIncomeConfirmService.selectAll(userLogin,uiSref));
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
        return JsonData.success(fiveFinanceIncomeConfirmService.moneyTurnCapital(money));
    }
    @PostMapping("/saveSelectInvoice.json")
    public JsonData saveSelectInvoice(int incomeConfirmId,String invoiceIds){
        return JsonData.success(fiveFinanceIncomeConfirmService.saveSelectInvoice(incomeConfirmId,invoiceIds));
    }
    @PostMapping("/getNotarizeIncome.json")
    public JsonData getNotarizeIncome(int incomeConfirmId,String userLogin){
        return JsonData.success(fiveFinanceIncomeConfirmService.getNotarizeIncome(incomeConfirmId,userLogin));
    }
}


