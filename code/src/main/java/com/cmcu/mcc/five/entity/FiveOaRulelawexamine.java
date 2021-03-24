package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaRulelawexamine {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="表单号不能为空!")
    @Size(max=45, message="表单号长度不能超过45")
    private String formNo;

    @NotNull(message="送审单位不能为空!")
    @Max(value=999999999, message="送审单位必须为数字")
    private Integer deptId;

     @NotNull(message="deptName不能为空!")
    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="送审人不能为空!")
    @Size(max=45, message="送审人长度不能超过45")
    private String sendMan;

    @NotNull(message="sendManName不能为空!")
    @Size(max=45, message="sendManName长度不能超过45")
    private String sendManName;

    @NotNull(message="送审时间不能为空!")
    @Size(max=45, message="送审时间长度不能超过45")
    private String sendTime;

    @NotNull(message="制度名称不能为空!")
    @Size(max=45, message="制度名称长度不能超过45")
    private String ruleName;

    @NotNull(message="制定概要说明不能为空!")
    @Size(max=45, message="制定概要说明长度不能超过45")
    private String ruleDescription;

    @NotNull(message="提供材料及法律规则名称不能为空!")
    @Size(max=45, message="提供材料及法律规则名称长度不能超过45")
    private String lawName;

    @NotNull(message="法律审查意见不能为空!")
    @Size(max=450, message="法律审查意见长度不能超过450")
    private String lawExamine;

    @NotNull(message="审查人不能为空!")
    @Size(max=45, message="审查人长度不能超过45")
    private String examineMan;

    @NotNull(message="examineManName不能为空!")
    @Size(max=45, message="examineManName长度不能超过45")
    private String examineManName;

    @NotNull(message="不能修改说明不能为空!")
    @Size(max=450, message="不能修改说明长度不能超过450")
    private String changeReason;

    @NotNull(message="送审单位领导意见不能为空!")
    @Size(max=450, message="送审单位领导意见长度不能超过450")
    private String sendDeptLeaderComment;

    @NotNull(message="法律顾问意见不能为空!")
    @Size(max=45, message="法律顾问意见长度不能超过45")
    private String counselorComment;

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
}