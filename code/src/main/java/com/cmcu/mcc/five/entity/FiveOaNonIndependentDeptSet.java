package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaNonIndependentDeptSet {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="表单号不能为空!")
    @Size(max=45, message="表单号长度不能超过45")
    private String formNo;

    @NotNull(message="中心名称不能为空!")
    @Size(max=45, message="中心名称长度不能超过45")
    private String centerName;

    @NotNull(message="依托单位不能为空!")
    @Max(value=999999999, message="依托单位必须为数字")
    private Integer deptId;

    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="研究方向不能为空!")
    @Size(max=45, message="研究方向长度不能超过45")
    private String researchDirection;

    @NotNull(message="联系人不能为空!")
    @Size(max=45, message="联系人长度不能超过45")
    private String linkMan;

    @NotNull(message="linkManName不能为空!")
    @Size(max=45, message="linkManName长度不能超过45")
    private String linkManName;

    @NotNull(message="联系人电话不能为空!")
    @Size(max=45, message="联系人电话长度不能超过45")
    private String linkManPhone;

    @NotNull(message="依托单位负责人不能为空!")
    @Size(max=45, message="依托单位负责人长度不能超过45")
    private String deptChargeMan;

    @NotNull(message="deptChargeManName不能为空!")
    @Size(max=45, message="deptChargeManName长度不能超过45")
    private String deptChargeManName;

    @NotNull(message="依托单位领导意见不能为空!")
    @Size(max=450, message="依托单位领导意见长度不能超过450")
    private String deptLeaderComment;

    @NotNull(message="专家意见不能为空!")
    @Size(max=450, message="专家意见长度不能超过450")
    private String specialistComment;

    @NotNull(message="科技质量部负责人不能为空!")
    @Size(max=45, message="科技质量部负责人长度不能超过45")
    private String technologyDeptMan;

    @NotNull(message="technologyDeptManName不能为空!")
    @Size(max=45, message="technologyDeptManName长度不能超过45")
    private String technologyDeptManName;

    @NotNull(message="提供材料及法律规则名称不能为空!")
    @Size(max=450, message="提供材料及法律规则名称长度不能超过450")
    private String technologyDeptComment;

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