package com.cmcu.mcc.controller;


import com.common.model.JsonData;
import com.cmcu.common.act.dto.UserTaskDto;
import com.cmcu.common.util.CookieSessionUtils;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.act.service.MyDiagramService;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.service.ActBusinessService;
import com.cmcu.mcc.service.ActService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/act")
public class ActController {

    @Autowired
    ActService actService;



    @Autowired
    ActBusinessService actBusinessService;

    @Autowired
    MyDiagramService myDiagramService;

    /**
     * 统一跳转   待删除  已调整到 IndexController
     * @param businessKey
     * @return
     */
    @PostMapping("/getNgRedirectUrl.json")
    public JsonData getNgRedirectUrl(String businessKey){
        String enLogin= CookieSessionUtils.getSession(MccConst.EN_LOGIN);
        return JsonData.success(actBusinessService.getNgDirectUrl(businessKey,enLogin));
    }

    /**
     * 流程关联跳转
     * @param businessKey
     * @return
     */
    @PostMapping("/getNgActRelevanceUrl.json")
    public JsonData getNgActRelevanceUrl(String businessKey){
        return JsonData.success(actBusinessService.getNgActRelevanceUrl(businessKey));
    }


    @PostMapping("/getEdStepTimeline.json")
    public JsonData getEdStepTimeline(String stepId ){
        return JsonData.success(actBusinessService.getEdStepTimeline(Integer.parseInt(stepId)));
    }


    @PostMapping("/getExploreStepTimeline.json")
    public JsonData getExploreStepTimeline(String stepId ){
        return JsonData.success(actBusinessService.getExploreStepTimeline(Integer.parseInt(stepId)));
    }

    @PostMapping("/getProjectTimeline.json")
    public JsonData getProjectTimeline(int projectId ) {
        return JsonData.success(actBusinessService.getProjectTimeline(projectId));
    }

    @PostMapping("/getExploreTimeline.json")
    public JsonData getExploreTimeline(int projectId ) {
        return JsonData.success(actBusinessService.getExploreTimeline(projectId));
    }

    @PostMapping("/getProcessInstanceDto.json")
    public JsonData getProcessInstanceDto(String processInstanceId,String userLogin){
        return JsonData.success(actService.getProcessInstanceDto(processInstanceId,userLogin));
    }

    @PostMapping("/listNextStep2.json")
    public JsonData listNextStep2(String taskId) {
        try {
            Map map = WebUtil.getRequestParameters();
            map.remove("taskId");
            List<Map> stepList=actService.listNextStep(taskId, map);
            //构造Tree
            List<Map> tree= Lists.newArrayList();
            for(int j=0;j<stepList.size();j++) {
                Map step=stepList.get(j);

                Map node = Maps.newHashMap();
                String taskKey=step.get("taskKey").toString();
                node.put("id", taskKey);
                node.put("text", step.get("activityName").toString());
                node.put("parent", "#");
                node.put("type",step.get("type").toString());
                Map state = Maps.newHashMap();
                state.put("opened", true);
                state.put("selected", false);
                state.put("disabled", false);
                node.put("state", state);
                tree.add(node);
                if(node.get("text").toString().equals("结束")) {
                    state.put("selected", true);
                }else {
                    List<Map> users = (List<Map>) step.get("users");
                    for (int i = 0; i < users.size(); i++) {
                        Map user = users.get(i);
                        Map userNode = Maps.newHashMap();
                        userNode.put("id", taskKey + "_" + user.get("userLogin"));
                        userNode.put("text", user.get("userName"));
                        userNode.put("parent", taskKey);
                        userNode.put("icon", "icon-user");
                        Map userState = Maps.newHashMap();
                        userState.put("opened", false);
                        userState.put("selected", user.get("selected"));
                        userState.put("disabled", false);
                        userNode.put("state", userState);
                        tree.add(userNode);
                    }
                }
            }
            return JsonData.success(tree);
        } catch (Exception ex) {
            ex.printStackTrace();
            return JsonData.fail(ex.getMessage());
        }
    }


    @PostMapping("/listBackStep2.json")
    public JsonData listBackStep2(String taskId) {
        try {
            List<Map> stepList=actService.listBackStep(taskId);
            List<Map> tree= Lists.newArrayList();
            //只把第一个节点默认选中
            String firstIncomingId=stepList.get(0).get("incomingId").toString();
            for(int j=0;j<stepList.size();j++) {
                Map step=stepList.get(j);
                Map node = Maps.newHashMap();
                String taskKey=step.get("taskKey").toString();
                String incomingId=step.get("incomingId").toString();
                node.put("id", taskKey);
                node.put("text", step.get("activityName").toString());
                node.put("parent", "#");
                Map state = Maps.newHashMap();
                state.put("opened", true);
                state.put("selected", false);
                state.put("disabled", false);
                node.put("state", state);
                tree.add(node);
                List<Map> users = (List<Map>) step.get("users");
                for(int i=0;i<users.size();i++) {
                    Map user = users.get(i);
                    Map userNode = Maps.newHashMap();
                    userNode.put("id", taskKey + "_" + user.get("userLogin"));
                    userNode.put("text", user.get("userName"));
                    userNode.put("parent", taskKey);
                    userNode.put("icon", "icon-user");
                    boolean selected=Boolean.parseBoolean(user.get("selected").toString());
                    if(selected){
                        selected=firstIncomingId.equalsIgnoreCase(incomingId);
                    }
                    Map userState = Maps.newHashMap();
                    userState.put("opened", false);
                    userState.put("selected", selected);
                    userState.put("disabled", false);
                    userNode.put("state", userState);
                    tree.add(userNode);
                }
            }
            return JsonData.success(tree);
        } catch (Exception ex) {
            ex.printStackTrace();
            return JsonData.fail(ex.getMessage());
        }
    }


    @PostMapping("/listNextStep.json")
    public JsonData listNextStep(String taskId) {
        try {
            List<Map> tree = Lists.newArrayList();
            List<UserTaskDto> userTasks = actBusinessService.listNextUserTask(taskId);
            for (UserTaskDto userTask : userTasks) {
                Map node = Maps.newHashMap();
                node.put("id", userTask.getTaskKey());
                node.put("text", userTask.getTaskName());
                node.put("parent", "#");
                node.put("multiple",userTask.isMultiple());
                Map state = Maps.newHashMap();
                state.put("opened", true);
                //没有用户将其默认为选中,比如结束节点
                state.put("selected", userTask.getUsers().size()==0);
                state.put("disabled", false);
                node.put("state", state);
                tree.add(node);
                List<Map> users =userTask.getUsers();
                for (int i = 0; i < users.size(); i++) {
                    Map user = users.get(i);
                    Map userNode = Maps.newHashMap();
                    userNode.put("id", userTask.getTaskKey() + "_" + user.get("userLogin"));
                    userNode.put("text", user.get("userName"));
                    userNode.put("parent", userTask.getTaskKey());
                    userNode.put("icon", "icon-user");
                    Map userState = Maps.newHashMap();
                    userState.put("opened", false);
                    userState.put("selected", user.get("selected"));
                    userState.put("disabled", false);
                    userNode.put("state", userState);
                    tree.add(userNode);
                }


            }
            return JsonData.success(tree);
        } catch (Exception ex) {
            ex.printStackTrace();
            return JsonData.fail(ex.getMessage());
        }
    }



    @PostMapping("/listBackStep.json")
    public JsonData listBackStep(String taskId) {
        try {
            List<Map> tree = Lists.newArrayList();
            List<UserTaskDto> userTasks = actBusinessService.listBackUserTask(taskId);
            for (int i=0;i<userTasks.size();i++) {
                UserTaskDto userTask=userTasks.get(i);

                String parentKey=userTask.getPreKey()+"-"+userTask.getTaskKey();
                Map node = Maps.newHashMap();
                node.put("id", parentKey);
                node.put("text", userTask.getTaskName());
                node.put("parent", "#");
                node.put("multiple",userTask.isMultiple());
                Map state = Maps.newHashMap();
                state.put("opened", userTasks.size()<=3);
                //没有用户将其默认为选中,比如结束节点
                boolean selected=i==0;
                state.put("selected", selected&&userTask.isMultiple());
                state.put("disabled", false);
                node.put("state", state);
                tree.add(node);
                List<Map> users =userTask.getUsers();
                for (int j = 0; j < users.size(); j++) {
                    Map user = users.get(j);
                    Map userNode = Maps.newHashMap();
                    userNode.put("id", parentKey + "_" + user.get("userLogin"));
                    userNode.put("text", user.get("userName"));
                    userNode.put("parent", parentKey);
                    userNode.put("icon", "icon-user");
                    Map userState = Maps.newHashMap();
                    userState.put("opened", false);
                    userState.put("selected", (selected)&&(boolean)user.get("selected"));
                    userState.put("disabled", false);
                    userNode.put("state", userState);
                    tree.add(userNode);
                }


            }
            return JsonData.success(tree);
        } catch (Exception ex) {
            ex.printStackTrace();
            return JsonData.fail(ex.getMessage());
        }
    }

//
//    @PostMapping("/handleSimpleTask.json")
//    public JsonData handleSimpleTask(String taskId,boolean passed,String comment){
//        if(passed){
//            actService.sendFlow(taskId,new String[0],comment);
//            List<Map> nextSteps=actService.listNextStep(taskId,Maps.newHashMap());
//
//
//
//
//
//
//        }else{
//            List<UserTaskDto> userTasks = actBusinessService.listBackUserTask(taskId);
//            if(userTasks.size()==0){
//                throw  new IllegalArgumentException("未找到前置节点,无法打回!");
//            }
//            List<String> users=Lists.newArrayList();
//            UserTaskDto userTask=userTasks.get(0);
//            if(userTask.getUsers().size()>0) {
//                String parentKey = userTask.getPreKey() + "-" + userTask.getTaskKey();
//                if (userTask.isMultiple()) {
//                    for (Map map : userTask.getUsers()) {
//                        users.add(parentKey + "_" + map.get("userLogin"));
//                    }
//                } else {
//                    boolean find = false;
//                    for (Map map : userTask.getUsers()) {
//                        if ((boolean) map.get("selected")) {
//                            users.add(parentKey + "_" + map.get("userLogin"));
//                            find = true;
//                            break;
//                        }
//                    }
//                    if (!find) {
//                        users.add(parentKey+"_"+userTask.getUsers().get(0).get("userLogin"));
//                    }
//                }
//            }
//            actService.backFlow(taskId,users.toArray(new String[0]),comment);
//        }
//        return JsonData.success();
//    }



    @PostMapping("/listTask.json")
    public JsonData listTask(String userLogin,int pageNum,int pageSize,String taskName){
       return JsonData.success(actService.listTask(userLogin,pageNum,pageSize,taskName));
    }

    @PostMapping("/listDoneTask.json")
    public JsonData listDoneTask(String userLogin,int pageNum,int pageSize,String taskName){
        return JsonData.success(actService.listDoneTask(userLogin,pageNum,pageSize,taskName));
    }

    @PostMapping("/listCadTask.json")
    public JsonData listCadTask(String userLogin,String description){
        return JsonData.success(actService.listCadTask(userLogin,description));
    }

    @PostMapping("/listDoneCadTask.json")
    public JsonData listDoneCadTask(String userLogin,String description){
        return JsonData.success(actService.listDoneCadTask(userLogin,description));
    }

    /**
     * 下载文件
     * @param processInstanceId
     * @param response
     * @throws IOException
     */
    @RequestMapping("/download/{processInstanceId}")
    public void  download2FileHistory(@PathVariable String processInstanceId, final HttpServletResponse response) throws IOException {
        byte[] data = FileCopyUtils.copyToByteArray(myDiagramService.getDefaultInstanceStream(processInstanceId));
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("abc.png", "utf-8").replace("+", "%20"));
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("multipart/form-data");
        OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
        outputStream.write(data);
        outputStream.flush();
        outputStream.close();
    }

    /**
     * 根据流程key 下载流程图片
     * @param processDefinitionKey 流程定义key
     * @param response
     * @throws IOException
     */
    @RequestMapping("/downloadPngByProcessDefinitionKey/{processDefinitionKey}")
    public void  downloadPngByProcessDefinitionKey(@PathVariable String processDefinitionKey, final HttpServletResponse response) throws IOException {
        byte[] data = FileCopyUtils.copyToByteArray(myDiagramService.getDefaultInstanceStream(processDefinitionKey));
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("abc.png", "utf-8").replace("+", "%20"));
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("multipart/form-data");
        OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
        outputStream.write(data);
        outputStream.flush();
        outputStream.close();
    }

}
