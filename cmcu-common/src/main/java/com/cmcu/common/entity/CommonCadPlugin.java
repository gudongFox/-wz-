package com.cmcu.common.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class CommonCadPlugin {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="tenetId不能为空!")
    @Size(max=45, message="tenetId长度不能超过45")
    private String tenetId;

    @NotNull(message="版本号不能为空!")
    @Size(max=45, message="版本号长度不能超过45")
    private String versionCode;

    @NotNull(message="附件不能为空!")
    @Max(value=999999999, message="附件必须为数字")
    private Integer attachId;

    @NotNull(message="zipName不能为空!")
    @Size(max=145, message="zipName长度不能超过145")
    private String zipName;

    @NotNull(message="sizeName不能为空!")
    @Size(max=45, message="sizeName长度不能超过45")
    private String sizeName;


    @NotNull(message="versionType!")
    @Size(max=45, message="versionType")
    private String versionType;

    @NotNull(message="说明不能为空!")
    @Size(max=450, message="说明长度不能超过450")
    private String versionDesc;

    @NotNull(message="creatorName不能为空!")
    @Size(max=45, message="creatorName长度不能超过45")
    private String creatorName;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="gmtCreate不能为空!")
    private Date gmtCreate;

    @NotNull(message="gmtModified不能为空!")
    private Date gmtModified;
}