package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveEdStamp {
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
    @Size(max=45,message="projectName长度不能大于45!")
    private String projectName;

    @NotNull(message="项目编号不能为空!")
    @Size(max=45,message="项目编号长度不能大于45!")
    private String projectNo;

    @NotNull(message="contractNo不能为空!")
    @Size(max=45,message="contractNo长度不能大于45!")
    private String contractNo;

    @NotNull(message="stageName不能为空!")
    @Size(max=45,message="stageName长度不能大于45!")
    private String stageName;

    @NotNull(message="stepId不能为空!")
    @Max(value=999999999, message="stepId必须为数字")
    private Integer stepId;

    @NotNull(message="stepName不能为空!")
    @Size(max=45,message="stepName长度不能大于45!")
    private String stepName;

    @NotNull(message="formNo不能为空!")
    @Size(max=45,message="formNo长度不能大于45!")
    private String formNo;

    @NotNull(message="deptId不能为空!")
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

    @NotNull(message="deptName不能为空!")
    @Size(max=145,message="deptName长度不能大于145!")
    private String deptName;

    @NotNull(message="出图章,其他公章不能为空!")
    @Size(max=45,message="出图章,其他公章长度不能大于45!")
    private String stampType;

    @NotNull(message="专业不能为空!")
    @Size(max=45,message="专业长度不能大于45!")
    private String majors;

    @NotNull(message="用印时间不能为空!")
    @Size(max=45,message="用印时间长度不能大于45!")
    private String useTime;

    @NotNull(message="文件类别不能为空!")
    @Size(max=45,message="文件类别长度不能大于45!")
    private String docType;

    @NotNull(message="用印明细不能为空!")
    @Size(max=145,message="用印明细长度不能大于145!")
    private String useStamp;

    @NotNull(message="用印事由不能为空!")
    @Size(max=450,message="用印事由长度不能大于450!")
    private String useDescription;

    @NotNull(message="remark不能为空!")
    @Size(max=450,message="remark长度不能大于450!")
    private String remark;

    @NotNull(message="creator不能为空!")
    @Size(max=45,message="creator长度不能大于45!")
    private String creator;

    @NotNull(message="creatorName不能为空!")
    @Size(max=45,message="creatorName长度不能大于45!")
    private String creatorName;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="gmtCreate不能为空!")
    private Date gmtCreate;

    @NotNull(message="gmtModified不能为空!")
    private Date gmtModified;

    @NotNull(message="processInstanceId不能为空!")
    @Size(max=45,message="processInstanceId长度不能大于45!")
    private String processInstanceId;

    @NotNull(message="isProcessEnd不能为空!")
    private Boolean processEnd;

    @NotNull(message="plotIds不能为空!")
    @Size(max=45,message="plotIds长度不能大于45!")
    private String plotIds;
}