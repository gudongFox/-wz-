package com.cmcu.act.dto;

import lombok.Data;

import java.util.Date;


@Data
public class ActCommentDto {

    private String id;

    private String message;

    private String taskId;

    private String userId;

    private String avatar;

    private String cnName;

    private Date  gmtCreate;

    private String agent;

}
