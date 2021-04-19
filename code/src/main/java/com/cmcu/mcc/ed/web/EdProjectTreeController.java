package com.cmcu.mcc.ed.web;
import com.common.model.JsonData;
import com.cmcu.mcc.ed.service.EdProjectTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ed/projectTree")
public class EdProjectTreeController {


    @Autowired
    EdProjectTreeService edProjectTreeService;


    @PostMapping("/listProjectTree.json")
    public JsonData listProjectTree(int projectId,String userLogin){
        return JsonData.success(edProjectTreeService.listProjectTree(projectId,userLogin));
    }


    @PostMapping("/listProjectJsTree.json")
    public JsonData listProjectJsTree(int projectId,Integer stepId,Integer nodeId,String enLogin){
        return JsonData.success(edProjectTreeService.listProjectJsTree(projectId,stepId,nodeId,enLogin));
    }


    @PostMapping("/genProjectTree.json")
    public JsonData genProjectTree(int projectId){
        edProjectTreeService.genProjectTree(projectId);
        return JsonData.success();
    }


    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(edProjectTreeService.getModelById(id));
    }


    @PostMapping("/rememberNodeState.json")
    public JsonData rememberNodeState(int id,boolean value,String stateType,String userLogin){

        edProjectTreeService.rememberNodeState(id,value,stateType,userLogin);
        return JsonData.success();
    }



}
