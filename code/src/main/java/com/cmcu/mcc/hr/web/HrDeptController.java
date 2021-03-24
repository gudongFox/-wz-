package com.cmcu.mcc.hr.web;

import com.cmcu.common.JsonData;
import com.cmcu.mcc.hr.dto.HrDeptDto;
import com.cmcu.mcc.hr.service.HrDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hr/dept")
public class HrDeptController {


    @Autowired
    HrDeptService hrDeptService;

    @PostMapping("/selectAll.json")
    public JsonData selectAll(){
        return JsonData.success(hrDeptService.selectAll());
    }

    @PostMapping("/selectByDeptIds.json")
    public JsonData selectByDeptIds(String deptIds){
        return JsonData.success(hrDeptService.selectByDeptIds(deptIds));
    }
    @PostMapping("/selectByDeptLeader.json")
    public JsonData selectByDeptLeader(String deptIds){
        return JsonData.success(hrDeptService.selectByDeptLeader(deptIds));
    }
    @PostMapping("/selectAllByUiSref.json")
    public JsonData selectAllByUiSref(String userLogin,String uiSref){
        return JsonData.success(hrDeptService.selectAllByUiSref(userLogin,uiSref));
    }


    @PostMapping("/selectDeptByDeptIds.json")
    public JsonData selectDeptByDeptIds(String deptIds,boolean bigDept){
        return JsonData.success(hrDeptService.selectDeptByDeptIds(deptIds,bigDept));
    }
    @PostMapping("/listDataByDeptIdList.json")
    public JsonData listDataByDeptIdList(String deptIdList){
        return JsonData.success(hrDeptService.listDataByDeptIdList(deptIdList));
    }

    @PostMapping("/listParentCandicate.json")
    public JsonData listParentCandicate(Integer id){
        return JsonData.success(hrDeptService.listParentCandicate(id));
    }

    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(hrDeptService.getModelById(id));
    }

    @ResponseBody
    @PostMapping("/update.json")
    public JsonData update(@RequestBody HrDeptDto item){
        hrDeptService.update(item);
        return JsonData.success();
    }

    @ResponseBody
    @PostMapping("/remove.json")
    public JsonData remove(int id){
        hrDeptService.remove(id);
        return JsonData.success();
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(int parentId){
        return JsonData.success(hrDeptService.getNewModel(parentId));
    }
    @PostMapping("/listMultipleDept.json")
    public JsonData listMultipleDept(){
        return JsonData.success(hrDeptService.listMultipleDept());
    }

    @PostMapping("/getDefaultDept.json")
    public JsonData getDefaultDept(int id){
        return JsonData.success(hrDeptService.getDefaultDept2(id));
    }
}
