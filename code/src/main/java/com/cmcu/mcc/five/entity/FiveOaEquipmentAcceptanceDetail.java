package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaEquipmentAcceptanceDetail {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="发票编号不能为空!")
    @Size(max=45, message="发票编号长度不能超过45")
    private String invoiceNo;

    @Size(max=45, message="固定资产编号长度不能超过45")
    private String fixedAssetNo;

    @NotNull(message="equipmentId不能为空!")
    @Max(value=999999999, message="equipmentId必须为数字")
    private Integer equipmentId;

    @NotNull(message="'设备名称'不能为空!")
    @Size(max=45, message="'设备名称'长度不能超过45")
    private String equipmentName;

    @NotNull(message="'设备规格'不能为空!")
    @Size(max=45, message="'设备规格'长度不能超过45")
    private String specification;

    @NotNull(message="deptId不能为空!")
    @Size(max=45, message="deptId长度不能超过45")
    private String deptId;

    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="'数量'不能为空!")
    @Max(value=999999999, message="'数量'必须为数字")
    private Integer number;

    @NotNull(message="'单价'不能为空!")
    @Size(max=45, message="'单价'长度不能超过45")
    private String price;

    @NotNull(message="'总价'不能为空!")
    @Size(max=45, message="'总价'长度不能超过45")
    private String totalPrice;

    @NotNull(message="remark不能为空!")
    @Size(max=45, message="remark长度不能超过45")
    private String remark;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="是否上传附件不能为空!")
    private Boolean uploadfile;

    @NotNull(message="creator不能为空!")
    @Size(max=45, message="creator长度不能超过45")
    private String creator;

    @NotNull(message="creatorName不能为空!")
    @Size(max=45, message="creatorName长度不能超过45")
    private String creatorName;

    @NotNull(message="gmtCreate不能为空!")
    private Date gmtCreate;

    @NotNull(message="gmtModified不能为空!")
    private Date gmtModified;
}