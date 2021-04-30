package com.cmcu.mcc.act.controller;

import com.common.model.JsonData;
import com.cmcu.common.act.dto.UserTaskDto;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.act.service.MyDiagramService;
import com.cmcu.mcc.act.service.MyHistoryService;
import com.cmcu.mcc.act.service.MySendService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/myAct")
public class MyActController {


    @Autowired
    MyHistoryService myHistoryService;

    @Autowired
    MyDiagramService myDiagramService;

    @Autowired
    MyActService myActService;

    @Autowired
    MySendService mySendService;



    /**
     * 下载文件
     * @param processInstanceId
     * @param response
     * @throws IOException
     */
    @RequestMapping("/download/{processInstanceId}")
    public void  download2FileHistory(@PathVariable String processInstanceId, final HttpServletResponse response) throws IOException {
        if(StringUtils.isNotEmpty(processInstanceId)&&!processInstanceId.contains("fav")) {
            try {
                byte[] data = FileCopyUtils.copyToByteArray(myDiagramService.getCustomProcessInstanceStream(processInstanceId));
                response.reset();
                response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("abc.png", "utf-8").replace("+", "%20"));
                response.addHeader("Content-Length", "" + data.length);
                response.setContentType("multipart/form-data");
                OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
                outputStream.write(data);
                outputStream.flush();
                outputStream.close();
            }catch (Exception ex){

            }
        }
    }

    /**
     * 根据流程key 下载流程图片
     * @param processDefinitionKey 流程定义key
     * @param response
     * @throws IOException
     */
    @RequestMapping("/downloadPngByProcessDefinitionKey/{processDefinitionKey}")
    public void  downloadPngByProcessDefinitionKey(@PathVariable String processDefinitionKey, final HttpServletResponse response) throws IOException {
        byte[] data = FileCopyUtils.copyToByteArray(myDiagramService.getCustomProcessInstanceStreamByKey(processDefinitionKey));
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("abc.png", "utf-8").replace("+", "%20"));
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("multipart/form-data");
        OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
        outputStream.write(data);
        outputStream.flush();
        outputStream.close();
    }

    @PostMapping("/listTask.json")
    public JsonData listTask(String userLogin,String processDefinitionName,String description){
        Map<String,Object> map=Maps.newHashMap();
        map.put("involvedUser",userLogin);
        map.put("involvedUser",userLogin);
        map.put("involvedUser",userLogin);

        return JsonData.success(myHistoryService.listTask(userLogin,processDefinitionName,description));
    }

    @PostMapping("/listDoneTask.json")
    public JsonData listDoneTask(String userLogin,int pageNum,int pageSize,String processDefinitionName,String description){
        return JsonData.success(myHistoryService.listDoneTask(userLogin,pageNum,pageSize,processDefinitionName,description));
    }

    @PostMapping("/listDoneEndTask.json")
    public JsonData listDoneEndTask(String userLogin,int pageNum,int pageSize,String processDefinitionName,String description){
        return JsonData.success(myHistoryService.listDoneEndTask(userLogin,pageNum,pageSize,processDefinitionName,description));
    }

    @PostMapping("/listMyProcess.json")
    public JsonData listMyProcess(String userLogin,int pageNum,int pageSize,String finish,String processDefinitionName,String description){
        return JsonData.success(myHistoryService.listMyProcess(userLogin,pageNum,pageSize,finish,processDefinitionName,description));
    }

    @PostMapping("/listHistoryTask.json")
    public JsonData listHistoryTask(String processInstanceId){
        return JsonData.success(myHistoryService.listHistoryTask(processInstanceId));
    }

    @PostMapping("/getMyProcessInstance.json")
    public JsonData getMyProcessInstance(String processInstanceId,String userLogin){
        return JsonData.success(myHistoryService.getMyProcessInstance(processInstanceId,userLogin));
    }

    @PostMapping("/fetchFlow.json")
    public JsonData fetchFlow(String processInstanceId,String userLogin,String comment){
        mySendService.fetchFlow(processInstanceId,userLogin,comment);
        return JsonData.success();
    }


    @PostMapping("/sendFlow.json")
    public JsonData sendFlow(String taskId,String[] users,String userLogin,String comment){
        mySendService.sendFlow(taskId,users,comment);
        return JsonData.success();
    }


    @PostMapping("/backFlow.json")
    public JsonData backFlow(String taskId,String[] users,String userLogin,String comment){
        mySendService.backFlow(taskId,users,comment);
        return JsonData.success();
    }


    @PostMapping("/handleSimpleTask.json")
    public JsonData handleSimpleTask(String taskId,boolean passed,String userLogin,String comment) {
        if (passed) {
            List<UserTaskDto> userTasks = myActService.listNextUserTask(taskId);
            List<String> candidates = Lists.newArrayList();
            for (UserTaskDto userTask : userTasks) {
                List<Map> users = userTask.getUsers();
                for (int i = 0; i < users.size(); i++) {
                    Map user = users.get(i);
                    if ((Boolean) user.get("selected")) {
                        candidates.add(userTask.getTaskKey() + "_" + user.get("userLogin"));
                    }
                }
            }
            sendFlow(taskId, candidates.toArray(new String[0]), userLogin, comment);
        } else {

            List<UserTaskDto> userTasks = myActService.listBackUserTask(taskId);
            if (userTasks.size() == 0) {
                throw new IllegalArgumentException("未找到前置节点,无法打回!");
            }
            List<String> users = Lists.newArrayList();
            UserTaskDto userTask = userTasks.get(0);
            if (userTask.getUsers().size() > 0) {
                String parentKey = userTask.getPreKey() + "-" + userTask.getTaskKey();
                if (userTask.isMultiple()) {
                    for (Map map : userTask.getUsers()) {
                        users.add(parentKey + "_" + map.get("userLogin"));
                    }
                } else {
                    boolean find = false;
                    for (Map map : userTask.getUsers()) {
                        if ((boolean) map.get("selected")) {
                            users.add(parentKey + "_" + map.get("userLogin"));
                            find = true;
                            break;
                        }
                    }
                    if (!find) {
                        users.add(parentKey + "_" + userTask.getUsers().get(0).get("userLogin"));
                    }
                }
            }
            backFlow(taskId, users.toArray(new String[0]), userLogin, comment);
        }
        return JsonData.success();
    }


    @PostMapping("/listNextStep.json")
    public JsonData listNextStep(String taskId) {
        try {
            List<Map> tree = Lists.newArrayList();
            List<UserTaskDto> userTasks = myActService.listNextUserTask(taskId);
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
            List<UserTaskDto> userTasks = myActService.listBackUserTask(taskId);
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


}
