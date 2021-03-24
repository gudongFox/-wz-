package com.cmcu.mcc.oa.entity;

import com.cmcu.common.annotation.FieldName;
import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class OaArchiveDetail {
    @FieldName("id")
    @NotNull
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @FieldName("attachId")
    @NotNull
    @Max(value=999999999, message="attachId必须为数字")
    private Integer attachId;

    @FieldName("archiveId")
    @NotNull
    @Max(value=999999999, message="archiveId必须为数字")
    private Integer archiveId;

    @FieldName("fileType")
    @NotNull
    @Size(max=45)
    private String fileType;

    @FieldName("fileName")
    @NotNull
    @Size(max=145)
    private String fileName;

    @FieldName("seq")
    @NotNull
    @Max(value=999999999, message="seq必须为数字")
    private Integer seq;

    @FieldName("电子档案")
    @NotNull
    private Boolean online;

    @FieldName("存放地址")
    @NotNull
    @Size(max=145)
    private String realAddress;

    @FieldName("isDeleted")
    @NotNull
    private Boolean deleted;

    @FieldName("remark")
    @NotNull
    @Size(max=450)
    private String remark;

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
}