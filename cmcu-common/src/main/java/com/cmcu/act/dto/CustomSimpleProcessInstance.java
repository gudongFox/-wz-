package com.cmcu.act.dto;


import com.google.common.collect.Lists;
import lombok.Data;
import org.activiti.engine.history.HistoricProcessInstance;

import java.util.List;

@Data
public class CustomSimpleProcessInstance {


    public CustomSimpleProcessInstance() {
        this.setMyRunningTaskNameList(Lists.newArrayList());
        this.setRunningTaskNameList(Lists.newArrayList());
    }

    public boolean finished;

    public String enLogin;

    private String keyInfo;

    //正在运行的任务列表
    public List<String> runningTaskNameList;

    //用户拥有的正在运行的任务列表
    public List<String> myRunningTaskNameList;

    //我的所有运行任务
    public String myRunningTaskName;

    //当前任务名称
    public String currentTaskName;

    public HistoricProcessInstance instance;

    public String taskId;

    public String taskName;

    public boolean firstTask;

    public String fetchTaskId;

    public String businessKey;

    public boolean haveContent;

    public String initiatorName;
}
