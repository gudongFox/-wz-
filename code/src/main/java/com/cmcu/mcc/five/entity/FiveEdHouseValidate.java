package com.cmcu.mcc.five.entity;

import com.cmcu.common.annotation.FieldName;
import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveEdHouseValidate {
    @FieldName("id")
    @NotNull
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @FieldName("businessKey")
    @NotNull
    @Size(max=45)
    private String businessKey;

    @FieldName("projectId")
    @NotNull
    @Max(value=999999999, message="projectId必须为数字")
    private Integer projectId;

    @FieldName("stepId")
    @NotNull
    @Max(value=999999999, message="stepId必须为数字")
    private Integer stepId;

    @FieldName("projectName")
    @NotNull
    @Size(max=45)
    private String projectName;

    @FieldName("项目编号")
    @NotNull
    @Size(max=45)
    private String projectNo;

    @FieldName("stageName")
    @NotNull
    @Size(max=45)
    private String stageName;

    @FieldName("stepName")
    @NotNull
    @Size(max=45)
    private String stepName;

    @FieldName("formNo")
    @NotNull
    @Size(max=45)
    private String formNo;

    @FieldName("contractNo")
    @NotNull
    @Size(max=45)
    private String contractNo;

    @FieldName("majorName")
    @NotNull
    @Size(max=45)
    private String majorName;

    @FieldName("校核人")
    @NotNull
    @Size(max=45)
    private String proofreadMen;

    @FieldName("proofreadMenName")
    @NotNull
    @Size(max=45)
    private String proofreadMenName;

    @FieldName("审核人")
    @NotNull
    @Size(max=45)
    private String auditMen;

    @FieldName("auditMenName")
    @NotNull
    @Size(max=45)
    private String auditMenName;

    @FieldName("批准人")
    @NotNull
    @Size(max=45)
    private String approveMen;

    @FieldName("approveMenName")
    @NotNull
    @Size(max=45)
    private String approveMenName;

    @FieldName("majorChargeMan")
    @NotNull
    @Size(max=45)
    private String majorChargeMan;

    @FieldName("majorChargeManName")
    @NotNull
    @Size(max=45)
    private String majorChargeManName;

    @FieldName("设计校审方式")
    @NotNull
    @Size(max=45)
    private String designReviewType;

    @FieldName("remark")
    @NotNull
    @Size(max=450)
    private String remark;

    @FieldName("isDeleted")
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

    @FieldName("processInstanceId")
    @NotNull
    @Size(max=45)
    private String processInstanceId;

    @FieldName("processEnd")
    @NotNull
    private Boolean processEnd;

    @FieldName("会签人员")
    @NotNull
    @Size(max=145)
    private String otherMen;

    @FieldName("会签人员名称")
    @NotNull
    @Size(max=145)
    private String otherMenName;
}