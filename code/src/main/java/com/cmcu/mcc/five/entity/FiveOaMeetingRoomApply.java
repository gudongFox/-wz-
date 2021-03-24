package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaMeetingRoomApply {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="meetingRoomId不能为空!")
    @Max(value=999999999, message="meetingRoomId必须为数字")
    private Integer meetingRoomId;

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

    @NotNull(message="processInstanceId不能为空!")
    @Size(max=45, message="processInstanceId长度不能超过45")
    private String processInstanceId;

    @NotNull(message="isProcessEnd不能为空!")
    private Boolean processEnd;

    @NotNull(message="remark不能为空!")
    @Size(max=450, message="remark长度不能超过450")
    private String remark;

    @NotNull(message="申请是否生效不能为空!")
    private Boolean applyComplete;

    @NotNull(message="hostMan不能为空!")
    @Size(max=45, message="hostMan长度不能超过45")
    private String hostMan;

    @NotNull(message="hostManName不能为空!")
    @Size(max=45, message="hostManName长度不能超过45")
    private String hostManName;

    @NotNull(message="chargeLeader不能为空!")
    @Size(max=145, message="chargeLeader长度不能超过145")
    private String chargeLeader;

    @NotNull(message="chargeLeaderName不能为空!")
    @Size(max=145, message="chargeLeaderName长度不能超过145")
    private String chargeLeaderName;

    @NotNull(message="attendUser不能为空!")
    @Size(max=145, message="attendUser长度不能超过145")
    private String attendUser;

    @NotNull(message="attendUserName不能为空!")
    @Size(max=145, message="attendUserName长度不能超过145")
    private String attendUserName;

    @NotNull(message="meetingRoomInfo不能为空!")
    @Size(max=65535, message="meetingRoomInfo长度不能超过65535")
    private String meetingRoomInfo;
}