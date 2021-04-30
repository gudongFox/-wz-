package com.cmcu.ed.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class EdConfigPlanDetail {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="templateId不能为空!")
    @Max(value=999999999, message="templateId必须为数字")
    private Integer templateId;

    @NotNull(message="cnName不能为空!")
    @Size(max=145, message="cnName长度不能超过145")
    private String cnName;

    @NotNull(message="durationDays不能为空!")
    @Max(value=999999999, message="durationDays必须为数字")
    private Integer durationDays;

    @Max(value=999999999, message="seq必须为数字")
    private Integer seq;

    private Boolean deleted;

    private Date gmtCreate;

    private Date gmtModified;

    @Size(max=450, message="remark长度不能超过450")
    private String remark;
}