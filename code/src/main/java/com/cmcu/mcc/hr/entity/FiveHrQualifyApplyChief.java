package com.cmcu.mcc.hr.entity;

import com.cmcu.common.annotation.FieldName;
import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveHrQualifyApplyChief {
    @FieldName("id")
    @NotNull
    @Max(value=999999999, message="id")
    private Integer id;

    @FieldName("formNo")
    @NotNull
    @Size(max=45)
    private String formNo;

    @FieldName("申请类型")
    @NotNull
    @Size(max=45)
    private String type;

    @FieldName("检查年份")
    @NotNull
    @Size(max=45)
    private String checkYear;

    @FieldName("deptId")
    @NotNull
    @Max(value=999999999, message="deptId")
    private Integer deptId;

    @FieldName("deptName")
    @NotNull
    @Size(max=145)
    private String deptName;

    @FieldName("userLogin")
    @NotNull
    @Size(max=45)
    private String userLogin;

    @FieldName("userName")
    @NotNull
    @Size(max=45)
    private String userName;

    @FieldName("职务")
    @NotNull
    @Size(max=45)
    private String position;

    @FieldName("毕业院校")
    @NotNull
    @Size(max=45)
    private String graduateCollege;

    @FieldName("所学专业")
    @NotNull
    @Size(max=45)
    private String graduateMajor;

    @FieldName("职称")
    @NotNull
    @Size(max=45)
    private String title;

    @FieldName("任职时间")
    @NotNull
    @Size(max=45)
    private String titleTime;

    @FieldName("执业注册资格")
    @NotNull
    @Size(max=45)
    private String certification;

    @FieldName("现从事专业")
    @NotNull
    @Size(max=45)
    private String major;

    @FieldName("现专业起始时间")
    @NotNull
    @Size(max=45)
    private String majorTime;

    @FieldName("先技术工作岗位")
    @NotNull
    @Size(max=45)
    private String workPost;

    @FieldName("现承担设计项目类型")
    @NotNull
    @Size(max=45)
    private String projectTypeNow;

    @FieldName("申请承担设计项目类型")
    @NotNull
    @Size(max=45)
    private String projectTypeApply;

    @FieldName("业绩表现")
    @NotNull
    @Size(max=45)
    private String performance;

    @FieldName("申报单位意见")
    @NotNull
    @Size(max=45)
    private String departmentOpinion;

    @FieldName("初审意见")
    @NotNull
    @Size(max=45)
    private String firstOpinion;

    @FieldName("考核评审意见")
    @NotNull
    @Size(max=45)
    private String assessOpinion;

    @FieldName("审定意见")
    @NotNull
    @Size(max=45)
    private String approveOpinion;

    @FieldName("remark")
    @NotNull
    @Size(max=450)
    private String remark;

    @FieldName("isDeleted")
    @NotNull
    private Boolean deleted;

    @FieldName("gmtCreate")
    @NotNull
    private Date gmtCreate;

    @FieldName("gmtModified")
    @NotNull
    private Date gmtModified;

    @FieldName("creator")
    @NotNull
    @Size(max=45)
    private String creator;

    @FieldName("creatorName")
    @NotNull
    @Size(max=45)
    private String creatorName;

    @FieldName("processInstanceId")
    @NotNull
    @Size(max=45)
    private String processInstanceId;

    @FieldName("isProcessEnd")
    @NotNull
    private Boolean processEnd;
}