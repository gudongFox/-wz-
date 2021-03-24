package com.cmcu.mcc.budget.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveBudgetScientificFundsOutDetail {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="scientificFundsOutId不能为空!")
    @Max(value=999999999, message="scientificFundsOutId必须为数字")
    private Integer scientificFundsOutId;

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

    @NotNull(message="设计费不能为空!")
    @Size(max=45, message="设计费长度不能超过45")
    private String designMoney;

    @NotNull(message="材料费不能为空!")
    @Size(max=45, message="材料费长度不能超过45")
    private String materialsMoney;

    @NotNull(message="外协费不能为空!")
    @Size(max=45, message="外协费长度不能超过45")
    private String outAssistMoney;

    @NotNull(message="试验费不能为空!")
    @Size(max=45, message="试验费长度不能超过45")
    private String testMoney;

    @NotNull(message="专用费不能为空!")
    @Size(max=45, message="专用费长度不能超过45")
    private String dedicatedMoney;

    @NotNull(message="固定资产使用费不能为空!")
    @Size(max=45, message="固定资产使用费长度不能超过45")
    private String assetMoney;

    @NotNull(message="职工薪酬不能为空!")
    @Size(max=45, message="职工薪酬长度不能超过45")
    private String payrollMoney;

    @NotNull(message="管理费不能为空!")
    @Size(max=45, message="管理费长度不能超过45")
    private String mangerMoney;
}