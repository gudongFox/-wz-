package com.cmcu.mcc.sys.entity;

import com.cmcu.common.annotation.FieldName;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class SysUserAcl {
    @FieldName("id")
    @NotNull
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @FieldName("userLogin")
    @NotNull
    @Size(max=45)
    private String userLogin;

    @FieldName("aclId")
    @NotNull
    @Max(value=999999999, message="aclId必须为数字")
    private Integer aclId;

    @FieldName("selectOpts")
    @NotNull
    @Size(max=145)
    private String selectOpts;

    @FieldName("功能点部门")
    @NotNull
    @Size(max=450)
    private String selectDepts;

    @FieldName("拥有该功能")
    @NotNull
    private Boolean owned;
}