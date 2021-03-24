package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaCarMaintain {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="carId不能为空!")
    @Max(value=999999999, message="carId必须为数字")
    private Integer carId;

    @NotNull(message="车牌号不能为空!")
    @Size(max=45, message="车牌号长度不能超过45")
    private String carNo;

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

    @NotNull(message="维护类型不能为空!")
    @Size(max=45, message="维护类型长度不能超过45")
    private String type;

    @NotNull(message="充卡 加油时间不能为空!")
    @Size(max=45, message="充卡 加油时间长度不能超过45")
    private String soilTime;

    @NotNull(message="加油充卡金额不能为空!")
    @Size(max=45, message="加油充卡金额长度不能超过45")
    private String soilMoney;

    @NotNull(message="保养里程不能为空!")
    @Size(max=45, message="保养里程长度不能超过45")
    private String upkeepCourse;

    @NotNull(message="保养时间不能为空!")
    @Size(max=45, message="保养时间长度不能超过45")
    private String upkeepTime;

    @NotNull(message="修理厂不能为空!")
    @Size(max=45, message="修理厂长度不能超过45")
    private String upkeepFactory;

    @NotNull(message="保养费用不能为空!")
    @Size(max=45, message="保养费用长度不能超过45")
    private String upkeepMoney;

    @NotNull(message="upkeepInvoiceNo不能为空!")
    @Size(max=45, message="upkeepInvoiceNo长度不能超过45")
    private String upkeepInvoiceNo;

    @NotNull(message="upkeepInvoiceMoney不能为空!")
    @Size(max=45, message="upkeepInvoiceMoney长度不能超过45")
    private String upkeepInvoiceMoney;

    @NotNull(message="年假时间不能为空!")
    @Size(max=45, message="年假时间长度不能超过45")
    private String checkTime;

    @NotNull(message="年检费用不能为空!")
    @Size(max=45, message="年检费用长度不能超过45")
    private String checkMoney;

    @NotNull(message="年检地址不能为空!")
    @Size(max=45, message="年检地址长度不能超过45")
    private String checkAddress;

    @NotNull(message="etcMoney不能为空!")
    @Size(max=45, message="etcMoney长度不能超过45")
    private String etcMoney;

    @NotNull(message="etcTime不能为空!")
    @Size(max=45, message="etcTime长度不能超过45")
    private String etcTime;

    @NotNull(message="otherType不能为空!")
    @Size(max=45, message="otherType长度不能超过45")
    private String otherType;

    @NotNull(message="otherMoney不能为空!")
    @Size(max=45, message="otherMoney长度不能超过45")
    private String otherMoney;
}