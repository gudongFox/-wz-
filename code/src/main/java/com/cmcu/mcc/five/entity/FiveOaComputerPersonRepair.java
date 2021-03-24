package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaComputerPersonRepair {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="formNo不能为空!")
    @Size(max=45, message="formNo长度不能超过45")
    private String formNo;

    @NotNull(message="申请单位不能为空!")
    @Max(value=999999999, message="申请单位必须为数字")
    private Integer deptId;

    @NotNull(message="申请单位名字不能为空!")
    @Size(max=145, message="申请单位名字长度不能超过145")
    private String deptName;

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

    @NotNull(message="dutyPersonId不能为空!")
    @Size(max=45, message="dutyPersonId长度不能超过45")
    private String dutyPersonId;

    @NotNull(message="责任人不能为空!")
    @Size(max=45, message="责任人长度不能超过45")
    private String dutyPersonName;

    @NotNull(message="设备编号不能为空!")
    @Size(max=45, message="设备编号长度不能超过45")
    private String deviceNo;

    @NotNull(message="repairMan不能为空!")
    @Size(max=45, message="repairMan长度不能超过45")
    private String repairMan;

    @NotNull(message="维修人不能为空!")
    @Size(max=45, message="维修人长度不能超过45")
    private String repairManName;

    @NotNull(message="deviceName不能为空!")
    @Size(max=45, message="deviceName长度不能超过45")
    private String deviceName;

    @NotNull(message="维修人员身份证号不能为空!")
    @Size(max=45, message="维修人员身份证号长度不能超过45")
    private String repairIdCard;

    @NotNull(message="设备密级不能为空!")
    @Size(max=45, message="设备密级长度不能超过45")
    private String deviceSecurity;

    @NotNull(message="联系电话不能为空!")
    @Size(max=45, message="联系电话长度不能超过45")
    private String repairTel;

    @NotNull(message="reason不能为空!")
    @Size(max=1000, message="reason长度不能超过1000")
    private String reason;

    @NotNull(message="解决方法不能为空!")
    @Size(max=1000, message="解决方法长度不能超过1000")
    private String solveWay;

    @NotNull(message="维修结果不能为空!")
    @Size(max=1000, message="维修结果长度不能超过1000")
    private String result;

    @NotNull(message="changeIs不能为空!")
    @Size(max=45, message="changeIs长度不能超过45")
    private String changeIs;

    @NotNull(message="removelSituation不能为空!")
    @Size(max=1000, message="removelSituation长度不能超过1000")
    private String removelSituation;

    @NotNull(message="changeSituation不能为空!")
    @Size(max=1000, message="changeSituation长度不能超过1000")
    private String changeSituation;

    @NotNull(message="removelMan不能为空!")
    @Size(max=45, message="removelMan长度不能超过45")
    private String removelMan;

    @NotNull(message="removelManName不能为空!")
    @Size(max=45, message="removelManName长度不能超过45")
    private String removelManName;

    @NotNull(message="accompanyMan不能为空!")
    @Size(max=45, message="accompanyMan长度不能超过45")
    private String accompanyMan;

    @NotNull(message="accompanyManName不能为空!")
    @Size(max=45, message="accompanyManName长度不能超过45")
    private String accompanyManName;
}