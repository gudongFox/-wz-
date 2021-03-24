package com.cmcu.mcc.business.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveBusinessAdvanceDetail {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="advanceId不能为空!")
    @Max(value=999999999, message="advanceId必须为数字")
    private Integer advanceId;

    @NotNull(message="人员类别不能为空!")
    @Size(max=45, message="人员类别长度不能超过45")
    private String personnelCategory;

    @NotNull(message="单位不能为空!")
    @Size(max=45, message="单位长度不能超过45")
    private String deptId;

    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="姓名不能为空!")
    @Size(max=45, message="姓名长度不能超过45")
    private String person;

    @NotNull(message="姓名不能为空!")
    @Size(max=45, message="姓名长度不能超过45")
    private String personName;

    @NotNull(message="职工号不能为空!")
    @Size(max=45, message="职工号长度不能超过45")
    private String personNo;

    @NotNull(message="排序不能为空!")
    @Max(value=999999999, message="排序必须为数字")
    private Integer seq;

    @NotNull(message="项目奖金不能为空!")
    @Size(max=45, message="项目奖金长度不能超过45")
    private String projectBonus;

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

    @NotNull(message="remark不能为空!")
    @Size(max=450, message="remark长度不能超过450")
    private String remark;
}