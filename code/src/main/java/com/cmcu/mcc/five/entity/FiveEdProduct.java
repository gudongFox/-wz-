package com.cmcu.mcc.five.entity;

import com.cmcu.common.annotation.FieldName;
import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveEdProduct {
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

    @FieldName("contractNo")
    @NotNull
    @Size(max=45)
    private String contractNo;

    @FieldName("stageName")
    @NotNull
    @Size(max=45)
    private String stageName;

    @FieldName("stepId")
    @NotNull
    @Max(value=999999999, message="stepId必须为数字")
    private Integer stepId;

    @FieldName("stepName")
    @NotNull
    @Size(max=45)
    private String stepName;

    @FieldName("deptId")
    @NotNull
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

    @FieldName("deptName")
    @NotNull
    @Size(max=145)
    private String deptName;

    @FieldName("验收单号")
    @NotNull
    @Size(max=45)
    private String formNo;

    @FieldName("日期")
    @NotNull
    @Size(max=45)
    private String productDate;

    @FieldName("姓名")
    @NotNull
    @Size(max=45)
    private String userName;

    @FieldName("电话")
    @NotNull
    @Size(max=45)
    private String userTel;

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