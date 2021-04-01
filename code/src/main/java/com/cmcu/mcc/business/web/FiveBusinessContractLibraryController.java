package com.cmcu.mcc.business.web;

import com.cmcu.common.JsonData;
import com.cmcu.common.util.FileUtil;
import com.cmcu.common.util.JsonMapper;
import com.cmcu.common.util.MyPoiUtil;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.business.dto.FiveBusinessContractLibraryDto;
import com.cmcu.mcc.business.service.FiveBusinessContractLibraryService;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("business/contractLibrary")
public class FiveBusinessContractLibraryController {
    @Autowired
    FiveBusinessContractLibraryService fiveBusinessContractLibraryService;

    @PostMapping(value = "/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(fiveBusinessContractLibraryService.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin){
        return JsonData.success(fiveBusinessContractLibraryService.getNewModel(userLogin));
    }
    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        fiveBusinessContractLibraryService.remove(id,userLogin);
        return JsonData.success();
    }
    @PostMapping("/update.json")
    public JsonData update(@RequestBody FiveBusinessContractLibraryDto item){
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"操作人不能为空!");
        fiveBusinessContractLibraryService.update(item);
        return JsonData.success();
    }
    @PostMapping("/selectAllNotHaveStampTax.json")
    public JsonData selectAllNotHaveStampTax(int contractId, String userLogin,String uiSref){
        return JsonData.success(fiveBusinessContractLibraryService.selectAllNotHaveStampTax(contractId,userLogin,uiSref));
    }
    @PostMapping("/selectAll.json")
    public JsonData selectAll(String userLogin,String uiSref){
        return JsonData.success(fiveBusinessContractLibraryService.selectAll(userLogin,uiSref));
    }
    @PostMapping("/selectSubpackage.json")
    public JsonData selectSubpackage(String userLogin,String uiSref,int libraryId){
        return JsonData.success(fiveBusinessContractLibraryService.selectSubpackage(userLogin,uiSref,libraryId));
    }
    @PostMapping("/selectMainReviewByIds.json")
    public JsonData selectMainReviewByIds(String userLogin,String uiSref,int libraryId){
        return JsonData.success(fiveBusinessContractLibraryService.selectMainReviewByIds(userLogin,uiSref,libraryId));
    }
    @PostMapping("/selectIncome.json")
    public JsonData selectIncome(String userLogin,String uiSref,int libraryId){
        return JsonData.success(fiveBusinessContractLibraryService.selectIncome(userLogin,uiSref,libraryId));
    }
    @PostMapping("/selectInvoice.json")
    public JsonData selectInvoice(String userLogin,String uiSref,int libraryId){
        return JsonData.success(fiveBusinessContractLibraryService.selectInvoice(userLogin,uiSref,libraryId));
    }
    @PostMapping("/getIncomeMoney.json")
    public JsonData getIncomeMoney(int incomeId){
        return JsonData.success(fiveBusinessContractLibraryService.getIncomeMoney(incomeId));
    }
    @PostMapping(value = "/listPagedData.json")
    public JsonData loadPagedData(String uiSref,int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        return JsonData.success(fiveBusinessContractLibraryService.listPagedData(uiSref,params,pageNum, pageSize));
    }
    @PostMapping(value = "/listPagedDataCommon.json")
    public JsonData loadPagedDataCommon(String uiSref,int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        return JsonData.success(fiveBusinessContractLibraryService.listPagedDataCommon(uiSref,params,pageNum, pageSize));
    }

    @PostMapping("/selectNotHaveContract.json")
    public JsonData selectNotHaveContract(int  contractLibraryId,String userLogin){
        return JsonData.success(fiveBusinessContractLibraryService.selectNotHaveContract(contractLibraryId,userLogin));
    }
    @PostMapping("/selectMainContract.json")
    public JsonData selectMainContract(String  contractType){
        return JsonData.success(fiveBusinessContractLibraryService.selectMainContract(contractType));
    }
    @PostMapping("/selectAllByIncome.json")
    public JsonData selectAllByIncome(int  contractLibraryId){
        return JsonData.success(fiveBusinessContractLibraryService.selectAllByIncome(contractLibraryId));
    }

    @PostMapping("/downTempleXls.json")
    public void downTempleXls(String userLogin,String real,String signTime,final HttpServletResponse response) {
        List<Map> list = fiveBusinessContractLibraryService.listMapData();
        MyPoiUtil.downloadExcel(list, FileUtil.getGoodFileName("签入合同库导入模板"+ DateFormatUtils.format(new Date(),"yyyyMMddHHmm")+".xls"), response);
    }
    @PostMapping("/statisticalData.json")
    public void statisticalData(String userLogin,String real,String signTime,final HttpServletResponse response) {
        List<Map> list = fiveBusinessContractLibraryService.statisticalData();
        MyPoiUtil.downloadExcel(list, FileUtil.getGoodFileName("签入合同库统计数据导出"+ DateFormatUtils.format(new Date(),"yyyyMMddHHmm")+".xls"), response);
    }



    /**
     * 导入数据
     * @param multipartFile
     * @return
     * @throws IOException
     */
    @RequestMapping("/receive.json")
    public JsonData receive(MultipartFile multipartFile, String userLogin) throws IOException {
        String message = fiveBusinessContractLibraryService.insertBatch(multipartFile.getInputStream(), userLogin);
        return JsonData.success(message);
    }

    /**
     * 返回字符串格式兼容IE9
     * @param multipartFile

     * @return
     * @throws IOException
     */
    @RequestMapping("/receive.do")
    public String receives(MultipartFile multipartFile,String userLogin) throws IOException {
        JsonData result=receive(multipartFile,userLogin);
        return JsonMapper.obj2String(result);
    }

    @PostMapping("/changeOpen.json")
    public JsonData changeOpen(int id,String userLogin){
        fiveBusinessContractLibraryService.changeOpen(id,userLogin);
        return JsonData.success();
    }


}
