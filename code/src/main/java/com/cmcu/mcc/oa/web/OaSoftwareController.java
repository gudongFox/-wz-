package com.cmcu.mcc.oa.web;

import com.cmcu.common.JsonData;
import com.cmcu.common.util.JsonMapper;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.oa.dto.OaHardwareDto;
import com.cmcu.mcc.oa.dto.OaSoftwareDto;
import com.cmcu.mcc.oa.service.OaHardwareService;
import com.cmcu.mcc.oa.service.OaSoftwareService;
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
import java.util.Map;

@RestController
@RequestMapping("/oa/software")
public class OaSoftwareController {

    @Autowired
    OaSoftwareService oaSoftwareService;

    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin, int pageNum,int pageSize) throws ParseException {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = oaSoftwareService.listPagedData(params,userLogin,pageNum,pageSize);
        return JsonData.success(pageInfo);
    }


    @PostMapping("/getModelById.json")
    public JsonData getModelById(int id)
    {
        return JsonData.success(oaSoftwareService.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin)
    {
        return JsonData.success(oaSoftwareService.getNewModel(userLogin));
    }

    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin)
    {
        oaSoftwareService.remove(id,userLogin);
        return JsonData.success();
    }

    @PostMapping("/update.json")
    public JsonData update(@RequestBody OaSoftwareDto item)
    {
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"操作人不能为空!");
        oaSoftwareService.update(item);
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
            msg=oaSoftwareService.insertByFile(multipartFile.getInputStream(),userLogin);
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
