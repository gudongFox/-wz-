package com.cmcu.mcc.five.web;

import com.common.model.JsonData;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.five.entity.FiveOaWordSize;
import com.cmcu.mcc.five.service.FiveOaWordSizeService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/sys/wordSize")
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

    @PostMapping("/getModelItemById.json")
    public JsonData getModelItemById(int  id){
        return JsonData.success(fiveOaWordSizeService.getModelItemById(id));
    }


    @PostMapping("/getNewModelItem.json")
    public JsonData getNewModelItem(int  id){
        return JsonData.success(fiveOaWordSizeService.getNewModelItem(id));
    }

    @PostMapping(value = "/removeWordByBusinessKey.json")
    public JsonData removeWordByBusinessKey(String businessKey){
        fiveOaWordSizeService.removeWordByBusinessKey(businessKey);
        return JsonData.success();
    }
    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin,String uiSref, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = fiveOaWordSizeService.listPagedData(params, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }
}
