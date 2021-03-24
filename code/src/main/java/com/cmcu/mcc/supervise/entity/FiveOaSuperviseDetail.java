package com.cmcu.mcc.supervise.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaSuperviseDetail {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="主表id不能为空!")
    @Max(value=999999999, message="主表id必须为数字")
    private Integer superviseId;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="承办单位不能为空!")
    @Max(value=999999999, message="承办单位必须为数字")
    private Integer deptId;

     @NotNull(message="deptName不能为空!")
    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="督办类型不能为空!")
    @Size(max=45, message="督办类型长度不能超过45")
    private String superviseType;

    @NotNull(message="任务来源不能为空!")
    @Size(max=45, message="任务来源长度不能超过45")
    private String taskSource;

    @NotNull(message="任务内容不能为空!")
    @Size(max=450, message="任务内容长度不能超过450")
    private String taskDefinition;

    @NotNull(message="办结时限不能为空!")
    @Size(max=45, message="办结时限长度不能超过45")
    private String timeLimit;

    @NotNull(message="反馈周期不能为空!")
    @Size(max=45, message="反馈周期长度不能超过45")
    private String feedbackTime;

    @NotNull(message="办理进度不能为空!")
    @Size(max=45, message="办理进度长度不能超过45")
    private String transactionPlan;

    @NotNull(message="transactor不能为空!")
    @Size(max=45, message="transactor长度不能超过45")
    private String transactor;

    @NotNull(message="办理人不能为空!")
    @Size(max=45, message="办理人长度不能超过45")
    private String transactorName;

    @NotNull(message="批示领导意见不能为空!")
    @Size(max=450, message="批示领导意见长度不能超过450")
    private String companyLeaderOpinion;

    @NotNull(message="remark不能为空!")
    @Size(max=45, message="remark长度不能超过45")
    private String remark;

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

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="isProcessEnd不能为空!")
    private Boolean processEnd;

    @NotNull(message="processInstanceId不能为空!")
    @Size(max=45, message="processInstanceId长度不能超过45")
    private String processInstanceId;
}