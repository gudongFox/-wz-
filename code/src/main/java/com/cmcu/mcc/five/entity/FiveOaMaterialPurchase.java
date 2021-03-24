package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaMaterialPurchase {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="formNo不能为空!")
    @Size(max=45, message="formNo长度不能超过45")
    private String formNo;

    @NotNull(message="deptId不能为空!")
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="申请人不能为空!")
    @Size(max=45, message="申请人长度不能超过45")
    private String applicantMan;

    @NotNull(message="applicantManName不能为空!")
    @Size(max=45, message="applicantManName长度不能超过45")
    private String applicantManName;

    @NotNull(message="职工号不能为空!")
    @Size(max=45, message="职工号长度不能超过45")
    private String applicantNo;

    @NotNull(message="电话不能为空!")
    @Size(max=45, message="电话长度不能超过45")
    private String applicantTel;

    @NotNull(message="单位负责人不能为空!")
    @Size(max=45, message="单位负责人长度不能超过45")
    private String companyCharge;

    @NotNull(message="companyChargeName不能为空!")
    @Size(max=45, message="companyChargeName长度不能超过45")
    private String companyChargeName;

    @NotNull(message="档案图书室不能为空!")
    @Size(max=45, message="档案图书室长度不能超过45")
    private String library;

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
}