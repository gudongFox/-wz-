package com.cmcu.mcc.five.web;

import com.cmcu.common.JsonData;
import com.cmcu.common.util.FileUtil;
import com.cmcu.common.util.JsonMapper;
import com.cmcu.common.util.MyPoiUtil;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.five.dto.FiveAssetComputerDto;
import com.cmcu.mcc.five.service.FiveAssetComputerService;
import com.github.pagehelper.PageInfo;
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
@RequestMapping("/five/asset/assetComputer")
public class FiveAssetComputerController {
    @Autowired
    FiveAssetComputerService fiveAssetComputerService;


    @PostMapping("/listDate.json")
    public JsonData listDate(String userLogin){
        return JsonData.success(fiveAssetComputerService.listDate(userLogin));
    }

    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        fiveAssetComputerService.remove(id,userLogin);
        return JsonData.success();
    }

    @PostMapping("/update.json")
    public JsonData update(@RequestBody FiveAssetComputerDto item){
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"操作人不能为空!");
        fiveAssetComputerService.update(item);
        return JsonData.success();
    }

    @PostMapping(value = "/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(fiveAssetComputerService.getModelById(id));
    }

    @PostMapping(value = "/getModelByComputerNo.json")
    public JsonData getModelByComputerNo(String computerNo){
        return JsonData.success(fiveAssetComputerService.getModelByComputerNo(computerNo));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin){
        return JsonData.success(fiveAssetComputerService.getNewModel(userLogin));
    }

    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin,String uiSref, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = fiveAssetComputerService.listPagedData(params, userLogin, uiSref, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }


    @PostMapping("/downTempleXls.json")
    public void downTempleXls(String startTime,String uiSref,String endTime,String userLogin,final HttpServletResponse response) {
        /*Map params = WebUtil.getRequestParameters();*/
        List<Map> list = fiveAssetComputerService.listMapData1();
        MyPoiUtil.downloadExcel(list, FileUtil.getGoodFileName("公司非密计算机及信息化设备台帐导入模板"+ DateFormatUtils.format(new Date(),"yyyyMMddHHmm")+".xls"), response);
    }

    @PostMapping("/downExcel.json")
    public void downExcel(String startTime,String uiSref,String endTime,String userLogin,final HttpServletResponse response) {
        Map params = WebUtil.getRequestParameters();
        List<Map> list = fiveAssetComputerService.listMapData(params,uiSref,startTime,endTime,userLogin);
        MyPoiUtil.downloadExcel(list, FileUtil.getGoodFileName("公司非密计算机及信息化设备台帐"+ DateFormatUtils.format(new Date(),"yyyyMMddHHmm")+".xls"), response);
    }

    /**
     * 导入数据
     * @param multipartFile
     * @return
     * @throws IOException
     */
    @RequestMapping("/receive.json")
    public JsonData receive(MultipartFile multipartFile, String userLogin) throws IOException {
        String message = fiveAssetComputerService.insertBatch(multipartFile.getInputStream(), userLogin);
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
