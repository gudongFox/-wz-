package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaProjectfundplanDetail {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="主表id不能为空!")
    @Size(max=45, message="主表id长度不能超过45")
    private String projectfundplanId;

    @NotNull(message="合同类型 总包 分包不能为空!")
    @Size(max=45, message="合同类型 总包 分包长度不能超过45")
    private String type;

    @NotNull(message="合同号不能为空!")
    @Size(max=45, message="合同号长度不能超过45")
    private String contractNo;

    @NotNull(message="对方单位名称不能为空!")
    @Size(max=145, message="对方单位名称长度不能超过145")
    private String deptName;

    @NotNull(message="项目名称不能为空!")
    @Size(max=45, message="项目名称长度不能超过45")
    private String projectName;

    @NotNull(message="合同额 不能为空!")
    @Size(max=45, message="合同额 长度不能超过45")
    private String contractPrice;

    @NotNull(message="本月前累计收款不能为空!")
    @Size(max=45, message="本月前累计收款长度不能超过45")
    private String accumulativePrice;

    @NotNull(message="本月应收不能为空!")
    @Size(max=45, message="本月应收长度不能超过45")
    private String receivablePrice;

    @NotNull(message="尾款不能为空!")
    @Size(max=45, message="尾款长度不能超过45")
    private String finalPrice;

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

    @Size(max=45, message="采购平台长度不能超过45")
    private String purchasePlatform;

    @Size(max=255, message="采购方式长度不能超过255")
    private String purchaseType;

    @Max(value=999999999, message="排序必须为数字")
    private Integer seq;
}