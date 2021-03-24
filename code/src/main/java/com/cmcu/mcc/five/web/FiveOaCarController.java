package com.cmcu.mcc.five.web;

import com.cmcu.common.JsonData;
import com.cmcu.common.util.FileUtil;
import com.cmcu.common.util.MyPoiUtil;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.five.dto.FiveOaCarDto;
import com.cmcu.mcc.five.entity.FiveOaCar;
import com.cmcu.mcc.five.service.FiveOaCarService;
import com.cmcu.mcc.oa.dto.OaCarDto;
import com.cmcu.mcc.oa.entity.OaCar;
import com.cmcu.mcc.oa.service.OaCarService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/five/oa/car")
public class FiveOaCarController {

    @Autowired
    FiveOaCarService fiveOaCarService;

    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin, int pageNum,int pageSize) throws ParseException {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = fiveOaCarService.listPagedData(params,userLogin,pageNum,pageSize);
        return JsonData.success(pageInfo);
    }

    @PostMapping("/listAllCar.json")
    public JsonData listAllCar(int applyId) throws ParseException {
        List<FiveOaCar> list = fiveOaCarService.listAllCar(applyId);
        return JsonData.success(list);
    }

    @PostMapping("/listAllCarByMaintain.json")
    public JsonData listAllCarByMaintain(int id) throws ParseException {
        List<FiveOaCar> list = fiveOaCarService.listAllCarByMaintain(id);
        return JsonData.success(list);
    }

    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id)
    {
        return JsonData.success(fiveOaCarService.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin)
    {
        return JsonData.success(fiveOaCarService.getNewModel(userLogin));
    }

    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin)
    {
        fiveOaCarService.remove(id,userLogin);
        return JsonData.success();
    }

    @PostMapping("/update.json")
    public JsonData update(@RequestBody FiveOaCarDto item)
    {
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"操作人不能为空!");
        fiveOaCarService.update(item);
        return JsonData.success();
    }


    @RequestMapping("/downloadModelExcel.json")
    public void  downloadModelExcel(final HttpServletRequest request, final HttpServletResponse response, String userLogin) throws IOException {
        Map params = WebUtil.getRequestParameters();
        List<Map> list = fiveOaCarService.listMapData(params);
        MyPoiUtil.downloadExcel(list, FileUtil.getGoodFileName( params.get("selectType")+"-"+ DateFormatUtils.format(new Date(),"yyyyMMddHHmm")+".xls"), response);
    }


}
