package com.cmcu.mcc.finance.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveFinanceTravelDeduction {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="relevanseBusinessKey不能为空!")
    @Size(max=45, message="relevanseBusinessKey长度不能超过45")
    private String relevanseBusinessKey;

    @NotNull(message="travelId不能为空!")
    @Max(value=999999999, message="travelId必须为数字")
    private Integer travelId;

    @NotNull(message="关联流程信息不能为空!")
    @Size(max=45, message="关联流程信息长度不能超过45")
    private String relevanceName;

    @NotNull(message="关联项不能为空!")
    @Max(value=999999999, message="关联项必须为数字")
    private Integer relevanceId;

    @NotNull(message="关联类型不能为空!")
    @Size(max=45, message="关联类型长度不能超过45")
    private String relevanceType;

    @NotNull(message="列支人不能为空!")
    @Size(max=45, message="列支人长度不能超过45")
    private String relevanceMoney;

    @NotNull(message="创建时间不能为空!")
    @Size(max=45, message="创建时间长度不能超过45")
    private String relevanceTime;

    @NotNull(message="剩余金额不能为空!")
    @Size(max=45, message="剩余金额长度不能超过45")
    private String relevanceRemainMoney;

    @NotNull(message="备注不能为空!")
    @Size(max=450, message="备注长度不能超过450")
    private String relevanceRemark;

    @NotNull(message="deptId不能为空!")
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

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
}