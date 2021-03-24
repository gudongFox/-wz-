package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveEdMajorDrawingCheck {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="projectId不能为空!")
    @Max(value=999999999, message="projectId必须为数字")
    private Integer projectId;

    @NotNull(message="项目名称不能为空!")
    @Size(max=145, message="项目名称长度不能超过145")
    private String projectName;

    @NotNull(message="项目编号不能为空!")
    @Size(max=45, message="项目编号长度不能超过45")
    private String projectNo;

    @NotNull(message="stageName不能为空!")
    @Size(max=45, message="stageName长度不能超过45")
    private String stageName;

    @NotNull(message="stepId不能为空!")
    @Max(value=999999999, message="stepId必须为数字")
    private Integer stepId;

    @NotNull(message="stepName不能为空!")
    @Size(max=45, message="stepName长度不能超过45")
    private String stepName;

    @NotNull(message="验收单号不能为空!")
    @Size(max=45, message="验收单号长度不能超过45")
    private String formNo;

    @NotNull(message="contractNo不能为空!")
    @Size(max=45, message="contractNo长度不能超过45")
    private String contractNo;

    @NotNull(message="专业不能为空!")
    @Size(max=45, message="专业长度不能超过45")
    private String majorName;

    @NotNull(message="项目总师不能为空!")
    @Size(max=45, message="项目总师长度不能超过45")
    private String totalChargeMan;

    @NotNull(message="totalChargeManName不能为空!")
    @Size(max=45, message="totalChargeManName长度不能超过45")
    private String totalChargeManName;

    @NotNull(message="交验日期不能为空!")
    @Size(max=45, message="交验日期长度不能超过45")
    private String checkTime;

    @NotNull(message="applyMan不能为空!")
    @Size(max=45, message="applyMan长度不能超过45")
    private String applyMan;

    @NotNull(message="applyManName不能为空!")
    @Size(max=45, message="applyManName长度不能超过45")
    private String applyManName;

    @Size(max=45, message="申请人电话长度不能超过45")
    private String applyPhone;

    @NotNull(message="buildIds不能为空!")
    @Size(max=45, message="buildIds长度不能超过45")
    private String buildIds;

    @NotNull(message="建筑物号不能为空!")
    @Size(max=450, message="建筑物号长度不能超过450")
    private String buildName;

    @NotNull(message="deptId不能为空!")
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

    @NotNull(message="设计单位不能为空!")
    @Size(max=145, message="设计单位长度不能超过145")
    private String deptName;

    @NotNull(message="remark不能为空!")
    @Size(max=450, message="remark长度不能超过450")
    private String remark;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="creator不能为空!")
    @Size(max=45, message="creator长度不能超过45")
    private String creator;

    @NotNull(message="creatorName不能为空!")
    @Size(max=45, message="creatorName长度不能超过45")
    private String creatorName;

    @NotNull(message="gmtCreate不能为空!")
    private Date gmtCreate;

    @NotNull(message="gmtModified不能为空!")
    private Date gmtModified;

    @NotNull(message="processInstanceId不能为空!")
    @Size(max=45, message="processInstanceId长度不能超过45")
    private String processInstanceId;

    @NotNull(message="isProcessEnd不能为空!")
    private Boolean processEnd;
}