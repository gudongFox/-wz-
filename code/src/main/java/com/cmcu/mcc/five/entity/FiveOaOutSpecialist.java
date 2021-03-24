package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaOutSpecialist {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="formNo不能为空!")
    @Size(max=45, message="formNo长度不能超过45")
    private String formNo;

    @NotNull(message="申请日期不能为空!")
    @Size(max=45, message="申请日期长度不能超过45")
    private String submitTime;

    @NotNull(message="姓名不能为空!")
    @Size(max=45, message="姓名长度不能超过45")
    private String name;

    @NotNull(message="身份证号不能为空!")
    @Size(max=45, message="身份证号长度不能超过45")
    private String identityCardNo;

    @NotNull(message="主办单位不能为空!")
    @Max(value=999999999, message="主办单位必须为数字")
    private Integer deptId;

    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="地址 房间号不能为空!")
    @Size(max=45, message="地址 房间号长度不能超过45")
    private String address;

    @NotNull(message="职务不能为空!")
    @Size(max=45, message="职务长度不能超过45")
    private String position;

    @NotNull(message="职称不能为空!")
    @Size(max=45, message="职称长度不能超过45")
    private String ranks;

    @NotNull(message="证书编号不能为空!")
    @Size(max=45, message="证书编号长度不能超过45")
    private String rankCode;

    @NotNull(message="政治面貌不能为空!")
    @Size(max=45, message="政治面貌长度不能超过45")
    private String politicsStatus;

    @NotNull(message="称号不能为空!")
    @Size(max=45, message="称号长度不能超过45")
    private String label;

    @NotNull(message="毕业院校不能为空!")
    @Size(max=45, message="毕业院校长度不能超过45")
    private String graduateCollege;

    @NotNull(message="最高学历不能为空!")
    @Size(max=45, message="最高学历长度不能超过45")
    private String highestEducation;

    @NotNull(message="主要擅长不能为空!")
    @Size(max=45, message="主要擅长长度不能超过45")
    private String good;

    @NotNull(message="专业不能为空!")
    @Size(max=45, message="专业长度不能超过45")
    private String major;

    @NotNull(message="手机不能为空!")
    @Size(max=45, message="手机长度不能超过45")
    private String phone;

    @NotNull(message="电话不能为空!")
    @Size(max=45, message="电话长度不能超过45")
    private String telephone;

    @NotNull(message="所属专家组不能为空!")
    @Size(max=45, message="所属专家组长度不能超过45")
    private String specialistGroup;

    @NotNull(message="拟担任外部专家外部机构名称不能为空!")
    @Size(max=45, message="拟担任外部专家外部机构名称长度不能超过45")
    private String institutionName;

    @NotNull(message="是否加盖公司章不能为空!")
    @Size(max=45, message="是否加盖公司章长度不能超过45")
    private String companyStamp;

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