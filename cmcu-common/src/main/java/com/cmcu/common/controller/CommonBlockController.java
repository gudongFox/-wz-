package com.cmcu.common.controller;

import com.common.model.JsonData;
import com.cmcu.common.service.CommonBlockService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/common/block")
public class CommonBlockController {

    @Resource
    CommonBlockService commonBlockService;

    @PostMapping("/selectAll.json")
    public JsonData selectAll(int categoryId) {
        return JsonData.success(commonBlockService.selectAll(categoryId));
    }

    @PostMapping("/insert.json")
    public JsonData insert(int categoryId, String fileName, int attachId, int thumbId, int minVersion, int maxVersion, String insertLayer, String enLogin) {
        int blockId = commonBlockService.insert(categoryId, fileName, attachId, thumbId, minVersion, maxVersion, insertLayer, enLogin);
        return JsonData.success(blockId);
    }

    @PostMapping("/insertAttrList.json")
    public JsonData insertAttrList(int blockId, String attrData) {
        commonBlockService.insertAttrList(blockId, attrData);
        return JsonData.success();
    }
}
