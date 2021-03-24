package com.cmcu.mcc.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;


/**
 * 流程处理情况表
 */
@Getter
@Setter
public class ActHistoryDto {

    private String taskId;

    private String activityName;

    private String startTime;

    private String receiveTime;

    private String endTime;

    private String comment;

    private boolean passed;

    private Date start;

    private Date receive;

    private Date end;

    private String userName;

    private String userLogin;

    private boolean finished;

}
