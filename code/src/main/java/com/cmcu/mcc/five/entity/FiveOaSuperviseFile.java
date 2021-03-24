package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaSuperviseFile {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="督办单位不能为空!")
    @Max(value=999999999, message="督办单位必须为数字")
    private Integer deptId;

    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="文件标题不能为空!")
    @Size(max=45, message="文件标题长度不能超过45")
    private String fileHeader;

    @NotNull(message="督办类型不能为空!")
    @Size(max=45, message="督办类型长度不能超过45")
    private String superviseType;

    @NotNull(message="反馈周期不能为空!")
    @Size(max=45, message="反馈周期长度不能超过45")
    private String feedbackTime;

    @NotNull(message="任务来源不能为空!")
    @Size(max=45, message="任务来源长度不能超过45")
    private String transactionPlan;

    @NotNull(message="任务内容不能为空!")
    @Size(max=450, message="任务内容长度不能超过450")
    private String taskDefinition;

    @NotNull(message="办结时限不能为空!")
    @Size(max=45, message="办结时限长度不能超过45")
    private String timeLimit;

    @NotNull(message="transactor不能为空!")
    @Size(max=45, message="transactor长度不能超过45")
    private String transactor;

    @NotNull(message="办理人不能为空!")
    @Size(max=45, message="办理人长度不能超过45")
    private String transactorName;

    @Size(max=45, message="办理人意见长度不能超过45")
    private String transactorOpinion;

    @NotNull(message="remark不能为空!")
    @Size(max=45, message="remark长度不能超过45")
    private String remark;

    @NotNull(message="批示领导不能为空!")
    @Size(max=45, message="批示领导长度不能超过45")
    private String companyLeader;

    @NotNull(message="companyLeaderName不能为空!")
    @Size(max=45, message="companyLeaderName长度不能超过45")
    private String companyLeaderName;

    @NotNull(message="领导批示意见不能为空!")
    @Size(max=45, message="领导批示意见长度不能超过45")
    private String companyLeaderOpinion;

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