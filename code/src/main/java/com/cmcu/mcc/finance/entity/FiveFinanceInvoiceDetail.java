package com.cmcu.mcc.finance.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveFinanceInvoiceDetail {
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

    @NotNull(message="contractLibraryId不能为空!")
    @Max(value=999999999, message="contractLibraryId必须为数字")
    private Integer contractLibraryId;

    @NotNull(message="合同号不能为空!")
    @Size(max=45, message="合同号长度不能超过45")
    private String contractNo;

    @NotNull(message="合同名称不能为空!")
    @Size(max=45, message="合同名称长度不能超过45")
    private String contractName;

    @NotNull(message="合同额不能为空!")
    @Size(max=45, message="合同额长度不能超过45")
    private String contractMoney;

    @NotNull(message="合同已开票额不能为空!")
    @Size(max=45, message="合同已开票额长度不能超过45")
    private String contractGetInvoice;

    @NotNull(message="收款性质不能为空!")
    @Size(max=45, message="收款性质长度不能超过45")
    private String receiveType;

    @NotNull(message="本次开票金额不能为空!")
    @Size(max=45, message="本次开票金额长度不能超过45")
    private String invoiceMoney;

    @NotNull(message="冲抵预收款不能为空!")
    @Size(max=45, message="冲抵预收款长度不能超过45")
    private String chargeAgainstPreMoney;

    @NotNull(message="销售额不能为空!")
    @Size(max=45, message="销售额长度不能超过45")
    private String saleMoney;

    @NotNull(message="销项税额不能为空!")
    @Size(max=45, message="销项税额长度不能超过45")
    private String outPutTaxMoney;

    @NotNull(message="发票号不能为空!")
    @Size(max=45, message="发票号长度不能超过45")
    private String invoiceNo;

    @NotNull(message="承汇款号码不能为空!")
    @Size(max=45, message="承汇款号码长度不能超过45")
    private String promiseRemittanceNo;

    @NotNull(message="票据金额不能为空!")
    @Size(max=45, message="票据金额长度不能超过45")
    private String noteMoney;

    @NotNull(message="收款阶段不能为空!")
    @Size(max=45, message="收款阶段长度不能超过45")
    private String incomeStage;

    @NotNull(message="收款子项不能为空!")
    @Size(max=45, message="收款子项长度不能超过45")
    private String incomeBuild;
}