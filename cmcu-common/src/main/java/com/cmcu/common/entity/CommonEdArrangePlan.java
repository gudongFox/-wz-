package com.cmcu.common.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class CommonEdArrangePlan {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="buildId不能为空!")
    @Max(value=999999999, message="buildId必须为数字")
    private Integer buildId;

    @NotNull(message="buildName不能为空!")
    @Size(max=145, message="buildName长度不能超过145")
    private String buildName;

    @NotNull(message="planName不能为空!")
    @Size(max=145, message="planName长度不能超过145")
    private String planName;


    @NotNull(message="planArea不能为空!")
    @Size(max=145, message="planArea长度不能超过145")
    private String planArea;

    @NotNull(message="planTip不能为空!")
    @Size(max=450, message="planTip长度不能超过450")
    private String planTip;

    @NotNull(message="planDesc不能为空!")
    @Size(max=450, message="planDesc长度不能超过450")
    private String planDesc;

    @NotNull(message="seq不能为空!")
    @Max(value=999999999, message="seq必须为数字")
    private Integer seq;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="gmtCreate不能为空!")
    private Date gmtCreate;

    @NotNull(message="gmtModified不能为空!")
    private Date gmtModified;
}