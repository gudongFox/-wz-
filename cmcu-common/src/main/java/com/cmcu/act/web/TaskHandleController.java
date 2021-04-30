package com.cmcu.act.web;


import com.cmcu.act.dto.ActCandidateTask;
import com.cmcu.act.dto.ActCandidateUser;
import com.cmcu.act.service.TaskHandleService;
import com.common.model.JsonData;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/act/taskHandle")
public class TaskHandleController {


    final
    TaskHandleService taskHandleService;

    public TaskHandleController(TaskHandleService taskHandleService) {
        this.taskHandleService = taskHandleService;
    }


    @PostMapping("/listNextCandidateTask.json")
    public JsonData listNextCandidateTask(String taskId) {
        List<Map> tree = Lists.newArrayList();
        List<ActCandidateTask> tasks = taskHandleService.listNextCandidateTask(taskId);
        if(tasks.size()>0) {
            for (ActCandidateTask task : tasks) {
                Map node = Maps.newHashMap();
                node.put("id", task.getCurrentTaskKey());
                node.put("text", task.getName());
                node.put("parent", "#");
                node.put("seq", task.getSeq());
                Map state = Maps.newHashMap();
                state.put("disabled", false);
                state.put("selected", false);
                state.put("opened", task.getCurrentTaskKey().equalsIgnoreCase(task.getPreTaskKey()));
                node.put("state", state);
                tree.add(node);
                for (ActCandidateUser user : task.getCandidateUserList()) {
                    Map userNode = Maps.newHashMap();
                    userNode.put("id", task.getCurrentTaskKey() + "|" + user.getEnLogin());
                    userNode.put("text", user.getCnName());
                    userNode.put("parent", task.getCurrentTaskKey());
                    userNode.put("icon", "fa fa-user");
                    Map userState = Maps.newHashMap();
                    userState.put("opened", false);
                    userState.put("selected", user.isSelected());
                    userState.put("disabled", false);
                    userNode.put("state", userState);
                    tree.add(userNode);
                }
            }
        }else {
            Map node = Maps.newHashMap();
            node.put("id", "end");
            node.put("text", "结束");
            node.put("parent", "#");
            Map state = Maps.newHashMap();
            state.put("disabled", false);
            state.put("selected", true);
            state.put("opened", false);
            node.put("state", state);
            tree.add(node);
        }
        return JsonData.success(tree);
    }

    @PostMapping("/listBackCandidateTask.json")
    public JsonData listBackCandidateTask(String taskId) {
        List<Map> tree = Lists.newArrayList();
        List<ActCandidateTask> tasks = taskHandleService.listBackCandidateTask(taskId);
        if (tasks.size() > 0) {
            int seq = tasks.get(0).getSeq();
            for (ActCandidateTask task : tasks) {
                Map node = Maps.newHashMap();
                node.put("id", task.getCurrentTaskKey());
                node.put("text", task.getName());
                node.put("parent", "#");
                node.put("taskKey",task.getCurrentTaskKey());
                Map state = Maps.newHashMap();
                state.put("disabled", false);
                state.put("selected", false);
                state.put("opened", seq == task.getSeq());
                node.put("state", state);
                tree.add(node);
                for (ActCandidateUser user : task.getCandidateUserList()) {
                    Map userNode = Maps.newHashMap();
                    userNode.put("id", task.getCurrentTaskKey() + "|" + user.getEnLogin());
                    userNode.put("text", user.getCnName());
                    userNode.put("parent", task.getCurrentTaskKey());
                    userNode.put("icon", "fa fa-user");
                    userNode.put("taskKey",task.getCurrentTaskKey());
                    Map userState = Maps.newHashMap();
                    userState.put("opened", false);
                    userState.put("selected", user.isSelected());
                    userState.put("disabled", false);
                    userNode.put("state", userState);
                    tree.add(userNode);
                }
            }
        }
        return JsonData.success(tree);
    }


    @PostMapping("/sendTask.json")
    public JsonData  sendTask(String taskId,String ccUser,String comment,String candidateUsers,String enLogin, boolean completeTask,String agent) {
        if(StringUtils.isEmpty(candidateUsers)) candidateUsers="";
        taskHandleService.sendTask(taskId,ccUser, comment, StringUtils.split(candidateUsers,","), enLogin,completeTask,agent);
        return JsonData.success();
    }

    @PostMapping("/setComment.json")
    public JsonData  setComment(String taskId,String comment,String agent,String enLogin) {
        taskHandleService.setComment(taskId, comment, agent, enLogin);
        return JsonData.success();
    }


    @PostMapping("/backTask.json")
    public JsonData  backTask(String taskId,String comment,String candidateUsers,String enLogin,boolean completeTask,String agent) {
        if(StringUtils.isEmpty(candidateUsers)) candidateUsers="";
        taskHandleService.backTask(taskId, comment, StringUtils.split(candidateUsers,","), enLogin,completeTask,agent);
        return JsonData.success();
    }


    @PostMapping("/sendTaskSimple.json")
    public JsonData sendTaskSimple(String taskId,String comment,String enLogin,String agent) {
        taskHandleService.sendTaskSimple(taskId,comment, enLogin,agent);
        return JsonData.success();
    }



    @PostMapping("/backTaskSimple.json")
    public JsonData backTaskSimple(String taskId,String comment,String enLogin,String agent) {
        taskHandleService.backTaskSimple(taskId, comment, enLogin,agent);
        return JsonData.success();
    }




    @PostMapping("/takeTask.json")
    public JsonData takeTask(String taskId,String enLogin) {
        taskHandleService.takeTask(taskId,enLogin);
        return JsonData.success();
    }


    @PostMapping("/fetchTask.json")
    public JsonData fetchTask(String taskId,String enLogin,String agent) {
        taskHandleService.fetchTask(taskId,enLogin,agent);
        return JsonData.success();
    }


    @PostMapping("/ccFinishTask.json")
    public JsonData ccFinishTask(String taskId,String comment,String agent) {
        taskHandleService.ccFinishTask(taskId,comment,agent);
        return JsonData.success();
    }



    @PostMapping("/resolveTask.json")
    public JsonData resolveTask(String taskId,String enLogin,String comment,String agent) {
        taskHandleService.resolveTask(taskId,enLogin,comment,agent);
        return JsonData.success();
    }

    @PostMapping("/transferTask.json")
    public JsonData transferTask(String taskId,String newOwner,String comment,String enLogin,String agent) {
        taskHandleService.transferTask(taskId,newOwner,comment,enLogin,agent);
        return JsonData.success();
    }

    @PostMapping("/delegateTask.json")
    public JsonData delegateTask(String taskId,String enLogin,String newAssignee,String comment,String agent) {
        taskHandleService.delegateTask(taskId,enLogin,newAssignee,comment,agent);
        return JsonData.success();
    }


    @PostMapping("/forceNewAssignee.json")
    public JsonData forceNewAssignee(String taskId,String newAssignee) {
        Assert.state(StringUtils.isNotEmpty(newAssignee)&&StringUtils.isNotEmpty(taskId),"任务不存在或办理人未指定!");
        taskHandleService.forceNewAssignee(taskId,newAssignee);


        return JsonData.success();
    }



}
