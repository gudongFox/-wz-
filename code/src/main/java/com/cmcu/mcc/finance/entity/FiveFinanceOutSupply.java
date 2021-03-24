package com.cmcu.mcc.finance.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveFinanceOutSupply {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="formNo不能为空!")
    @Size(max=45, message="formNo长度不能超过45")
    private String formNo;

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

    @NotNull(message="deptId不能为空!")
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

     @NotNull(message="deptName不能为空!")
    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="type不能为空!")
    @Size(max=45, message="type长度不能超过45")
    private String type;

    @NotNull(message="资信证明 是否注明银行余额不能为空!")
    private Boolean bankBalance;

    @NotNull(message="财务报表开始年不能为空!")
    @Size(max=45, message="财务报表开始年长度不能超过45")
    private String beginYear;

    @NotNull(message="结束年份不能为空!")
    @Size(max=45, message="结束年份长度不能超过45")
    private String endYear;

    @NotNull(message="资产负债表不能为空!")
    private Boolean assetLiabilities;

    @NotNull(message="利润及分配表不能为空!")
    private Boolean profitsAllocation;

    @NotNull(message="现金流量表不能为空!")
    private Boolean cash;

    @NotNull(message="isOther不能为空!")
    private Boolean other;
}