package com.cmcu.common.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class CommonEdBuild {

    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotEmpty(message="子项名称不能为空!")
    @Size(max=45, message="子项名称长度不能超过45")
    private String buildName;

    @NotEmpty(message="子项编码不能为空!")
    @Size(max=45, message="子项编码长度不能超过45")
    private String buildNo;

    @NotNull(message="buildArea不能为空!")
    @Size(max=45, message="buildArea长度不能超过45")
    private String buildArea;

    @NotNull(message="seq不能为空!")
    @Max(value=999999999, message="seq必须为数字")
    private Integer seq;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="gmtCreate不能为空!")
    private Date gmtCreate;

    @NotNull(message="gmtModified不能为空!")
    private Date gmtModified;

    @Size(max=450, message="备注长度不能超过450")
    private String remark;
}