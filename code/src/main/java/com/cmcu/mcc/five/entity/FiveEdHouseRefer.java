package com.cmcu.mcc.five.entity;

import com.cmcu.common.annotation.FieldName;
import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveEdHouseRefer {
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
    @Size(max=45)
    private String projectName;

    @FieldName("stepId")
    @NotNull
    @Max(value=999999999, message="stepId必须为数字")
    private Integer stepId;

    @FieldName("stageName")
    @NotNull
    @Size(max=45)
    private String stageName;

    @FieldName("stepName")
    @NotNull
    @Size(max=45)
    private String stepName;

    @FieldName("contractNo")
    @NotNull
    @Size(max=45)
    private String contractNo;

    @FieldName("formNo")
    @NotNull
    @Size(max=45)
    private String formNo;

    @FieldName("提出专业")
    @NotNull
    @Size(max=45)
    private String sourceMajor;

    @FieldName("接受专业")
    @NotNull
    @Size(max=45)
    private String destMajor;

    @FieldName("提出专业说明")
    @NotNull
    @Size(max=4500)
    private String referDesc;

    @FieldName("接受专业说明")
    @NotNull
    @Size(max=4500)
    private String destResult;

    @FieldName("remark")
    @NotNull
    @Size(max=45)
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