package com.cmcu.mcc.five.entity;

import com.cmcu.common.annotation.FieldName;
import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveEdServiceDiscuss {
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

    @FieldName("项目编号")
    @NotNull
    @Size(max=45)
    private String projectNo;

    @FieldName("projectName")
    @NotNull
    @Size(max=145)
    private String projectName;

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

    @FieldName("formNo")
    @NotNull
    @Size(max=45)
    private String formNo;

    @FieldName("buildName")
    @NotNull
    @Size(max=45)
    private String buildName;

    @FieldName("majorName")
    @NotNull
    @Size(max=45)
    private String majorName;

    @FieldName("drawNo")
    @NotNull
    @Size(max=45)
    private String drawNo;

    @FieldName("changeNo")
    @NotNull
    @Size(max=45)
    private String changeNo;

    @FieldName("changeReason")
    @NotNull
    @Size(max=45)
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
    @Size(max=45)
    private String needChangeMajor;

    @FieldName("changeContent")
    @NotNull
    @Size(max=45)
    private String changeContent;

    @FieldName("洽商变更内容")
    @NotNull
    @Size(max=450)
    private String discussContent;

    @FieldName("attachNo")
    @NotNull
    @Size(max=45)
    private String attachNo;

    @FieldName("attachCount")
    @NotNull
    @Max(value=999999999, message="attachCount必须为数字")
    private Integer attachCount;

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