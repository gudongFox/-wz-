package com.cmcu.mcc.finance.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveFinanceTravelExpenseDetail {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="travelExpenseId不能为空!")
    @Max(value=999999999, message="travelExpenseId必须为数字")
    private Integer travelExpenseId;

    @NotNull(message="项目类别不能为空!")
    @Size(max=45, message="项目类别长度不能超过45")
    private String projectType;

    @NotNull(message="报销项目不能为空!")
    @Size(max=45, message="报销项目长度不能超过45")
    private String applyRefundProject;

    @NotNull(message="deptId不能为空!")
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="applicant不能为空!")
    @Size(max=45, message="applicant长度不能超过45")
    private String applicant;

    @NotNull(message="列支人不能为空!")
    @Size(max=45, message="列支人长度不能超过45")
    private String applicantName;

    @NotNull(message="出差天数不能为空!")
    @Size(max=45, message="出差天数长度不能超过45")
    private String travelExpenseDays;

    @NotNull(message="在途时间不能为空!")
    @Size(max=45, message="在途时间长度不能超过45")
    private String onRoadTime;

    @NotNull(message="报销标准不能为空!")
    @Size(max=45, message="报销标准长度不能超过45")
    private String applyStandard;

    @NotNull(message="报销金额不能为空!")
    @Size(max=45, message="报销金额长度不能超过45")
    private String applyMoney;

    @NotNull(message="在途补助不能为空!")
    @Size(max=45, message="在途补助长度不能超过45")
    private String onRoadSubsidy;

    @NotNull(message="金融小计不能为空!")
    @Size(max=45, message="金融小计长度不能超过45")
    private String count;

    @NotNull(message="财务确认不能为空!")
    @Size(max=45, message="财务确认长度不能超过45")
    private String financialConfirmation;

    @NotNull(message="会计科目不能为空!")
    @Size(max=45, message="会计科目长度不能超过45")
    private String accountSubject;

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

    @NotNull(message="住宿费补贴不能为空!")
    private Boolean accommodationAllowance;

    @NotNull(message="出差补助不能为空!")
    private Boolean travelAllowance;

    @NotNull(message="夜间晚补不能为空!")
    private Boolean dinnerAllowance;

    @NotNull(message="工地补贴不能为空!")
    private Boolean siteAllowance;

    private Integer flag = 0;//新建item的标志，在保存时判断，若为1则插入，为0则update
}