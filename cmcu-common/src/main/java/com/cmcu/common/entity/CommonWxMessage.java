package com.cmcu.common.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class CommonWxMessage {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="tenetId不能为空!")
    @Size(max=45, message="tenetId长度不能超过45")
    private String tenetId;

    @NotNull(message="msgType不能为空!")
    @Size(max=145, message="msgType长度不能超过145")
    private String msgType;

    @NotNull(message="taskId不能为空!")
    @Size(max=45, message="taskId长度不能超过45")
    private String taskId;

    @NotNull(message="msgTitle不能为空!")
    @Size(max=145, message="msgTitle长度不能超过145")
    private String msgTitle;

    @NotNull(message="msgUrl不能为空!")
    @Size(max=450, message="msgUrl长度不能超过450")
    private String msgUrl;

    @NotNull(message="toUser不能为空!")
    @Size(max=65535, message="toUser长度不能超过4500")
    private String toUser;

    @NotNull(message="isSended不能为空!")
    private Boolean sended;

    @NotNull(message="tryCount不能为空!")
    @Max(value=999999999, message="tryCount必须为数字")
    private Integer tryCount;

    @NotNull(message="tryTime不能为空!")
    private Date tryTime;

    @NotNull(message="gmtCreate不能为空!")
    private Date gmtCreate;

    @NotNull(message="gmtModified不能为空!")
    private Date gmtModified;

    @NotNull(message="remark不能为空!")
    @Size(max=450, message="remark长度不能超过450")
    private String remark;

    @NotNull(message="msgData不能为空!")
    @Size(max=65535, message="msgData长度不能超过65535")
    private String msgData;
}