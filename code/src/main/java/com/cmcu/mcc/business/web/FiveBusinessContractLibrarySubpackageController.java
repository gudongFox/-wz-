package com.cmcu.mcc.business.web;

import com.cmcu.common.JsonData;
import com.cmcu.common.util.FileUtil;
import com.cmcu.common.util.JsonMapper;
import com.cmcu.common.util.MyPoiUtil;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.business.dto.FiveBusinessContractLibrarySubpackageDto;
import com.cmcu.mcc.business.service.FiveBusinessContractLibrarySubpackageService;
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
@RequestMapping("business/contractLibrarySubpackage")
public class FiveBusinessContractLibrarySubpackageController {
    @Autowired
    FiveBusinessContractLibrarySubpackageService fiveBusinessContractLibrarySubpackageService;

    @PostMapping(value = "/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(fiveBusinessContractLibrarySubpackageService.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin,String uiSref){
        return JsonData.success(fiveBusinessContractLibrarySubpackageService.getNewModel(userLogin,uiSref));
    }
    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        fiveBusinessContractLibrarySubpackageService.remove(id,userLogin);
        return JsonData.success();
    }
    @PostMapping("/update.json")
    public JsonData update(@RequestBody FiveBusinessContractLibrarySubpackageDto item){
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"操作人不能为空!");
        fiveBusinessContractLibrarySubpackageService.update(item);
        return JsonData.success();
    }

    @PostMapping(value = "/listPagedData.json")
    public JsonData loadPagedData(String uiSref,int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        return JsonData.success(fiveBusinessContractLibrarySubpackageService.listPagedData(uiSref,params,pageNum, pageSize));
    }
    @PostMapping(value = "/listPagedDataSelect.json")
    public JsonData listPagedDataSelect(String uiSref,int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        return JsonData.success(fiveBusinessContractLibrarySubpackageService.listPagedDataSelect(uiSref,params,pageNum, pageSize));
    }



    @PostMapping("/listSubpackageByUserLogin.json")
    public JsonData listRecordByUserLogin(String userLogin,int subpackageId) {
        return JsonData.success(fiveBusinessContractLibrarySubpackageService.listSubpackageByUserLogin(userLogin,subpackageId));
    }

    @PostMapping("/downTempleXls.json")
    public void downTempleXls(String userLogin,String real,String signTime,final HttpServletResponse response) {
        List<Map> list = fiveBusinessContractLibrarySubpackageService.listMapData();
        MyPoiUtil.downloadExcel(list, FileUtil.getGoodFileName("签出合同库导入模板"+ DateFormatUtils.format(new Date(),"yyyyMMddHHmm")+".xls"), response);
    }

    /**
     * 导入数据
     * @param multipartFile
     * @return
     * @throws IOException
     */
    @RequestMapping("/receive.json")
    public JsonData receive(MultipartFile multipartFile, String userLogin) throws IOException {
        String message = fiveBusinessContractLibrarySubpackageService.insertBatch(multipartFile.getInputStream(), userLogin);
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



}
