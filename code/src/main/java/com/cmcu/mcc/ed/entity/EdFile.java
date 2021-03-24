package com.cmcu.mcc.ed.entity;

import com.cmcu.common.annotation.FieldName;
import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class EdFile {
    @FieldName("id")
    @NotNull
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @FieldName("ä¸šåŠ¡key_ä¸šåŠ¡id, ed_business_change_1")
    @NotNull
    @Size(max=45)
    private String businessId;

    @FieldName("sys_attach_id")
    @NotNull
    @Max(value=999999999, message="sys_attach_id必须为数字")
    private Integer attachId;

    @FieldName("æ•°æ®å­—å…¸ sys_code")
    @NotNull
    @Size(max=45)
    private String fileType;

    @FieldName("sys_attach_name")
    @NotNull
    @Size(max=145)
    private String fileName;

    @FieldName("seq")
    @NotNull
    @Max(value=999999999, message="seq必须为数字")
    private Integer seq;

    @FieldName("isDeleted")
    @NotNull
    private Boolean deleted;

    @FieldName("creator")
    @NotNull
    @Size(max=45)
    private String creator;

    @FieldName("creatorName")
    @NotNull
    @Size(max=45)
    private String creatorName;

    @FieldName("gmtCreate")
    @NotNull
    private Date gmtCreate;

    @FieldName("gmtModified")
    @NotNull
    private Date gmtModified;

    @NotNull
    private Integer cpFileId;

    @FieldName("remark")
    @NotNull
    @Size(max=450)
    private String remark;

    @FieldName("attachIds")
    @NotNull
    @Size(max=145)
    private String attachIds;

    @FieldName("signId")
    @Max(value=999999999, message="signId必须为数字")
    private Integer signId;

    @FieldName("signUser")
    @Size(max=45)
    private String signUser;

    @FieldName("gmtSign")
    private Date gmtSign;
}