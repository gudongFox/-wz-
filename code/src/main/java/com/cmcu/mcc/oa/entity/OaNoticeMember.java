package com.cmcu.mcc.oa.entity;

import javax.validation.constraints.*;
import lombok.Data;

@Data
public class OaNoticeMember {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="noticeMemberId不能为空!")
    @Size(max=45, message="noticeMemberId长度不能超过45")
    private String noticeMemberId;

    @NotNull(message="users不能为空!")
    @Size(max=45, message="users长度不能超过45")
    private String users;

    @NotNull(message="usersName不能为空!")
    @Size(max=45, message="usersName长度不能超过45")
    private String usersName;
}