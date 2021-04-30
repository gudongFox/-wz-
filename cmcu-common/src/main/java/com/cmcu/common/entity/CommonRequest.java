package com.cmcu.common.entity;

import java.math.BigDecimal;
import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class CommonRequest {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Long id;


    @NotNull(message="tenetId不能为空!")
    @Size(max=50, message="tenetId长度不能超过45")
    private String tenetId;

    @NotNull(message="requestName不能为空!")
    @Size(max=50, message="requestName长度不能超过45")
    private String requestName;

    @NotNull(message="requestMethod不能为空!")
    @Size(max=20, message="requestMethod长度不能超过45")
    private String requestMethod;

    @NotNull(message="requestUrl不能为空!")
    @Size(max=145, message="requestUrl长度不能超过145")
    private String requestUrl;

    @NotNull(message="finishTime不能为空!")
    private Date finishTime;

    @NotNull(message="耗时不能为空!")
    @Max(value=999999999, message="耗时必须为数字")
    private BigDecimal requestSecond;

    @Size(max=1500, message="requestParameter长度不能超过1500")
    private String requestParameter;

    @Size(max=45, message="requestHost长度不能超过45")
    private String requestHost;

    @NotNull(message="requestIp不能为空!")
    @Size(max=45, message="requestIp长度不能超过45")
    private String requestIp;

    @Size(max=45, message="用户标识长度不能超过45")
    private String requestLogin;

    @Size(max=450, message="userAgent长度不能超过450")
    private String userAgent;

    @Size(max=450, message="referer长度不能超过450")
    private String referer;

    @NotNull(message="gmtCreate不能为空!")
    private Date gmtCreate;

    @NotNull(message="gmtModified不能为空!")
    private Date gmtModified;

    @Size(max=450, message="remark长度不能超过450")
    private String remark;
}