package com.cmcu.mcc.finance.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveFinanceInvoiceCancel {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="invoiceId不能为空!")
    @Max(value=999999999, message="invoiceId必须为数字")
    private Integer invoiceId;

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

    @NotNull(message="applyTime不能为空!")
    @Size(max=45, message="applyTime长度不能超过45")
    private String applyTime;

    @NotNull(message="applyReason不能为空!")
    @Size(max=45, message="applyReason长度不能超过45")
    private String applyReason;

    @NotNull(message="合同名称不能为空!")
    @Size(max=45, message="合同名称长度不能超过45")
    private String contractName;

    @NotNull(message="发票号不能为空!")
    @Size(max=45, message="发票号长度不能超过45")
    private String invoiceNo;

    @NotNull(message="contractNo不能为空!")
    @Size(max=45, message="contractNo长度不能超过45")
    private String contractNo;

    @NotNull(message="invoiceMoney不能为空!")
    @Size(max=45, message="invoiceMoney长度不能超过45")
    private String invoiceMoney;
}