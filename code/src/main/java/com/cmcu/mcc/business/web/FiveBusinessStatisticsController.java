package com.cmcu.mcc.business.web;

import com.cmcu.common.util.FileUtil;
import com.cmcu.common.util.MyPoiUtil;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.business.dto.FiveBusinessAdvanceDto;
import com.cmcu.mcc.business.dto.FiveBusinessStatisticsDto;
import com.cmcu.mcc.business.entity.FiveBusinessAdvanceDetail;
import com.cmcu.mcc.business.entity.FiveBusinessConsultingContractStatistics;
import com.cmcu.mcc.business.service.FiveBusinessAdvanceSevice;
import com.cmcu.mcc.business.service.FiveBusinessStatisticsSevice;
import com.common.model.JsonData;
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
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/business/statistics")
public class FiveBusinessStatisticsController {
    @Autowired
    FiveBusinessStatisticsSevice fiveBusinessStatisticsSevice;

    /*@PostMapping(value = "/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(fiveBusinessStatisticsSevice.getModelById(id));
    }
*/
    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin){
        return JsonData.success(fiveBusinessStatisticsSevice.getNewModel(userLogin));
    }
    /*@PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        fiveBusinessStatisticsSevice.remove(id,userLogin);
        return JsonData.success();
    }
    @PostMapping("/update.json")
    public JsonData update(@RequestBody FiveBusinessStatisticsDto item){
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"操作人不能为空!");
        fiveBusinessStatisticsSevice.update(item);
        return JsonData.success();
    }*/
    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin,String uiSref, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = fiveBusinessStatisticsSevice.listPagedData(params, userLogin, uiSref, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }


    /*@PostMapping(value = "/getNewModelDetail.json")
    public JsonData getNewModelDetail(int advanceId){
        return JsonData.success(fiveBusinessStatisticsSevice.getNewModelDetail(advanceId));
    }*/

    @PostMapping("/getModelDetailById.json")
    public JsonData getModelDetailById(int  id){
        return JsonData.success(fiveBusinessStatisticsSevice.getModelDetailById(id));
    }
    @PostMapping("/removeDetail.json")
    public JsonData removeDetail(int  id){
        fiveBusinessStatisticsSevice.removeDetail(id);
        return JsonData.success();
    }
    @PostMapping("/updateDetail.json")
    public JsonData updateDetail(@RequestBody FiveBusinessConsultingContractStatistics item){
        fiveBusinessStatisticsSevice.updateDetail(item);
        return JsonData.success();
    }
    @PostMapping("/listDetail.json")
    public JsonData listDetail(int  advanceId){
        return JsonData.success(fiveBusinessStatisticsSevice.listDetail(advanceId));
    }


    /*@PostMapping("/downTempleXls.json")
    public void downTempleXls(String uiSref,String userLogin,final HttpServletResponse response,int advanceId) {
        Map params = WebUtil.getRequestParameters();
        List<Map> list = fiveBusinessStatisticsSevice.listMapData(params,uiSref,userLogin,advanceId);
        FiveBusinessAdvanceDto dto = fiveBusinessStatisticsSevice.getModelById(advanceId);
        MyPoiUtil.downloadExcel(list, FileUtil.getGoodFileName(dto.getDeclareType()+"明细表"+ DateFormatUtils.format(new Date(),"yyyyMMddHHmm")+".xls"), response);
    }*/
    /*@PostMapping("/updateExcl.json")
    public JsonData updateExcl(MultipartFile multipartFile, int advanceId, String userLogin) throws IOException, ParseException {
        List<Map> data = MyPoiUtil.listTableData(multipartFile.getInputStream(), 0, 0, true);
        fiveBusinessStatisticsSevice.uploadExcl(data,advanceId,userLogin);
        return JsonData.success();
    }*/

}
