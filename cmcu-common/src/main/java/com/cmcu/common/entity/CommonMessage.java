package com.cmcu.common.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class CommonMessage {
    private Integer id;

    @NotNull(message="tenetId不能为空!")
    @Size(max=45, message="tenetId长度不能超过45")
    private String tenetId;

    @NotNull(message="sender不能为空!")
    @Size(max=45, message="sender长度不能超过45")
    private String sender;

    @NotNull(message="senderName不能为空!")
    @Size(max=45, message="senderName长度不能超过45")
    private String senderName;

    @NotNull(message="receiver不能为空!")
    @Size(max=45, message="receiver长度不能超过45")
    private String receiver;

    @NotNull(message="receiverName不能为空!")
    @Size(max=45, message="receiverName长度不能超过45")
    private String receiverName;

    @NotNull(message="协同设计、设计管理不能为空!")
    @Size(max=45, message="协同设计、设计管理长度不能超过45")
    private String msgType;

    @NotNull(message="msgCaption不能为空!")
    @Size(max=45, message="msgCaption长度不能超过45")
    private String msgCaption;

    @NotNull(message="msgText不能为空!")
    @Size(max=450, message="msgText长度不能超过450")
    private String msgText;

    @NotNull(message="msgUrl不能为空!")
    @Size(max=145, message="msgUrl长度不能超过145")
    private String msgUrl;


    @NotNull(message="msgBtnTxt不能为空!")
    @Size(max=45, message="msgBtnTxt长度不能超过45")
    private String msgBtnTxt;

    @NotNull(message="msgImageKey不能为空!")
    @Size(max=45, message="msgImageKey长度不能超过45")
    private String msgImageKey;

    @NotNull(message="isReceived不能为空!")
    private Boolean received;

    @NotNull(message="isHandled不能为空!")
    private Boolean handled;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="gmtCreate不能为空!")
    private Date gmtCreate;

    private Date gmtReceived;

    @NotNull(message="remark不能为空!")
    @Size(max=450, message="remark长度不能超过450")
    private String remark;
}