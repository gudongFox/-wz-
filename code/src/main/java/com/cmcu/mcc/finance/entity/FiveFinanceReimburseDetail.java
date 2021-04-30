package com.cmcu.mcc.finance.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveFinanceReimburseDetail {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="reimburseId不能为空!")
    @Max(value=999999999, message="reimburseId必须为数字")
    private Integer reimburseId;

    @NotNull(message="项目类型不能为空!")
    @Size(max=45, message="项目类型长度不能超过45")
    private String projectType;

    @NotNull(message="费用项目不能为空!")
    @Size(max=45, message="费用项目长度不能超过45")
    private String costProject;

    @NotNull(message="applicant不能为空!")
    @Size(max=45, message="applicant长度不能超过45")
    private String applicant;

    @NotNull(message="列支人不能为空!")
    @Size(max=45, message="列支人长度不能超过45")
    private String applicantName;

    @NotNull(message="deptId不能为空!")
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="项目不能为空!")
    @Size(max=45, message="项目长度不能超过45")
    private String project;

    @NotNull(message="申请报销金额不能为空!")
    @Size(max=45, message="申请报销金额长度不能超过45")
    private String applyMoney;

    @NotNull(message="财务确认金额不能为空!")
    @Size(max=45, message="财务确认金额长度不能超过45")
    private String confirmMoney;

    @NotNull(message="会计科目不能为空!")
    @Size(max=45, message="会计科目长度不能超过45")
    private String accountSubject;

    @NotNull(message="统计不能为空!")
    @Size(max=45, message="统计长度不能超过45")
    private String count;

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

    @NotNull(message="创建时间不能为空!")
    private Date gmtCreate;

    @NotNull(message="修改时间不能为空!")
    private Date gmtModified;

    @NotNull(message="budgetType不能为空!")
    @Size(max=145, message="budgetType长度不能超过145")
    private String budgetType;

    @NotNull(message="budgetId不能为空!")
    @Max(value=999999999, message="budgetId必须为数字")
    private Integer budgetId;

    @NotNull(message="预算号不能为空!")
    @Size(max=45, message="预算号长度不能超过45")
    private String budgetNo;

    @NotNull(message="预算可用余额不能为空!")
    @Size(max=45, message="预算可用余额长度不能超过45")
    private String budgetBalance;

}