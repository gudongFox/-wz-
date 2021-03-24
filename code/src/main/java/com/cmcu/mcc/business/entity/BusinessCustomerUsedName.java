package com.cmcu.mcc.business.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class BusinessCustomerUsedName {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="customerId不能为空!")
    @Max(value=999999999, message="customerId必须为数字")
    private Integer customerId;

    @NotNull(message="变更前名称不能为空!")
    @Size(max=45, message="变更前名称长度不能超过45")
    private String oldName;

    @NotNull(message="变更后名称不能为空!")
    @Size(max=145, message="变更后名称长度不能超过145")
    private String name;

    @NotNull(message="code不能为空!")
    @Size(max=45, message="code长度不能超过45")
    private String code;

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

    @NotNull(message="creator不能为空!")
    @Size(max=45, message="creator长度不能超过45")
    private String creator;

    @NotNull(message="creatorName不能为空!")
    @Size(max=45, message="creatorName长度不能超过45")
    private String creatorName;
}