package com.cmcu.mcc.sys.web;

import com.common.model.JsonData;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.sys.dto.SysSoftwareDto;
import com.cmcu.mcc.sys.service.SysSoftwareService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author hnz
 * @date 2019/11/5
 */
@RestController
@RequestMapping("/sys/software")
public class SysSoftwareController {
    @Autowired
    SysSoftwareService sysSoftwareService;

    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = sysSoftwareService.listPagedData(params,userLogin,pageNum, pageSize);
        return JsonData.success(pageInfo);
    }

    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(sysSoftwareService.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin){
        return JsonData.success(sysSoftwareService.getNewModel(userLogin));
    }
    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        sysSoftwareService.remove(id,userLogin);
        return JsonData.success();
    }
    @PostMapping("/update.json")
    public JsonData update(@RequestBody SysSoftwareDto item){
        Assert.state(item.getOperateUserLogin().equals(item.getCreator()),"只能由创建者修改!");
        sysSoftwareService.update(item);
        return JsonData.success();
    }
}
