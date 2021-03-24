package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaComputerChange {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="assetId不能为空!")
    @Max(value=999999999, message="assetId必须为数字")
    private Integer assetId;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="设备信息化编号不能为空!")
    @Size(max=45, message="设备信息化编号长度不能超过45")
    private String computerNo;

    @NotNull(message="设备名称不能为空!")
    @Size(max=45, message="设备名称长度不能超过45")
    private String computerName;

    @NotNull(message="固定资产编号不能为空!")
    @Size(max=45, message="固定资产编号长度不能超过45")
    private String assetNo;

    @NotNull(message="macAddress不能为空!")
    @Size(max=45, message="macAddress长度不能超过45")
    private String macAddress;

    @NotNull(message="申请人职工号不能为空!")
    @Size(max=45, message="申请人职工号长度不能超过45")
    private String applyUserLogin;

    @NotNull(message="applyUserName不能为空!")
    @Size(max=45, message="applyUserName长度不能超过45")
    private String applyUserName;

    @NotNull(message="申请人电话不能为空!")
    @Size(max=45, message="申请人电话长度不能超过45")
    private String applyPhone;

    @NotNull(message="申请原因不能为空!")
    @Size(max=1000, message="申请原因长度不能超过1000")
    private String reason;

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

    @NotNull(message="责任人不能为空!")
    @Size(max=45, message="责任人长度不能超过45")
    private String dutyName;

    @NotNull(message="dutyLogin不能为空!")
    @Size(max=45, message="dutyLogin长度不能超过45")
    private String dutyLogin;

    @NotNull(message="dutySecurity不能为空!")
    @Size(max=45, message="dutySecurity长度不能超过45")
    private String dutySecurity;

    @NotNull(message="dutyDeptId不能为空!")
    @Max(value=999999999, message="dutyDeptId必须为数字")
    private Integer dutyDeptId;

    @NotNull(message="dutyDeptName不能为空!")
    @Size(max=45, message="dutyDeptName长度不能超过45")
    private String dutyDeptName;

    @NotNull(message="usersName不能为空!")
    @Size(max=45, message="usersName长度不能超过45")
    private String usersName;

    @NotNull(message="usersLogin不能为空!")
    @Size(max=45, message="usersLogin长度不能超过45")
    private String usersLogin;

    @NotNull(message="usersSecurty不能为空!")
    @Size(max=45, message="usersSecurty长度不能超过45")
    private String usersSecurty;

    @NotNull(message="usersDeptId不能为空!")
    @Max(value=999999999, message="usersDeptId必须为数字")
    private Integer usersDeptId;

    @NotNull(message="usersDeptName不能为空!")
    @Size(max=45, message="usersDeptName长度不能超过45")
    private String usersDeptName;

    @NotNull(message="设备所属单位不能为空!")
    @Max(value=999999999, message="设备所属单位必须为数字")
    private Integer deptId;

     @NotNull(message="deptName不能为空!")
    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="使用情况不能为空!")
    @Size(max=45, message="使用情况长度不能超过45")
    private String useSituation;

    @NotNull(message="用途不能为空!")
    @Size(max=45, message="用途长度不能超过45")
    private String useWay;

    @NotNull(message="使用类别不能为空!")
    @Size(max=45, message="使用类别长度不能超过45")
    private String useType;

    @NotNull(message="物联网类型不能为空!")
    @Size(max=45, message="物联网类型长度不能超过45")
    private String network;

    @NotNull(message="放置地点不能为空!")
    @Size(max=45, message="放置地点长度不能超过45")
    private String room;

    @NotNull(message="hardDisk不能为空!")
    @Size(max=45, message="hardDisk长度不能超过45")
    private String hardDisk;

    @NotNull(message="usb状态不能为空!")
    @Size(max=45, message="usb状态长度不能超过45")
    private String usb;

    @NotNull(message="newDutyName不能为空!")
    @Size(max=45, message="newDutyName长度不能超过45")
    private String newDutyName;

    @NotNull(message="newDutyLogin不能为空!")
    @Size(max=45, message="newDutyLogin长度不能超过45")
    private String newDutyLogin;

    @NotNull(message="newDutySecurity不能为空!")
    @Size(max=45, message="newDutySecurity长度不能超过45")
    private String newDutySecurity;

    @NotNull(message="newDutyDeptId不能为空!")
    @Max(value=999999999, message="newDutyDeptId必须为数字")
    private Integer newDutyDeptId;

    @NotNull(message="newDutyDeptName不能为空!")
    @Size(max=45, message="newDutyDeptName长度不能超过45")
    private String newDutyDeptName;

    @NotNull(message="newUsersName不能为空!")
    @Size(max=45, message="newUsersName长度不能超过45")
    private String newUsersName;

    @NotNull(message="newUsersLogin不能为空!")
    @Size(max=45, message="newUsersLogin长度不能超过45")
    private String newUsersLogin;

    @NotNull(message="newUsersSecurty不能为空!")
    @Size(max=45, message="newUsersSecurty长度不能超过45")
    private String newUsersSecurty;

    @NotNull(message="newUsersDeptId不能为空!")
    @Max(value=999999999, message="newUsersDeptId必须为数字")
    private Integer newUsersDeptId;

    @NotNull(message="newUsersDeptName不能为空!")
    @Size(max=45, message="newUsersDeptName长度不能超过45")
    private String newUsersDeptName;

    @NotNull(message="newDeptId不能为空!")
    @Max(value=999999999, message="newDeptId必须为数字")
    private Integer newDeptId;

    @NotNull(message="newDeptName不能为空!")
    @Size(max=45, message="newDeptName长度不能超过45")
    private String newDeptName;

    @NotNull(message="newUseSituation不能为空!")
    @Size(max=45, message="newUseSituation长度不能超过45")
    private String newUseSituation;

    @NotNull(message="newUseWay不能为空!")
    @Size(max=45, message="newUseWay长度不能超过45")
    private String newUseWay;

    @NotNull(message="newUseType不能为空!")
    @Size(max=45, message="newUseType长度不能超过45")
    private String newUseType;

    @NotNull(message="newNetwork不能为空!")
    @Size(max=45, message="newNetwork长度不能超过45")
    private String newNetwork;

    @NotNull(message="newRoom不能为空!")
    @Size(max=45, message="newRoom长度不能超过45")
    private String newRoom;

    @NotNull(message="newHardDisk不能为空!")
    @Size(max=45, message="newHardDisk长度不能超过45")
    private String newHardDisk;

    @NotNull(message="newUsb不能为空!")
    @Size(max=45, message="newUsb长度不能超过45")
    private String newUsb;
}