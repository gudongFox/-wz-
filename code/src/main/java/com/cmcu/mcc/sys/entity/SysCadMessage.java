package com.cmcu.mcc.sys.entity;

import com.cmcu.common.annotation.FieldName;
import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class SysCadMessage {
    @FieldName("id")
    @NotNull
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @FieldName("confirm,error,flow,message,notice,warning")
    @NotEmpty
    @Size(max=45)
    private String messageType;

    @FieldName("messageCaption")
    @NotEmpty
    @Size(max=45)
    private String messageCaption;

    @FieldName("messageContent")
    @NotEmpty
    @Size(max=145)
    private String messageContent;

    @FieldName("sender")
    @NotEmpty
    @Size(max=45)
    private String sender;

    @FieldName("senderName")
    @NotEmpty
    @Size(max=45)
    private String senderName;

    @FieldName("receiver")
    @NotEmpty
    @Size(max=45)
    private String receiver;

    @FieldName("receiverName")
    @NotEmpty
    @Size(max=45)
    private String receiverName;

    @FieldName("delaySecond")
    @NotNull
    @Max(value=999999999, message="delaySecond必须为数字")
    private Integer delaySecond;

    @FieldName("isReceived")
    @NotNull
    private Boolean received;

    @FieldName("gmtCreate")
    @NotNull
    private Date gmtCreate;

    @FieldName("gmtReceived")
    private Date gmtReceived;
}