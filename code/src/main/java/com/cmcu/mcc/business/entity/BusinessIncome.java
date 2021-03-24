package com.cmcu.mcc.business.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class BusinessIncome {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="合同库不能为空!")
    @Max(value=999999999, message="合同库必须为数字")
    private Integer contractLibraryId;

    @NotNull(message="incomeConfirmId不能为空!")
    @Max(value=999999999, message="incomeConfirmId必须为数字")
    private Integer incomeConfirmId;

    @NotNull(message="invoiceId不能为空!")
    @Max(value=999999999, message="invoiceId必须为数字")
    private Integer invoiceId;

    @NotNull(message="发票号不能为空!")
    @Size(max=45, message="发票号长度不能超过45")
    private String invoiceNo;

    @NotNull(message="法人单位不能为空!")
    @Size(max=45, message="法人单位长度不能超过45")
    private String legalDept;

    @NotNull(message="合同名称\n不能为空!")
    @Size(max=450, message="合同名称\n长度不能超过450")
    private String contractName;

    @NotNull(message="合同号不能为空!")
    @Size(max=45, message="合同号长度不能超过45")
    private String contractNo;

    @NotNull(message="projectName不能为空!")
    @Size(max=450, message="projectName长度不能超过450")
    private String projectName;

    @NotNull(message="projectNo不能为空!")
    @Size(max=45, message="projectNo长度不能超过45")
    private String projectNo;

    @NotNull(message="recordProjectName不能为空!")
    @Size(max=450, message="recordProjectName长度不能超过450")
    private String recordProjectName;

    @NotNull(message="合同金额不能为空!")
    @Size(max=45, message="合同金额长度不能超过45")
    private String contractMoney;

    @NotNull(message="已领金额不能为空!")
    @Size(max=45, message="已领金额长度不能超过45")
    private String contractIncomeMoney;

    @NotNull(message="管理成本百分比不能为空!")
    @Size(max=45, message="管理成本百分比长度不能超过45")
    private String managePercent;

    @NotNull(message="deptId不能为空!")
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="收入金额不能为空!")
    @Size(max=45, message="收入金额长度不能超过45")
    private String incomeMoney;

    @NotNull(message="收入金额 大写不能为空!")
    @Size(max=45, message="收入金额 大写长度不能超过45")
    private String incomeMoneyMax;

    @NotNull(message="财务确认时间不能为空!")
    @Size(max=45, message="财务确认时间长度不能超过45")
    private String verifyTime;

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

    @NotNull(message="processInstanceId不能为空!")
    @Size(max=45, message="processInstanceId长度不能超过45")
    private String processInstanceId;

    @NotNull(message="isProcessEnd不能为空!")
    private Boolean processEnd;

    @NotNull(message="发票补领不能为空!")
    @Max(value=999999999, message="发票补领必须为数字")
    private Integer invoiceReplenishId;
}