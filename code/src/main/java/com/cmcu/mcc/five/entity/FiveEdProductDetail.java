package com.cmcu.mcc.five.entity;

import com.cmcu.common.annotation.FieldName;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveEdProductDetail {
    @FieldName("id")
    @NotNull
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @FieldName("productId")
    @NotNull
    @Max(value=999999999, message="productId必须为数字")
    private Integer productId;

    @FieldName("buildName")
    @NotNull
    @Size(max=145)
    private String buildName;

    @FieldName("majorName")
    @NotNull
    @Size(max=45)
    private String majorName;

    @FieldName("drawNo")
    @NotNull
    @Size(max=45)
    private String drawNo;

    @FieldName("constructionArea")
    @NotNull
    @Size(max=45)
    private String constructionArea;

    @FieldName("deptId")
    @NotNull
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

    @FieldName("deptName")
    @NotNull
    @Size(max=145)
    private String deptName;

    @FieldName("图纸张数")
    @NotNull
    @Max(value=999999999, message="图纸张数必须为数字")
    private Integer copyCount;

    @FieldName("remark1")
    @NotNull
    @Size(max=45)
    private String remark1;

    @FieldName("remark2")
    @NotNull
    @Size(max=45)
    private String remark2;

    @FieldName("remark3")
    @NotNull
    @Size(max=45)
    private String remark3;

    @FieldName("remark4")
    @NotNull
    @Size(max=45)
    private String remark4;

    @FieldName("remark5")
    @NotNull
    @Size(max=45)
    private String remark5;

    @FieldName("remark6")
    @NotNull
    @Size(max=45)
    private String remark6;

    @FieldName("remark7")
    @NotNull
    @Size(max=45)
    private String remark7;

    @FieldName("seq")
    @NotNull
    @Max(value=999999999, message="seq必须为数字")
    private Integer seq;
}