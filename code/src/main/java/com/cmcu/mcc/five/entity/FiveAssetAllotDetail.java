package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveAssetAllotDetail {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="assetAllotId不能为空!")
    @Max(value=999999999, message="assetAllotId必须为数字")
    private Integer assetAllotId;

    @NotNull(message="入账时间不能为空!")
    @Size(max=45, message="入账时间长度不能超过45")
    private String enterTime;

    @NotNull(message="固定资产编码不能为空!")
    @Size(max=45, message="固定资产编码长度不能超过45")
    private String assetCode;

    @NotNull(message="固定资产名称不能为空!")
    @Size(max=45, message="固定资产名称长度不能超过45")
    private String assetName;

    @NotNull(message="规格型号不能为空!")
    @Size(max=45, message="规格型号长度不能超过45")
    private String model;

    @NotNull(message="原使用单位不能为空!")
    @Size(max=45, message="原使用单位长度不能超过45")
    private String useUnit;

    @NotNull(message="原使用单位不能为空!")
    @Size(max=45, message="原使用单位长度不能超过45")
    private String useMan;

    @NotNull(message="原保管人不能为空!")
    @Size(max=45, message="原保管人长度不能超过45")
    private String useManName;

    @NotNull(message="转入单位不能为空!")
    @Size(max=45, message="转入单位长度不能超过45")
    private String transferUnit;

    @NotNull(message="reserveMan不能为空!")
    @Size(max=45, message="reserveMan长度不能超过45")
    private String reserveMan;

    @NotNull(message="现保管人不能为空!")
    @Size(max=45, message="现保管人长度不能超过45")
    private String reserveManName;

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