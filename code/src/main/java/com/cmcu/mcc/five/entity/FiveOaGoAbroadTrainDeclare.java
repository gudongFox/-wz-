package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaGoAbroadTrainDeclare {
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

    @NotNull(message="申报日期不能为空!")
    @Size(max=45, message="申报日期长度不能超过45")
    private String declareTime;

    @NotNull(message="通知名称不能为空!")
    @Size(max=45, message="通知名称长度不能超过45")
    private String noticName;

    @NotNull(message="培训单位不能为空!")
    @Size(max=45, message="培训单位长度不能超过45")
    private String trainDeptId;

    @NotNull(message="培训部门不能为空!")
    @Size(max=45, message="培训部门长度不能超过45")
    private String trainDeptName;

    @NotNull(message="相关通知不能为空!")
    @Size(max=45, message="相关通知长度不能超过45")
    private String otherNotic;

    @NotNull(message="培训名称不能为空!")
    @Size(max=45, message="培训名称长度不能超过45")
    private String trainName;

    @NotNull(message="拟派人员不能为空!")
    @Size(max=45, message="拟派人员长度不能超过45")
    private String attendMan;

    @NotNull(message="attendManName不能为空!")
    @Size(max=45, message="attendManName长度不能超过45")
    private String attendManName;

    @NotNull(message="选派理由不能为空!")
    @Size(max=450, message="选派理由长度不能超过450")
    private String attendReason;

    @NotNull(message="单位意见不能为空!")
    @Size(max=450, message="单位意见长度不能超过450")
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