package com.cmcu.mcc.oa.web;

import com.common.model.JsonData;
import com.cmcu.common.util.JsonMapper;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.oa.dto.OaCarDto;
import com.cmcu.mcc.oa.dto.OaHardwareDto;
import com.cmcu.mcc.oa.entity.OaCar;
import com.cmcu.mcc.oa.entity.OaHardware;
import com.cmcu.mcc.oa.service.OaCarService;
import com.cmcu.mcc.oa.service.OaHardwareService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/oa/hardware")
public class OaHardwareController {

    @Autowired
    OaHardwareService oaHardwareService;

    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin, int pageNum,int pageSize) throws ParseException {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = oaHardwareService.listPagedData(params,userLogin,pageNum,pageSize);
        return JsonData.success(pageInfo);
    }


    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id)
    {
        return JsonData.success(oaHardwareService.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin)
    {
        return JsonData.success(oaHardwareService.getNewModel(userLogin));
    }

    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin)
    {
        oaHardwareService.remove(id,userLogin);
        return JsonData.success();
    }

    @PostMapping("/update.json")
    public JsonData update(@RequestBody OaHardwareDto item)
    {
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"操作人不能为空!");
        oaHardwareService.update(item);
        return JsonData.success();
    }

    /**
     * 上传文件
     *
     * @param multipartFile
     * @return
     * @throws IOException
     */
    @RequestMapping("/receive.json")
    public JsonData receive(MultipartFile multipartFile, String userLogin) throws IOException {
        String fileName = multipartFile.getOriginalFilename();
        String msg="";
        if (!fileName.endsWith(".xls")) {
            Assert.state(false, "请将文件格式转换为 .xls");
        } else {
            msg=oaHardwareService.insertByFile(multipartFile.getInputStream(),userLogin);
        }
        return JsonData.success("",msg);
    }

    /**
     * 返回字符串格式兼容IE9
     *
     * @param multipartFile
     * @return
     * @throws IOException
     */
    @RequestMapping("/receive.do")
    public String receives(MultipartFile multipartFile, String userLogin) throws IOException {
        JsonData result = receive(multipartFile, userLogin);
        return JsonMapper.obj2String(result);
    }

}
