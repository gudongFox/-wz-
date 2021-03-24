package com.cmcu.mcc.business.entity;

import com.cmcu.common.annotation.FieldName;
import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class BusinessBidProjectCharge {
    @FieldName("id")
    @NotNull
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @FieldName("bidAttendId")
    @NotNull
    @Max(value=999999999, message="bidAttendId必须为数字")
    private Integer bidAttendId;

    @FieldName("deptId")
    @NotNull
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

    @FieldName("deptName")
    @NotNull
    @Size(max=145)
    private String deptName;

    @FieldName("projectName")
    @NotNull
    @Size(max=45)
    private String projectName;

    @FieldName("customerName")
    @NotNull
    @Size(max=45)
    private String customerName;

    @FieldName("项目规模")
    @NotNull
    @Size(max=45)
    private String projectScale;


    private String projectType;

    @FieldName("projectAddress")
    @NotNull
    @Size(max=45)
    private String projectAddress;

    @FieldName("projectContent")
    @NotNull
    @Size(max=1450)
    private String projectContent;

    @FieldName("chargeRule")
    @NotNull
    @Size(max=45)
    private String chargeRule;

    @FieldName("openTime")
    @NotNull
    @Size(max=45)
    private String openTime;

    @FieldName("exploreMan")
    @NotNull
    @Size(max=45)
    private String exploreMan;

    @FieldName("designMan")
    @NotNull
    @Size(max=45)
    private String designMan;

    @FieldName("constructionMan")
    @NotNull
    @Size(max=45)
    private String constructionMan;

    @FieldName("majorMen")
    @NotNull
    @Size(max=45)
    private String majorMen;

    @FieldName("exploreManName")
    @NotNull
    @Size(max=45)
    private String exploreManName;

    @FieldName("designManName")
    @NotNull
    @Size(max=45)
    private String designManName;

    @FieldName("constructionManName")
    @NotNull
    @Size(max=45)
    private String constructionManName;

    @FieldName("remark")
    @NotNull
    @Size(max=450)
    private String remark;

    @FieldName("isDeleted")
    @NotNull
    private Boolean deleted;

    @FieldName("creator")
    @NotEmpty
    @Size(max=45)
    private String creator;

    @FieldName("creatorName")
    @NotEmpty
    @Size(max=45)
    private String creatorName;

    @FieldName("gmtCreate")
    @NotNull
    private Date gmtCreate;

    @FieldName("gmtModified")
    @NotNull
    private Date gmtModified;

    @FieldName("processInstanceId")
    @NotEmpty
    @Size(max=45)
    private String processInstanceId;

    @FieldName("isProcessEnd")
    @NotNull
    private Boolean processEnd;
}