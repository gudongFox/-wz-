package com.cmcu.mcc.oa.entity;

import com.cmcu.common.annotation.FieldName;
import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class OaMeetingRoom {
    @FieldName("id")
    @NotNull
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @FieldName("businessKey")
    @NotNull
    @Size(max=45)
    private String businessKey;

    @FieldName("所属部门")
    @NotNull
    @Size(max=145)
    private String deptName;

    @FieldName("会议室名称")
    @NotNull
    @Size(max=45)
    private String roomName;

    @FieldName("会议室地址")
    @NotNull
    @Size(max=145)
    private String roomAddress;

    @FieldName("会议室容量 多少人")
    @NotNull
    @Size(max=145)
    private String roomCapacity;

    @FieldName("会议室介绍")
    @NotNull
    @Size(max=450)
    private String roomDesc;

    @FieldName("正常、维修、使用中")
    @NotNull
    @Size(max=45)
    private String roomStatus;

    @FieldName("remark")
    @NotNull
    @Size(max=450)
    private String remark;

    @FieldName("isDeleted")
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

    @FieldName("gmtCreate")
    @NotNull
    private Date gmtCreate;

    @FieldName("gmtModified")
    @NotNull
    private Date gmtModified;
}