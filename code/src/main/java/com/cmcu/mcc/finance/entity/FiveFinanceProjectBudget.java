package com.cmcu.mcc.finance.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveFinanceProjectBudget {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="formNo不能为空!")
    @Size(max=45, message="formNo长度不能超过45")
    private String formNo;

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

    @NotNull(message="isProcessEnd不能为空!")
    private Boolean processEnd;

    @NotNull(message="processInstanceId不能为空!")
    @Size(max=45, message="processInstanceId长度不能超过45")
    private String processInstanceId;

    @NotNull(message="contractLibraryId不能为空!")
    @Max(value=999999999, message="contractLibraryId必须为数字")
    private Integer contractLibraryId;

    @NotNull(message="contractName不能为空!")
    @Size(max=45, message="contractName长度不能超过45")
    private String contractName;

    @NotNull(message="contractNo不能为空!")
    @Size(max=45, message="contractNo长度不能超过45")
    private String contractNo;

    @NotNull(message="projectName不能为空!")
    @Size(max=45, message="projectName长度不能超过45")
    private String projectName;

    @NotNull(message="项目类型不能为空!")
    @Size(max=45, message="项目类型长度不能超过45")
    private String projectType;

    @NotNull(message="budgetTotalMoney不能为空!")
    @Size(max=45, message="budgetTotalMoney长度不能超过45")
    private String budgetTotalMoney;

    @NotNull(message="责任成本不能为空!")
    @Size(max=45, message="责任成本长度不能超过45")
    private String dutyCost;

    @NotNull(message="责任成本占比不能为空!")
    @Size(max=45, message="责任成本占比长度不能超过45")
    private String dutyCostProportion;

    @NotNull(message="利润率不能为空!")
    @Size(max=45, message="利润率长度不能超过45")
    private String profitProportion;
}