package com.cmcu.mcc.oa.web;

import com.common.model.JsonData;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.hr.dto.HrDeptDto;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.entity.HrEmployee;
import com.cmcu.mcc.hr.service.HrDeptService;
import com.cmcu.mcc.hr.service.HrEmployeeService;
import com.cmcu.mcc.oa.dto.OaStampApplyDto;
import com.cmcu.mcc.oa.dto.OaTechnologyDto;
import com.cmcu.mcc.oa.service.OaStampApplyService;
import com.cmcu.mcc.oa.service.OaTechnologyService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/oa/stampApply")
public class OaStampApplyController {
    @Autowired
    OaStampApplyService oaStampApplyService;
    @Autowired
    HrDeptService  deptService;
    @Autowired
    HrEmployeeService hrEmployeeService;

    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin,String uiSref,String types,int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = oaStampApplyService.listPagedData(params,userLogin,uiSref,types,pageNum, pageSize);
        return JsonData.success(pageInfo);
    }
    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(oaStampApplyService.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin){
        return JsonData.success(oaStampApplyService.getNewModel(userLogin));
    }

    @PostMapping("/getNewModelByStampName.json")
    public JsonData getNewModelByStampName(String userLogin,String stampName){
        return JsonData.success(oaStampApplyService.getNewModelByStampName(userLogin,stampName));
    }


    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        oaStampApplyService.remove(id,userLogin);
        return JsonData.success();
    }
    @PostMapping("/update.json")
    public JsonData update(@RequestBody OaStampApplyDto item){
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"操作人不能为空!");
        oaStampApplyService.update(item);
        return JsonData.success();
    }
    @PostMapping("/getUserByDeptName.json")
    public JsonData getUserByDeptName(String deptName){
        List<HrEmployeeDto> result=new ArrayList<>();
        HrDeptDto dept=deptService.getModelByName(deptName);
        if(dept!=null)
        {
            List<HrEmployeeDto> tArray=hrEmployeeService.listEmployeeByDeptId(dept.getId(),true);
            if(tArray!=null){
                result.addAll(tArray);
            }
        }
        return JsonData.success(result);
    }
    @PostMapping("/getPrintData.json")
    public JsonData getPrintData(int id){
        return JsonData.success(oaStampApplyService.getPrintData(id));
    }


}
