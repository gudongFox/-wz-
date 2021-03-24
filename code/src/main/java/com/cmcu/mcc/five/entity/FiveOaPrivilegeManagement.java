package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaPrivilegeManagement {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="formNo不能为空!")
    @Size(max=45, message="formNo长度不能超过45")
    private String formNo;

    @NotNull(message="remark不能为空!")
    @Size(max=450, message="remark长度不能超过450")
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

    @NotNull(message="deptId不能为空!")
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

     @NotNull(message="deptName不能为空!")
    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="applicationMan不能为空!")
    @Size(max=45, message="applicationMan长度不能超过45")
    private String applicationMan;

    @NotNull(message="applicationManName不能为空!")
    @Size(max=45, message="applicationManName长度不能超过45")
    private String applicationManName;

    @NotNull(message="applicationTime不能为空!")
    @Size(max=45, message="applicationTime长度不能超过45")
    private String applicationTime;

    @NotNull(message="flow不能为空!")
    @Size(max=45, message="flow长度不能超过45")
    private String flow;

    @NotNull(message="delegationMan不能为空!")
    @Size(max=45, message="delegationMan长度不能超过45")
    private String delegationMan;

    @NotNull(message="delegationManName不能为空!")
    @Size(max=45, message="delegationManName长度不能超过45")
    private String delegationManName;

    @NotNull(message="delegationPrivilege不能为空!")
    @Size(max=45, message="delegationPrivilege长度不能超过45")
    private String delegationPrivilege;

    @NotNull(message="delegationTime不能为空!")
    @Size(max=45, message="delegationTime长度不能超过45")
    private String delegationTime;
}