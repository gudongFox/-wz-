package com.cmcu.mcc.five.entity;

import com.cmcu.common.annotation.FieldName;
import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveEdServiceChange {
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
    @Size(max=45)
    private String projectName;

    @FieldName("项目编号")
    @NotNull
    @Size(max=45)
    private String projectNo;

    @FieldName("contractNo")
    @NotNull
    @Size(max=45)
    private String contractNo;

    @FieldName("stepId")
    @NotNull
    @Max(value=999999999, message="stepId必须为数字")
    private Integer stepId;

    @FieldName("stageName")
    @NotNull
    @Size(max=45)
    private String stageName;

    @FieldName("formNo")
    @NotNull
    @Size(max=45)
    private String formNo;

    @FieldName("stepName")
    @NotNull
    @Size(max=45)
    private String stepName;

    @FieldName("buildName")
    @NotNull
    @Size(max=45)
    private String buildName;

    @FieldName("majorName")
    @NotNull
    @Size(max=45)
    private String majorName;

    @FieldName("changeNo")
    @NotNull
    @Size(max=45)
    private String changeNo;

    @FieldName("drawNo")
    @NotNull
    @Size(max=45)
    private String drawNo;

    @FieldName("changeReason")
    @NotNull
    @Size(max=145)
    private String changeReason;

    @FieldName("changeReasonDetail")
    @NotNull
    @Size(max=45)
    private String changeReasonDetail;

    @FieldName("needChangeOther")
    @NotNull
    private Boolean needChangeOther;

    @FieldName("needChangeMajor")
    @NotNull
    @Size(max=145)
    private String needChangeMajor;

    @FieldName("changeContent")
    @NotNull
    @Size(max=4500)
    private String changeContent;

    @FieldName("attachCount")
    @NotNull
    @Max(value=999999999, message="attachCount必须为数字")
    private Integer attachCount;

    @FieldName("attachNo")
    @NotNull
    @Size(max=45)
    private String attachNo;

    @FieldName("remark")
    @NotNull
    @Size(max=450)
    private String remark;

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

    @FieldName("processInstanceId")
    @NotNull
    @Size(max=45)
    private String processInstanceId;

    @FieldName("isProcessEnd")
    @NotNull
    private Boolean processEnd;
}