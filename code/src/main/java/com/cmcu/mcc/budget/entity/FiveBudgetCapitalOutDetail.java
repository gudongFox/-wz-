package com.cmcu.mcc.budget.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveBudgetCapitalOutDetail {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="capitalOutId不能为空!")
    @Max(value=999999999, message="capitalOutId必须为数字")
    private Integer capitalOutId;

    @NotNull(message="parentId不能为空!")
    @Max(value=999999999, message="parentId必须为数字")
    private Integer parentId;

    @NotNull(message="类型名称不能为空!")
    @Size(max=45, message="类型名称长度不能超过45")
    private String typeName;

    @NotNull(message="deptId不能为空!")
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

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

    @NotNull(message="办公自动化不能为空!")
    @Size(max=45, message="办公自动化长度不能超过45")
    private String oaMoney;

    @NotNull(message="办公家具不能为空!")
    @Size(max=45, message="办公家具长度不能超过45")
    private String furnitureMoney;

    @NotNull(message="车辆预算不能为空!")
    @Size(max=45, message="车辆预算长度不能超过45")
    private String carMoney;

    @NotNull(message="软件预算不能为空!")
    @Size(max=45, message="软件预算长度不能超过45")
    private String softMoney;

    @NotNull(message="股权投资不能为空!")
    @Size(max=45, message="股权投资长度不能超过45")
    private String stockMoney;

    @NotNull(message="上缴房租预算不能为空!")
    @Size(max=45, message="上缴房租预算长度不能超过45")
    private String rentMoney;

    @NotNull(message="去年预算 不能为空!")
    @Size(max=45, message="去年预算 长度不能超过45")
    private String lastYearMoney;

    @NotNull(message="去年预算完成不能为空!")
    @Size(max=45, message="去年预算完成长度不能超过45")
    private String lastYearSuccess;
}