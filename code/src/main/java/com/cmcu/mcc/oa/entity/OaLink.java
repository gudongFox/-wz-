package com.cmcu.mcc.oa.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class OaLink {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @Size(max=255, message="链接地址长度不能超过255")
    private String linkUrl;

    @Size(max=255, message="链接名称长度不能超过255")
    private String linkName;

    @Size(max=50, message="logo地址长度不能超过50")
    private String linkLogo;

    private Date gmtCreate;

    private Date gmtModified;

    @Size(max=45, message="creator长度不能超过45")
    private String creator;

    @Max(value=999999999, message="序号必须为数字")
    private Integer seq;

    private Boolean visible;

    @Size(max=255, message="备注长度不能超过255")
    private String remark;

    @Size(max=45, message="创建人名称长度不能超过45")
    private String creatorName;
}