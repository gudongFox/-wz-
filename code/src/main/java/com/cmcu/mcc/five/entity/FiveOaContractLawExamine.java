package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaContractLawExamine {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="表单号不能为空!")
    @Size(max=45, message="表单号长度不能超过45")
    private String formNo;

    @NotNull(message="送审时间不能为空!")
    @Size(max=45, message="送审时间长度不能超过45")
    private String examineTime;

    @NotNull(message="送审单位不能为空!")
    @Max(value=999999999, message="送审单位必须为数字")
    private Integer deptId;

     @NotNull(message="deptName不能为空!")
    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="合同编号不能为空!")
    @Size(max=45, message="合同编号长度不能超过45")
    private String contractNo;

    @NotNull(message="合同性质不能为空!")
    @Size(max=45, message="合同性质长度不能超过45")
    private String contractProperty;

    @NotNull(message="甲方不能为空!")
    @Size(max=45, message="甲方长度不能超过45")
    private String firstParty;

    @NotNull(message="乙方不能为空!")
    @Size(max=45, message="乙方长度不能超过45")
    private String secondParty;

    @NotNull(message="丙方不能为空!")
    @Size(max=45, message="丙方长度不能超过45")
    private String thirdParty;

    @NotNull(message="丁方不能为空!")
    @Size(max=45, message="丁方长度不能超过45")
    private String fourthParty;

    @NotNull(message="送审人不能为空!")
    @Size(max=45, message="送审人长度不能超过45")
    private String submitMan;

    @NotNull(message="submitManName不能为空!")
    @Size(max=45, message="submitManName长度不能超过45")
    private String submitManName;

    @NotNull(message="联系方式不能为空!")
    @Size(max=45, message="联系方式长度不能超过45")
    private String link;

    @NotNull(message="项目名称不能为空!")
    @Size(max=45, message="项目名称长度不能超过45")
    private String projectName;

    @NotNull(message="合同额 万元不能为空!")
    @Size(max=45, message="合同额 万元长度不能超过45")
    private String contractPrice;

    @NotNull(message="履行期限不能为空!")
    @Size(max=45, message="履行期限长度不能超过45")
    private String performanceDeadline;

    @NotNull(message="projectAddress不能为空!")
    @Size(max=45, message="projectAddress长度不能超过45")
    private String projectAddress;

    @NotNull(message="remark不能为空!")
    @Size(max=450, message="remark长度不能超过450")
    private String remark;

    @NotNull(message="法律审查意见不能为空!")
    @Size(max=450, message="法律审查意见长度不能超过450")
    private String lawExamineComment;

    @NotNull(message="同意 或 修改说明不能为空!")
    @Size(max=450, message="同意 或 修改说明长度不能超过450")
    private String changeReason;

    @NotNull(message="法律审核意见不能为空!")
    @Size(max=450, message="法律审核意见长度不能超过450")
    private String lawAuditComment;

    @NotNull(message="送审单位领导意见不能为空!")
    @Size(max=450, message="送审单位领导意见长度不能超过450")
    private String submitDeptLeaderComment;

    @NotNull(message="公司主管单位领导意见不能为空!")
    @Size(max=450, message="公司主管单位领导意见长度不能超过450")
    private String companyLeaderComment;

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

    @NotNull(message="是否领导审批不能为空!")
    @Size(max=45, message="是否领导审批长度不能超过45")
    private String flag;
}