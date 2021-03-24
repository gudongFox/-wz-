package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaInformationEquipmentProcurementDetail {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="主表id不能为空!")
    @Size(max=45, message="主表id长度不能超过45")
    private String informationEquipmentProcurementId;

    @NotNull(message="设备名称不能为空!")
    @Size(max=45, message="设备名称长度不能超过45")
    private String equipmentName;

    @NotNull(message="设别型号不能为空!")
    @Size(max=45, message="设别型号长度不能超过45")
    private String equipmentType;

    @Size(max=45, message="设备类型长度不能超过45")
    private String equipmentModel;

    @NotNull(message="用途功能不能为空!")
    @Size(max=45, message="用途功能长度不能超过45")
    private String useType;

    @NotNull(message="数量不能为空!")
    @Size(max=45, message="数量长度不能超过45")
    private String number;

    @NotNull(message="单价(万元)不能为空!")
    @Size(max=45, message="单价(万元)长度不能超过45")
    private String price;

    @NotNull(message="总价 万元不能为空!")
    @Size(max=45, message="总价 万元长度不能超过45")
    private String totalPrice;

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