package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaFurniturePurchaseDetail {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="furnitureId不能为空!")
    @Max(value=999999999, message="furnitureId必须为数字")
    private Integer furnitureId;

    @NotNull(message="家具名称不能为空!")
    @Size(max=45, message="家具名称长度不能超过45")
    private String furnitureName;

    @NotNull(message="家具数量不能为空!")
    @Size(max=45, message="家具数量长度不能超过45")
    private String number;

    @NotNull(message="单价不能为空!")
    @Size(max=45, message="单价长度不能超过45")
    private String price;

    @NotNull(message="小计不能为空!")
    @Size(max=45, message="小计长度不能超过45")
    private String totalPrice;

    @NotNull(message="备注不能为空!")
    @Size(max=45, message="备注长度不能超过45")
    private String remark;

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

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="固定资产编号不能为空!")
    @Size(max=45, message="固定资产编号长度不能超过45")
    private String assetCode;
}