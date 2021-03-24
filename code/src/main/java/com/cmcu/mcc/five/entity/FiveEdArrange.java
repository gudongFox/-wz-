package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveEdArrange {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="stepId不能为空!")
    @Max(value=999999999, message="stepId必须为数字")
    private Integer stepId;

    @NotNull(message="projectId不能为空!")
    @Max(value=999999999, message="projectId必须为数字")
    private Integer projectId;

    @NotNull(message="项目编号不能为空!")
    @Size(max=45, message="项目编号长度不能超过45")
    private String projectNo;

    @NotNull(message="部门名称不能为空!")
    @Size(max=145, message="部门名称长度不能超过145")
    private String deptName;

    @NotNull(message="projectName不能为空!")
    @Size(max=145, message="projectName长度不能超过145")
    private String projectName;

    @NotNull(message="stageName不能为空!")
    @Size(max=45, message="stageName长度不能超过45")
    private String stageName;

    @NotNull(message="stepName不能为空!")
    @Size(max=45, message="stepName长度不能超过45")
    private String stepName;

    @NotNull(message="编号不能为空!")
    @Size(max=45, message="编号长度不能超过45")
    private String formNo;

    @NotNull(message="合同号不能为空!")
    @Size(max=45, message="合同号长度不能超过45")
    private String contractNo;

    @NotNull(message="方案讨论日期不能为空!")
    @Size(max=45, message="方案讨论日期长度不能超过45")
    private String planDiscussTime;

    @NotNull(message="设计评审方式不能为空!")
    @Size(max=45, message="设计评审方式长度不能超过45")
    private String reviewType;

    @NotNull(message="校审制度不能为空!")
    @Size(max=45, message="校审制度长度不能超过45")
    private String designReviewType;

    @NotNull(message="设计评审日期不能为空!")
    @Size(max=45, message="设计评审日期长度不能超过45")
    private String designReviewTime;

    @NotNull(message="交总师时间不能为空!")
    @Size(max=45, message="交总师时间长度不能超过45")
    private String submitTotalDesignerTime;

    @NotNull(message="验收日期不能为空!")
    @Size(max=45, message="验收日期长度不能超过45")
    private String acceptTime;

    @NotNull(message="发图日期不能为空!")
    @Size(max=45, message="发图日期长度不能超过45")
    private String sendPictureTime;

    @NotNull(message="涉及专业不能为空!")
    @Size(max=145, message="涉及专业长度不能超过145")
    private String designMajorList;

    @NotNull(message="planMajorList不能为空!")
    @Size(max=145, message="planMajorList长度不能超过145")
    private String planMajorList;

    @NotNull(message="remark不能为空!")
    @Size(max=145, message="remark长度不能超过145")
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