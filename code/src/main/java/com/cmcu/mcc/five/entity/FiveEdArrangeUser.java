package com.cmcu.mcc.five.entity;

import com.cmcu.common.annotation.FieldName;
import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveEdArrangeUser {
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
    @Size(max=45)
    private String majorName;

    @FieldName("专业负责人")
    @NotNull
    @Size(max=45)
    private String majorChargeMen;

    @FieldName("专业负责人名字")
    @NotNull
    @Size(max=45)
    private String majorChargeMenName;

    @FieldName("专业负责人电话")
    @NotNull
    @Size(max=145)
    private String majorChargeMenMobile;

    @FieldName("审定人")
    @NotNull
    @Size(max=45)
    private String approveMen;

    @FieldName("approveMenName")
    @NotNull
    @Size(max=45)
    private String approveMenName;

    @FieldName("校队")
    @NotNull
    @Size(max=45)
    private String proofreadMen;

    @FieldName("proofreadMenName")
    @NotNull
    @Size(max=45)
    private String proofreadMenName;

    @FieldName("设计人")
    @NotNull
    @Size(max=145)
    private String designMen;

    @FieldName("designMenName")
    @NotNull
    @Size(max=145)
    private String designMenName;

    @FieldName("审核")
    @NotNull
    @Size(max=45)
    private String auditMen;

    @FieldName("auditMenName")
    @NotNull
    @Size(max=45)
    private String auditMenName;

    @FieldName("标准化审查")
    @NotNull
    @Size(max=45)
    private String criterionExamineMen;

    @FieldName("criterionExamineMenName")
    @NotNull
    @Size(max=45)
    private String criterionExamineMenName;

    @FieldName("所有人")
    @NotNull
    @Size(max=450)
    private String allMen;

    @FieldName("allMenName")
    @NotNull
    @Size(max=450)
    private String allMenName;

    @FieldName("isDeleted")
    @NotNull
    private Boolean deleted;

    @FieldName("gmtCreate")
    @NotNull
    private Date gmtCreate;

    @FieldName("gmtModified")
    @NotNull
    private Date gmtModified;

    @FieldName("真实项目负责人")
    @NotNull
    @Size(max=45)
    private String realMajorChargeMen;

    @FieldName("realMajorChargeMenName")
    @NotNull
    @Size(max=45)
    private String realMajorChargeMenName;

    @FieldName("realMajorChargeMenMobile")
    @NotNull
    @Size(max=45)
    private String realMajorChargeMenMobile;

    @FieldName("realApproveMen")
    @NotNull
    @Size(max=45)
    private String realApproveMen;

    @FieldName("realApproveMenName")
    @NotNull
    @Size(max=45)
    private String realApproveMenName;

    @FieldName("realProofreadMen")
    @NotNull
    @Size(max=45)
    private String realProofreadMen;

    @FieldName("realProofreadMenName")
    @NotNull
    @Size(max=45)
    private String realProofreadMenName;

    @FieldName("realDesignMen")
    @NotNull
    @Size(max=450)
    private String realDesignMen;

    @FieldName("realDesignMenName")
    @NotNull
    @Size(max=450)
    private String realDesignMenName;

    @FieldName("realAuditMen")
    @NotNull
    @Size(max=45)
    private String realAuditMen;

    @FieldName("realAuditMenName")
    @NotNull
    @Size(max=45)
    private String realAuditMenName;

    @FieldName("realCriterionExamineMen")
    @NotNull
    @Size(max=45)
    private String realCriterionExamineMen;

    @FieldName("realCriterionExamineMenName")
    @NotNull
    @Size(max=45)
    private String realCriterionExamineMenName;

    @FieldName("seq")
    @NotNull
    @Max(value=999999999, message="seq必须为数字")
    private Integer seq;
}