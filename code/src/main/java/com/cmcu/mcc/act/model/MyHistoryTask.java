package com.cmcu.mcc.act.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class MyHistoryTask {

    private String id;

    private Date startTime;

    private Date endTime;

    private Date claimTime;

    private String assignee;

    private String assigneeName;

    private String headAttachId;

    private String name;

    private String taskDefinitionKey;

    private String comment;

    private String opt;

}
