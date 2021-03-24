package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaNonSecretEquipmentScrapDetail {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="主表id不能为空!")
    @Size(max=45, message="主表id长度不能超过45")
    private String nonsecretEquipmentScrapId;

    @NotNull(message="设备类型不能为空!")
    @Size(max=45, message="设备类型长度不能超过45")
    private String type;

    @NotNull(message="设备编号不能为空!")
    @Size(max=45, message="设备编号长度不能超过45")
    private String equipmentNo;

    @NotNull(message="品牌不能为空!")
    @Size(max=45, message="品牌长度不能超过45")
    private String bank;

    @NotNull(message="保密等级不能为空!")
    @Size(max=45, message="保密等级长度不能超过45")
    private String secretRank;

    @NotNull(message="责任人不能为空!")
    @Size(max=45, message="责任人长度不能超过45")
    private String chargeMan;

    @NotNull(message="chargeManName不能为空!")
    @Size(max=45, message="chargeManName长度不能超过45")
    private String chargeManName;

    @NotNull(message="磁盘序列号不能为空!")
    @Size(max=45, message="磁盘序列号长度不能超过45")
    private String diskNo;

    @NotNull(message="启用时间不能为空!")
    @Size(max=45, message="启用时间长度不能超过45")
    private String startTime;

    @NotNull(message="报废/停用不能为空!")
    @Size(max=45, message="报废/停用长度不能超过45")
    private String scrapType;

    @NotNull(message="custodyMan不能为空!")
    @Size(max=45, message="custodyMan长度不能超过45")
    private String custodyMan;

    @NotNull(message="custodyManName不能为空!")
    @Size(max=45, message="custodyManName长度不能超过45")
    private String custodyManName;

    @NotNull(message="deptId不能为空!")
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

     @NotNull(message="deptName不能为空!")
    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="固定资产编号不能为空!")
    @Size(max=45, message="固定资产编号长度不能超过45")
    private String assetsNo;

    @NotNull(message="原值(元)不能为空!")
    @Size(max=45, message="原值(元)长度不能超过45")
    private String price;

    @NotNull(message="折旧年限不能为空!")
    @Size(max=45, message="折旧年限长度不能超过45")
    private String depreciationYear;

    @NotNull(message="已折旧(元)不能为空!")
    @Size(max=45, message="已折旧(元)长度不能超过45")
    private String depreciationPrice;

    @NotNull(message="净值(元)不能为空!")
    @Size(max=45, message="净值(元)长度不能超过45")
    private String netWorth;

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