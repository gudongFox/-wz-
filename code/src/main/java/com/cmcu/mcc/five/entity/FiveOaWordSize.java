package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaWordSize {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="字不能为空!")
    @Size(max=45, message="字长度不能超过45")
    private String word;

    @NotNull(message="年份不能为空!")
    @Size(max=45, message="年份长度不能超过45")
    private String year;

    @NotNull(message="号不能为空!")
    @Max(value=999999999, message="号必须为数字")
    private Integer mark;

    @NotNull(message="类型不能为空!")
    @Size(max=45, message="类型长度不能超过45")
    private String type;

    @NotNull(message="remark不能为空!")
    @Size(max=45, message="remark长度不能超过45")
    private String remark;

    @Size(max=45, message="废弃号长度不能超过45")
    private String abandonMark;

    private Date gmtModified;

    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

    @Size(max=45, message="deptName长度不能超过45")
    private String deptName;
}