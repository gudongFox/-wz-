package com.cmcu.mcc.finance.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveFinanceNode {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="收入id不能为空!")
    @Max(value=999999999, message="收入id必须为数字")
    private Integer incomeId;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="formNo不能为空!")
    @Size(max=45, message="formNo长度不能超过45")
    private String formNo;

    @NotNull(message="票据种类不能为空!")
    @Size(max=45, message="票据种类长度不能超过45")
    private String type;

    @NotNull(message="来源账户不能为空!")
    @Size(max=45, message="来源账户长度不能超过45")
    private String sourceAccount;

    @NotNull(message="接收账户不能为空!")
    @Size(max=45, message="接收账户长度不能超过45")
    private String targetAccount;

    @NotNull(message="deptId不能为空!")
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

    @NotNull(message="部门名称不能为空!")
    @Size(max=145, message="部门名称长度不能超过145")
    private String deptName;

    @NotNull(message="票据形式不能为空!")
    @Size(max=45, message="票据形式长度不能超过45")
    private String modality;

    @NotNull(message="票据状态不能为空!")
    @Size(max=45, message="票据状态长度不能超过45")
    private String state;

    @NotNull(message="抵付/承兑日期不能为空!")
    @Size(max=45, message="抵付/承兑日期长度不能超过45")
    private String useTime;

    @NotNull(message="票据号码不能为空!")
    @Size(max=45, message="票据号码长度不能超过45")
    private String nodeNo;

    @NotNull(message="使用次数不能为空!")
    @Size(max=45, message="使用次数长度不能超过45")
    private String useNum;

    @NotNull(message="财务确认时间不能为空!")
    @Size(max=45, message="财务确认时间长度不能超过45")
    private String financeVerifyTime;

    @NotNull(message="剩余到期时间不能为空!")
    @Size(max=45, message="剩余到期时间长度不能超过45")
    private String remainTime;

    @NotNull(message="票据生效时间不能为空!")
    @Size(max=45, message="票据生效时间长度不能超过45")
    private String nodeStartTime;

    @NotNull(message="票据失效时间不能为空!")
    @Size(max=45, message="票据失效时间长度不能超过45")
    private String nodeEndTime;

    @NotNull(message="开具银行不能为空!")
    @Size(max=45, message="开具银行长度不能超过45")
    private String bank;

    @NotNull(message="金额不能为空!")
    @Size(max=45, message="金额长度不能超过45")
    private String money;

    @NotNull(message="金额大写不能为空!")
    @Size(max=45, message="金额大写长度不能超过45")
    private String moneyMax;

    @NotNull(message="出票单位不能为空!")
    @Size(max=45, message="出票单位长度不能超过45")
    private String drawDept;

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
}