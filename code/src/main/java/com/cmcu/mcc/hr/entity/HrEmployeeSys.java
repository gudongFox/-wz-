package com.cmcu.mcc.hr.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class HrEmployeeSys {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="userLogin不能为空!")
    @Size(max=45, message="userLogin长度不能超过45")
    private String userLogin;

    @NotNull(message="wxId不能为空!")
    @Size(max=90, message="wxId长度不能超过90")
    private String wxId;

    @NotNull(message="deptId不能为空!")
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

    @NotNull(message="password不能为空!")
    @Size(max=45, message="password长度不能超过45")
    private String password;

    @NotNull(message="行政职级不能为空!")
    @Size(max=45, message="行政职级长度不能超过45")
    private String positionIds;

    @NotNull(message="positionNames不能为空!")
    @Size(max=145, message="positionNames长度不能超过145")
    private String positionNames;

    @NotNull(message="roleIds不能为空!")
    @Size(max=45, message="roleIds长度不能超过45")
    private String roleIds;

    @NotNull(message="roleNames不能为空!")
    @Size(max=145, message="roleNames长度不能超过145")
    private String roleNames;

    @NotNull(message="configData不能为空!")
    @Size(max=2450, message="configData长度不能超过2450")
    private String configData;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="cadHide不能为空!")
    @Size(max=145, message="cadHide长度不能超过145")
    private String cadHide;

    private Date gmtModified;

    @NotNull
    private Integer dwgAttachId;
}