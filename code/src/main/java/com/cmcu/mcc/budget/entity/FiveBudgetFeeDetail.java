package com.cmcu.mcc.budget.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveBudgetFeeDetail {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="feeId不能为空!")
    @Max(value=999999999, message="feeId必须为数字")
    private Integer feeId;

    @NotNull(message="deptId不能为空!")
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

     @NotNull(message="deptName不能为空!")
    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="parentId不能为空!")
    @Max(value=999999999, message="parentId必须为数字")
    private Integer parentId;

    @NotNull(message="类型名称不能为空!")
    @Size(max=45, message="类型名称长度不能超过45")
    private String typeName;

    @NotNull(message="勘察设计、咨询收费不能为空!")
    @Size(max=45, message="勘察设计、咨询收费长度不能超过45")
    private String designFee;

    @NotNull(message="工程承包收费不能为空!")
    @Size(max=45, message="工程承包收费长度不能超过45")
    private String projectFee;

    @NotNull(message="其他收费不能为空!")
    @Size(max=45, message="其他收费长度不能超过45")
    private String otherFee;

    @NotNull(message="预计资金不能为空!")
    @Size(max=45, message="预计资金长度不能超过45")
    private String budgetMoney;

    @NotNull(message="预算占比不能为空!")
    @Size(max=45, message="预算占比长度不能超过45")
    private String budgetProportion;

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

    @NotNull(message="seq不能为空!")
    @Max(value=999999999, message="seq必须为数字")
    private Integer seq;

    @NotNull(message="去年预算 不能为空!")
    @Size(max=45, message="去年预算 长度不能超过45")
    private String lastYearMoney;

    @NotNull(message="去年预算完成不能为空!")
    @Size(max=45, message="去年预算完成长度不能超过45")
    private String lastYearSuccess;
}