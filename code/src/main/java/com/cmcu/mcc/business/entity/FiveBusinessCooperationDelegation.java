package com.cmcu.mcc.business.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveBusinessCooperationDelegation {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="projectName不能为空!")
    @Size(max=450, message="projectName长度不能超过450")
    private String projectName;

    @NotNull(message="主项目号不能为空!")
    @Size(max=45, message="主项目号长度不能超过45")
    private String projectNo;

    @NotNull(message="主项目名称不能为空!")
    @Max(value=999999999, message="主项目名称必须为数字")
    private Integer recordId;

    @NotNull(message="deptId不能为空!")
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

    @NotNull(message="主办单位不能为空!")
    @Size(max=45, message="主办单位长度不能超过45")
    private String deptName;

    @NotNull(message="内部合作编号不能为空!")
    @Size(max=145, message="内部合作编号长度不能超过145")
    private String interiorProjectNo;

    @NotNull(message="内部合作项目名称不能为空!")
    @Size(max=450, message="内部合作项目名称长度不能超过450")
    private String interiorProjectName;

    @NotNull(message="内部合作项目类型不能为空!")
    @Size(max=45, message="内部合作项目类型长度不能超过45")
    private String interiorProjectType;

    @NotNull(message="delegationDeptId不能为空!")
    @Max(value=999999999, message="delegationDeptId必须为数字")
    private Integer delegationDeptId;

    @NotNull(message="委托方不能为空!")
    @Size(max=145, message="委托方长度不能超过145")
    private String delegationDeptName;

    @NotNull(message="cooperationDeptName不能为空!")
    @Size(max=45, message="cooperationDeptName长度不能超过45")
    private String cooperationDeptName;

    @NotNull(message="协作方不能为空!")
    @Max(value=999999999, message="协作方必须为数字")
    private Integer cooperationDeptId;

    @NotNull(message="remark不能为空!")
    @Size(max=450, message="remark长度不能超过450")
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

    @NotNull(message="title不能为空!")
    @Size(max=145, message="title长度不能超过145")
    private String title;

    @NotNull(message="委托时间不能为空!")
    @Size(max=45, message="委托时间长度不能超过45")
    private String delegationTime;
}