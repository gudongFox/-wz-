package com.cmcu.mcc.sys.entity;

import com.cmcu.common.annotation.FieldName;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class CoApp {
    @FieldName("id")
    @NotNull
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @FieldName("公司代码")
    @NotNull
    @Size(max=45)
    private String cCode;

    @FieldName("appVersion")
    @NotNull
    @Size(max=45)
    private String appVersion;

    @FieldName("appMessage")
    @NotNull
    @Size(max=145)
    private String appMessage;

    @FieldName("appPath")
    @NotNull
    @Size(max=145)
    private String appPath;

    @FieldName("isDeleted")
    @NotNull
    private Boolean deleted;

    @FieldName("remark")
    @NotNull
    @Size(max=450)
    private String remark;
}