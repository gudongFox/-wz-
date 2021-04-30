package com.cmcu.act.dto;

import lombok.Data;

@Data
public class ActCandidateUser {

    private String taskDefinitionKey;

    private String enLogin;

    private String cnName;

    //是否默认选中
    private boolean selected;
}
