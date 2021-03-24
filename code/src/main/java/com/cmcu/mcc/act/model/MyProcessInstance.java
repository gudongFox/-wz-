package com.cmcu.mcc.act.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class MyProcessInstance {

    private String processInstanceId;

    private String processDefinitionName;

    private Boolean myTaskFirst;

    private Boolean fetchAble;

    private Boolean printAble;

    private Boolean backAble;

    private Boolean passAble;

    private String businessKey;

    private List<Map> taskList;

    private String processName;

    private Date processTime;

    private String userLogin;

    private String userName;

    private boolean processEnd;

    private String taskUsers;

    //上次处理时间
    private Date preHandleTime;


    //发送、打回任务使用
    private String myTaskId;

    private String myTaskName;

    private Date myTaskTime;




}
