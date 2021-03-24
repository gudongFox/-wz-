package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaUseWordSize {
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

    @NotNull(message="remark不能为空!")
    @Size(max=45, message="remark长度不能超过45")
    private String remark;

    @NotNull(message="gmtModified不能为空!")
    private Date gmtModified;

     @NotNull(message="deptName不能为空!")
    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="创建者不能为空!")
    @Size(max=45, message="创建者长度不能超过45")
    private String creator;

    @NotNull(message="creatorName不能为空!")
    @Size(max=45, message="creatorName长度不能超过45")
    private String creatorName;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="wordId不能为空!")
    @Max(value=999999999, message="wordId必须为数字")
    private Integer wordId;
}