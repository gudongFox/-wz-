package com.cmcu.mcc.oa.web;

import com.cmcu.common.JsonData;
import com.cmcu.common.dto.CommonFileDto;
import com.cmcu.common.entity.CommonAttach;
import com.cmcu.common.service.CommonAttachService;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.oa.dto.OaNoticeDto;
import com.cmcu.mcc.oa.entity.OaLink;
import com.cmcu.mcc.oa.service.OaLinkService;
import com.cmcu.mcc.oa.service.OaNoticeService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/oa/link")
public class OaLinkController {
    @Autowired
    CommonAttachService commonAttachService;
    @Autowired
    OaLinkService oaLinkService;



    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = oaLinkService.listPagedData(params,pageNum, pageSize);
        return JsonData.success(pageInfo);
    }

    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id,String userLogin){
        return JsonData.success(oaLinkService.getModelById(id,userLogin));
    }



    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        oaLinkService.remove(id,userLogin);
        return JsonData.success();
    }
    @PostMapping("/update.json")
    public JsonData update(@RequestBody OaLink item){
        if(item.getId()==null){
            item.setGmtModified(new Date());
            item.setGmtCreate(new Date());
            oaLinkService.add(item);
        }
        else {
            item.setGmtModified(new Date());
            oaLinkService.update(item);
        }

        return JsonData.success();
    }
    @ResponseBody
    @RequestMapping("/receive.json")
    public JsonData receive(MultipartFile file, String userLogin) throws IOException {
        int id= commonAttachService.insert(file,file.getOriginalFilename(),"",userLogin);
        CommonAttach attach = commonAttachService.getModelById(id);
        return JsonData.success(attach);
    }
    @ResponseBody
    @RequestMapping("/getLogoAttachById.json")
    public JsonData receive(int id) throws IOException {
        CommonAttach attach = commonAttachService.getModelById(id);
        return JsonData.success(attach);
    }
}


