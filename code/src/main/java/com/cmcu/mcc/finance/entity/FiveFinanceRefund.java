package com.cmcu.mcc.finance.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveFinanceRefund {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="formNo不能为空!")
    @Size(max=45, message="formNo长度不能超过45")
    private String formNo;

    @NotNull(message="单据号不能为空!")
    @Size(max=45, message="单据号长度不能超过45")
    private String receiptsNumber;

    @NotNull(message="单据状态不能为空!")
    @Size(max=45, message="单据状态长度不能超过45")
    private String receiptsState;

    @NotNull(message="deptId不能为空!")
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

    @NotNull(message="部门不能为空!")
    @Size(max=145, message="部门长度不能超过145")
    private String deptName;

    @NotNull(message="applicant不能为空!")
    @Size(max=45, message="applicant长度不能超过45")
    private String applicant;

    @NotNull(message="还款人不能为空!")
    @Size(max=45, message="还款人长度不能超过45")
    private String applicantName;

    @NotNull(message="还款时间不能为空!")
    @Size(max=45, message="还款时间长度不能超过45")
    private String applicantTime;

    @NotNull(message="单位不能为空!")
    @Size(max=45, message="单位长度不能超过45")
    private String unit;

    @NotNull(message="项目不能为空!")
    @Max(value=999999999, message="项目必须为数字")
    private Integer projectId;

    @NotNull(message="projectName不能为空!")
    @Size(max=45, message="projectName长度不能超过45")
    private String projectName;

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

    @NotNull(message="费用报销 不能为空!")
    @Max(value=999999999, message="费用报销 必须为数字")
    private Integer reimburseId;

    @NotNull(message="付款账号名称不能为空!")
    @Size(max=45, message="付款账号名称长度不能超过45")
    private String payName;

    @NotNull(message="payId不能为空!")
    @Size(max=45, message="payId长度不能超过45")
    private String payId;

    @NotNull(message="付款单位不能为空!")
    @Size(max=45, message="付款单位长度不能超过45")
    private String payDept;

    @NotNull(message="开户银行不能为空!")
    @Size(max=45, message="开户银行长度不能超过45")
    private String payBank;

    @NotNull(message="卡号不能为空!")
    @Size(max=45, message="卡号长度不能超过45")
    private String payAccount;

    @NotNull(message="收款账户不能为空!")
    @Size(max=45, message="收款账户长度不能超过45")
    private String receiveName;

    @NotNull(message="receiveId不能为空!")
    @Max(value=999999999, message="receiveId必须为数字")
    private Integer receiveId;

    @NotNull(message="收款单位不能为空!")
    @Size(max=45, message="收款单位长度不能超过45")
    private String receiveDept;

    @NotNull(message="receiveBank不能为空!")
    @Size(max=45, message="receiveBank长度不能超过45")
    private String receiveBank;

    @NotNull(message="卡号不能为空!")
    @Size(max=45, message="卡号长度不能超过45")
    private String receiveAccount;

    @NotNull(message="还款总额不能为空!")
    @Size(max=45, message="还款总额长度不能超过45")
    private String totalRefundMoney;

    @NotNull(message="标题不能为空!")
    @Size(max=45, message="标题长度不能超过45")
    private String title;

    @NotNull(message="差旅费报销不能为空!")
    @Max(value=999999999, message="差旅费报销必须为数字")
    private Integer travelId;

    @NotNull(message="应还款金额(补充还款)不能为空!")
    @Size(max=45, message="应还款金额(补充还款)长度不能超过45")
    private String shouldRefundMoney;
}