package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaInformationEquipmentExamine {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="表单号不能为空!")
    @Size(max=45, message="表单号长度不能超过45")
    private String formNo;

    @NotNull(message="验收日期不能为空!")
    @Size(max=45, message="验收日期长度不能超过45")
    private String acceptTime;

    @NotNull(message="设备编号不能为空!")
    @Size(max=45, message="设备编号长度不能超过45")
    private String equipmentNo;

    @NotNull(message="设备所属部门不能为空!")
    @Max(value=999999999, message="设备所属部门必须为数字")
    private Integer deptId;

     @NotNull(message="deptName不能为空!")
    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="责任人不能为空!")
    @Size(max=45, message="责任人长度不能超过45")
    private String chargeMan;

    @NotNull(message="chargeManName不能为空!")
    @Size(max=45, message="chargeManName长度不能超过45")
    private String chargeManName;

    @NotNull(message="责任人 职工号不能为空!")
    @Size(max=45, message="责任人 职工号长度不能超过45")
    private String chargeManNo;

    @NotNull(message="user不能为空!")
    @Size(max=45, message="user长度不能超过45")
    private String user;

    @NotNull(message="使用人不能为空!")
    @Size(max=45, message="使用人长度不能超过45")
    private String userName;

    @NotNull(message="设备名称不能为空!")
    @Size(max=45, message="设备名称长度不能超过45")
    private String equipmentName;

    @NotNull(message="品牌不能为空!")
    @Size(max=45, message="品牌长度不能超过45")
    private String brand;

    @NotNull(message="型号不能为空!")
    @Size(max=45, message="型号长度不能超过45")
    private String type;

    @NotNull(message="密级不能为空!")
    @Size(max=45, message="密级长度不能超过45")
    private String secretRank;

    @NotNull(message="使用方式(办公设计)不能为空!")
    @Size(max=45, message="使用方式(办公设计)长度不能超过45")
    private String useType;

    @NotNull(message="放置地点不能为空!")
    @Size(max=45, message="放置地点长度不能超过45")
    private String address;

    @NotNull(message="操作系统版本不能为空!")
    @Size(max=45, message="操作系统版本长度不能超过45")
    private String osVersion;

    @NotNull(message="操作系统 安装日期不能为空!")
    @Size(max=45, message="操作系统 安装日期长度不能超过45")
    private String osInstallTime;

    @NotNull(message="磁盘序列号不能为空!")
    @Size(max=45, message="磁盘序列号长度不能超过45")
    private String diskNo;

    @NotNull(message="MAC地址不能为空!")
    @Size(max=45, message="MAC地址长度不能超过45")
    private String macAddress;

    @NotNull(message="联网类型不能为空!")
    @Size(max=45, message="联网类型长度不能超过45")
    private String netType;

    @NotNull(message="ip地址不能为空!")
    @Size(max=45, message="ip地址长度不能超过45")
    private String ipAddress;

    @NotNull(message="显示器型号不能为空!")
    @Size(max=45, message="显示器型号长度不能超过45")
    private String displayNo;

    @NotNull(message="光驱类型不能为空!")
    @Size(max=45, message="光驱类型长度不能超过45")
    private String driveType;

    @NotNull(message="usb口状态不能为空!")
    @Size(max=45, message="usb口状态长度不能超过45")
    private String usbType;

    @NotNull(message="启用时间不能为空!")
    @Size(max=45, message="启用时间长度不能超过45")
    private String startTime;

    @NotNull(message="使用情况不能为空!")
    @Size(max=45, message="使用情况长度不能超过45")
    private String useCondition;

    @NotNull(message="验收价格不能为空!")
    @Size(max=45, message="验收价格长度不能超过45")
    private String checkPrice;

    @NotNull(message="固定资产编号不能为空!")
    @Size(max=45, message="固定资产编号长度不能超过45")
    private String fixedAssetNo;

    @NotNull(message="验收人不能为空!")
    @Size(max=45, message="验收人长度不能超过45")
    private String examineMan;

    @NotNull(message="examineManName不能为空!")
    @Size(max=45, message="examineManName长度不能超过45")
    private String examineManName;

    @NotNull(message="验收人意见不能为空!")
    @Size(max=450, message="验收人意见长度不能超过450")
    private String examineComment;

    @NotNull(message="行政事务部意见不能为空!")
    @Size(max=450, message="行政事务部意见长度不能超过450")
    private String affairComment;

    @NotNull(message="技术质量部意见不能为空!")
    @Size(max=450, message="技术质量部意见长度不能超过450")
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