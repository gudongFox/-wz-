package com.cmcu.mcc.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ProcessInstanceDto {

    private String processInstanceId;

    private String processDefinitionName;

    private String businessKey;

    private List<Map> taskList;

    private boolean passAble;

    private boolean backAble;

    private boolean fetchAble;

    private String processName;

    private Date processTime;

    private String userLogin;

    private String userName;

    private String myTaskId;

    private String myTaskName;

    private boolean myTaskFirst;

    private Date myTaskTime;

    private boolean processEnd;

    private String taskUsers;

}
