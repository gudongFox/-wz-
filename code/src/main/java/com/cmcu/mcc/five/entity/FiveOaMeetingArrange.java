package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaMeetingArrange {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="会议室名称不能为空!")
    @Size(max=45, message="会议室名称长度不能超过45")
    private String meetingName;

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

    @NotNull(message="meetingDes不能为空!")
    @Size(max=450, message="meetingDes长度不能超过450")
    private String meetingDes;

    @NotNull(message="会议时间不能为空!")
    @Size(max=45, message="会议时间长度不能超过45")
    private String meetingTime;

    @Size(max=45, message="会议室长度不能超过45")
    private String room;

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

    @NotNull(message="isProcessEnd不能为空!")
    private Boolean processEnd;

    @NotNull(message="processInstanceId不能为空!")
    @Size(max=45, message="processInstanceId长度不能超过45")
    private String processInstanceId;
}