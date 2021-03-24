package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaInformationEquipmentApply {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="设备购置单编号不能为空!")
    @Size(max=45, message="表单号长度不能超过45")
    private String formNo;

    @NotNull(message="送审单位不能为空!")
    @Max(value=999999999, message="送审单位必须为数字")
    private Integer deptId;

     @NotNull(message="deptName不能为空!")
    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="申请时间不能为空!")
    @Size(max=45, message="申请时间长度不能超过45")
    private String applyTime;

    @NotNull(message="联系人不能为空!")
    @Size(max=45, message="联系人长度不能超过45")
    private String linkMan;

    @NotNull(message="linkManName不能为空!")
    @Size(max=45, message="linkManName长度不能超过45")
    private String linkManName;

    @NotNull(message="联系人电话不能为空!")
    @Size(max=45, message="联系人电话长度不能超过45")
    private String linkManPhone;

    @NotNull(message="是否计划内不能为空!")
    private Boolean plan;

    @NotNull(message="设备用途 采购理由不能为空!")
    @Size(max=450, message="设备用途 采购理由长度不能超过45")
    private String equipmentUse;

    @NotNull(message="总价不能为空!")
    @Size(max=45, message="总价长度不能超过45")
    private String totalMoney;

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

    @NotNull(message="processInstanceId不能为空!")
    @Size(max=45, message="processInstanceId长度不能超过45")
    private String processInstanceId;

    @NotNull(message="isProcessEnd不能为空!")
    private Boolean processEnd;
}