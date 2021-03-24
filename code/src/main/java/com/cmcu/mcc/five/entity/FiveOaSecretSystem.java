package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaSecretSystem {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="家具编号不能为空!")
    @Size(max=45, message="家具编号长度不能超过45")
    private String applyUserName;

    @Size(max=45, message="applyUserLogin长度不能超过45")
    private String applyUserLogin;

    @NotNull(message="采购理由不能为空!")
    @Size(max=45, message="采购理由长度不能超过45")
    private String applyUserNo;

    @NotNull(message="单位名称不能为空!")
    @Max(value=999999999, message="单位名称必须为数字")
    private Integer deptId;

     @NotNull(message="deptName不能为空!")
    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="总价不能为空!")
    @Size(max=45, message="总价长度不能超过45")
    private String phone;

    @NotNull(message="systemName不能为空!")
    @Size(max=145, message="systemName长度不能超过145")
    private String systemName;

    @NotNull(message="涉密等级不能为空!")
    @Size(max=45, message="涉密等级长度不能超过45")
    private String secretLevel;

    @NotNull(message="accountType不能为空!")
    @Size(max=145, message="accountType长度不能超过145")
    private String accountType;

    @NotNull(message="jurisdictionType不能为空!")
    @Size(max=45, message="jurisdictionType长度不能超过45")
    private String jurisdictionType;

    @NotNull(message="applyReason不能为空!")
    @Size(max=45, message="applyReason长度不能超过45")
    private String applyReason;

    @NotNull(message="executeType不能为空!")
    @Size(max=45, message="executeType长度不能超过45")
    private String executeType;

    @NotNull(message="executeRemark不能为空!")
    @Size(max=45, message="executeRemark长度不能超过45")
    private String executeRemark;

    @NotNull(message="remark不能为空!")
    @Size(max=450, message="remark长度不能超过450")
    private String remark;

    @NotNull(message="creator不能为空!")
    @Size(max=45, message="creator长度不能超过45")
    private String creator;

    @NotNull(message="creatorName不能为空!")
    @Size(max=45, message="creatorName长度不能超过45")
    private String creatorName;

    @NotNull(message="gmtCreate不能为空!")
    private Date gmtCreate;

    @NotNull(message="gmtModified不能为空!")
    private Date gmtModified;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="isProcessEnd不能为空!")
    private Boolean processEnd;

    @NotNull(message="processInstanceId不能为空!")
    @Size(max=45, message="processInstanceId长度不能超过45")
    private String processInstanceId;
}