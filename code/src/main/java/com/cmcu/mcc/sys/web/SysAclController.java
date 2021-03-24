package com.cmcu.mcc.sys.web;

import com.cmcu.common.JsonData;
import com.cmcu.mcc.sys.dto.SysAclDto;
import com.cmcu.mcc.sys.service.SysAclModuleService;
import com.cmcu.mcc.sys.service.SysAclService;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Roy
 * Time: 2019/2/25 13:57
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/sys/acl")
public class SysAclController {

    @Autowired
    SysAclService sysAclService;

    @Autowired
    SysAclModuleService sysAclModuleService;



    @ResponseBody
    @PostMapping("/insert.json")
    public JsonData insert(@RequestBody SysAclDto item){
        sysAclService.insert(item);
        return JsonData.success();
    }

    @ResponseBody
    @PostMapping("/update.json")
    public JsonData update(@RequestBody SysAclDto item){
        sysAclService.update(item);
        return JsonData.success();
    }


    @ResponseBody
    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id){
        SysAclDto item= sysAclService.getModelById(id);
        return JsonData.success(item);
    }

    @ResponseBody
    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(int aclModuleId){
        SysAclDto item= sysAclService.getNewModel(aclModuleId);
        return JsonData.success(item);
    }

    @ResponseBody
    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(Integer rootId,String q,int pageNum, int pageSize) {
        Map<String, Object> params = Maps.newHashMap();
        if(rootId!=null){
            List<Integer> aclModuleIds=sysAclModuleService.getTreeIds(rootId);
            params.put("aclModuleIds",aclModuleIds);
        }
        if(StringUtils.isNotEmpty(q)){
            params.put("q",q);
        }
        PageInfo<SysAclDto> pageInfo = sysAclService.listPagedData(params, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }

    @ResponseBody
    @PostMapping("/listAclByModules.json")
    public JsonData listAclByModules(int moduleId,String q) {
        List<Integer> aclModuleIds = sysAclModuleService.getTreeIds(moduleId);
        List<SysAclDto> list = sysAclService.listAclByModules(aclModuleIds, q);
        return JsonData.success(list);
    }




}
