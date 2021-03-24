package com.cmcu.mcc.finance.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveFinanceIncomeConfirm {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="收入管理不能为空!")
    @Max(value=999999999, message="收入管理必须为数字")
    private Integer incomeId;

    @NotNull(message="contractLibraryIds不能为空!")
    @Size(max=45, message="contractLibraryIds长度不能超过45")
    private String contractLibraryIds;

    @NotNull(message="invoiceIds不能为空!")
    @Size(max=45, message="invoiceIds长度不能超过45")
    private String invoiceIds;

    @NotNull(message="合同收费不能为空!")
    @Size(max=45, message="合同收费长度不能超过45")
    private String incomeIds;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="formNo不能为空!")
    @Size(max=45, message="formNo长度不能超过45")
    private String formNo;

    @NotNull(message="收入类型不能为空!")
    @Size(max=45, message="收入类型长度不能超过45")
    private String type;

    @NotNull(message="来源账户不能为空!")
    @Size(max=45, message="来源账户长度不能超过45")
    private String sourceAccount;

    @NotNull(message="接收账户不能为空!")
    @Size(max=45, message="接收账户长度不能超过45")
    private String targetAccount;

    @NotNull(message="收入金额不能为空!")
    @Size(max=45, message="收入金额长度不能超过45")
    private String money;

    @NotNull(message="金额大写不能为空!")
    @Size(max=45, message="金额大写长度不能超过45")
    private String moneyMax;

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

    @NotNull(message="收款时间不能为空!")
    @Size(max=45, message="收款时间长度不能超过45")
    private String incomeTime;

    @NotNull(message="deptId不能为空!")
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="设计目标不能为空!")
    @Size(max=45, message="设计目标长度不能超过45")
    private String designTargetMoney;

    @NotNull(message="设计完成金额不能为空!")
    @Size(max=45, message="设计完成金额长度不能超过45")
    private String designSucessMoney;

    @NotNull(message="设计咨询(超比)不能为空!")
    @Size(max=45, message="设计咨询(超比)长度不能超过45")
    private String designAskRate;

    @NotNull(message="本年管控比不能为空!")
    @Size(max=45, message="本年管控比长度不能超过45")
    private String managerRate;

    @NotNull(message="总承包目标不能为空!")
    @Size(max=45, message="总承包目标长度不能超过45")
    private String totalTargetMoney;

    @NotNull(message="总承包完成额不能为空!")
    @Size(max=45, message="总承包完成额长度不能超过45")
    private String totalSucessMoney;

    @NotNull(message="总承包(超比)不能为空!")
    @Size(max=45, message="总承包(超比)长度不能超过45")
    private String totalRate;

    @NotNull(message="incomeRemark不能为空!")
    @Size(max=450, message="incomeRemark长度不能超过450")
    private String incomeRemark;

    @NotNull(message="是否退款不能为空!")
    private Boolean refund;
}