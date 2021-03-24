package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaCarApply {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="carId不能为空!")
    @Max(value=999999999, message="carId必须为数字")
    private Integer carId;

    @Size(max=45, message="carName长度不能超过45")
    private String carName;

    @NotNull(message="applyReason不能为空!")
    @Size(max=450, message="applyReason长度不能超过450")
    private String applyReason;

    @NotNull(message="beginTime不能为空!")
    @Size(max=45, message="beginTime长度不能超过45")
    private String beginTime;

    @NotNull(message="endTime不能为空!")
    @Size(max=45, message="endTime长度不能超过45")
    private String endTime;

     @NotNull(message="deptName不能为空!")
    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="deptId不能为空!")
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

    @NotNull(message="gmtCreate不能为空!")
    private Date gmtCreate;

    @NotNull(message="gmtModified不能为空!")
    private Date gmtModified;

    @NotNull(message="0不能为空!")
    private Boolean deleted;

    @NotNull(message="creator不能为空!")
    @Size(max=45, message="creator长度不能超过45")
    private String creator;

    @NotNull(message="creatorName不能为空!")
    @Size(max=45, message="creatorName长度不能超过45")
    private String creatorName;

    @NotNull(message="remark不能为空!")
    @Size(max=450, message="remark长度不能超过450")
    private String remark;

    @NotNull(message="processInstanceId不能为空!")
    @Size(max=45, message="processInstanceId长度不能超过45")
    private String processInstanceId;

    @NotNull(message="isProcessEnd不能为空!")
    private Boolean processEnd;

    @NotNull(message="申请类型不能为空!")
    @Size(max=45, message="申请类型长度不能超过45")
    private String applyType;

    @NotNull(message="目的地不能为空!")
    @Size(max=45, message="目的地长度不能超过45")
    private String destination;

    @NotNull(message="里程数不能为空!")
    @Size(max=45, message="里程数长度不能超过45")
    private String mileage;

    @NotNull(message="乘车人数不能为空!")
    @Size(max=45, message="乘车人数长度不能超过45")
    private String userNum;

    @NotNull(message="驾驶员不能为空!")
    @Size(max=45, message="驾驶员长度不能超过45")
    private String driver;

    @NotNull(message="driverName不能为空!")
    @Size(max=45, message="driverName长度不能超过45")
    private String driverName;

    @NotNull(message="乘客 不能为空!")
    @Size(max=45, message="乘客 长度不能超过45")
    private String passenger;

    @Size(max=45, message="油费长度不能超过45")
    private String soilPay;

    @Size(max=45, message="过桥 过路费长度不能超过45")
    private String passPay;

    @Size(max=45, message="停车费长度不能超过45")
    private String partPay;

    @Size(max=45, message="个人用车/领导用车长度不能超过45")
    private String type;

    private Boolean selfDrive;

    @NotNull(message="carInfo不能为空!")
    @Size(max=65535, message="carInfo长度不能超过65535")
    private String carInfo;
}