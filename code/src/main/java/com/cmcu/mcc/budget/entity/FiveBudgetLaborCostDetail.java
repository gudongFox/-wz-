package com.cmcu.mcc.budget.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveBudgetLaborCostDetail {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="laborCostId不能为空!")
    @Max(value=999999999, message="laborCostId必须为数字")
    private Integer laborCostId;

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

    @NotNull(message="工资不能为空!")
    @Size(max=45, message="工资长度不能超过45")
    private String salaryMoney;

    @NotNull(message="奖金不能为空!")
    @Size(max=45, message="奖金长度不能超过45")
    private String bonusMoney;

    @NotNull(message="防暑降温不能为空!")
    @Size(max=45, message="防暑降温长度不能超过45")
    private String coolingMoney;

    @NotNull(message="车补不能为空!")
    @Size(max=45, message="车补长度不能超过45")
    private String carMoney;

    @NotNull(message="餐补不能为空!")
    @Size(max=45, message="餐补长度不能超过45")
    private String eatingMoney;

    @NotNull(message="体检不能为空!")
    @Size(max=45, message="体检长度不能超过45")
    private String healthMoney;

    @NotNull(message="住房补贴不能为空!")
    @Size(max=45, message="住房补贴长度不能超过45")
    private String houseMoney;

    @NotNull(message="供暖费不能为空!")
    @Size(max=45, message="供暖费长度不能超过45")
    private String heatingMoney;

    @NotNull(message="社会保险不能为空!")
    @Size(max=45, message="社会保险长度不能超过45")
    private String societyMoney;

    @NotNull(message="住房公积金不能为空!")
    @Size(max=45, message="住房公积金长度不能超过45")
    private String fundsMoney;

    @NotNull(message="工会经费不能为空!")
    @Size(max=45, message="工会经费长度不能超过45")
    private String laborUnionMoney;

    @NotNull(message="教育经费不能为空!")
    @Size(max=45, message="教育经费长度不能超过45")
    private String educationMoney;

    @NotNull(message="劳务派遣不能为空!")
    @Size(max=45, message="劳务派遣长度不能超过45")
    private String labourServiceMoney;

    @NotNull(message="其他费用不能为空!")
    @Size(max=45, message="其他费用长度不能超过45")
    private String otherMoney;

    @NotNull(message="职工人数不能为空!")
    @Size(max=45, message="职工人数长度不能超过45")
    private String staffNumber;

    @NotNull(message="从业人员不能为空!")
    @Size(max=45, message="从业人员长度不能超过45")
    private String employeeNumber;

    @NotNull(message="去年职工不能为空!")
    @Size(max=45, message="去年职工长度不能超过45")
    private String lastYearStaff;

    @NotNull(message="去年从业不能为空!")
    @Size(max=45, message="去年从业长度不能超过45")
    private String lastYearEmployee;
}