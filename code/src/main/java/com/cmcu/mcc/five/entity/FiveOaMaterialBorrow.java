package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaMaterialBorrow {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="formNo不能为空!")
    @Size(max=45, message="formNo长度不能超过45")
    private String formNo;

    @NotNull(message="申请人不能为空!")
    @Size(max=45, message="申请人长度不能超过45")
    private String applicant;

    @NotNull(message="申请人姓名不能为空!")
    @Size(max=45, message="申请人姓名长度不能超过45")
    private String applicantName;

    @NotNull(message="职工号不能为空!")
    @Size(max=45, message="职工号长度不能超过45")
    private String applicantNo;

    @NotNull(message="deptId不能为空!")
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="applicantMajor不能为空!")
    @Size(max=45, message="applicantMajor长度不能超过45")
    private String applicantMajor;

    @NotNull(message="applicantTel不能为空!")
    @Size(max=45, message="applicantTel长度不能超过45")
    private String applicantTel;

    @NotNull(message="借出不能为空!")
    @Size(max=45, message="借出长度不能超过45")
    private String borrow;

    @NotNull(message="查阅不能为空!")
    @Size(max=45, message="查阅长度不能超过45")
    private String consult;

    @NotNull(message="复制不能为空!")
    @Size(max=45, message="复制长度不能超过45")
    private String copy;

    @NotNull(message="共计不能为空!")
    @Size(max=45, message="共计长度不能超过45")
    private String count;

    @NotNull(message="申请单位负责人审批不能为空!")
    @Size(max=45, message="申请单位负责人审批长度不能超过45")
    private String applicantComment;

    @NotNull(message="借用人接收不能为空!")
    @Size(max=45, message="借用人接收长度不能超过45")
    private String borrowerComment;

    @NotNull(message="档案管理人员移交不能为空!")
    @Size(max=45, message="档案管理人员移交长度不能超过45")
    private String fileTransferComment;

    @NotNull(message="借用人归还不能为空!")
    @Size(max=45, message="借用人归还长度不能超过45")
    private String borrowerReturn;

    @NotNull(message="档案管理人员接收不能为空!")
    @Size(max=45, message="档案管理人员接收长度不能超过45")
    private String fileTransferRecieve;

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

    @NotNull(message="isProcessEnd不能为空!")
    private Boolean processEnd;

    @NotNull(message="创建时间不能为空!")
    private Date gmtCreate;

    @NotNull(message="修改时间不能为空!")
    private Date gmtModified;

    @NotNull(message="processInstanceId不能为空!")
    @Size(max=45, message="processInstanceId长度不能超过45")
    private String processInstanceId;
}