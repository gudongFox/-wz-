package com.cmcu.mcc.business.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveBusinessContractLibrarySubpackage {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="contractLibraryId不能为空!")
    @Max(value=999999999, message="contractLibraryId必须为数字")
    private Integer contractLibraryId;

    @NotNull(message="主合同号不能为空!")
    @Size(max=45, message="主合同号长度不能超过45")
    private String contractNo;

    @NotNull(message="主合同名称不能为空!")
    @Size(max=45, message="主合同名称长度不能超过45")
    private String contractName;

    @NotNull(message="主合同额不能为空!")
    @Size(max=45, message="主合同额长度不能超过45")
    private String contractMoney;

    @NotNull(message="主合同类型不能为空!")
    @Size(max=45, message="主合同类型长度不能超过45")
    private String contractType;

    @NotNull(message="主合同 项目性质不能为空!")
    @Size(max=45, message="主合同 项目性质长度不能超过45")
    private String projectNature;

    @NotNull(message="分包合同类型不能为空!")
    @Size(max=45, message="分包合同类型长度不能超过45")
    private String subContractType;

    @NotNull(message="分包合同名称不能为空!")
    @Size(max=45, message="分包合同名称长度不能超过45")
    private String subContractName;

    @NotNull(message="分包合同金额不能为空!")
    @Size(max=45, message="分包合同金额长度不能超过45")
    private String subContractMoney;

    @NotNull(message="subContractNo不能为空!")
    @Size(max=45, message="subContractNo长度不能超过45")
    private String subContractNo;

    @NotNull(message="发包方不能为空!")
    @Max(value=999999999, message="发包方必须为数字")
    private Integer deptId;

     @NotNull(message="deptName不能为空!")
    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="分包合同主要情况说明不能为空!")
    @Size(max=450, message="分包合同主要情况说明长度不能超过450")
    private String subContractDesc;

    @NotNull(message="是否有保证金不能为空!")
    private Boolean cashDeposit;

    @NotNull(message="保证金额不能为空!")
    @Size(max=45, message="保证金额长度不能超过45")
    private String cashDepositMoney;

    @NotNull(message="isBackletter不能为空!")
    private Boolean backletter;

    @NotNull(message="保函额不能为空!")
    @Size(max=45, message="保函额长度不能超过45")
    private String backletterMoney;

    @NotNull(message="合同是否已签章不能为空!")
    private Boolean sign;

    @NotNull(message="合同状态不能为空!")
    @Size(max=45, message="合同状态长度不能超过45")
    private String contractStatus;

    @NotNull(message="supplierId不能为空!")
    @Max(value=999999999, message="supplierId必须为数字")
    private Integer supplierId;

    @NotNull(message="供方名称不能为空!")
    @Size(max=45, message="供方名称长度不能超过45")
    private String supplierName;

    @NotNull(message="供方 信用代码不能为空!")
    @Size(max=45, message="供方 信用代码长度不能超过45")
    private String supplierCreditCode;

    @NotNull(message="supplierLinkMan不能为空!")
    @Size(max=45, message="supplierLinkMan长度不能超过45")
    private String supplierLinkMan;

    @NotNull(message="supplierLinkTel不能为空!")
    @Size(max=45, message="supplierLinkTel长度不能超过45")
    private String supplierLinkTel;

    @NotNull(message="开户银行不能为空!")
    @Size(max=145, message="开户银行长度不能超过145")
    private String depositBank;

    @NotNull(message="银行账号不能为空!")
    @Size(max=145, message="银行账号长度不能超过145")
    private String bankAccount;

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

    @NotNull(message="对内分包不能为空!")
    private Boolean inCompany;

    @NotNull(message="对内承接不能为空!")
    @Max(value=999999999, message="对内承接必须为数字")
    private Integer inDeptId;

    @NotNull(message="inDeptName不能为空!")
    @Size(max=45, message="inDeptName长度不能超过45")
    private String inDeptName;

    @NotNull(message="备案不能为空!")
    @Max(value=999999999, message="备案必须为数字")
    private Integer recordId;

    @NotNull(message="分包不能为空!")
    @Max(value=999999999, message="分包必须为数字")
    private Integer subpackageId;

    @NotNull(message="评审级别不能为空!")
    @Size(max=45, message="评审级别长度不能超过45")
    private String reviewLevel;

    @NotNull(message="contractChargeLeader不能为空!")
    @Size(max=45, message="contractChargeLeader长度不能超过45")
    private String contractChargeLeader;

    @NotNull(message="contractChargeLeaderName不能为空!")
    @Size(max=45, message="contractChargeLeaderName长度不能超过45")
    private String contractChargeLeaderName;

    @NotNull(message="reviewUser不能为空!")
    @Size(max=45, message="reviewUser长度不能超过45")
    private String reviewUser;

    @NotNull(message="reviewUserName不能为空!")
    @Size(max=45, message="reviewUserName长度不能超过45")
    private String reviewUserName;

    @NotNull(message="是否为采购合同不能为空!")
    private Boolean purchase;

    @NotNull(message="合同扫描件不能为空!")
    @Size(max=45, message="合同扫描件长度不能超过45")
    private String contractAttachUrl;

    @NotNull(message="税目不能为空!")
    @Size(max=45, message="税目长度不能超过45")
    private String taxType;

    @NotNull(message="税率不能为空!")
    @Size(max=45, message="税率长度不能超过45")
    private String taxNum;

    @NotNull(message="印花税额不能为空!")
    @Size(max=45, message="印花税额长度不能超过45")
    private String stampTaxMoney;

    @NotNull(message="是否开票不能为空!")
    private Boolean openStamp;

    @NotNull(message="合同签订时间不能为空!")
    @Size(max=45, message="合同签订时间长度不能超过45")
    private String signTime;

    @NotNull(message="deptReviewUser不能为空!")
    @Size(max=45, message="deptReviewUser长度不能超过45")
    private String deptReviewUser;

    @NotNull(message="deptReviewUserName不能为空!")
    @Size(max=45, message="deptReviewUserName长度不能超过45")
    private String deptReviewUserName;

    @NotNull(message="deptChargeMen不能为空!")
    @Size(max=45, message="deptChargeMen长度不能超过45")
    private String deptChargeMen;

    @NotNull(message="部门领导会签不能为空!")
    @Size(max=145, message="部门领导会签长度不能超过145")
    private String deptChargeMenName;

    @NotNull(message="插入类型  1 补录不能为空!")
    @Max(value=999999999, message="插入类型  1 补录必须为数字")
    private Integer insertType;

    @NotNull(message="主合同不能为空!")
    @Max(value=999999999, message="主合同必须为数字")
    private Integer mainContractLibraryId;

    @NotNull(message="mainContractLibraryName不能为空!")
    @Size(max=450, message="mainContractLibraryName长度不能超过450")
    private String mainContractLibraryName;

    @NotNull(message="mainContractLibraryNo不能为空!")
    @Size(max=45, message="mainContractLibraryNo长度不能超过45")
    private String mainContractLibraryNo;

    @NotNull(message="分包 采购评审 补充合同关联不能为空!")
    @Size(max=145, message="分包 采购评审 补充合同关联长度不能超过145")
    private String reviewIds;
}