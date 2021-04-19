package com.cmcu.mcc.business.web;

import com.common.model.JsonData;
import com.cmcu.common.service.CommonAttachService;
import com.cmcu.common.util.FileUtil;
import com.cmcu.common.util.JsonMapper;
import com.cmcu.common.util.MyPoiUtil;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.business.dto.BusinessIncomeDto;
import com.cmcu.mcc.business.service.BusinessIncomeService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/business/income")
public class BusinessIncomeController {
    @Autowired
    BusinessIncomeService businessIncomeService;
    @Autowired
    CommonAttachService commonAttachService;

    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id) {
        return JsonData.success(businessIncomeService.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin,String uiSref) {
        return JsonData.success(businessIncomeService.getNewModel(userLogin,uiSref));
    }

    @PostMapping("/remove.json")
    public JsonData remove(int id, String userLogin) {
        businessIncomeService.remove(id, userLogin);
        return JsonData.success();
    }

    @PostMapping("/update.json")
    public JsonData update(@RequestBody BusinessIncomeDto item) {
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()), "操作人不能为空!");
        businessIncomeService.update(item);
        return JsonData.success();
    }


    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin,String uiSref, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = businessIncomeService.listPagedData(params, userLogin, uiSref, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }

    @PostMapping("/selectAll.json")
    public JsonData selectAll(String userLogin,String uiSref){
        return JsonData.success(businessIncomeService.selectAll(userLogin,uiSref));
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
        return JsonData.success(businessIncomeService.moneyTurnCapital(money));
    }
    @PostMapping("/getNewModelByConfirm.json")
    public JsonData getNewModelByConfirm(int incomeConfirmId, String userLogin, String uiSref){
        return JsonData.success(businessIncomeService.getNewModelByConfirm(incomeConfirmId,0,userLogin));
    }
    @PostMapping("/getNewModelByConfirm2.json")
    public JsonData getNewModelByConfirm2(int incomeConfirmId, int libraryId,String userLogin, String uiSref){
        return JsonData.success(businessIncomeService.getNewModelByConfirm2(incomeConfirmId,libraryId,0,userLogin));
    }
    @PostMapping("/getIncomesByIncomeConfirmId.json")
    public JsonData getIncomesByIncomeConfirmId(int incomeConfirmId){
        return JsonData.success(businessIncomeService.getIncomesByIncomeConfirmId(incomeConfirmId));
    }

    @PostMapping("/downTempleXls.json")
    public void downTempleXls(String userLogin,final HttpServletResponse response) {
        List<Map> list = businessIncomeService.listMapData();
        MyPoiUtil.downloadExcel(list, FileUtil.getGoodFileName("合同收费"+ DateFormatUtils.format(new Date(),"yyyyMMddHHmm")+".xls"), response);
    }

    /**
     * 导入数据
     * @param multipartFile
     * @return
     * @throws IOException
     */
    @RequestMapping("/receive.json")
    public JsonData receive(MultipartFile multipartFile, String enLogin) throws Exception {
        List<Map> list = businessIncomeService.insertBatch(multipartFile.getInputStream(), enLogin);
        InputStream inputStream = MyPoiUtil.buildInputStream(list);

        MultipartFile toMultipartFile = new MockMultipartFile("file",
                "合同收费"+ DateFormatUtils.format(new Date(),"yyyyMMddHHmm")+".xls",
                "text/plain", IOUtils.toByteArray(inputStream));

        int id= commonAttachService.insert(toMultipartFile,
                "合同收费(未匹配数据)"+ DateFormatUtils.format(new Date(),"yyyyMMddHHmm")+".xls",
                "",
                "luodong");

        return JsonData.success(id);
        //MyPoiUtil.downloadExcel(list, FileUtil.getGoodFileName("合同收费"+ DateFormatUtils.format(new Date(),"yyyyMMddHHmm")+".xls"), response);
    }


    @PostMapping("/getNotarizeIncome.json")
    public JsonData getNotarizeIncome(int incomeId,String userLogin){
        return JsonData.success(businessIncomeService.getNotarizeIncome(incomeId,userLogin));
    }


    /**
     * InputStream 转 File
     * @param ins
     * @param file
     */
    public static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}


