package com.cmcu.mcc.finance.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveFinanceInvoiceCollection {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="invoiceId不能为空!")
    @Max(value=999999999, message="invoiceId必须为数字")
    private Integer invoiceId;

    @NotNull(message="invoiceNo不能为空!")
    @Size(max=45, message="invoiceNo长度不能超过45")
    private String invoiceNo;

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

    @NotNull(message="合同号不能为空!")
    @Size(max=45, message="合同号长度不能超过45")
    private String contractNo;

    @NotNull(message="合同名称不能为空!")
    @Size(max=45, message="合同名称长度不能超过45")
    private String contractName;

    @NotNull(message="合同额不能为空!")
    @Size(max=45, message="合同额长度不能超过45")
    private String contractMoney;

    @NotNull(message="本次开票金额不能为空!")
    @Size(max=45, message="本次开票金额长度不能超过45")
    private String invoiceMoney;

    @NotNull(message="发票收入金额不能为空!")
    @Size(max=45, message="发票收入金额长度不能超过45")
    private String invoiceGetMoney;

    @NotNull(message="发票正在收入金额不能为空!")
    @Size(max=45, message="发票正在收入金额长度不能超过45")
    private String invoiceGetMoneyIng;

    @NotNull(message="催收责任人不能为空!")
    @Size(max=45, message="催收责任人长度不能超过45")
    private String remindReceiveMan;

    @NotNull(message="remindReceiveManName不能为空!")
    @Size(max=45, message="remindReceiveManName长度不能超过45")
    private String remindReceiveManName;

    @NotNull(message="deptId不能为空!")
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

     @NotNull(message="deptName不能为空!")
    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;
}