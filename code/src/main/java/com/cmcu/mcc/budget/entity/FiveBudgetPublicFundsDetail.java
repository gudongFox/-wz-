package com.cmcu.mcc.budget.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveBudgetPublicFundsDetail {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="publicFundsId不能为空!")
    @Max(value=999999999, message="publicFundsId必须为数字")
    private Integer publicFundsId;

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

    @NotNull(message="水费不能为空!")
    @Size(max=45, message="水费长度不能超过45")
    private String waterMoney;

    @NotNull(message="电费不能为空!")
    @Size(max=45, message="电费长度不能超过45")
    private String electricMoney;

    @NotNull(message="供暖费不能为空!")
    @Size(max=45, message="供暖费长度不能超过45")
    private String heatingMoneyTotal;

    @NotNull(message="供暖费 个人补贴不能为空!")
    @Size(max=45, message="供暖费 个人补贴长度不能超过45")
    private String heatingMoneySelf;

    @NotNull(message="供暖费 公共不能为空!")
    @Size(max=45, message="供暖费 公共长度不能超过45")
    private String heatingMoneyPublic;

    @NotNull(message="住房补贴不能为空!")
    @Size(max=45, message="住房补贴长度不能超过45")
    private String houseMoney;

    @NotNull(message="体检费不能为空!")
    @Size(max=45, message="体检费长度不能超过45")
    private String healthMoney;
}