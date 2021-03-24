package com.cmcu.mcc.oa.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class OaStampApply {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="部门id不能为空!")
    @Max(value=999999999, message="部门id必须为数字")
    private Integer deptId;

    @NotNull(message="部门名称不能为空!")
    @Size(max=145, message="部门名称长度不能超过145")
    private String deptName;

    @NotNull(message="印章类型：公司章、法人章、法人签名、合同专用章不能为空!")
    @Size(max=145, message="印章类型：公司章、法人章、法人签名、合同专用章长度不能超过145")
    private String stampName;

    @NotNull(message="用印时间：日期不能为空!")
    @Size(max=45, message="用印时间：日期长度不能超过45")
    private String stampDate;

    @NotNull(message="文件名称/图纸名称不能为空!")
    @Size(max=145, message="文件名称/图纸名称长度不能超过145")
    private String fileName;

    @NotNull(message="文件份数不能为空!")
    @Size(max=45, message="文件份数长度不能超过45")
    private String fileCount;

    @NotNull(message="是否需法律审核不能为空!")
    private Boolean legalReview;

    @NotNull(message="法律审核人用户名不能为空!")
    @Size(max=45, message="法律审核人用户名长度不能超过45")
    private String legalMan;

    @NotNull(message="法律审核人名称不能为空!")
    @Size(max=45, message="法律审核人名称长度不能超过45")
    private String legalManName;

    @NotNull(message="事项归口管理部门不能为空!")
    @Max(value=999999999, message="事项归口管理部门必须为数字")
    private Integer functionDeptId;

    @NotNull(message="functionDeptName不能为空!")
    @Size(max=45, message="functionDeptName长度不能超过45")
    private String functionDeptName;

    @NotNull(message="评审级别 公司级 院级不能为空!")
    @Size(max=45, message="评审级别 公司级 院级长度不能超过45")
    private String companyLevel;

    @NotNull(message="副职领导不能为空!")
    @Size(max=45, message="副职领导长度不能超过45")
    private String viceLeader;

    @NotNull(message="部门领导名称不能为空!")
    @Size(max=45, message="部门领导名称长度不能超过45")
    private String viceLeaderName;

    @NotNull(message="创建人不能为空!")
    @Size(max=45, message="创建人长度不能超过45")
    private String creator;

    @NotNull(message="创建人名称不能为空!")
    @Size(max=45, message="创建人名称长度不能超过45")
    private String creatorName;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="remark不能为空!")
    @Size(max=450, message="remark长度不能超过450")
    private String remark;

    @NotNull(message="压力管道公司技术人员不能为空!")
    @Size(max=145, message="压力管道公司技术人员长度不能超过145")
    private String qualityCompanyMan;

    @NotNull(message="qualityCompanyManName不能为空!")
    @Size(max=145, message="qualityCompanyManName长度不能超过145")
    private String qualityCompanyManName;

    @NotNull(message="线上or线下不能为空!")
    @Size(max=450, message="线上or线下长度不能超过450")
    private String online;

    @NotNull(message="成品室负责人不能为空!")
    @Size(max=145, message="成品室负责人长度不能超过145")
    private String finishedMan;

    @NotNull(message="finishedManName不能为空!")
    @Size(max=145, message="finishedManName长度不能超过145")
    private String finishedManName;

    @NotNull(message="gmtCreate不能为空!")
    private Date gmtCreate;

    @NotNull(message="gmtModified不能为空!")
    private Date gmtModified;

    @NotNull(message="流程实例id不能为空!")
    @Size(max=45, message="流程实例id长度不能超过45")
    private String processInstanceId;

    @NotNull(message="流程是否结束不能为空!")
    private Boolean processEnd;

    @NotNull(message="合同评审人员不能为空!")
    @Size(max=45, message="合同评审人员长度不能超过45")
    private String contractSealMan;

    @NotNull(message="contractSealManName不能为空!")
    @Size(max=45, message="contractSealManName长度不能超过45")
    private String contractSealManName;
}