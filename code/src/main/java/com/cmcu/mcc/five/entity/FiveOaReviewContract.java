package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaReviewContract {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45,message="businessKey长度不能大于45!")
    private String businessKey;

    @NotNull(message="单位名称不能为空!")
    @Max(value=999999999, message="单位名称必须为数字")
    private Integer deptId;

    @NotNull(message="承办部门不能为空!")
    @Size(max=45,message="承办部门长度不能大于45!")
    private String deptName;

    @NotNull(message="项目类别不能为空!")
    @Size(max=45,message="项目类别长度不能大于45!")
    private String projectType;

    @NotNull(message="日期不能为空!")
    @Size(max=45,message="日期长度不能大于45!")
    private String contractTime;

    @NotNull(message="项目名称不能为空!")
    @Size(max=45,message="项目名称长度不能大于45!")
    private String projectName;

    @NotNull(message="合同编号不能为空!")
    @Size(max=45,message="合同编号长度不能大于45!")
    private String contractNo;

    @NotNull(message="合同额：（万元）不能为空!")
    @Size(max=45,message="合同额：（万元）长度不能大于45!")
    private String contractMoney;

    @NotNull(message="评审级别不能为空!")
    @Size(max=45,message="评审级别长度不能大于45!")
    private String reviewLevel;

    @NotNull(message="甲方不能为空!")
    @Size(max=45,message="甲方长度不能大于45!")
    private String firstParty;

    @NotNull(message="乙方不能为空!")
    @Size(max=45,message="乙方长度不能大于45!")
    private String secondParty;

    @NotNull(message="总设计师不能为空!")
    @Size(max=45,message="总设计师长度不能大于45!")
    private String totalDesigner;

    @NotNull(message="totalDesignerName不能为空!")
    @Size(max=45,message="totalDesignerName长度不能大于45!")
    private String totalDesignerName;

    @NotNull(message="评审时间不能为空!")
    @Size(max=45,message="评审时间长度不能大于45!")
    private String reviewTime;

    @NotNull(message="评审地点不能为空!")
    @Size(max=45,message="评审地点长度不能大于45!")
    private String reviewAddress;

    @NotNull(message="reviewUser不能为空!")
    @Size(max=45,message="reviewUser长度不能大于45!")
    private String reviewUser;

    @NotNull(message="reviewUserName不能为空!")
    @Size(max=45,message="reviewUserName长度不能大于45!")
    private String reviewUserName;

    @NotNull(message="合同内容不能为空!")
    @Size(max=45,message="合同内容长度不能大于45!")
    private String contractContent;

    @NotNull(message="评审内容不能为空!")
    @Size(max=45,message="评审内容长度不能大于45!")
    private String reviewContent;

    @NotNull(message="评审结论不能为空!")
    @Size(max=45,message="评审结论长度不能大于45!")
    private String reviewConclusion;

    @NotNull(message="reviewLawId不能为空!")
    @Size(max=45,message="reviewLawId长度不能大于45!")
    private String reviewLawId;

    @NotNull(message="法律审查意见流程不能为空!")
    @Size(max=45,message="法律审查意见流程长度不能大于45!")
    private String reviewLawName;

    @NotNull(message="remark不能为空!")
    @Size(max=45,message="remark长度不能大于45!")
    private String remark;

    @NotNull(message="creator不能为空!")
    @Size(max=45,message="creator长度不能大于45!")
    private String creator;

    @NotNull(message="creatorName不能为空!")
    @Size(max=45,message="creatorName长度不能大于45!")
    private String creatorName;

    @NotNull(message="gmtCreate不能为空!")
    private Date gmtCreate;

    @NotNull(message="gmtModified不能为空!")
    private Date gmtModified;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="isProcessEnd不能为空!")
    private Boolean processEnd;

    @NotNull(message="processInstanceId不能为空!")
    @Size(max=45,message="processInstanceId长度不能大于45!")
    private String processInstanceId;
}