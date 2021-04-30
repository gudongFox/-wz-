package com.cmcu.act.web;

import com.cmcu.act.dto.CustomHistoricTaskInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskQueryService;
import com.common.model.JsonData;
import com.cmcu.common.util.WebUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/act/taskQuery")
public class TaskQueryController {

    @Resource
    TaskQueryService taskQueryService;

    @Resource
    ProcessQueryService processQueryService;



    @PostMapping("/listPagedTask.json")
    public JsonData listPagedTask(String enLogin,int pageNum,int pageSize) {
        Map params = WebUtil.getRequestParameters();
        return JsonData.success(taskQueryService.listPagedTask(pageNum, pageSize,enLogin,params));
    }


    @PostMapping("/listPagedCcTask.json")
    public JsonData listPagedCcTask(String enLogin,int pageNum,int pageSize) {
        Map params = WebUtil.getRequestParameters();
        return JsonData.success(taskQueryService.listPagedCcTask(pageNum, pageSize,enLogin,params));
    }


    @PostMapping("/getCcTaskCount.json")
    public JsonData getCcTaskCount(String enLogin) {
        return JsonData.success(taskQueryService.getCcTaskCount(enLogin, Maps.newHashMap()));
    }

    @PostMapping("/getTaskCount.json")
    public JsonData getTaskCount(String enLogin) {
        Map params = WebUtil.getRequestParameters();
        return JsonData.success(taskQueryService.getTaskCount(enLogin, params));
    }




    @PostMapping("/listTaskByH5.json")
    public JsonData listTaskByH5(String enLogin,int firstResult, int maxResults) {
        Map params=WebUtil.getRequestParameters();
        return JsonData.success(taskQueryService.listTaskByH5(enLogin,firstResult,maxResults,params));
    }


    @PostMapping("/listTaskGroupByCad.json")
    public JsonData listTaskGroupByCad(String enLogin) {
        return JsonData.success(taskQueryService.listTaskGroupByCad(enLogin));
    }




    /**
     * 查完成的任务
     * @param enLogin
     * @param pageNum
     * @param pageSize
     * @return
     */
    @PostMapping("/listPagedHistoricTask.json")
    public JsonData listPagedHistoricTask(String enLogin,int pageNum,int pageSize) {
        Map params = WebUtil.getRequestParameters();
        return JsonData.success(taskQueryService.listPagedHistoricTask(params, enLogin, pageNum, pageSize));
    }

    @PostMapping("/listHistoricTaskInstanceByInstanceId.json")
    public JsonData listHistoricTaskInstanceByInstanceId(String processInstanceId,String enLogin) {
        List<CustomHistoricTaskInstance> list=taskQueryService.listHistoricTaskInstance(processInstanceId);
        return JsonData.success(list);
    }

    @PostMapping("/listHistoricTaskMap.json")
    public JsonData listHistoricTaskMap(String processInstanceId,String enLogin) {
        List<Map> list = taskQueryService.listHistoricTaskMap(processInstanceId);
        return JsonData.success(list);
    }




    @PostMapping("/getHistoricTaskInstance.json")
    public JsonData getHistoricTaskInstance(String taskId) {
        return JsonData.success(taskQueryService.getHistoricTaskInstance(taskId));
    }


    @PostMapping("/listHistoricTaskInstance.json")
    public JsonData listHistoricTaskInstance(String processInstanceId) {
        List<CustomHistoricTaskInstance> list=taskQueryService.listHistoricTaskInstance(processInstanceId);
        return JsonData.success(list);
    }


}
