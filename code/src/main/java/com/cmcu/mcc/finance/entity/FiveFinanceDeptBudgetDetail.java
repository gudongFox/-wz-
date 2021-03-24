package com.cmcu.mcc.finance.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveFinanceDeptBudgetDetail {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="deptBudgetId不能为空!")
    @Max(value=999999999, message="deptBudgetId必须为数字")
    private Integer deptBudgetId;

    @NotNull(message="formNo不能为空!")
    @Size(max=45, message="formNo长度不能超过45")
    private String formNo;

    @NotNull(message="deptId不能为空!")
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

     @NotNull(message="deptName不能为空!")
    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="seq不能为空!")
    @Size(max=45, message="seq长度不能超过45")
    private String seq;

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

    @NotNull(message="年初余额不能为空!")
    @Size(max=45, message="年初余额长度不能超过45")
    private String lastYearRemainMoney;

    @NotNull(message="本年预算金额不能为空!")
    @Size(max=45, message="本年预算金额长度不能超过45")
    private String budgetMoney;

    @NotNull(message="差旅费不能为空!")
    @Size(max=45, message="差旅费长度不能超过45")
    private String travelMoney;

    @NotNull(message="车辆使用不能为空!")
    @Size(max=45, message="车辆使用长度不能超过45")
    private String carMoney;

    @NotNull(message="materialsMoney不能为空!")
    @Size(max=45, message="materialsMoney长度不能超过45")
    private String materialsMoney;
}