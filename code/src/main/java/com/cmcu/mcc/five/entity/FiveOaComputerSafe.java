package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaComputerSafe {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="formNo不能为空!")
    @Size(max=45, message="formNo长度不能超过45")
    private String formNo;

    @NotNull(message="申请单位不能为空!")
    @Max(value=999999999, message="申请单位必须为数字")
    private Integer deptId;

    @NotNull(message="申请单位名字不能为空!")
    @Size(max=145, message="申请单位名字长度不能超过145")
    private String deptName;

    @NotNull(message="remark不能为空!")
    @Size(max=1000, message="remark长度不能超过1000")
    private String remark;

    @NotNull(message="creator不能为空!")
    @Size(max=45, message="creator长度不能超过45")
    private String creator;

    @NotNull(message="创建人姓名不能为空!")
    @Size(max=45, message="创建人姓名长度不能超过45")
    private String creatorName;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="创建时间不能为空!")
    private Date gmtCreate;

    @NotNull(message="修改时间不能为空!")
    private Date gmtModified;

    @NotNull(message="processInstanceId不能为空!")
    @Size(max=45, message="processInstanceId长度不能超过45")
    private String processInstanceId;

    @NotNull(message="isProcessEnd不能为空!")
    private Boolean processEnd;

    @NotNull(message="deviceNo不能为空!")
    @Size(max=45, message="deviceNo长度不能超过45")
    private String deviceNo;

    @NotNull(message="deviceName不能为空!")
    @Size(max=45, message="deviceName长度不能超过45")
    private String deviceName;

    @NotNull(message="deviceSecurty不能为空!")
    @Size(max=45, message="deviceSecurty长度不能超过45")
    private String deviceSecurty;

    @NotNull(message="dutyMan不能为空!")
    @Size(max=45, message="dutyMan长度不能超过45")
    private String dutyMan;

    @NotNull(message="dutyManName不能为空!")
    @Size(max=45, message="dutyManName长度不能超过45")
    private String dutyManName;

    @NotNull(message="dutyTime不能为空!")
    @Size(max=45, message="dutyTime长度不能超过45")
    private String dutyTime;

    @NotNull(message="handleReason不能为空!")
    @Size(max=45, message="handleReason长度不能超过45")
    private String handleReason;

    @NotNull(message="handleContent不能为空!")
    @Size(max=1000, message="handleContent长度不能超过1000")
    private String handleContent;

    @NotNull(message="handleMan不能为空!")
    @Size(max=45, message="handleMan长度不能超过45")
    private String handleMan;

    @NotNull(message="handleManName不能为空!")
    @Size(max=45, message="handleManName长度不能超过45")
    private String handleManName;

    @NotNull(message="receiveMan不能为空!")
    @Size(max=45, message="receiveMan长度不能超过45")
    private String receiveMan;

    @NotNull(message="receiveManName不能为空!")
    @Size(max=45, message="receiveManName长度不能超过45")
    private String receiveManName;
}