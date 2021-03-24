package com.cmcu.mcc.budget.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveBudgetScientificFundsInDetail {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="scientificFundsInId不能为空!")
    @Max(value=999999999, message="scientificFundsInId必须为数字")
    private Integer scientificFundsInId;

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

    @NotNull(message="项目开始时间不能为空!")
    @Size(max=45, message="项目开始时间长度不能超过45")
    private String beginTime;

    @NotNull(message="项目结束时间不能为空!")
    @Size(max=45, message="项目结束时间长度不能超过45")
    private String endTime;

    @NotNull(message="批准文号不能为空!")
    @Size(max=45, message="批准文号长度不能超过45")
    private String allowNo;

    @NotNull(message="研制(研发)总目标不能为空!")
    @Size(max=45, message="研制(研发)总目标长度不能超过45")
    private String totalTarget;

    @NotNull(message="项目总经费不能为空!")
    @Size(max=45, message="项目总经费长度不能超过45")
    private String projectTotalMoney;

    @NotNull(message="国拨资金不能为空!")
    @Size(max=45, message="国拨资金长度不能超过45")
    private String countryMoney;

    @NotNull(message="自筹资金不能为空!")
    @Size(max=45, message="自筹资金长度不能超过45")
    private String selfMoney;

    @NotNull(message="其他资金不能为空!")
    @Size(max=45, message="其他资金长度不能超过45")
    private String otherMoney;

    @NotNull(message="项目累计收入不能为空!")
    @Size(max=45, message="项目累计收入长度不能超过45")
    private String projectAddInMoney;

    @NotNull(message="项目累计支出不能为空!")
    @Size(max=45, message="项目累计支出长度不能超过45")
    private String projectAddOutMoney;

    @NotNull(message="本年项目经费不能为空!")
    @Size(max=45, message="本年项目经费长度不能超过45")
    private String projectYearMoney;

    @NotNull(message="国拨资金 本年不能为空!")
    @Size(max=45, message="国拨资金 本年长度不能超过45")
    private String yearCountryMoney;

    @NotNull(message="自筹资金 本年不能为空!")
    @Size(max=45, message="自筹资金 本年长度不能超过45")
    private String yearSelfMoney;

    @NotNull(message="其他资金 本年不能为空!")
    @Size(max=45, message="其他资金 本年长度不能超过45")
    private String yearOtherMoney;
}