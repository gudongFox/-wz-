package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaMeetingRoomDetail {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="meetingId不能为空!")
    @Max(value=999999999, message="meetingId必须为数字")
    private Integer meetingId;

    @NotNull(message="提供服务不能为空!")
    @Size(max=45, message="提供服务长度不能超过45")
    private String project;

    @NotNull(message="服务收费不能为空!")
    @Size(max=45, message="服务收费长度不能超过45")
    private String projectCost;

    @NotNull(message="服务数量不能为空!")
    @Max(value=999999999, message="服务数量必须为数字")
    private Integer projectNum;

    @NotNull(message="备注不能为空!")
    @Size(max=255, message="备注长度不能超过255")
    private String remark;

    @NotNull(message="gmtCreate不能为空!")
    private Date gmtCreate;

    @NotNull(message="gmtModified不能为空!")
    private Date gmtModified;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;
}