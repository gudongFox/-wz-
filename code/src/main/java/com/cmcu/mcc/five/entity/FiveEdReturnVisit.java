package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveEdReturnVisit {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45,message="businessKey长度不能大于45!")
    private String businessKey;

    @NotNull(message="projectId不能为空!")
    @Max(value=999999999, message="projectId必须为数字")
    private Integer projectId;

    @NotNull(message="projectName不能为空!")
    @Size(max=145,message="projectName长度不能大于145!")
    private String projectName;

    @NotNull(message="项目编号不能为空!")
    @Size(max=45,message="项目编号长度不能大于45!")
    private String projectNo;

    @NotNull(message="stageName不能为空!")
    @Size(max=45,message="stageName长度不能大于45!")
    private String stageName;

    @NotNull(message="stepId不能为空!")
    @Max(value=999999999, message="stepId必须为数字")
    private Integer stepId;

    @NotNull(message="stepName不能为空!")
    @Size(max=45,message="stepName长度不能大于45!")
    private String stepName;

    @NotNull(message="contractNo不能为空!")
    @Size(max=45,message="contractNo长度不能大于45!")
    private String contractNo;

    @NotNull(message="formNo不能为空!")
    @Size(max=45,message="formNo长度不能大于45!")
    private String formNo;

    @NotNull(message="建设单位不能为空!")
    @Size(max=45,message="建设单位长度不能大于45!")
    private String constructionOrg;

    @NotNull(message="建设地址不能为空!")
    @Size(max=45,message="建设地址长度不能大于45!")
    private String constructionAddress;

    @NotNull(message="设计完成日期不能为空!")
    @Size(max=45,message="设计完成日期长度不能大于45!")
    private String designFinishTime;

    @NotNull(message="竣工日期不能为空!")
    @Size(max=45,message="竣工日期长度不能大于45!")
    private String constructionFinishTime;

    @NotNull(message="回访人不能为空!")
    @Size(max=45,message="回访人长度不能大于45!")
    private String visitor;

    @NotNull(message="回访时间不能为空!")
    @Size(max=45,message="回访时间长度不能大于45!")
    private String visitTime;

    @NotNull(message="业主建议和意见不能为空!")
    @Size(max=45,message="业主建议和意见长度不能大于45!")
    private String ownerComment;

    @NotNull(message="受访人（单位）不能为空!")
    @Size(max=45,message="受访人（单位）长度不能大于45!")
    private String ownerName;

    @NotNull(message="设计单位处理意见不能为空!")
    @Size(max=45,message="设计单位处理意见长度不能大于45!")
    private String designSolve;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

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

    @NotNull(message="remark不能为空!")
    @Size(max=450,message="remark长度不能大于450!")
    private String remark;

    @NotNull(message="processInstanceId不能为空!")
    @Size(max=45,message="processInstanceId长度不能大于45!")
    private String processInstanceId;

    @NotNull(message="isProcessEnd不能为空!")
    private Boolean processEnd;

    @NotNull(message="回访形式不能为空!")
    @Size(max=45,message="回访形式长度不能大于45!")
    private String returnType;

    @NotNull(message="提出问题不能为空!")
    @Size(max=45,message="提出问题长度不能大于45!")
    private String raiseQuestion;

    @NotNull(message="措施不能为空!")
    @Size(max=45,message="措施长度不能大于45!")
    private String measure;
}