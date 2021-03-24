package com.cmcu.mcc.finance.web;

import com.cmcu.common.JsonData;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.finance.dto.FiveFinanceInvoiceDto;
import com.cmcu.mcc.finance.service.FiveFinanceInvoiceService;
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
import java.util.Map;

@RestController
@RequestMapping("/five/finance/invoice")
public class FiveFinanceInvoiceController {
    @Autowired
    FiveFinanceInvoiceService fiveFinanceInvoiceService;

    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id) {
        return JsonData.success(fiveFinanceInvoiceService.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin,String uiSref) {
        return JsonData.success(fiveFinanceInvoiceService.getNewModel(userLogin,uiSref));
    }

    @PostMapping("/remove.json")
    public JsonData remove(int id, String userLogin) {
        fiveFinanceInvoiceService.remove(id, userLogin);
        return JsonData.success();
    }
    @PostMapping("/removeReplenish.json")
    public JsonData removeReplenish(int id, String userLogin) {
        fiveFinanceInvoiceService.removeReplenish(id, userLogin);
        return JsonData.success();
    }
    @PostMapping("/removeInIncomeConfirm.json")
    public JsonData removeInIncomeConfirm(int id, int incomeConfirmId,String userLogin) {
        fiveFinanceInvoiceService.removeInIncomeConfirm(id,incomeConfirmId, userLogin);
        return JsonData.success();
    }
    @PostMapping("/getInvoicesByIncomeConfirmId.json")
    public JsonData getInvoicesByIncomeConfirmId(int incomeConfirmId) {
        return JsonData.success( fiveFinanceInvoiceService.getInvoicesByIncomeConfirmId(incomeConfirmId));
    }
    @PostMapping("/getNewModelByConfirm.json")
    public JsonData getNewModelByConfirm(int incomeConfirmId, String userLogin, String uiSref){
        return JsonData.success(fiveFinanceInvoiceService.getNewModelByConfirm(incomeConfirmId,userLogin, uiSref));
    }

    @PostMapping("/getInvoicesWithoutConfirm.json")
    public JsonData getInvoicesWithoutConfirm(int incomeConfirmId, String userLogin, String uiSref){
        return JsonData.success(fiveFinanceInvoiceService.getInvoicesWithoutConfirm(incomeConfirmId,userLogin, uiSref));
    }
    @PostMapping("/selectIncomeConfirm.json")
    public JsonData selectIncomeConfirm(String  incomeConfirmIds){
        return JsonData.success(fiveFinanceInvoiceService.selectIncomeConfirm(incomeConfirmIds));
    }
    @PostMapping("/replenishInvoiceByIncome.json")
    public JsonData replenishInvoiceByIncome(int incomeId ,String userLogin){
        return JsonData.success(fiveFinanceInvoiceService.replenishInvoiceByIncome(incomeId,userLogin));
    }


    @PostMapping("/update.json")
    public JsonData update(@RequestBody FiveFinanceInvoiceDto item) {
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()), "操作人不能为空!");
        fiveFinanceInvoiceService.update(item);
        return JsonData.success();
    }


    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin,String uiSref, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = fiveFinanceInvoiceService.listPagedData(params, userLogin, uiSref, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }

    @PostMapping("/selectAll.json")
    public JsonData selectAll(String userLogin,String uiSref){
        return JsonData.success(fiveFinanceInvoiceService.selectAll(userLogin,uiSref));
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
        return JsonData.success(fiveFinanceInvoiceService.moneyTurnCapital(money));
    }

    @PostMapping("/listInvoice.json")
    public JsonData listAssociation(int invoiceId){
        return JsonData.success(fiveFinanceInvoiceService.listInvoice(invoiceId));
    }
    @PostMapping("/listInvoiceByCollection.json")
    public JsonData listInvoiceByCollection(int invoiceId){
        return JsonData.success(fiveFinanceInvoiceService.listInvoiceByCollection(invoiceId));
    }
}


