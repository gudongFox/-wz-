package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaComputerNetworkDetail {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="computerNetworkId不能为空!")
    @Max(value=999999999, message="computerNetworkId必须为数字")
    private Integer computerNetworkId;

    @NotNull(message="送签部门不能为空!")
    @Max(value=999999999, message="送签部门必须为数字")
    private Integer deptId;

     @NotNull(message="deptName不能为空!")
    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="userLogin不能为空!")
    @Size(max=45, message="userLogin长度不能超过45")
    private String userLogin;

    @NotNull(message="userName不能为空!")
    @Size(max=45, message="userName长度不能超过45")
    private String userName;

    @NotNull(message="phone不能为空!")
    @Size(max=45, message="phone长度不能超过45")
    private String phone;

    @NotNull(message="room不能为空!")
    @Size(max=45, message="room长度不能超过45")
    private String room;

    @NotNull(message="设备类型不能为空!")
    @Size(max=45, message="设备类型长度不能超过45")
    private String computerType;

    @NotNull(message="网卡物理地址不能为空!")
    @Size(max=45, message="网卡物理地址长度不能超过45")
    private String computerMacAddress;

    @NotNull(message="入网类型不能为空!")
    @Size(max=45, message="入网类型长度不能超过45")
    private String networkType;

    @NotNull(message="creator不能为空!")
    @Size(max=45, message="creator长度不能超过45")
    private String creator;

    @NotNull(message="creatorName不能为空!")
    @Size(max=45, message="creatorName长度不能超过45")
    private String creatorName;

    @NotNull(message="gmtCreate不能为空!")
    private Date gmtCreate;

    @NotNull(message="gmtModified不能为空!")
    private Date gmtModified;
}