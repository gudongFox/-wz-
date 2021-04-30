package com.cmcu.common.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class CommonEdDwgStd {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="tenetId不能为空!")
    @Size(max=45, message="tenetId长度不能超过45")
    private String tenetId;

    @NotNull(message="deptId不能为空!")
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

    @NotNull(message="deptName不能为空!")
    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="majorName不能为空!")
    @Size(max=45, message="majorName长度不能超过45")
    private String majorName;

    @NotNull(message="stdName不能为空!")
    @Size(max=145, message="stdName长度不能超过145")
    private String stdName;

    @NotNull(message="stdDesc不能为空!")
    @Size(max=245, message="stdDesc长度不能超过245")
    private String stdDesc;

    @NotNull(message="attachId不能为空!")
    @Size(max=45, message="attachId长度不能超过45")
    private String attachId;

    @NotNull(message="creator不能为空!")
    @Size(max=45, message="creator长度不能超过45")
    private String creator;

    @NotNull(message="creatorName不能为空!")
    @Size(max=45, message="creatorName长度不能超过45")
    private String creatorName;

    @NotNull(message="是否公开所有不能为空!")
    private Boolean standard;

    @NotNull(message="seq不能为空!")
    @Max(value=999999999, message="seq必须为数字")
    private Integer seq;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="gmtCreate不能为空!")
    private Date gmtCreate;

    @NotNull(message="gmtModified不能为空!")
    private Date gmtModified;

    @NotNull(message="remark不能为空!")
    @Size(max=145, message="remark长度不能超过145")
    private String remark;
}