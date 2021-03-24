package com.cmcu.mcc.oa.web;

import com.cmcu.common.JsonData;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.oa.dto.OaCarDto;
import com.cmcu.mcc.oa.entity.OaCar;
import com.cmcu.mcc.oa.service.OaCarService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/oa/car")
public class OaCarController {

    @Autowired
    OaCarService oaCarService;

    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin, int pageNum,int pageSize) throws ParseException {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = oaCarService.listPagedData(params,userLogin,pageNum,pageSize);
        return JsonData.success(pageInfo);
    }

    @PostMapping("/listAllCar.json")
    public JsonData listAllCar() throws ParseException {
        List<OaCar> list = oaCarService.listAllCar();
        return JsonData.success(list);
    }

    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id)
    {
        return JsonData.success(oaCarService.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin)
    {
        return JsonData.success(oaCarService.getNewModel(userLogin));
    }

    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin)
    {
        oaCarService.remove(id,userLogin);
        return JsonData.success();
    }

    @PostMapping("/update.json")
    public JsonData update(@RequestBody OaCarDto item)
    {
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"操作人不能为空!");
        oaCarService.update(item);
        return JsonData.success();
    }

}
