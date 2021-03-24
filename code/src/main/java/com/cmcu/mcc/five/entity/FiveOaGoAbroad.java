package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaGoAbroad {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="formNo不能为空!")
    @Size(max=45, message="formNo长度不能超过45")
    private String formNo;

    @NotNull(message="批示不能为空!")
    @Size(max=450, message="批示长度不能超过450")
    private String instructions;

    @NotNull(message="因公出国主管领导不能为空!")
    @Size(max=45, message="因公出国主管领导长度不能超过45")
    private String chargeLeader;

    @NotNull(message="chargeLeaderName不能为空!")
    @Size(max=45, message="chargeLeaderName长度不能超过45")
    private String chargeLeaderName;

    @NotNull(message="业务主管领导不能为空!")
    @Size(max=45, message="业务主管领导长度不能超过45")
    private String businessChargeLeader;

    @NotNull(message="businessChargeLeaderName不能为空!")
    @Size(max=45, message="businessChargeLeaderName长度不能超过45")
    private String businessChargeLeaderName;

    @NotNull(message="单位负责人不能为空!")
    @Size(max=45, message="单位负责人长度不能超过45")
    private String departmentChargeMen;

    @NotNull(message="departmentChargeMenName不能为空!")
    @Size(max=45, message="departmentChargeMenName长度不能超过45")
    private String departmentChargeMenName;

    @NotNull(message="拟稿人不能为空!")
    @Size(max=45, message="拟稿人长度不能超过45")
    private String drafter;

    @NotNull(message="drafterName不能为空!")
    @Size(max=45, message="drafterName长度不能超过45")
    private String drafterName;

    @NotNull(message="主办单位不能为空!")
    @Max(value=999999999, message="主办单位必须为数字")
    private Integer deptId;

     @NotNull(message="deptName不能为空!")
    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="部门办理意见不能为空!")
    @Size(max=45, message="部门办理意见长度不能超过45")
    private String departmentComment;

    @NotNull(message="标题不能为空!")
    @Size(max=450, message="标题长度不能超过450")
    private String title;

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

    @NotNull(message="公司领导是否会签不能为空!")
    @Size(max=45, message="公司领导是否会签长度不能超过45")
    private String flag;

    @NotNull(message="董事长批示不能为空!")
    @Size(max=45, message="董事长批示长度不能超过45")
    private String sign;

    @NotNull(message="公司副职领导会签不能为空!")
    @Size(max=45, message="公司副职领导会签长度不能超过45")
    private String viceleader;

    @NotNull(message="viceleaderName不能为空!")
    @Size(max=45, message="viceleaderName长度不能超过45")
    private String viceleaderName;
}