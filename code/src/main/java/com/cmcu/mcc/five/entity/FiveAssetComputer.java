package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveAssetComputer {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="所属部门不能为空!")
    @Max(value=999999999, message="所属部门必须为数字")
    private Integer deptId;

    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="使用人不能为空!")
    @Size(max=45, message="使用人长度不能超过45")
    private String userLogin;

    @NotNull(message="userName不能为空!")
    @Size(max=45, message="userName长度不能超过45")
    private String userName;

    @NotNull(message="设备编号不能为空!")
    @Size(max=45, message="设备编号长度不能超过45")
    private String computerNo;

    @NotNull(message="设备名称不能为空!")
    @Size(max=45, message="设备名称长度不能超过45")
    private String computerName;

    @NotNull(message="品牌不能为空!")
    @Size(max=45, message="品牌长度不能超过45")
    private String computerBrand;

    @NotNull(message="使用类别不能为空!")
    @Size(max=45, message="使用类别长度不能超过45")
    private String securityLevel;

    @NotNull(message="用途不能为空!")
    @Size(max=45, message="用途长度不能超过45")
    private String useWay;

    @NotNull(message="放置地点不能为空!")
    @Size(max=45, message="放置地点长度不能超过45")
    private String room;

    @NotNull(message="操作系统不能为空!")
    @Size(max=45, message="操作系统长度不能超过45")
    private String operatingSystem;

    @NotNull(message="操作系统安装时间不能为空!")
    @Size(max=45, message="操作系统安装时间长度不能超过45")
    private String operatingSystemTime;

    @NotNull(message="SN号不能为空!")
    @Size(max=45, message="SN号长度不能超过45")
    private String snNo;

    @NotNull(message="硬盘序列号不能为空!")
    @Size(max=45, message="硬盘序列号长度不能超过45")
    private String hardDiskNo;

    @NotNull(message="MAC地址不能为空!")
    @Size(max=45, message="MAC地址长度不能超过45")
    private String macAddress;

    @NotNull(message="联网类型不能为空!")
    @Size(max=45, message="联网类型长度不能超过45")
    private String network;

    @NotNull(message="IP地址不能为空!")
    @Size(max=45, message="IP地址长度不能超过45")
    private String ipAddress;

    @NotNull(message="显示器类型不能为空!")
    @Size(max=45, message="显示器类型长度不能超过45")
    private String displayType;

    @NotNull(message="光驱类型不能为空!")
    @Size(max=45, message="光驱类型长度不能超过45")
    private String cdType;

    @NotNull(message="USB口状态不能为空!")
    @Size(max=45, message="USB口状态长度不能超过45")
    private String usbStatus;

    @NotNull(message="启用时间不能为空!")
    @Size(max=45, message="启用时间长度不能超过45")
    private String useTime;

    @NotNull(message="使用情况不能为空!")
    @Size(max=45, message="使用情况长度不能超过45")
    private String useStatus;

    @NotNull(message="备注不能为空!")
    @Size(max=45, message="备注长度不能超过45")
    private String remark;

    @NotNull(message="使用人不能为空!")
    @Size(max=45, message="使用人长度不能超过45")
    private String useLogin;

    @NotNull(message="useName不能为空!")
    @Size(max=45, message="useName长度不能超过45")
    private String useName;

    @NotNull(message="creator不能为空!")
    @Size(max=45, message="creator长度不能超过45")
    private String creator;

    @NotNull(message="creatorName不能为空!")
    @Size(max=45, message="creatorName长度不能超过45")
    private String creatorName;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="gmtCreate不能为空!")
    private Date gmtCreate;

    @NotNull(message="gmtModified不能为空!")
    private Date gmtModified;

    @Size(max=45, message="processInstanceId长度不能超过45")
    private String processInstanceId;

    private Boolean processEnd;

    @Size(max=45, message="负责人长度不能超过45")
    private String chargeMan;

    @Size(max=45, message="chargeManName长度不能超过45")
    private String chargeManName;

    @Size(max=45, message="设备序列号长度不能超过45")
    private String equipmentNo;

    @Size(max=45, message="设备类型长度不能超过45")
    private String equipmentType;

    @Size(max=45, message="固定资产编号长度不能超过45")
    private String fixedAssetNo;
}