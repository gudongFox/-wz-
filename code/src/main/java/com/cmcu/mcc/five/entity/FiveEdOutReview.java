package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveEdOutReview {
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

    @NotNull(message="formNo不能为空!")
    @Size(max=45,message="formNo长度不能大于45!")
    private String formNo;

    @NotNull(message="stepName不能为空!")
    @Size(max=45,message="stepName长度不能大于45!")
    private String stepName;

    @NotNull(message="contractNo不能为空!")
    @Size(max=45,message="contractNo长度不能大于45!")
    private String contractNo;

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

    @NotNull(message="reviewLevel不能为空!")
    @Size(max=45,message="reviewLevel长度不能大于45!")
    private String reviewLevel;

    @NotNull(message="reviewType不能为空!")
    @Size(max=45,message="reviewType长度不能大于45!")
    private String reviewType;

    @NotNull(message="attendUser不能为空!")
    @Size(max=450,message="attendUser长度不能大于450!")
    private String attendUser;
}