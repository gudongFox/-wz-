package com.cmcu.mcc.ed.web;


import com.cmcu.common.JsonData;
import com.cmcu.mcc.ed.service.EdSignPaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/ed/signPaper")
public class EdSignPaperController {


    @Resource
    EdSignPaperService edSignPaperService;

    @RequestMapping("/getDwgInfo.json")
    public JsonData getDwgInfo(int fileId,String enLogin){
        return JsonData.success(edSignPaperService.getDwgInformation(fileId,enLogin));
    }


}
