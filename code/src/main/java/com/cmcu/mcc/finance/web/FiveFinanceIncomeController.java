package com.cmcu.mcc.finance.web;

import com.cmcu.common.JsonData;
import com.cmcu.common.util.FileUtil;
import com.cmcu.common.util.JsonMapper;
import com.cmcu.common.util.MyPoiUtil;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.finance.dto.FiveFinanceIncomeDto;
import com.cmcu.mcc.finance.service.FiveFinanceIncomeService;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/five/finance/income")
public class FiveFinanceIncomeController {
    @Autowired
    FiveFinanceIncomeService fiveFinanceIncomeService;

    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id) throws ParseException {
        return JsonData.success(fiveFinanceIncomeService.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin,String uiSref) {
        return JsonData.success(fiveFinanceIncomeService.getNewModel(userLogin,uiSref));
    }

    @PostMapping("/remove.json")
    public JsonData remove(int id, String userLogin) throws ParseException {
        fiveFinanceIncomeService.remove(id, userLogin);
        return JsonData.success();
    }

    @PostMapping("/update.json")
    public JsonData update(@RequestBody FiveFinanceIncomeDto item) {
        fiveFinanceIncomeService.update(item);
        return JsonData.success();
    }
    
    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin,String uiSref, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = fiveFinanceIncomeService.listPagedData(params, userLogin, uiSref, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }

    @PostMapping("/listPagedDataConfirmed.json")
    public JsonData listPagedDataConfirmed(String userLogin,String uiSref, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = fiveFinanceIncomeService.listPagedDataConfirmed(params, userLogin, uiSref, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }

    @PostMapping("/selectAll.json")
    public JsonData selectAll(String userLogin,String uiSref){
        return JsonData.success(fiveFinanceIncomeService.selectAll(userLogin,uiSref));
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
    public JsonData moneyTurnCapital(int incomeId,String money) throws ParseException {
        return JsonData.success(fiveFinanceIncomeService.moneyTurnCapital(incomeId,money));
    }
    @PostMapping("/getNewIncomeConfirm.json")
    public JsonData getNewIncomeConfirm(int incomeId,String userLogin){
        return JsonData.success(fiveFinanceIncomeService.getNewIncomeConfirm(incomeId,userLogin));
    }
    @PostMapping("/getNotarizeIncome.json")
    public JsonData getNotarizeIncome(int incomeId,String userLogin){
        return JsonData.success(fiveFinanceIncomeService.getNotarizeIncome(incomeId,userLogin));
    }

    @PostMapping("/saveSelectNodes.json")
    public JsonData saveSelectNodes(int incomeId,String nodeIds) throws ParseException {
        return JsonData.success(fiveFinanceIncomeService.saveSelectNodes(incomeId,nodeIds));
    }

    /**
     * 上传文件
     *
     * @param multipartFile
     * @return
     * @throws IOException
     */
    @RequestMapping("/receive.json")
    public JsonData receive(MultipartFile multipartFile, String userLogin) throws IOException {
        String fileName = multipartFile.getOriginalFilename();
        String msg="";
        if (!fileName.endsWith(".xls")) {
            Assert.state(false, "请将文件格式转换为 .xls");
        } else {
            msg=fiveFinanceIncomeService.insertByFile(multipartFile.getInputStream(),userLogin);
        }
        return JsonData.success("",msg);
    }

    /**
     * 返回字符串格式兼容IE9
     *
     * @param multipartFile
     * @return
     * @throws IOException
     */
    @RequestMapping("/receive.do")
    public String receives(MultipartFile multipartFile, String userLogin) throws IOException {
        JsonData result = receive(multipartFile, userLogin);
        return JsonMapper.obj2String(result);
    }

    @PostMapping("/downTempleXls.json")
    public void downTempleXls(String userLogin,String real,String signTime,final HttpServletResponse response) {
        List<Map> list = fiveFinanceIncomeService.listMapData();
        MyPoiUtil.downloadExcel(list, FileUtil.getGoodFileName("收入管理导入模板"+ DateFormatUtils.format(new Date(),"yyyyMMddHHmm")+".xls"), response);
    }

    @PostMapping("/downIncome.json")
    public void downIncome(String userLogin,String real,String signTime,final HttpServletResponse response) {
        List<Map> list = fiveFinanceIncomeService.downIncome();
        MyPoiUtil.downloadExcel(list, FileUtil.getGoodFileName("收入信息导出"+ DateFormatUtils.format(new Date(),"yyyyMMddHHmm")+".xls"), response);
    }




}


