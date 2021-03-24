package com.cmcu.mcc.budget.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveBudgetIndependentDetail {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="budgetIndependentId不能为空!")
    @Max(value=999999999, message="budgetIndependentId必须为数字")
    private Integer budgetIndependentId;

    @NotNull(message="parentId不能为空!")
    @Max(value=999999999, message="parentId必须为数字")
    private Integer parentId;

    @NotNull(message="类型名称不能为空!")
    @Size(max=45, message="类型名称长度不能超过45")
    private String typeName;

    @NotNull(message="采购包编号不能为空!")
    @Size(max=45, message="采购包编号长度不能超过45")
    private String purchaseNo;

    @NotNull(message="预算金额不能为空!")
    @Size(max=45, message="预算金额长度不能超过45")
    private String budgetMoney;

    @NotNull(message="预算占比不能为空!")
    @Size(max=45, message="预算占比长度不能超过45")
    private String budgetProportion;

    @NotNull(message="seq不能为空!")
    @Max(value=999999999, message="seq必须为数字")
    private Integer seq;

    @NotNull(message="remark不能为空!")
    @Size(max=45, message="remark长度不能超过45")
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

    @NotNull(message="外部关联键不能为空!")
    @Max(value=999999999, message="外部关联键必须为数字")
    private Integer foreignKey;

    @NotNull(message="去年预算 不能为空!")
    @Size(max=45, message="去年预算 长度不能超过45")
    private String lastYearMoney;

    @NotNull(message="去年预算完成不能为空!")
    @Size(max=45, message="去年预算完成长度不能超过45")
    private String lastYearSuccess;

    @NotNull(message="isPublicBudget不能为空!")
    private Boolean publicBudget;

    @NotNull(message="可用预算部门不能为空!")
    @Size(max=450, message="可用预算部门长度不能超过450")
    private String publicDeptIds;
}