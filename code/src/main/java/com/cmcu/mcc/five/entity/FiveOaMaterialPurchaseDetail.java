package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaMaterialPurchaseDetail {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="materialPurchaseId不能为空!")
    @Max(value=999999999, message="materialPurchaseId必须为数字")
    private Integer materialPurchaseId;

    @NotNull(message="标准号不能为空!")
    @Size(max=45, message="标准号长度不能超过45")
    private String standardNo;

    @NotNull(message="资料名称不能为空!")
    @Size(max=45, message="资料名称长度不能超过45")
    private String dataName;

    @Size(max=45, message="本数长度不能超过45")
    private String bookNumber;

    @NotNull(message="remark不能为空!")
    @Size(max=450, message="remark长度不能超过450")
    private String remark;

    @NotNull(message="creator不能为空!")
    @Size(max=45, message="creator长度不能超过45")
    private String creator;

    @NotNull(message="创建人姓名不能为空!")
    @Size(max=45, message="创建人姓名长度不能超过45")
    private String creatorName;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="创建时间不能为空!")
    private Date gmtCreate;

    @NotNull(message="修改时间不能为空!")
    private Date gmtModified;
}