package com.cmcu.mcc.oa.entity;

import com.cmcu.common.annotation.FieldName;
import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class OaMeetingRoomApply {
    @FieldName("id")
    @NotNull
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @FieldName("businessKey")
    @NotNull
    @Size(max=45)
    private String businessKey;

    @FieldName("meetingRoomId")
    @NotNull
    @Max(value=999999999, message="meetingRoomId必须为数字")
    private Integer meetingRoomId;

    @FieldName("applyReason")
    @NotNull
    @Size(max=450)
    private String applyReason;

    @FieldName("beginTime")
    @NotNull
    private String beginTime;

    @FieldName("endTime")
    @NotNull
    private String endTime;

    @FieldName("deptName")
    @NotNull
    @Size(max=145)
    private String deptName;

    @FieldName("deptId")
    @NotNull
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

    @FieldName("gmtCreate")
    @NotNull
    private Date gmtCreate;

    @FieldName("gmtModified")
    @NotNull
    private Date gmtModified;

    @FieldName("0")
    @NotNull
    private Boolean deleted;

    @FieldName("creator")
    @NotNull
    @Size(max=45)
    private String creator;

    @FieldName("creatorName")
    @NotNull
    @Size(max=45)
    private String creatorName;

    @FieldName("processInstanceId")
    @NotNull
    @Size(max=45)
    private String processInstanceId;

    @FieldName("isProcessEnd")
    @NotNull
    private Boolean processEnd;

    @FieldName("remark")
    @NotNull
    @Size(max=450)
    private String remark;

    @FieldName("meetingRoomInfo")
    @NotNull
    @Size(max=65535)
    private String meetingRoomInfo;
}