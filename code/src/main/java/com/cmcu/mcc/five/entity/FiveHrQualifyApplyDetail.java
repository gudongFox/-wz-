package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveHrQualifyApplyDetail {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="qualifyApplyId不能为空!")
    @Max(value=999999999, message="qualifyApplyId必须为数字")
    private Integer qualifyApplyId;

    @NotNull(message="userLogin不能为空!")
    @Size(max=45, message="userLogin长度不能超过45")
    private String userLogin;

    @NotNull(message="userName不能为空!")
    @Size(max=45, message="userName长度不能超过45")
    private String userName;

    @NotNull(message="deptId不能为空!")
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

     @NotNull(message="deptName不能为空!")
    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="从事专业不能为空!")
    @Size(max=45, message="从事专业长度不能超过45")
    private String majorName;

    @NotNull(message="年度不能为空!")
    @Size(max=45, message="年度长度不能超过45")
    private String checkYear;

    @NotNull(message="项目负责人不能为空!")
    private Boolean projectCharge;

    @NotNull(message="专业负责人不能为空!")
    private Boolean majorCharge;

    @NotNull(message="设计人不能为空!")
    private Boolean design;

    @NotNull(message="校核人不能为空!")
    private Boolean proofread;

    @NotNull(message="审核人不能为空!")
    private Boolean audit;

    @NotNull(message="审定人不能为空!")
    private Boolean approve;

    @NotNull(message="项目类型不能为空!")
    @Size(max=45, message="项目类型长度不能超过45")
    private String projectType;

    @NotNull(message="总设计师不能为空!")
    private Boolean chiefDesigner;

    @NotNull(message="是否兼职总设计师不能为空!")
    private Boolean proChief;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="gmtCreate不能为空!")
    private Date gmtCreate;

    @NotNull(message="gmtModified不能为空!")
    private Date gmtModified;

    @NotNull(message="remark不能为空!")
    @Size(max=450, message="remark长度不能超过450")
    private String remark;

    @NotNull(message="勘察  项目工程师不能为空!")
    private Boolean exploreEngineer;

    @NotNull(message="勘察 项目审核人不能为空!")
    private Boolean exploreAudit;

    @NotNull(message="勘察 项目负责人不能为空!")
    private Boolean explorePrincipal;

    @NotNull(message="工程咨询-设计不能为空!")
    private Boolean consultDesign;

    @NotNull(message="工程设计 审核不能为空!")
    private Boolean consultAudit;

    @NotNull(message="工程咨询-校核不能为空!")
    private Boolean consultProofread;

    @NotNull(message="工程承包-设计不能为空!")
    private Boolean contractDesign;

    @NotNull(message="工程承包-审核不能为空!")
    private Boolean contractAudit;

    @NotNull(message="工程承包-校核不能为空!")
    private Boolean contractProofread;

    @NotNull(message="监理-设计不能为空!")
    private Boolean supervisorDesign;

    @NotNull(message="监理-审核不能为空!")
    private Boolean supervisorAudit;

    @NotNull(message="监理-审核不能为空!")
    private Boolean supervisorProofread;
}