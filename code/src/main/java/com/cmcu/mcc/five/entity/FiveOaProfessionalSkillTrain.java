package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaProfessionalSkillTrain {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="表单号不能为空!")
    @Size(max=45, message="表单号长度不能超过45")
    private String formNo;

    @NotNull(message="申请单位不能为空!")
    @Max(value=999999999, message="申请单位必须为数字")
    private Integer applyDeptId;

    @NotNull(message="applyDeptName不能为空!")
    @Size(max=45, message="applyDeptName长度不能超过45")
    private String applyDeptName;

    @NotNull(message="培训时间不能为空!")
    @Size(max=45, message="培训时间长度不能超过45")
    private String trainTime;

    @NotNull(message="培训类别不能为空!")
    @Size(max=45, message="培训类别长度不能超过45")
    private String trainType;

    @NotNull(message="申请人不能为空!")
    @Size(max=45, message="申请人长度不能超过45")
    private String applyMan;

    @NotNull(message="applyManName不能为空!")
    @Size(max=45, message="applyManName长度不能超过45")
    private String applyManName;

    @NotNull(message="培训地点不能为空!")
    @Size(max=45, message="培训地点长度不能超过45")
    private String trainAddress;

    @NotNull(message="培训费用不能为空!")
    @Size(max=45, message="培训费用长度不能超过45")
    private String trainPrice;

    @NotNull(message="本单位意见不能为空!")
    @Size(max=450, message="本单位意见长度不能超过450")
    private String deptComment;

    @NotNull(message="科技质量部意见不能为空!")
    @Size(max=450, message="科技质量部意见长度不能超过450")
    private String technologyComment;

    @NotNull(message="remark不能为空!")
    @Size(max=450, message="remark长度不能超过450")
    private String remark;

    @NotNull(message="creator不能为空!")
    @Size(max=45, message="creator长度不能超过45")
    private String creator;

    @NotNull(message="创建人姓名不能为空!")
    @Size(max=45, message="创建人姓名长度不能超过45")
    private String creatorName;

    private Boolean plan;

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

    @NotNull(message="相关通知不能为空!")
    @Size(max=65535, message="相关通知长度不能超过65535")
    private String trainContent;
}