package com.cmcu.act.dto;

import lombok.Data;


import java.util.Date;


@Data
public class ActProcessInstance {

    private String businessKey;

    private String processInstanceId;

    private String tenantId;

    private String processDefinitionId;

    private String processDefinitionName;

    private Date startTime;

    private Date endTime;

    private String currentState;

    private String currentUser;

    private String initiator;

    private boolean finished;

    private boolean suspend;

}
