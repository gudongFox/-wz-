package com.cmcu.mcc.five.entity;

import com.cmcu.common.annotation.FieldName;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveEdQualityCheckDetail {
    @FieldName("id")
    @NotNull
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @FieldName("qualityCheckId")
    @NotNull
    @Max(value=999999999, message="qualityCheckId必须为数字")
    private Integer qualityCheckId;

    @FieldName("图号或文件名")
    @NotNull
    @Size(max=145)
    private String drawNo;

    @FieldName("差错类别")
    @NotNull
    @Size(max=45)
    private String markCategory;

    @FieldName("审校意见")
    @NotNull
    @Size(max=450)
    private String markContent;

    @FieldName("强条条文号")
    @NotNull
    @Size(max=145)
    private String ruleNo;

    @FieldName("处理结果")
    @NotNull
    @Size(max=450)
    private String solutionContent;

    @FieldName("排序字段")
    @NotNull
    @Max(value=999999999, message="排序字段必须为数字")
    private Integer seq;
}