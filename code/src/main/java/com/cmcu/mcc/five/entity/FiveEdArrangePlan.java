package com.cmcu.mcc.five.entity;

import com.cmcu.common.annotation.FieldName;
import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveEdArrangePlan {
    @FieldName("id")
    @NotNull
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @FieldName("arrangeId")
    @NotNull
    @Max(value=999999999, message="arrangeId必须为数字")
    private Integer arrangeId;

    @FieldName("majorName")
    @NotNull
    @Size(max=145)
    private String majorName;

    @FieldName("总图")
    @NotNull
    @Size(max=145)
    private String destGeneralView;

    @FieldName("工艺")
    @NotNull
    @Size(max=145)
    private String destCraft;

    @FieldName("建筑")
    @NotNull
    @Size(max=145)
    private String destArchitecture;

    @FieldName("结构")
    @NotNull
    @Size(max=145)
    private String destStructure;

    @FieldName("水道")
    @NotNull
    @Size(max=145)
    private String destWaterway;

    @FieldName("暖通")
    @NotNull
    @Size(max=145)
    private String destHvac;

    @FieldName("热机")
    @NotNull
    @Size(max=145)
    private String destHeatEngine;

    @FieldName("电气")
    @NotNull
    @Size(max=145)
    private String destElectric;

    @FieldName("自控")
    @NotNull
    @Size(max=145)
    private String destAutomatic;

    @FieldName("设备")
    @NotNull
    @Size(max=145)
    private String destEquipment;

    @FieldName("电信")
    @NotNull
    @Size(max=145)
    private String destTelecom;

    @FieldName("isDeleted")
    @NotNull
    private Boolean deleted;

    @FieldName("gmtCreate")
    @NotNull
    private Date gmtCreate;

    @FieldName("gmtModified")
    @NotNull
    private Date gmtModified;

    @FieldName("备用专业1")
    @NotNull
    @Size(max=145)
    private String destValue1;

    @FieldName("备用专业2")
    @NotNull
    @Size(max=145)
    private String destValue2;

    @FieldName("备用专业3")
    @NotNull
    @Size(max=145)
    private String destValue3;

    @FieldName("seq")
    @NotNull
    @Max(value=999999999, message="seq必须为数字")
    private Integer seq;
}