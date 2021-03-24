package com.cmcu.mcc.finance.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveFinanceInvoice {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="incomeConfirmIds不能为空!")
    @Size(max=45, message="incomeConfirmIds长度不能超过45")
    private String incomeConfirmIds;

    @NotNull(message="incomeConfirmId不能为空!")
    @Max(value=999999999, message="incomeConfirmId必须为数字")
    private Integer incomeConfirmId;

    @NotNull(message="发票报废不能为空!")
    @Max(value=999999999, message="发票报废必须为数字")
    private Integer invoiceCancelId;

    @NotNull(message="发票应收款催款不能为空!")
    @Size(max=45, message="发票应收款催款长度不能超过45")
    private String invoiceCollectionIds;

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

    @NotNull(message="用户姓名不能为空!")
    @Size(max=45, message="用户姓名长度不能超过45")
    private String applyManName;

    @NotNull(message="用户电话不能为空!")
    @Size(max=45, message="用户电话长度不能超过45")
    private String userTel;

    @NotNull(message="发票开具日期不能为空!")
    @Size(max=45, message="发票开具日期长度不能超过45")
    private String invoiceTime;

    @NotNull(message="合同单位名称不能为空!")
    @Size(max=45, message="合同单位名称长度不能超过45")
    private String contractCustomer;

    @NotNull(message="本地发票类型不能为空!")
    @Size(max=45, message="本地发票类型长度不能超过45")
    private String localInvoiceType;

    @NotNull(message="异地发票类型不能为空!")
    @Size(max=45, message="异地发票类型长度不能超过45")
    private String otherInvoiceType;

    @NotNull(message="发票抬头名称不能为空!")
    @Size(max=45, message="发票抬头名称长度不能超过45")
    private String invoiceHeadName;

    @NotNull(message="纳税人识别号不能为空!")
    @Size(max=45, message="纳税人识别号长度不能超过45")
    private String taxNo;

    @NotNull(message="客户地址不能为空!")
    @Size(max=45, message="客户地址长度不能超过45")
    private String customerAdderss;

    @NotNull(message="客户电话不能为空!")
    @Size(max=45, message="客户电话长度不能超过45")
    private String customerTel;

    @NotNull(message="客户单位开票代码不能为空!")
    @Size(max=45, message="客户单位开票代码长度不能超过45")
    private String customerTaxCode;

    @NotNull(message="开户银行不能为空!")
    @Size(max=45, message="开户银行长度不能超过45")
    private String bank;

    @NotNull(message="银行账号不能为空!")
    @Size(max=45, message="银行账号长度不能超过45")
    private String bankAccount;

    @NotNull(message="预计到款时间不能为空!")
    @Size(max=45, message="预计到款时间长度不能超过45")
    private String receiveTime;

    @NotNull(message="催收责任人不能为空!")
    @Size(max=45, message="催收责任人长度不能超过45")
    private String remindReceiveMan;

    @NotNull(message="remindReceiveManName不能为空!")
    @Size(max=45, message="remindReceiveManName长度不能超过45")
    private String remindReceiveManName;

    @NotNull(message="deptChargeMan不能为空!")
    @Size(max=45, message="deptChargeMan长度不能超过45")
    private String deptChargeMan;

    @NotNull(message="deptChargeManName不能为空!")
    @Size(max=45, message="deptChargeManName长度不能超过45")
    private String deptChargeManName;

    @NotNull(message="财务确认时间不能为空!")
    @Size(max=45, message="财务确认时间长度不能超过45")
    private String financeConfirmTime;

    @NotNull(message="发票备注内容不能为空!")
    @Size(max=450, message="发票备注内容长度不能超过450")
    private String invoiceRemark;

    @NotNull(message="contractLibraryId不能为空!")
    @Max(value=999999999, message="contractLibraryId必须为数字")
    private Integer contractLibraryId;

    @NotNull(message="合同号不能为空!")
    @Size(max=45, message="合同号长度不能超过45")
    private String contractNo;

    @NotNull(message="合同名称不能为空!")
    @Size(max=450, message="合同名称长度不能超过450")
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

    @NotNull(message="发票收入金额不能为空!")
    @Size(max=45, message="发票收入金额长度不能超过45")
    private String invoiceGetMoney;

    @NotNull(message="发票补录不能为空!")
    @Max(value=999999999, message="发票补录必须为数字")
    private Integer incomeId;

    @NotNull(message="发票正在收入金额不能为空!")
    @Size(max=45, message="发票正在收入金额长度不能超过45")
    private String invoiceGetMoneyIng;
}