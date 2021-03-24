package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaInformationEquipmentApplyDetail {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="主表id不能为空!")
    @Size(max=45, message="主表id长度不能超过45")
    private String informationEquipmentApplyId;

    @NotNull(message="设备所属部门不能为空!")
    @Max(value=999999999, message="设备所属部门必须为数字")
    private Integer deptId;

    @NotNull(message="设备所在单位不能为空!")
    @Size(max=45, message="设备所在单位长度不能超过45")
    private String deptName;

    @NotNull(message="设备名称不能为空!")
    @Size(max=45, message="设备名称长度不能超过45")
    private String equipmentName;

    @NotNull(message="品牌不能为空!")
    @Size(max=45, message="品牌长度不能超过45")
    private String brand;

    @NotNull(message="设别型号不能为空!")
    @Size(max=45, message="设别型号长度不能超过45")
    private String equipmentType;

    @NotNull(message="数量不能为空!")
    @Size(max=45, message="数量长度不能超过45")
    private String number;

    @NotNull(message="入网类型不能为空!")
    @Size(max=45, message="入网类型长度不能超过45")
    private String netType;

    @NotNull(message="单价不能为空!")
    @Size(max=45, message="单价长度不能超过45")
    private String price;

    @NotNull(message="总价 元不能为空!")
    @Size(max=45, message="总价 元长度不能超过45")
    private String totalPrice;

    @NotNull(message="其他需求不能为空!")
    @Size(max=45, message="其他需求长度不能超过45")
    private String otherRequirement;

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