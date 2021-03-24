package com.cmcu.mcc.five.entity;

import com.cmcu.common.annotation.FieldName;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveEdReturnVisitDetail {
    @FieldName("id")
    @NotNull
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @FieldName("visitId")
    @NotNull
    @Max(value=999999999, message="visitId必须为数字")
    private Integer visitId;

    @FieldName("appraiseType")
    @NotNull
    @Size(max=45)
    private String appraiseType;

    @FieldName("appraiseResult")
    @NotNull
    @Size(max=45)
    private String appraiseResult;
}