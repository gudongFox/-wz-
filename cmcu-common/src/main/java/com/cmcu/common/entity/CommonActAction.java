package com.cmcu.common.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class CommonActAction {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="tenetId不能为空!")
    @Size(max=45, message="tenetId长度不能超过45")
    private String tenetId;

    @NotNull(message="processInstanceId不能为空!")
    @Size(max=145, message="processInstanceId长度不能超过145")
    private String processInstanceId;

    @NotNull(message="enLogin不能为空!")
    @Size(max=145, message="enLogin长度不能超过145")
    private String enLogin;

    @NotNull(message="actionName不能为空!")
    @Size(max=45, message="actionName长度不能超过45")
    private String actionName;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="gmtCreate不能为空!")
    private Date gmtCreate;

    @NotNull(message="gmtModified不能为空!")
    private Date gmtModified;
}