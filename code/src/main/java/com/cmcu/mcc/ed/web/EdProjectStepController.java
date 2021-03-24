package com.cmcu.mcc.ed.web;

import com.cmcu.common.JsonData;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.ed.dto.EdProjectStepDto;
import com.cmcu.mcc.ed.entity.EdProjectStep;
import com.cmcu.mcc.ed.service.EdProjectStepService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ed/projectStep")
public class EdProjectStepController {


    @Autowired
    EdProjectStepService edProjectStepService;

    @PostMapping("/update.json")
    public JsonData update(@RequestBody EdProjectStepDto item){
        edProjectStepService.update(item);
        return JsonData.success();
    }

    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(edProjectStepService.getModelById(id));
    }

    @PostMapping("/listDataByStageNodeId.json")
    public JsonData listDataByStageNodeId(int stageNodeId) {
        List<EdProjectStep> list = edProjectStepService.listDataByStageNodeId(stageNodeId);
        return JsonData.success(list);
    }

    @PostMapping("/listCpPagedData.json")
    public JsonData listCpPagedData(int pageNum,int pageSize) {
        Map params = WebUtil.getRequestParameters();
        return JsonData.success(edProjectStepService.listCpPagedData(params, pageNum, pageSize));
    }

    /**
     * 获取用户参与的协同项目
     * CAD客户端
     * @param userLogin
     * @return
     */
    @PostMapping("/listCpData.json")
    public JsonData listCpData(String userLogin){
        return JsonData.success(edProjectStepService.listCpData(userLogin));
    }


    @PostMapping("/getNewModel.json")
    public JsonData getModelById(int stageNodeId,String userLogin){
        return JsonData.success(edProjectStepService.getNewModel(stageNodeId,userLogin));
    }

    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        edProjectStepService.remove(id,userLogin);
        return JsonData.success();
    }

    @PostMapping("/checkIsChargeUser.json")
    public JsonData checkIsChargeUser(int id,int stepId,String userLogin){
        return JsonData.success(edProjectStepService.checkIsChargeUser(id,stepId,userLogin));
    }

    @PostMapping("/listPagedAttendStep.json")
    public JsonData listPagedAttendStep(String userLogin,Boolean cp,int pageNum,int pageSize) {
        Map params = WebUtil.getRequestParameters();
        return JsonData.success(edProjectStepService.listPagedAttendStep(params,userLogin,cp,pageNum, pageSize));
    }

    @PostMapping("/listPagedAllStep.json")
    public JsonData listPagedAllStep(String userLogin,int pageNum,int pageSize) {
        Map params = WebUtil.getRequestParameters();
        return JsonData.success(edProjectStepService.listPagedAllStep(params,userLogin,pageNum, pageSize));
    }
    @PostMapping("/toggleCadHide.json")
    public JsonData toggleCadHide(int stepId,String userLogin){
        edProjectStepService.toggleCadHide(stepId,userLogin);
        return JsonData.success();
    }

    @PostMapping("/listAllStep.json")
    public JsonData listAllStep(){
        return JsonData.success( edProjectStepService.listAllStep());
    }

    @PostMapping("/listAllStepByDeptId.json")
    public JsonData listAllStepByDeptId(int deptId){
        return JsonData.success( edProjectStepService.listAllStepByDeptId(deptId));
    }
}
