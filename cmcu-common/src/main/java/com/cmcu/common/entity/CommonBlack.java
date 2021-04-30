package com.cmcu.common.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class CommonBlack {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="tenetId不能为空!")
    @Size(max=45, message="tenetId长度不能超过45")
    private String tenetId;

    @NotNull(message="*,luodong不能为空!")
    @Size(max=45, message="*,luodong长度不能超过45")
    private String targetUser;

    @NotNull(message="*,或者特定不能为空!")
    @Size(max=145, message="*,或者特定长度不能超过145")
    private String targetIp;

    @NotNull(message="禁止 允许不能为空!")
    private Boolean forbidden;

    @NotNull(message="deleted不能为空!")
    private Boolean deleted;

    @NotNull(message="gmtCreate不能为空!")
    private Date gmtCreate;

    @NotNull(message="gmtModified不能为空!")
    private Date gmtModified;

    @NotNull(message="配置过期时间不能为空!")
    private Date gmtExpired;

    @NotNull(message="remark不能为空!")
    @Size(max=145, message="remark长度不能超过145")
    private String remark;
}