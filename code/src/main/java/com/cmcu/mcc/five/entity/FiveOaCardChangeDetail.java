package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaCardChangeDetail {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="changeId不能为空!")
    @Max(value=999999999, message="changeId必须为数字")
    private Integer changeId;

    @NotNull(message="userLogin不能为空!")
    @Size(max=45, message="userLogin长度不能超过45")
    private String userLogin;

    @NotNull(message="userName不能为空!")
    @Size(max=45, message="userName长度不能超过45")
    private String userName;

    @NotNull(message="userType不能为空!")
    @Size(max=45, message="userType长度不能超过45")
    private String userType;

    @NotNull(message="cardTypeNow不能为空!")
    @Size(max=45, message="cardTypeNow长度不能超过45")
    private String cardTypeNow;

    @NotNull(message="cardTypeChange不能为空!")
    @Size(max=45, message="cardTypeChange长度不能超过45")
    private String cardTypeChange;

    @NotNull(message="remark不能为空!")
    @Size(max=45, message="remark长度不能超过45")
    private String remark;

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

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;
}