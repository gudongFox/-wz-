package com.cmcu.mcc.finance.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveFinanceTransferAccountsDetail {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="transferAccountsId不能为空!")
    @Max(value=999999999, message="transferAccountsId必须为数字")
    private Integer transferAccountsId;

    @NotNull(message="预算类型不能为空!")
    @Size(max=145, message="预算类型长度不能超过145")
    private String budgetType;

    @NotNull(message="budgetId不能为空!")
    @Size(max=45, message="budgetId长度不能超过45")
    private String budgetId;

    @NotNull(message="费用计划类型不能为空!")
    @Size(max=45, message="费用计划类型长度不能超过45")
    private String chargePlan;

    @NotNull(message="费用项目不能为空!")
    @Size(max=45, message="费用项目长度不能超过45")
    private String chargeProject;

    @NotNull(message="事由不能为空!")
    @Size(max=45, message="事由长度不能超过45")
    private String item;

    @NotNull(message="申请金额不能为空!")
    @Size(max=45, message="申请金额长度不能超过45")
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

    @NotNull(message="抵扣状态不能为空!")
    @Size(max=45, message="抵扣状态长度不能超过45")
    private String deduction;

    @NotNull(message="冲销状态不能为空!")
    @Size(max=45, message="冲销状态长度不能超过45")
    private String chargeAgainst;
}