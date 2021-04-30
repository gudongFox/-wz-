package com.cmcu.act.dto;

import com.google.common.collect.Lists;
import lombok.Data;
import org.activiti.engine.history.HistoricProcessInstance;

import java.util.Date;
import java.util.List;

/**
 * 流程状态
 */
@Data
public class CustomProcessInstance extends CustomSimpleProcessInstance {


    public CustomProcessInstance(){
        this.takeAble=false;
        this.ccAble=false;
        this.backAble=false;
        this.sendAble=false;
        this.delegateAble=false;
        this.transferAble=false;
        this.resolveAble=false;
        this.ccFinishAble=false;
        this.finished=true;
        this.firstTask=false;
        this.lastTask=false;
        this.fetchAble=false;
        runningTaskNameList= Lists.newArrayList();
        myRunningTaskNameList=Lists.newArrayList();
        myRunningTaskName="";
        currentTaskName="已完成";
    }


    private String processCategory;



    private String processDescription;


    /**
     * 发起人（部门名称）
     */
    private String initiatorNameDept;

    private Date initiatorTime;



    private Date startTime;

    private Date claimTime;

    //是否可以取回
    private  boolean fetchAble;

    //发送
    private boolean sendAble;

    //打回
    private boolean backAble;

    private boolean firstTask;

    private boolean lastTask;

    //抄送
    private boolean ccAble;

    private String ccUser;

    private String ccUserName;

    private String ccTaskId;

    private String ccTaskName;


    //可移交任务
    private boolean transferAble;

    //可委托任务
    private boolean delegateAble;

    //委托的任务办理完成
    private boolean resolveAble;

    //可接受任务
    private boolean takeAble;

    private boolean ccFinishAble;

    private boolean stared;



}
