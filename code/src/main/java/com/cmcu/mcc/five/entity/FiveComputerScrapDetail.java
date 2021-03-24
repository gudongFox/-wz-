package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveComputerScrapDetail {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="入账时间不能为空!")
    @Max(value=999999999, message="入账时间必须为数字")
    private Integer computerScrapId;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="设备类型不能为空!")
    @Size(max=45, message="设备类型长度不能超过45")
    private String deviceType;

    @NotNull(message="设备编号不能为空!")
    @Size(max=45, message="设备编号长度不能超过45")
    private String deviceNo;

    @NotNull(message="品牌型号不能为空!")
    @Size(max=45, message="品牌型号长度不能超过45")
    private String deviceModel;

    @NotNull(message="密级不能为空!")
    @Size(max=45, message="密级长度不能超过45")
    private String secretLevel;

    @NotNull(message="责任人不能为空!")
    @Size(max=45, message="责任人长度不能超过45")
    private String dutyPerson;

    @NotNull(message="硬盘序列号不能为空!")
    @Size(max=45, message="硬盘序列号长度不能超过45")
    private String hardDiskNo;

    @NotNull(message="启用时间不能为空!")
    @Size(max=45, message="启用时间长度不能超过45")
    private String startTime;

    @NotNull(message="原值不能为空!")
    @Size(max=45, message="原值长度不能超过45")
    private String originalValue;

    @NotNull(message="折旧年限不能为空!")
    @Size(max=45, message="折旧年限长度不能超过45")
    private String depreciableLife;

    @NotNull(message="已提折旧不能为空!")
    @Size(max=45, message="已提折旧长度不能超过45")
    private String submitted;

    @NotNull(message="净值不能为空!")
    @Size(max=45, message="净值长度不能超过45")
    private String netWorth;

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

    @NotNull(message="uploadfile不能为空!")
    private Boolean uploadfile;

    @NotNull(message="创建时间不能为空!")
    private Date gmtCreate;

    @NotNull(message="修改时间不能为空!")
    private Date gmtModified;
}