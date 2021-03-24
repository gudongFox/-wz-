package com.cmcu.mcc.business.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class BusinessChangeSupplier {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="seq不能为空!")
    @Max(value=999999999, message="seq必须为数字")
    private Integer seq;

    @NotNull(message="deptId不能为空!")
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

     @NotNull(message="deptName不能为空!")
    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

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

    @NotNull(message="isProcessEnd不能为空!")
    private Boolean processEnd;

    @NotNull(message="processInstanceId不能为空!")
    @Size(max=45, message="processInstanceId长度不能超过45")
    private String processInstanceId;

    @NotNull(message="supplierId不能为空!")
    @Max(value=999999999, message="supplierId必须为数字")
    private Integer supplierId;

    @NotNull(message="supplierName不能为空!")
    @Size(max=45, message="supplierName长度不能超过45")
    private String supplierName;

    @NotNull(message="变更内容不能为空!")
    @Size(max=450, message="变更内容长度不能超过450")
    private String changeContent;

    @NotNull(message="title不能为空!")
    @Size(max=45, message="title长度不能超过45")
    private String title;
}