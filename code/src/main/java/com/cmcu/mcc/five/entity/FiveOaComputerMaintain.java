package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaComputerMaintain {
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

    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="设备所在房间号不能为空!")
    @Size(max=45, message="设备所在房间号长度不能超过45")
    private String deviceRoom;

    @NotNull(message="设备编号不能为空!")
    @Size(max=45, message="设备编号长度不能超过45")
    private String deviceNo;

    @NotNull(message="设备名字不能为空!")
    @Size(max=45, message="设备名字长度不能超过45")
    private String deviceName;

    @NotNull(message="设备密级不能为空!")
    @Size(max=45, message="设备密级长度不能超过45")
    private String deviceLevel;

    @NotNull(message="设备型号不能为空!")
    @Size(max=45, message="设备型号长度不能超过45")
    private String deviceType;

    @NotNull(message="责任人不能为空!")
    @Size(max=45, message="责任人长度不能超过45")
    private String chargeMan;

    @NotNull(message="责任人姓名不能为空!")
    @Size(max=45, message="责任人姓名长度不能超过45")
    private String chargeName;

    @NotNull(message="职工号不能为空!")
    @Size(max=45, message="职工号长度不能超过45")
    private String chargeNo;

    @NotNull(message="联系电话不能为空!")
    @Size(max=45, message="联系电话长度不能超过45")
    private String chargeTel;

    @NotNull(message="报修时间不能为空!")
    @Size(max=45, message="报修时间长度不能超过45")
    private String repairTime;

    @NotNull(message="故障及需求不能为空!")
    @Size(max=45, message="故障及需求长度不能超过45")
    private String chargeComment;

    @NotNull(message="计算所审核不能为空!")
    @Size(max=45, message="计算所审核长度不能超过45")
    private String computerComment;

    @NotNull(message="设备领取人不能为空!")
    @Size(max=45, message="设备领取人长度不能超过45")
    private String receiveMan;

    @NotNull(message="设备领取人不能为空!")
    @Size(max=45, message="设备领取人长度不能超过45")
    private String receiveManName;

    @NotNull(message="领取时间不能为空!")
    @Size(max=45, message="领取时间长度不能超过45")
    private String receiveTime;

    @NotNull(message="维修人不能为空!")
    @Size(max=45, message="维修人长度不能超过45")
    private String maintainMan;

    @NotNull(message="维修人不能为空!")
    @Size(max=45, message="维修人长度不能超过45")
    private String maintainManName;

    @NotNull(message="维修时间不能为空!")
    @Size(max=45, message="维修时间长度不能超过45")
    private String maintainTime;

    @NotNull(message="维修意见不能为空!")
    @Size(max=450, message="维修意见长度不能超过450")
    private String maintainAdvice;

    @NotNull(message="故障原因不能为空!")
    @Size(max=450, message="故障原因长度不能超过450")
    private String failureCause;

    @NotNull(message="deptSecurityMan不能为空!")
    @Size(max=45, message="deptSecurityMan长度不能超过45")
    private String deptSecurityMan;

    @NotNull(message="deptSecurityManName不能为空!")
    @Size(max=45, message="deptSecurityManName长度不能超过45")
    private String deptSecurityManName;

    @NotNull(message="otherContent不能为空!")
    @Size(max=200, message="otherContent长度不能超过200")
    private String otherContent;

    @Size(max=450, message="故障描述长度不能超过450")
    private String errorDescription;

    @NotNull(message="反馈不能为空!")
    @Size(max=45, message="反馈长度不能超过45")
    private String comment;

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