package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveEdReviewMajor {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="projectId不能为空!")
    @Max(value=999999999, message="projectId必须为数字")
    private Integer projectId;

    @NotNull(message="projectName不能为空!")
    @Size(max=145, message="projectName长度不能超过145")
    private String projectName;

    @NotNull(message="项目编号不能为空!")
    @Size(max=45, message="项目编号长度不能超过45")
    private String projectNo;

    @NotNull(message="contractNo不能为空!")
    @Size(max=45, message="contractNo长度不能超过45")
    private String contractNo;

    @NotNull(message="stepId不能为空!")
    @Max(value=999999999, message="stepId必须为数字")
    private Integer stepId;

    @NotNull(message="stepName不能为空!")
    @Size(max=145, message="stepName长度不能超过145")
    private String stepName;

    @NotNull(message="stageName不能为空!")
    @Size(max=145, message="stageName长度不能超过145")
    private String stageName;

    @NotNull(message="majorName不能为空!")
    @Size(max=45, message="majorName长度不能超过45")
    private String majorName;

    @NotNull(message="设计评审方式不能为空!")
    @Size(max=45, message="设计评审方式长度不能超过45")
    private String designReviewType;

    @NotNull(message="评审形式：项目组评审、设计单位评审、公司评审不能为空!")
    @Size(max=45, message="评审形式：项目组评审、设计单位评审、公司评审长度不能超过45")
    private String reviewType;

    @NotNull(message="参与人不能为空!")
    @Size(max=450, message="参与人长度不能超过450")
    private String attendUser;

    @NotNull(message="attendUserName不能为空!")
    @Size(max=450, message="attendUserName长度不能超过450")
    private String attendUserName;

    @NotNull(message="proofreadMen不能为空!")
    @Size(max=145, message="proofreadMen长度不能超过145")
    private String proofreadMen;

    @NotNull(message="proofreadMenName不能为空!")
    @Size(max=145, message="proofreadMenName长度不能超过145")
    private String proofreadMenName;

    @NotNull(message="auditMen不能为空!")
    @Size(max=145, message="auditMen长度不能超过145")
    private String auditMen;

    @NotNull(message="auditMenName不能为空!")
    @Size(max=145, message="auditMenName长度不能超过145")
    private String auditMenName;

    @NotNull(message="approveMen不能为空!")
    @Size(max=145, message="approveMen长度不能超过145")
    private String approveMen;

    @NotNull(message="approveMenName不能为空!")
    @Size(max=145, message="approveMenName长度不能超过145")
    private String approveMenName;

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

    @NotNull(message="方案概况不能为空!")
    @Size(max=145, message="方案概况长度不能超过145")
    private String solutionOverview;

    @NotNull(message="对方案指导意见不能为空!")
    @Size(max=145, message="对方案指导意见长度不能超过145")
    private String solutionGuidance;

    @NotNull(message="重要技术问题讨论不能为空!")
    @Size(max=145, message="重要技术问题讨论长度不能超过145")
    private String solutionDiscussion;

    @NotNull(message="主持人不能为空!")
    @Size(max=145, message="主持人长度不能超过145")
    private String hostMen;

    @NotNull(message="hostMenName不能为空!")
    @Size(max=145, message="hostMenName长度不能超过145")
    private String hostMenName;
}