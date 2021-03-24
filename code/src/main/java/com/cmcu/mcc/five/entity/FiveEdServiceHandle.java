package com.cmcu.mcc.five.entity;

import com.cmcu.common.annotation.FieldName;
import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveEdServiceHandle {
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

    @FieldName("项目名称")
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

    @FieldName("formNo")
    @NotNull
    @Size(max=45)
    private String formNo;

    @FieldName("用户单位名称")
    @NotNull
    @Size(max=145)
    private String departmentName;

    @FieldName("registerTime")
    @NotNull
    @Size(max=45)
    private String registerTime;

    @FieldName("类别")
    @NotNull
    @Size(max=45)
    private String category;

    @FieldName("登记编号")
    @NotNull
    @Size(max=45)
    private String registerNo;

    @FieldName("registerContent")
    @NotNull
    @Size(max=450)
    private String registerContent;

    @FieldName("主管领导总设计师意见")
    @NotNull
    @Size(max=450)
    private String departmentReview;

    @FieldName("专业人员处理意见")
    @NotNull
    @Size(max=450)
    private String specialistReview;

    @FieldName("经办人")
    @NotNull
    @Size(max=45)
    private String agent;

    @FieldName("replyTime")
    @NotNull
    @Size(max=45)
    private String replyTime;

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