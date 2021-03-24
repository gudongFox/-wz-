package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaSoftware {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="申请单位不能为空!")
    @Max(value=999999999, message="申请单位必须为数字")
    private Integer deptId;

     @NotNull(message="deptName不能为空!")
    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="申请类型不能为空!")
    @Size(max=45, message="申请类型长度不能超过45")
    private String applyStyle;

    @NotNull(message="申请日期不能为空!")
    @Size(max=45, message="申请日期长度不能超过45")
    private String applyTime;

    @NotNull(message="软件名称不能为空!")
    @Size(max=45, message="软件名称长度不能超过45")
    private String softwareName;

    @NotNull(message="软件授权类型不能为空!")
    @Size(max=45, message="软件授权类型长度不能超过45")
    private String softwareLicenceStyle;

    @NotNull(message="软件公司名称不能为空!")
    @Size(max=45, message="软件公司名称长度不能超过45")
    private String softwareCompanyName;

    @NotNull(message="软件公司地址不能为空!")
    @Size(max=45, message="软件公司地址长度不能超过45")
    private String softwareCompanyUrl;

    @NotNull(message="软件授权点数不能为空!")
    @Size(max=45, message="软件授权点数长度不能超过45")
    private String softwareLicenceCount;

    @NotNull(message="软件报价不能为空!")
    @Size(max=45, message="软件报价长度不能超过45")
    private String softwareOffer;

    @NotNull(message="成交价格不能为空!")
    @Size(max=45, message="成交价格长度不能超过45")
    private String softwarePrice;

    @NotNull(message="软件公司联系人不能为空!")
    @Size(max=45, message="软件公司联系人长度不能超过45")
    private String softwareLink;

    @NotNull(message="联系人电话不能为空!")
    @Size(max=45, message="联系人电话长度不能超过45")
    private String softwarePhone;

    @NotNull(message="使用软件专业不能为空!")
    @Size(max=125, message="使用软件专业长度不能超过125")
    private String softwareUseMajor;

    @NotNull(message="软件用途及购置理由不能为空!")
    @Size(max=450, message="软件用途及购置理由长度不能超过450")
    private String softwareUseWay;

    @Size(max=450, message="软件安装概要长度不能超过450")
    private String softwareInstall;

    @NotNull(message="remark不能为空!")
    @Size(max=45, message="remark长度不能超过45")
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

    @NotNull(message="isPlan不能为空!")
    private Boolean plan;
}