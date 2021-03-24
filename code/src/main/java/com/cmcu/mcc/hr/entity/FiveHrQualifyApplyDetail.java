package com.cmcu.mcc.hr.entity;

import com.cmcu.common.annotation.FieldName;
import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveHrQualifyApplyDetail {
    @FieldName("id")
    @NotNull
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @FieldName("qualifyId")
    @NotNull
    @Max(value=999999999, message="qualifyId必须为数字")
    private Integer qualifyId;

    @FieldName("userLogin")
    @NotNull
    @Size(max=45)
    private String userLogin;

    @FieldName("userName")
    @NotNull
    @Size(max=45)
    private String userName;

    @FieldName("deptId")
    @NotNull
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

    @FieldName("deptName")
    @NotNull
    @Size(max=145)
    private String deptName;

    @FieldName("从事专业")
    @NotNull
    @Size(max=45)
    private String majorName;

    @FieldName("年度")
    @NotNull
    @Max(value=999999999, message="年度必须为数字")
    private Integer checkYear;

    @FieldName("项目负责人")
    @NotNull
    private Boolean projectCharge;

    @FieldName("专业负责人")
    @NotNull
    private Boolean majorCharge;

    @FieldName("设计人")
    @NotNull
    private Boolean design;

    @FieldName("校核人")
    @NotNull
    private Boolean proofread;

    @FieldName("审核人")
    @NotNull
    private Boolean audit;

    @FieldName("审定人")
    @NotNull
    private Boolean approve;

    @FieldName("项目类型")
    @NotNull
    @Size(max=45)
    private String projectType;

    @FieldName("总设计师")
    @NotNull
    private Boolean chiefDesigner;

    @FieldName("是否兼职总设计师")
    @NotNull
    private Boolean proChief;

    @FieldName("isDeleted")
    @NotNull
    private Boolean deleted;

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