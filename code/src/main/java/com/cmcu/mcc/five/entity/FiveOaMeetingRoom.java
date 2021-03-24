package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaMeetingRoom {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="所属部门不能为空!")
    @Size(max=145, message="所属部门长度不能超过145")
    private String deptName;

    @NotNull(message="会议室名称不能为空!")
    @Size(max=45, message="会议室名称长度不能超过45")
    private String roomName;

    @NotNull(message="会议室地址不能为空!")
    @Size(max=145, message="会议室地址长度不能超过145")
    private String roomAddress;

    @NotNull(message="会议室容量 多少人不能为空!")
    @Size(max=145, message="会议室容量 多少人长度不能超过145")
    private String roomCapacity;

    @NotNull(message="会议室介绍不能为空!")
    @Size(max=450, message="会议室介绍长度不能超过450")
    private String roomDesc;

    @NotNull(message="正常、维修、使用中不能为空!")
    @Size(max=45, message="正常、维修、使用中长度不能超过45")
    private String roomStatus;

    @NotNull(message="remark不能为空!")
    @Size(max=450, message="remark长度不能超过450")
    private String remark;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

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
}