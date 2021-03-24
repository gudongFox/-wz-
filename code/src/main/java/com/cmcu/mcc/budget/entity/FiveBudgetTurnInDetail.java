package com.cmcu.mcc.budget.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveBudgetTurnInDetail {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="turnInId不能为空!")
    @Max(value=999999999, message="turnInId必须为数字")
    private Integer turnInId;

    @NotNull(message="deptId不能为空!")
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

     @NotNull(message="deptName不能为空!")
    @Size(max=145, message="deptName长度不能超过145")
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

    @NotNull(message="创建时间不能为空!")
    private Date gmtCreate;

    @NotNull(message="修改时间不能为空!")
    private Date gmtModified;

    @NotNull(message="上缴利润不能为空!")
    @Size(max=45, message="上缴利润长度不能超过45")
    private String turnProfits;

    @NotNull(message="发展基金不能为空!")
    @Size(max=45, message="发展基金长度不能超过45")
    private String developmentFund;

    @NotNull(message="seq不能为空!")
    @Max(value=999999999, message="seq必须为数字")
    private Integer seq;

    @NotNull(message="parentId不能为空!")
    @Max(value=999999999, message="parentId必须为数字")
    private Integer parentId;

    @NotNull(message="类型名称不能为空!")
    @Size(max=45, message="类型名称长度不能超过45")
    private String typeName;

    @NotNull(message="预算金额不能为空!")
    @Size(max=45, message="预算金额长度不能超过45")
    private String budgetMoney;

    @NotNull(message="预算占比不能为空!")
    @Size(max=45, message="预算占比长度不能超过45")
    private String budgetProportion;

    @NotNull(message="去年预算 不能为空!")
    @Size(max=45, message="去年预算 长度不能超过45")
    private String lastYearMoney;

    @NotNull(message="去年预算完成不能为空!")
    @Size(max=45, message="去年预算完成长度不能超过45")
    private String lastYearSuccess;
}