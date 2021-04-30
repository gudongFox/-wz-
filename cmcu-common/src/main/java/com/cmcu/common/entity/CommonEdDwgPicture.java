package com.cmcu.common.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class CommonEdDwgPicture {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="tenetId不能为空!")
    @Size(max=45, message="tenetId长度不能超过45")
    private String tenetId;

    @NotNull(message="attachId不能为空!")
    @Max(value=999999999, message="attachId必须为数字")
    private Integer attachId;

    @NotNull(message="图框名称不能为空!")
    @Size(max=145, message="图框名称长度不能超过145")
    private String pictureName;

    @NotNull(message="图框尺寸不能为空!")
    @Size(max=45, message="图框尺寸长度不能超过45")
    private String pictureSize;

    @NotNull(message="expandSize不能为空!")
    @Size(max=45, message="expandSize长度不能超过45")
    private String expandSize;

    @NotNull(message="pictureDirection不能为空!")
    @Size(max=45, message="pictureDirection长度不能超过45")
    private String pictureDirection;

    @NotNull(message="gmtCreate不能为空!")
    private Date gmtCreate;

    @NotNull(message="gmtModified不能为空!")
    private Date gmtModified;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="seq不能为空!")
    @Max(value=999999999, message="seq必须为数字")
    private Integer seq;

    @NotNull(message="remark不能为空!")
    @Size(max=450, message="remark长度不能超过450")
    private String remark;
}