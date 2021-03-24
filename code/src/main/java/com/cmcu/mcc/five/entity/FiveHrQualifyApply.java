package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveHrQualifyApply {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="单号不能为空!")
    @Size(max=45, message="单号长度不能超过45")
    private String formNo;

    @NotNull(message="申请部门不能为空!")
    @Max(value=999999999, message="申请部门必须为数字")
    private Integer deptId;

    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="申请认定类型不能为空!")
    @Size(max=45, message="申请认定类型长度不能超过45")
    private String applyType;

    @NotNull(message="检查年份不能为空!")
    @Size(max=45, message="检查年份长度不能超过45")
    private String checkYear;

    @NotNull(message="身份证不能为空!")
    @Size(max=45, message="身份证长度不能超过45")
    private String identity;

    @NotNull(message="拟申报岗位不能为空!")
    @Size(max=45, message="拟申报岗位长度不能超过45")
    private String planPost;

    @NotNull(message="职工号不能为空!")
    @Size(max=45, message="职工号长度不能超过45")
    private String userLogin;

    @NotNull(message="姓名不能为空!")
    @Size(max=45, message="姓名长度不能超过45")
    private String userName;

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

    @NotNull(message="业绩及获奖情况不能为空!")
    @Size(max=450, message="业绩及获奖情况长度不能超过450")
    private String remark;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

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

    @NotNull(message="processInstanceId不能为空!")
    @Size(max=45, message="processInstanceId长度不能超过45")
    private String processInstanceId;

    @NotNull(message="isProcessEnd不能为空!")
    private Boolean processEnd;

    @NotNull(message="是否已经处理入库不能为空!")
    private Boolean handled;
}