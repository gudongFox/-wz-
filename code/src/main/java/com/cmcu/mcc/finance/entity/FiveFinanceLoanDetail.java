package com.cmcu.mcc.finance.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveFinanceLoanDetail {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="loanId不能为空!")
    @Max(value=999999999, message="loanId必须为数字")
    private Integer loanId;

    @NotNull(message="预算类型不能为空!")
    @Size(max=145, message="预算类型长度不能超过145")
    private String budgetType;

    @NotNull(message="budgetId不能为空!")
    @Max(value=999999999, message="budgetId必须为数字")
    private Integer budgetId;

    @NotNull(message="预算号不能为空!")
    @Size(max=45, message="预算号长度不能超过45")
    private String budgetNo;

    @NotNull(message="预算维度不能为空!")
    @Size(max=45, message="预算维度长度不能超过45")
    private String budgetDegree;

    @NotNull(message="控制可用余额不能为空!")
    @Size(max=45, message="控制可用余额长度不能超过45")
    private String controlBalance;

    @NotNull(message="预算可用余额不能为空!")
    @Size(max=45, message="预算可用余额长度不能超过45")
    private String budgetBalance;

    @NotNull(message="原币申请金额不能为空!")
    @Size(max=45, message="原币申请金额长度不能超过45")
    private String applyMoney;

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

    private Integer flag=0;//新建item的标志，在保存时判断，若为1则插入，为0则update
}