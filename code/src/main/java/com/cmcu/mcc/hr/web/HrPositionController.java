package com.cmcu.mcc.hr.web;

import com.cmcu.common.JsonData;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.hr.dto.HrEducationDto;
import com.cmcu.mcc.hr.dto.HrPositionDto;
import com.cmcu.mcc.hr.entity.HrPosition;
import com.cmcu.mcc.hr.service.HrPositionService;
import com.cmcu.mcc.sys.entity.SysRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/hr/position")
public class HrPositionController {


    @Autowired
    HrPositionService hrPositionService;

    @PostMapping("/update.json")
    public JsonData update(@RequestBody HrPositionDto item){
        hrPositionService.update(item);
        return JsonData.success();
    }

    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(hrPositionService.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel() {
        return JsonData.success(hrPositionService.getNewModel());
    }

    @PostMapping("/remove.json")
    public JsonData remove(int id){
        hrPositionService.remove(id);
        return JsonData.success();
    }

    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        return JsonData.success(hrPositionService.loadPagedData(params, pageNum, pageSize));
    }

    @PostMapping("/selectAll.json")

    public JsonData selectAll() {
        return JsonData.success(hrPositionService.selectAll());
    }
    @ResponseBody
    @PostMapping("/listSortedAll.json")
    public JsonData listSortedAll() {
        List<HrPosition> list = hrPositionService.listSortedAll();
        return JsonData.success(list);
    }

    @ResponseBody
    @PostMapping("/getUserTree.json")
    public JsonData getUserTree(int positionId) {
        List<Map> treeData=hrPositionService.getUserTree(positionId);
        return JsonData.success(treeData);
    }

    @ResponseBody
    @PostMapping("/savePositionUserList.json")
    public JsonData savePositionUserList(int positionId,String userList) {
        hrPositionService.savePositionUserList(positionId,userList);
        return JsonData.success();
    }

}
