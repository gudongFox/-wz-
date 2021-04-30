package com.cmcu.common.entity;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
public class CommonEdStamp {

    private Integer id;

    @NotNull(message="tenetId不能为空!")
    @Size(max=45, message="tenetId长度不能超过45")
    private String tenetId;

    @NotNull(message="stampType不能为空!")
    @Size(max=45, message="stampType长度不能超过45")
    private String stampType;

    @NotNull(message="userName不能为空!")
    @Size(max=45, message="userName长度不能超过45")
    private String userName;

    @Max(value=999999999, message="attachId必须为数字")
    private Integer attachId;

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
    @Size(max=450, message="remark长度不能超过450")
    private String remark;
}