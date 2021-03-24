package com.cmcu.mcc.business.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveBusinessTenderDocumentReview {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="项目信息备案不能为空!")
    @Max(value=999999999, message="项目信息备案必须为数字")
    private Integer recordId;

    @NotNull(message="formNo不能为空!")
    @Size(max=45, message="formNo长度不能超过45")
    private String formNo;

    @NotNull(message="deptId不能为空!")
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

    @NotNull(message="deptName不能为空!")
    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="计划发包时间不能为空!")
    @Size(max=45, message="计划发包时间长度不能超过45")
    private String beginTime;

    @NotNull(message="计划开始时间不能为空!")
    @Size(max=45, message="计划开始时间长度不能超过45")
    private String planBeginTime;

    @NotNull(message="总投资不能为空!")
    @Size(max=45, message="总投资长度不能超过45")
    private String totalPrice;

    @NotNull(message="备注不能为空!")
    @Size(max=450, message="备注长度不能超过450")
    private String remark;

    @NotNull(message="creator不能为空!")
    @Size(max=45, message="creator长度不能超过45")
    private String creator;

    @NotNull(message="creatorName不能为空!")
    @Size(max=45, message="creatorName长度不能超过45")
    private String creatorName;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="创建时间不能为空!")
    private Date gmtCreate;

    @NotNull(message="修改时间不能为空!")
    private Date gmtModified;

    @NotNull(message="processInstanceId不能为空!")
    @Size(max=45, message="processInstanceId长度不能超过45")
    private String processInstanceId;

    @NotNull(message="isProcessEnd不能为空!")
    private Boolean processEnd;

    @NotNull(message="projectName不能为空!")
    @Size(max=45, message="projectName长度不能超过45")
    private String projectName;

    @NotNull(message="评审级别不能为空!")
    @Size(max=45, message="评审级别长度不能超过45")
    private String projectLevel;

    @NotNull(message="项目地点不能为空!")
    @Size(max=45, message="项目地点长度不能超过45")
    private String projectLocation;

    @NotNull(message="项目经理不能为空!")
    @Size(max=45, message="项目经理长度不能超过45")
    private String projectManager;

    @NotNull(message="projectManagerName不能为空!")
    @Size(max=45, message="projectManagerName长度不能超过45")
    private String projectManagerName;

    @NotNull(message="标书来源不能为空!")
    @Size(max=45, message="标书来源长度不能超过45")
    private String tenderSource;

    @NotNull(message="联合体不能为空!")
    @Size(max=45, message="联合体长度不能超过45")
    private String combo;

    @NotNull(message="reviewUser不能为空!")
    @Size(max=45, message="reviewUser长度不能超过45")
    private String reviewUser;

    @NotNull(message="reviewUsername不能为空!")
    @Size(max=45, message="reviewUsername长度不能超过45")
    private String reviewUsername;

    @NotNull(message="deptReviewUser不能为空!")
    @Size(max=45, message="deptReviewUser长度不能超过45")
    private String deptReviewUser;

    @NotNull(message="deptReviewUsername不能为空!")
    @Size(max=45, message="deptReviewUsername长度不能超过45")
    private String deptReviewUsername;
}