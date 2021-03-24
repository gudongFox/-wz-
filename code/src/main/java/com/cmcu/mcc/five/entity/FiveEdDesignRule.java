package com.cmcu.mcc.five.entity;

import com.cmcu.common.annotation.FieldName;
import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveEdDesignRule {
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

    @FieldName("projectName")
    @NotNull
    @Size(max=145)
    private String projectName;

    @FieldName("projectNo")
    @NotNull
    @Size(max=45)
    private String projectNo;

    @FieldName("stepId")
    @NotNull
    @Max(value=999999999, message="stepId必须为数字")
    private Integer stepId;

    @FieldName("stepName")
    @NotNull
    @Size(max=45)
    private String stepName;

    @FieldName("stageName")
    @NotNull
    @Size(max=45)
    private String stageName;

    @FieldName("contractNo")
    @NotNull
    @Size(max=45)
    private String contractNo;

    @FieldName("编号")
    @NotNull
    @Size(max=45)
    private String formNo;

    @FieldName("proofreadMen")
    @NotNull
    @Size(max=145)
    private String proofreadMen;

    @FieldName("proofreadMenName")
    @NotNull
    @Size(max=145)
    private String proofreadMenName;

    @FieldName("auditMen")
    @NotNull
    @Size(max=145)
    private String auditMen;

    @FieldName("auditMenName")
    @NotNull
    @Size(max=145)
    private String auditMenName;

    @FieldName("approveMen")
    @NotNull
    @Size(max=145)
    private String approveMen;

    @FieldName("approveMenName")
    @NotNull
    @Size(max=145)
    private String approveMenName;

    @FieldName("designReviewType")
    @NotNull
    @Size(max=145)
    private String designReviewType;

    @FieldName("creator")
    @NotNull
    @Size(max=45)
    private String creator;

    @FieldName("creatorName")
    @NotNull
    @Size(max=45)
    private String creatorName;

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

    @FieldName("processInstanceId")
    @NotNull
    @Size(max=45)
    private String processInstanceId;

    @FieldName("isProcessEnd")
    @NotNull
    private Boolean processEnd;
}