package com.cmcu.common.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class CommonEdArrangeUser {
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="arrangeId不能为空!")
    @Max(value=999999999, message="arrangeId必须为数字")
    private Integer arrangeId;

    @NotNull(message="roleName不能为空!")
    @Size(max=45, message="roleName长度不能超过45")
    private String roleName;

    @NotNull(message="roleCode不能为空!")
    @Size(max=45, message="roleCode长度不能超过45")
    private String roleCode;

    @NotNull(message="allUser不能为空!")
    @Size(max=450, message="allUser长度不能超过450")
    private String allUser;

    @NotNull(message="allUserName不能为空!")
    @Size(max=450, message="allUserName长度不能超过450")
    private String allUserName;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="gmtCreate不能为空!")
    private Date gmtCreate;

    @NotNull(message="gmtModified不能为空!")
    private Date gmtModified;
}