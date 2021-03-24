package com.cmcu.mcc.budget.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveBudgetMaintainDetail {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="maintainId不能为空!")
    @Max(value=999999999, message="maintainId必须为数字")
    private Integer maintainId;

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

    @NotNull(message="系统名称不能为空!")
    @Size(max=45, message="系统名称长度不能超过45")
    private String systemName;

    @NotNull(message="项目类型不能为空!")
    @Size(max=45, message="项目类型长度不能超过45")
    private String projectType;

    @NotNull(message="工作内容不能为空!")
    @Size(max=45, message="工作内容长度不能超过45")
    private String workContent;

    @NotNull(message="工程内容不能为空!")
    @Size(max=45, message="工程内容长度不能超过45")
    private String projectContent;

    @NotNull(message="预计资金不能为空!")
    @Size(max=45, message="预计资金长度不能超过45")
    private String budgetMoney;

    @NotNull(message="业务支出-项目名称不能为空!")
    @Size(max=45, message="业务支出-项目名称长度不能超过45")
    private String projectName;

    @NotNull(message="去年项目不能为空!")
    @Size(max=45, message="去年项目长度不能超过45")
    private String lastYearSystemName;

    @NotNull(message="lastYearMoney不能为空!")
    @Size(max=45, message="lastYearMoney长度不能超过45")
    private String lastYearMoney;

    @NotNull(message="lastYearSuccess不能为空!")
    @Size(max=45, message="lastYearSuccess长度不能超过45")
    private String lastYearSuccess;

    @NotNull(message="lastYearId不能为空!")
    @Max(value=999999999, message="lastYearId必须为数字")
    private Integer lastYearId;
}