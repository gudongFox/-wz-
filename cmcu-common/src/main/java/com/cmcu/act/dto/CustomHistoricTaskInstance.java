package com.cmcu.act.dto;

import lombok.Getter;
import lombok.Setter;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Comment;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class CustomHistoricTaskInstance {


    private String id;

    private String taskDefinitionKey;

    private String processInstanceId;

    /**
     * assignee头像
     */
    private String avatar;

    private String assignee;

    private String name;

    private Date startTime;

    private Date claimTime;

    private Date endTime;

    //通过的任务
    private boolean passed;


    private boolean ccTask;

    private String ccUser;

    private String ccUserName;

    private boolean ccAble;


    private String ownerName;

    private String assigneeName;

    private String candidateUsers;

    private String candidateUserNames;

    /**
     * 最后意见
     */
    private String latestComment;

    /**
     * 最后意见属于我的
     */
    private String currentComment;

    private List<ActCommentDto> comments;

}
