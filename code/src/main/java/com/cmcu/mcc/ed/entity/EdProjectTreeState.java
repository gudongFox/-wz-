package com.cmcu.mcc.ed.entity;

import com.cmcu.common.annotation.FieldName;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class EdProjectTreeState {
    @FieldName("id")
    @NotNull
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @FieldName("userLogin")
    @NotEmpty
    @Size(max=45)
    private String userLogin;

    @FieldName("projectId")
    @NotNull
    @Max(value=999999999, message="projectId必须为数字")
    private Integer projectId;

    @FieldName("nodeId")
    @NotNull
    @Max(value=999999999, message="nodeId必须为数字")
    private Integer nodeId;

    @FieldName("opened")
    @NotNull
    private Boolean opened;

    @FieldName("selected")
    @NotNull
    private Boolean selected;
}