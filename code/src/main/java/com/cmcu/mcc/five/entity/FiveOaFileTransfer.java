package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaFileTransfer {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="所在部门不能为空!")
    @Max(value=999999999, message="所在部门必须为数字")
    private Integer deptId;

     @NotNull(message="deptName不能为空!")
    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="发送人不能为空!")
    @Size(max=45, message="发送人长度不能超过45")
    private String sender;

    @NotNull(message="senderName不能为空!")
    @Size(max=45, message="senderName长度不能超过45")
    private String senderName;

    @NotNull(message="发送时间不能为空!")
    @Size(max=45, message="发送时间长度不能超过45")
    private String sendTime;

    @NotNull(message="文件名称不能为空!")
    @Size(max=45, message="文件名称长度不能超过45")
    private String fileName;

    @NotNull(message="接收者不能为空!")
    @Size(max=45, message="接收者长度不能超过45")
    private String receiver;

    @NotNull(message="receiverName不能为空!")
    @Size(max=45, message="receiverName长度不能超过45")
    private String receiverName;

    @NotNull(message="文件类别不能为空!")
    @Size(max=45, message="文件类别长度不能超过45")
    private String fileType;

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

    @NotNull(message="remark不能为空!")
    @Size(max=45, message="remark长度不能超过45")
    private String remark;

    @NotNull(message="接收者确认不能为空!")
    private Boolean affirm;
}