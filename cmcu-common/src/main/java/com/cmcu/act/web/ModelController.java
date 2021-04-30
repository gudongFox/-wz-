package com.cmcu.act.web;

import com.cmcu.act.service.ModelService;
import com.common.model.JsonData;
import com.cmcu.common.util.JsonMapper;
import com.cmcu.common.util.WebUtil;

import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/act/model")
public class ModelController {


    final
    ModelService modelService;

    public ModelController(ModelService modelService) {
        this.modelService = modelService;
    }

    @PostMapping("/listModelCategory.json")
    public JsonData listModelCategory(String enLogin) {
        return JsonData.success(modelService.listModelCategory(enLogin));
    }


    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String enLogin) throws Exception {
        return JsonData.success(modelService.getNewModel(enLogin));
    }

    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(int pageNum,int pageSize){
        Map params = WebUtil.getRequestParameters();
        return JsonData.success(modelService.listPagedData(pageNum,pageSize,params));
    }

    @PostMapping("/deployment.json")
    public JsonData deployment(String id) throws IOException {
        modelService.deployment(id);
        return JsonData.success();
    }

    @PostMapping("/remove.json")
    public JsonData remove(String id){
        modelService.remove(id);
        return JsonData.success();
    }


    @PostMapping(value = "/save.json")
    public JsonData save(String modelId, @RequestBody MultiValueMap<String, String> values) {
        modelService.save(modelId,values);
        return JsonData.success();
    }

    @RequestMapping(value = "/downloadModel.json")
    public JsonData downloadModel(String modelId, HttpServletResponse response) throws Exception {
        modelService.downloadModel(modelId,response);
        return JsonData.success();
    }
    /**
     * 导入数据
     * @param multipartFile
     * @return
     * @throws IOException
     */
    @RequestMapping("/receive.json")
    public JsonData receive(MultipartFile multipartFile,String enLogin) throws Exception {
        String message = modelService.uploadBpmn(multipartFile,enLogin);
        return JsonData.success(message);
    }

    /**
     * 返回字符串格式兼容IE9
     * @param multipartFile
     * @return
     * @throws IOException
     */
    @RequestMapping("/receive.do")
    public String receives(MultipartFile multipartFile,String enLogin) throws Exception {
        JsonData result=receive(multipartFile,enLogin);
        return JsonMapper.obj2String(result);
    }



}
