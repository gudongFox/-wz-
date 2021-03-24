package com.cmcu.mcc.finance.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveFinanceTransferAccounts {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="formNo不能为空!")
    @Size(max=45, message="formNo长度不能超过45")
    private String formNo;

    @NotNull(message="账户名称不能为空!")
    @Size(max=45, message="账户名称长度不能超过45")
    private String accountName;

    @NotNull(message="转出行名称不能为空!")
    @Size(max=45, message="转出行名称长度不能超过45")
    private String outBankName;

    @NotNull(message="转出行账号不能为空!")
    @Size(max=45, message="转出行账号长度不能超过45")
    private String outBankAccount;

    @NotNull(message="转入行名称不能为空!")
    @Size(max=45, message="转入行名称长度不能超过45")
    private String inBankName;

    @NotNull(message="转入行账号不能为空!")
    @Size(max=45, message="转入行账号长度不能超过45")
    private String inBankAccount;

    @NotNull(message="applicant不能为空!")
    @Size(max=45, message="applicant长度不能超过45")
    private String applicant;

    @NotNull(message="申请人不能为空!")
    @Size(max=45, message="申请人长度不能超过45")
    private String applicantName;

    @NotNull(message="申请日期不能为空!")
    @Size(max=45, message="申请日期长度不能超过45")
    private String applicantTime;

    @NotNull(message="单据号不能为空!")
    @Size(max=45, message="单据号长度不能超过45")
    private String accountNumber;

    @NotNull(message="deptId不能为空!")
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

    @NotNull(message="申请部门不能为空!")
    @Size(max=45, message="申请部门长度不能超过45")
    private String deptName;

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

    @NotNull(message="isProcessEnd不能为空!")
    private Boolean processEnd;

    @NotNull(message="创建时间不能为空!")
    private Date gmtCreate;

    @NotNull(message="修改时间不能为空!")
    private Date gmtModified;

    @NotNull(message="processInstanceId不能为空!")
    @Size(max=45, message="processInstanceId长度不能超过45")
    private String processInstanceId;

    @NotNull(message="receive不能为空!")
    @Size(max=45, message="receive长度不能超过45")
    private String receive;

    @NotNull(message="收款人不能为空!")
    @Size(max=45, message="收款人长度不能超过45")
    private String receiveName;

    @NotNull(message="收款单位不能为空!")
    @Size(max=45, message="收款单位长度不能超过45")
    private String receiveDeptName;

    @NotNull(message="title不能为空!")
    @Size(max=45, message="title长度不能超过45")
    private String title;

    @NotNull(message="项目id不能为空!")
    @Max(value=999999999, message="项目id必须为数字")
    private Integer projectId;

    @NotNull(message="项目名称不能为空!")
    @Size(max=45, message="项目名称长度不能超过45")
    private String projectName;

    @NotNull(message="businessManager不能为空!")
    @Size(max=45, message="businessManager长度不能超过45")
    private String businessManager;

    @NotNull(message="项目经理不能为空!")
    @Size(max=45, message="项目经理长度不能超过45")
    private String businessManagerName;

    @NotNull(message="总额不能为空!")
    @Size(max=45, message="总额长度不能超过45")
    private String totalMoney;

    @NotNull(message="总报销额不能为空!")
    @Size(max=45, message="总报销额长度不能超过45")
    private String totalApplyMoney;

    @NotNull(message="报销状态不能为空!")
    @Size(max=45, message="报销状态长度不能超过45")
    private String state;
}