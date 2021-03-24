package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveAssetAllot {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="formNo不能为空!")
    @Size(max=45, message="formNo长度不能超过45")
    private String formNo;

    @NotNull(message="applicant不能为空!")
    @Size(max=45, message="applicant长度不能超过45")
    private String applicant;

    @NotNull(message="申请人不能为空!")
    @Size(max=45, message="申请人长度不能超过45")
    private String applicantName;

    @NotNull(message="deptId不能为空!")
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="入账时间不能为空!")
    @Size(max=45, message="入账时间长度不能超过45")
    private String enterTime;

    @NotNull(message="固定资产编码不能为空!")
    @Size(max=45, message="固定资产编码长度不能超过45")
    private String assetCode;

    @NotNull(message="固定资产名称不能为空!")
    @Size(max=45, message="固定资产名称长度不能超过45")
    private String assetName;

    @NotNull(message="规格型号不能为空!")
    @Size(max=45, message="规格型号长度不能超过45")
    private String model;

    @NotNull(message="原使用单位不能为空!")
    @Size(max=45, message="原使用单位长度不能超过45")
    private String useUnit;

    @NotNull(message="原使用单位不能为空!")
    @Size(max=45, message="原使用单位长度不能超过45")
    private String useMan;

    @NotNull(message="原保管人不能为空!")
    @Size(max=45, message="原保管人长度不能超过45")
    private String useManName;

    @NotNull(message="转入单位不能为空!")
    @Size(max=45, message="转入单位长度不能超过45")
    private String transferUnit;

    @NotNull(message="reserveMan不能为空!")
    @Size(max=45, message="reserveMan长度不能超过45")
    private String reserveMan;

    @NotNull(message="现保管人不能为空!")
    @Size(max=45, message="现保管人长度不能超过45")
    private String reserveManName;

    @NotNull(message="receive不能为空!")
    @Size(max=45, message="receive长度不能超过45")
    private String receive;

    @NotNull(message="接收人(发起人选取)不能为空!")
    @Size(max=45, message="接收人(发起人选取)长度不能超过45")
    private String receiveName;

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

    @NotNull(message="原使用单位不能为空!")
    @Max(value=999999999, message="原使用单位必须为数字")
    private Integer useUnitId;

    @NotNull(message="转入单位不能为空!")
    @Max(value=999999999, message="转入单位必须为数字")
    private Integer transferUnitId;
}