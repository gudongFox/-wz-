package com.cmcu.mcc.sys.web;

import com.cmcu.common.JsonData;
import com.cmcu.mcc.sys.dto.SysAclModuleDto;
import com.cmcu.mcc.sys.entity.SysAclModule;
import com.cmcu.mcc.sys.service.SysAclModuleService;
import org.apache.http.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/sys/aclModule")
public class SysAclModuleController {


    @Autowired
    private SysAclModuleService sysAclModuleService;


    @RequestMapping("/index")
    public ModelAndView index(){
        ModelAndView modelAndView=new ModelAndView("/sys/aclModule");
        return modelAndView;
    }

    @ResponseBody
    @PostMapping("/insert.json")
    public JsonData insert(@RequestBody SysAclModuleDto item){
        sysAclModuleService.insert(item);
        return JsonData.success();
    }

    @ResponseBody
    @PostMapping("/update.json")
    public JsonData update(@RequestBody SysAclModuleDto item){
        sysAclModuleService.update(item);
        return JsonData.success();
    }

    @ResponseBody
    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id){
        SysAclModuleDto dto= sysAclModuleService.getModelById(id);
        return JsonData.success(dto);
    }

    @ResponseBody
    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(int parentId){
        SysAclModuleDto dto= sysAclModuleService.getNewModel(parentId);
        return JsonData.success(dto);
    }


    @ResponseBody
    @PostMapping("/listSortedAll.json")
    public JsonData listSortedAll(Integer excludeId) {
        List<SysAclModule> list = sysAclModuleService.listSortedAll(excludeId);
        return JsonData.success(list);
    }





}
