package com.cmcu.mcc.five.web;

import com.cmcu.common.JsonData;
import com.cmcu.mcc.five.entity.FiveOaWordSize;
import com.cmcu.mcc.five.service.FiveOaWordSizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/five/oa/wordSize")
public class FiveOaWordSizeController {
    @Autowired
    FiveOaWordSizeService fiveOaWordSizeService;

    @PostMapping(value = "/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(fiveOaWordSizeService.getModelById(id));
    }
    @PostMapping(value = "/getCanUseWord.json")
    public JsonData getCanUseWord(String keyNames,String year,String type){
        return JsonData.success(fiveOaWordSizeService.getCanUseWord(keyNames,year,type));
    }


    @PostMapping(value = "/updateWordSize.json")
    public JsonData updateWordSize(String selectWord ,String selectYear,int selectMark,String businessKey,String userLogin){
        fiveOaWordSizeService.updateWordSize(selectWord,selectYear,selectMark,businessKey,userLogin);
        return JsonData.success();
    }

    @PostMapping(value = "/getMarkByChange.json")
    public JsonData getMarkByChange(String word,String year){
        return JsonData.success(fiveOaWordSizeService.getMarkByChange(word,year));
    }

    @PostMapping(value = "/listUserWord.json")
    public JsonData listUserWord(String word,String year,String key){
        return JsonData.success(fiveOaWordSizeService.listUserWord(word,year,key));
    }
    @PostMapping(value = "/removeWordByBusinessKey.json")
    public JsonData removeWordByBusinessKey(String businessKey){
        fiveOaWordSizeService.removeWordByBusinessKey(businessKey);
        return JsonData.success();
    }
}
