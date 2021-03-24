package com.cmcu.mcc.business.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class BusinessSupplierAptitude {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="supplierId不能为空!")
    @Max(value=999999999, message="supplierId必须为数字")
    private Integer supplierId;

    @NotNull(message="资质类别不能为空!")
    @Size(max=45, message="资质类别长度不能超过45")
    private String type;

    @NotNull(message="等级不能为空!")
    @Size(max=45, message="等级长度不能超过45")
    private String grade;

    @NotNull(message="编号不能为空!")
    @Size(max=45, message="编号长度不能超过45")
    private String code;

    @NotNull(message="有效期不能为空!")
    @Size(max=45, message="有效期长度不能超过45")
    private String validityTime;

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