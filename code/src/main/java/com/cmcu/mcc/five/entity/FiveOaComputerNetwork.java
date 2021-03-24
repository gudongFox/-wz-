package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaComputerNetwork {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="送签部门不能为空!")
    @Max(value=999999999, message="送签部门必须为数字")
    private Integer deptId;

     @NotNull(message="deptName不能为空!")
    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="使用人职工号不能为空!")
    @Size(max=45, message="使用人职工号长度不能超过45")
    private String userLogin;

    @NotNull(message="使用人不能为空!")
    @Size(max=45, message="使用人长度不能超过45")
    private String userName;

    @NotNull(message="责任人职工号不能为空!")
    @Size(max=45, message="责任人职工号长度不能超过45")
    private String chargeMan;

    @NotNull(message="责任人不能为空!")
    @Size(max=45, message="责任人长度不能超过45")
    private String chargeManName;

    @NotNull(message="设备信息化编号不能为空!")
    @Size(max=45, message="设备信息化编号长度不能超过45")
    private String equipmentNo;

    @NotNull(message="设备名称不能为空!")
    @Size(max=45, message="设备名称长度不能超过45")
    private String equipmentName;

    @NotNull(message="设备类型不能为空!")
    @Size(max=45, message="设备类型长度不能超过45")
    private String equipmentType;

    @NotNull(message="linkPhone不能为空!")
    @Size(max=45, message="linkPhone长度不能超过45")
    private String linkPhone;

    @NotNull(message="互联网不能为空!")
    private Boolean networkEach;

    @NotNull(message="非密内网不能为空!")
    private Boolean networkNoSecret;

    @NotNull(message="中间机不能为空!")
    private Boolean networkMiddle;

    @NotNull(message="单机不能为空!")
    private Boolean networkAlone;

    @NotNull(message="其他不能为空!")
    private Boolean networkOther;

    @NotNull(message="其他描述不能为空!")
    @Size(max=45, message="其他描述长度不能超过45")
    private String networkOtherRemark;

    @NotNull(message="光驱不能为空!")
    private Boolean modelCd;

    @NotNull(message="modelUsb不能为空!")
    private Boolean modelUsb;

    @NotNull(message="modelOther不能为空!")
    private Boolean modelOther;

    @NotNull(message="modelOtherRemark不能为空!")
    @Size(max=45, message="modelOtherRemark长度不能超过45")
    private String modelOtherRemark;

    @NotNull(message="申请用途不能为空!")
    @Size(max=450, message="申请用途长度不能超过450")
    private String applyReason;

    @NotNull(message="备注不能为空!")
    @Size(max=145, message="备注长度不能超过145")
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

    @Size(max=45, message="硬盘序列号长度不能超过45")
    private String hardDiskNo;

    @Size(max=45, message="操作系统长度不能超过45")
    private String operatingSystem;

    @Size(max=45, message="操作系统安装时间长度不能超过45")
    private String operatingSystemTime;

    @Size(max=45, message="MAC地址长度不能超过45")
    private String macAddress;

    @Size(max=45, message="ip地址长度不能超过45")
    private String ipAddress;

    @Size(max=45, message="设备编号长度不能超过45")
    private String serialNo;
}