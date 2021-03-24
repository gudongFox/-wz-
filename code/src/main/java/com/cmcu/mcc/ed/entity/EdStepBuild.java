package com.cmcu.mcc.ed.entity;

import com.cmcu.common.annotation.FieldName;
import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class EdStepBuild {
    @FieldName("id")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @FieldName("stepId")
    @NotNull
    @Max(value=999999999, message="stepId必须为数字")
    private Integer stepId;

    @FieldName("子项名称")
    @NotEmpty
    @Size(max=45)
    private String buildName;

    @FieldName("子项编码")
    @NotEmpty
    @Size(max=45)
    private String buildNo;

    @FieldName("排序字段")
    @NotNull
    @Max(value=100, message="seq必须为数字")
    private Integer seq;

    @FieldName("isDeleted")
    @NotNull
    private Boolean deleted;

    @FieldName("gmtCreate")
    @NotNull
    private Date gmtCreate;

    @FieldName("gmtModified")
    @NotNull
    private Date gmtModified;

    @FieldName("备注")
    @Size(max=450)
    private String remark;
}