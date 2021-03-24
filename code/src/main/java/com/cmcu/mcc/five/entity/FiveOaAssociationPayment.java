package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaAssociationPayment {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="applyId不能为空!")
    @Max(value=999999999, message="applyId必须为数字")
    private Integer applyId;

    @NotNull(message="申请部门、单位不能为空!")
    @Max(value=999999999, message="申请部门、单位必须为数字")
    private Integer deptId;

    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="经办人不能为空!")
    @Size(max=45, message="经办人长度不能超过45")
    private String handleMan;

    @NotNull(message="handleManName不能为空!")
    @Size(max=45, message="handleManName长度不能超过45")
    private String handleManName;

    @NotNull(message="协（学）会名称不能为空!")
    @Size(max=45, message="协（学）会名称长度不能超过45")
    private String associationName;

    @NotNull(message="协会级别不能为空!")
    @Size(max=45, message="协会级别长度不能超过45")
    private String associationLevel;

    @NotNull(message="主管单位名称不能为空!")
    @Size(max=45, message="主管单位名称长度不能超过45")
    private String deptChargeName;

    @NotNull(message="协会负责人不能为空!")
    @Size(max=45, message="协会负责人长度不能超过45")
    private String recommendMan;

    @NotNull(message="associationRole不能为空!")
    @Size(max=45, message="associationRole长度不能超过45")
    private String associationRole;

    @NotNull(message="联系人不能为空!")
    @Size(max=45, message="联系人长度不能超过45")
    private String linkMan;

    @NotNull(message="会费（元）不能为空!")
    @Size(max=45, message="会费（元）长度不能超过45")
    private String associationFee;

    @NotNull(message="缴费金额不能为空!")
    @Size(max=45, message="缴费金额长度不能超过45")
    private String paymentMoney;

    @NotNull(message="备注不能为空!")
    @Size(max=45, message="备注长度不能超过45")
    private String remark;

    @NotNull(message="creator不能为空!")
    @Size(max=45, message="creator长度不能超过45")
    private String creator;

    @NotNull(message="creatorName不能为空!")
    @Size(max=45, message="creatorName长度不能超过45")
    private String creatorName;

    @NotNull(message="gmtCreate不能为空!")
    private Date gmtCreate;

    @NotNull(message="gmtModified不能为空!")
    private Date gmtModified;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="isProcessEnd不能为空!")
    private Boolean processEnd;

    @NotNull(message="processInstanceId不能为空!")
    @Size(max=45, message="processInstanceId长度不能超过45")
    private String processInstanceId;
}