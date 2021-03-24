package com.cmcu.mcc.hr.web;

import com.cmcu.common.JsonData;
import com.cmcu.mcc.hr.service.HrTrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/hr/train")
public class HrTrainController {


    @Autowired
    HrTrainService hrTrainService;

    @PostMapping("/listHrTrainMember.json")
    public JsonData listHrTrainMember(String trainKey){
        return JsonData.success(hrTrainService.listHrTrainMember(trainKey));
    }

    @PostMapping("/removeHrMember.json")
    public JsonData removeHrMember(int id){
        hrTrainService.removeHrMember(id);
        return JsonData.success();
    }

    @PostMapping("/saveHrMember.json")
    public JsonData saveHrMember(@RequestBody Map data) {
        hrTrainService.saveHrMember(data);
        return JsonData.success();
    }

    @PostMapping("/save.json")
    public JsonData save(@RequestBody Map data) {
        int id = Integer.parseInt(data.get("id").toString());
        String enLogin = data.get("enLogin").toString();
        data.remove("id");
        data.remove("enLogin");
        hrTrainService.save(id, data, enLogin);
        return JsonData.success();
    }


    @PostMapping("/newData.json")
    public JsonData newData(String trainKey,String owners,String enLogin) {
        hrTrainService.newData(trainKey,owners,enLogin);
        return JsonData.success();
    }

}
