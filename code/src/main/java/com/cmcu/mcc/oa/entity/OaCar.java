package com.cmcu.mcc.oa.entity;

import com.cmcu.common.annotation.FieldName;
import java.math.BigDecimal;
import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class OaCar {
    @FieldName("id")
    @NotNull
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @FieldName("businessKey")
    @NotNull
    @Size(max=45)
    private String businessKey;

    @FieldName("所属部门")
    @NotNull
    @Size(max=145)
    private String deptName;

    @FieldName("车牌号")
    @NotNull
    @Size(max=45)
    private String carNo;

    @FieldName("轿车、SUV")
    @NotNull
    @Size(max=45)
    private String carType;

    @FieldName("汽车排量")
    @NotNull
    @Size(max=45)
    private String carCc;

    @FieldName("汽车价格")
    @NotNull
    @Max(value=999999999, message="汽车价格必须为数字")
    private BigDecimal carPrice;

    @FieldName("汽车品牌")
    @NotNull
    @Size(max=45)
    private String carBrand;

    @FieldName("车辆颜色")
    @NotNull
    @Size(max=45)
    private String carColor;

    @FieldName("状态：正常、维修、使用中")
    @NotNull
    @Size(max=45)
    private String carStatus;

    @FieldName("buyDate")
    @NotNull
    @Size(max=45)
    private String buyDate;

    @FieldName("是否删除")
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

    @FieldName("remark")
    @NotNull
    @Size(max=450)
    private String remark;
}