package com.cmcu.common.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class CommonEdArrange {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=145, message="businessKey长度不能超过145")
    private String businessKey;

    @NotNull(message="buildId不能为空!")
    @Max(value=999999999, message="buildId必须为数字")
    private Integer buildId;

    @NotNull(message="buildName不能为空!")
    @Size(max=45, message="buildName长度不能超过45")
    private String buildName;

    @NotNull(message="majorName不能为空!")
    @Size(max=45, message="majorName长度不能超过45")
    private String majorName;

    @NotNull(message="allUser不能为空!")
    @Size(max=450, message="allUser长度不能超过450")
    private String allUser;

    @NotNull(message="seq不能为空!")
    @Max(value=999999999, message="seq必须为数字")
    private Integer seq;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="gmtCreate不能为空!")
    private Date gmtCreate;

    @NotNull(message="gmtModified不能为空!")
    private Date gmtModified;
}