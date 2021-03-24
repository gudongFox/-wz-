package com.cmcu.mcc.finance.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveFinanceReceipt {
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

    @NotNull(message="isProcessEnd不能为空!")
    private Boolean processEnd;

    @NotNull(message="processInstanceId不能为空!")
    @Size(max=45, message="processInstanceId长度不能超过45")
    private String processInstanceId;

    @NotNull(message="法人单位不能为空!")
    @Size(max=45, message="法人单位长度不能超过45")
    private String legalDept;

    @NotNull(message="deptId不能为空!")
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

     @NotNull(message="deptName不能为空!")
    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="applyMan不能为空!")
    @Size(max=45, message="applyMan长度不能超过45")
    private String applyMan;

    @NotNull(message="applyManName不能为空!")
    @Size(max=45, message="applyManName长度不能超过45")
    private String applyManName;

    @NotNull(message="applyManTel不能为空!")
    @Size(max=45, message="applyManTel长度不能超过45")
    private String applyManTel;

    @NotNull(message="申请时间不能为空!")
    @Size(max=45, message="申请时间长度不能超过45")
    private String applyTime;

    @NotNull(message="合同名称不能为空!")
    @Size(max=45, message="合同名称长度不能超过45")
    private String contractName;

    @NotNull(message="合同号不能为空!")
    @Size(max=45, message="合同号长度不能超过45")
    private String contractNo;

    @NotNull(message="客户名称不能为空!")
    @Size(max=45, message="客户名称长度不能超过45")
    private String customerName;

    @NotNull(message="款项性质不能为空!")
    @Size(max=45, message="款项性质长度不能超过45")
    private String moneyType;

    @NotNull(message="开具时间不能为空!")
    @Size(max=45, message="开具时间长度不能超过45")
    private String receiptTime;

    @NotNull(message="开具金额不能为空!")
    @Size(max=45, message="开具金额长度不能超过45")
    private String receiptMoney;

    @NotNull(message="收款号码不能为空!")
    @Size(max=45, message="收款号码长度不能超过45")
    private String receiptNo;

    @NotNull(message="收款理由不能为空!")
    @Size(max=450, message="收款理由长度不能超过450")
    private String receiptRemark;

    @NotNull(message="预计收款时间不能为空!")
    @Size(max=45, message="预计收款时间长度不能超过45")
    private String receiveTime;
}