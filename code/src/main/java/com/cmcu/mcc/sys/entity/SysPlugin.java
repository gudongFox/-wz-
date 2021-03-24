package com.cmcu.mcc.sys.entity;

import com.cmcu.common.annotation.FieldName;
import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class SysPlugin {
    @FieldName("id")
    @NotNull
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @FieldName("版本号")
    @NotEmpty
    @Size(max=45)
    private String versionCode;

    @FieldName("附件")
    @NotNull
    @Max(value=999999999, message="附件必须为数字")
    private Integer attachId;

    @FieldName("zipName")
    @NotEmpty
    @Size(max=145)
    private String zipName;

    @FieldName("sizeName")
    @NotEmpty
    @Size(max=45)
    private String sizeName;

    @FieldName("说明")
    @NotEmpty
    @Size(max=450)
    private String versionDesc;

    @FieldName("creatorName")
    @NotEmpty
    @Size(max=45)
    private String creatorName;

    @FieldName("isDeleted")
    @NotNull
    private Boolean deleted;

    @FieldName("gmtCreate")
    @NotNull
    private Date gmtCreate;

    @FieldName("gmtModified")
    @NotNull
    private Date gmtModified;
}