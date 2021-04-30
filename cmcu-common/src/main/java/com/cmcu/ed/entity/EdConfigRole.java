package com.cmcu.ed.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class EdConfigRole {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="tenetId不能为空!")
    @Size(max=45, message="tenetId长度不能超过45")
    private String tenetId;

    @NotNull(message="cnName不能为空!")
    @Size(max=45, message="cnName长度不能超过45")
    private String cnName;

    @NotNull(message="enName不能为空!")
    @Size(max=45, message="enName长度不能超过45")
    private String enName;

    @NotNull(message="isMultiple不能为空!")
    private Boolean multiple;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="seq不能为空!")
    @Max(value=999999999, message="seq必须为数字")
    private Integer seq;

    @NotNull(message="gmtCreate不能为空!")
    private Date gmtCreate;

    @NotNull(message="gmtModified不能为空!")
    private Date gmtModified;

    @NotNull(message="remark不能为空!")
    @Size(max=450, message="remark长度不能超过450")
    private String remark;
}