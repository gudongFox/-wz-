package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaDecisionMaking {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="签报关键字不能为空!")
    @Size(max=45, message="签报关键字长度不能超过45")
    private String businessId;

    @NotNull(message="deptId不能为空!")
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

     @NotNull(message="deptName不能为空!")
    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="会议类型不能为空!")
    @Size(max=45, message="会议类型长度不能超过45")
    private String meetingType;

    @NotNull(message="事项不能为空!")
    @Size(max=450, message="事项长度不能超过450")
    private String item;

    @NotNull(message="上会议时间不能为空!")
    @Size(max=45, message="上会议时间长度不能超过45")
    private String meetingTime;

    @NotNull(message="公司领导或部门负责不能为空!")
    @Size(max=45, message="公司领导或部门负责长度不能超过45")
    private String companyLeader;

    @NotNull(message="companyLeaderName不能为空!")
    @Size(max=45, message="companyLeaderName长度不能超过45")
    private String companyLeaderName;

    @NotNull(message="remark不能为空!")
    @Size(max=450, message="remark长度不能超过450")
    private String remark;

    @NotNull(message="creator不能为空!")
    @Size(max=45, message="creator长度不能超过45")
    private String creator;

    @NotNull(message="创建人姓名不能为空!")
    @Size(max=45, message="创建人姓名长度不能超过45")
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

    @NotNull(message="地点不能为空!")
    @Size(max=45, message="地点长度不能超过45")
    private String meetingTimePlan;

    @NotNull(message="会议结果不能为空!")
    @Size(max=450, message="会议结果长度不能超过450")
    private String meetingResult;

    @NotNull(message="主持人不能为空!")
    @Size(max=45, message="主持人长度不能超过45")
    private String compere;

    @NotNull(message="compereName不能为空!")
    @Size(max=45, message="compereName长度不能超过45")
    private String compereName;

    @NotNull(message="列席不能为空!")
    @Size(max=45, message="列席长度不能超过45")
    private String attender;

    @NotNull(message="attenderName不能为空!")
    @Size(max=45, message="attenderName长度不能超过45")
    private String attenderName;

    @NotNull(message="记录人不能为空!")
    @Size(max=45, message="记录人长度不能超过45")
    private String recordMan;

    @NotNull(message="recordManName不能为空!")
    @Size(max=45, message="recordManName长度不能超过45")
    private String recordManName;

    @NotNull(message="年份不能为空!")
    @Size(max=45, message="年份长度不能超过45")
    private String year;

    @Size(max=45, message="stages长度不能超过45")
    private String stages;

    @Size(max=255, message="缺席领导长度不能超过255")
    private String absentLeader;
}