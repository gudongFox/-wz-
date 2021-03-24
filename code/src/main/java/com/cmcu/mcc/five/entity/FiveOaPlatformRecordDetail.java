package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaPlatformRecordDetail {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="recordId不能为空!")
    @Max(value=999999999, message="recordId必须为数字")
    private Integer recordId;

    @NotNull(message="备案人不能为空!")
    @Size(max=45,message="备案人长度不能大于45!")
    private String recordMan;

    @NotNull(message="备案时间不能为空!")
    @Size(max=45,message="备案时间长度不能大于45!")
    private String recordTime;

    @NotNull(message="省份不能为空!")
    @Size(max=45,message="省份长度不能大于45!")
    private String province;

    @NotNull(message="城市或地区不能为空!")
    @Size(max=45,message="城市或地区长度不能大于45!")
    private String city;

    @NotNull(message="备案平台名称不能为空!")
    @Size(max=45,message="备案平台名称长度不能大于45!")
    private String platformName;

    @NotNull(message="平台网址不能为空!")
    @Size(max=45,message="平台网址长度不能大于45!")
    private String platformUrl;

    @NotNull(message="备案内容不能为空!")
    @Size(max=450,message="备案内容长度不能大于450!")
    private String recordContent;

    @NotNull(message="有效期不能为空!")
    @Size(max=45,message="有效期长度不能大于45!")
    private String recordValidity;

    @NotNull(message="是否有密码不能为空!")
    private Boolean password;

    @NotNull(message="密码管理人不能为空!")
    @Size(max=45,message="密码管理人长度不能大于45!")
    private String passwordCustodian;

    @NotNull(message="备注不能为空!")
    @Size(max=45,message="备注长度不能大于45!")
    private String remark;

    @NotNull(message="creator不能为空!")
    @Size(max=45,message="creator长度不能大于45!")
    private String creator;

    @NotNull(message="creatorName不能为空!")
    @Size(max=45,message="creatorName长度不能大于45!")
    private String creatorName;

    @NotNull(message="gmtCreate不能为空!")
    private Date gmtCreate;

    @NotNull(message="gmtModified不能为空!")
    private Date gmtModified;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;
}