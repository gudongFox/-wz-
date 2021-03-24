package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveHrQualifyChief {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="formNo不能为空!")
    @Size(max=45, message="formNo长度不能超过45")
    private String formNo;

    @NotNull(message="申请类型:总工程师,兼职总工程师不能为空!")
    @Size(max=45, message="申请类型:总工程师,兼职总工程师长度不能超过45")
    private String applyType;

    @NotNull(message="检查年份不能为空!")
    @Size(max=45, message="检查年份长度不能超过45")
    private String checkYear;

    @NotNull(message="deptId不能为空!")
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

     @NotNull(message="deptName不能为空!")
    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="chiefUserLogin不能为空!")
    @Size(max=45, message="chiefUserLogin长度不能超过45")
    private String chiefUserLogin;

    @NotNull(message="chiefUserName不能为空!")
    @Size(max=45, message="chiefUserName长度不能超过45")
    private String chiefUserName;

    @NotNull(message="职务不能为空!")
    @Size(max=45, message="职务长度不能超过45")
    private String position;

    @NotNull(message="现承担设计项目类型不能为空!")
    @Size(max=45, message="现承担设计项目类型长度不能超过45")
    private String projectTypeNow;

    @NotNull(message="申请承担设计项目类型不能为空!")
    @Size(max=45, message="申请承担设计项目类型长度不能超过45")
    private String projectTypeApply;

    @NotNull(message="业绩表现不能为空!")
    @Size(max=45, message="业绩表现长度不能超过45")
    private String performance;

    @NotNull(message="性别不能为空!")
    private Boolean gender;

    @NotNull(message="出生日期不能为空!")
    @Size(max=45, message="出生日期长度不能超过45")
    private String birthDay;

    @NotNull(message="职称不能为空!")
    @Size(max=45, message="职称长度不能超过45")
    private String ranks;

    @NotNull(message="职称认定时间不能为空!")
    @Size(max=45, message="职称认定时间长度不能超过45")
    private String rankTime;

    @NotNull(message="员工类别不能为空!")
    @Size(max=45, message="员工类别长度不能超过45")
    private String userType;

    @NotNull(message="最高学历不能为空!")
    @Size(max=45, message="最高学历长度不能超过45")
    private String educationBackground;

    @NotNull(message="入职时间不能为空!")
    @Size(max=45, message="入职时间长度不能超过45")
    private String joinCompanyTime;

    @NotNull(message="remark不能为空!")
    @Size(max=450, message="remark长度不能超过450")
    private String remark;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="gmtCreate不能为空!")
    private Date gmtCreate;

    @NotNull(message="gmtModified不能为空!")
    private Date gmtModified;

    @NotNull(message="creator不能为空!")
    @Size(max=45, message="creator长度不能超过45")
    private String creator;

    @NotNull(message="设计专业不能为空!")
    @Size(max=45, message="设计专业长度不能超过45")
    private String creatorName;

    @NotNull(message="processInstanceId不能为空!")
    @Size(max=45, message="processInstanceId长度不能超过45")
    private String processInstanceId;

    @NotNull(message="isProcessEnd不能为空!")
    private Boolean processEnd;

    @NotNull(message="是否已经处理入库不能为空!")
    private Boolean handled;

    @NotNull(message="设计专业不能为空!")
    @Size(max=45, message="设计专业长度不能超过45")
    private String specialty;
}