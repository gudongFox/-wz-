package com.cmcu.mcc.finance.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveFinanceReimburse {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="formNo不能为空!")
    @Size(max=45, message="formNo长度不能超过45")
    private String formNo;

    @NotNull(message="用款事项不能为空!")
    @Size(max=45, message="用款事项长度不能超过45")
    private String moneyItem;

    @NotNull(message="项目类型不能为空!")
    @Size(max=45, message="项目类型长度不能超过45")
    private String projectType;

    @NotNull(message="项目不能为空!")
    @Size(max=145, message="项目长度不能超过145")
    private String projectName;

    @NotNull(message="抵扣后金额是否大于报销金额不能为空!")
    private Boolean greaterThan;

    @NotNull(message="单据号不能为空!")
    @Size(max=45, message="单据号长度不能超过45")
    private String receiptsNumber;

    @NotNull(message="applicant不能为空!")
    @Size(max=45, message="applicant长度不能超过45")
    private String applicant;

    @NotNull(message="报销人不能为空!")
    @Size(max=45, message="报销人长度不能超过45")
    private String applicantName;

    @NotNull(message="deptId不能为空!")
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

    @NotNull(message="报销部门不能为空!")
    @Size(max=145, message="报销部门长度不能超过145")
    private String deptName;

    @NotNull(message="报销时间不能为空!")
    @Size(max=45, message="报销时间长度不能超过45")
    private String applicantTime;

    @NotNull(message="找账户名称不能为空!")
    @Size(max=45, message="找账户名称长度不能超过45")
    private String accountName;

    @NotNull(message="开户姓名不能为空!")
    @Size(max=45, message="开户姓名长度不能超过45")
    private String bankName;

    @NotNull(message="银行账号不能为空!")
    @Size(max=45, message="银行账号长度不能超过45")
    private String bankAccount;

    @NotNull(message="确认合计不能为空!")
    @Max(value=999999999, message="确认合计必须为数字")
    private Double count;

    @NotNull(message="receive不能为空!")
    @Size(max=45, message="receive长度不能超过45")
    private String receive;

    @NotNull(message="收款人不能为空!")
    @Size(max=45, message="收款人长度不能超过45")
    private String receiveName;

    @NotNull(message="receiveDeptId不能为空!")
    @Max(value=999999999, message="receiveDeptId必须为数字")
    private Integer receiveDeptId;

    @NotNull(message="收款部门不能为空!")
    @Size(max=45, message="收款部门长度不能超过45")
    private String receiveDeptName;

    @NotNull(message="备注不能为空!")
    @Size(max=450, message="备注长度不能超过450")
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

    @NotNull(message="收款银行不能为空!")
    @Size(max=145, message="收款银行长度不能超过145")
    private String receiveBank;

    @NotNull(message="收款账户不能为空!")
    @Size(max=45, message="收款账户长度不能超过45")
    private String receiveAccount;

    @NotNull(message="projectId不能为空!")
    @Max(value=999999999, message="projectId必须为数字")
    private Integer projectId;

    @NotNull(message="businessManager不能为空!")
    @Size(max=45, message="businessManager长度不能超过45")
    private String businessManager;

    @NotNull(message="businessManagerName不能为空!")
    @Size(max=45, message="businessManagerName长度不能超过45")
    private String businessManagerName;

    @NotNull(message="scientific不能为空!")
    @Size(max=45, message="scientific长度不能超过45")
    private String scientific;

    @NotNull(message="title不能为空!")
    @Size(max=45, message="title长度不能超过45")
    private String title;

    @NotNull(message="是否特批不能为空!")
    private Boolean extraApprove;
}