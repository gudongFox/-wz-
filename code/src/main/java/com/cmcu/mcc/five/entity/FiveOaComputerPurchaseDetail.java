package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaComputerPurchaseDetail {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="computerPurchaseId不能为空!")
    @Max(value=999999999, message="computerPurchaseId必须为数字")
    private Integer computerPurchaseId;

    @NotNull(message="单位不能为空!")
    @Size(max=45, message="单位长度不能超过45")
    private String deptId;

    @NotNull(message="单位名称不能为空!")
    @Size(max=45, message="单位名称长度不能超过45")
    private String deptName;

    @NotNull(message="姓名不能为空!")
    @Size(max=45, message="姓名长度不能超过45")
    private String person;

    @NotNull(message="姓名不能为空!")
    @Size(max=45, message="姓名长度不能超过45")
    private String personName;

    @NotNull(message="职工号不能为空!")
    @Size(max=45, message="职工号长度不能超过45")
    private String personNo;

    @NotNull(message="密级不能为空!")
    @Size(max=45, message="密级长度不能超过45")
    private String secretLevel;

    @NotNull(message="电话不能为空!")
    @Size(max=45, message="电话长度不能超过45")
    private String phone;

    @NotNull(message="病房不能为空!")
    @Size(max=45, message="病房长度不能超过45")
    private String roomNo;

    @NotNull(message="设备类型不能为空!")
    @Size(max=45, message="设备类型长度不能超过45")
    private String deviceType;

    @NotNull(message="入网类型不能为空!")
    @Size(max=45, message="入网类型长度不能超过45")
    private String netType;

    @NotNull(message="特殊需求不能为空!")
    @Size(max=45, message="特殊需求长度不能超过45")
    private String command;

    @NotNull(message="key号不能为空!")
    @Size(max=45, message="key号长度不能超过45")
    private String keyNo;

    @NotNull(message="物理地址不能为空!")
    @Size(max=45, message="物理地址长度不能超过45")
    private String physicsAddress;

    @NotNull(message="ip地址不能为空!")
    @Size(max=45, message="ip地址长度不能超过45")
    private String ipAddress;

    @NotNull(message="硬盘序列号不能为空!")
    @Size(max=45, message="硬盘序列号长度不能超过45")
    private String diskNo;

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
}