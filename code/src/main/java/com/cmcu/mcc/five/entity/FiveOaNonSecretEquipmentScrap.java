package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaNonSecretEquipmentScrap {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="表单号不能为空!")
    @Size(max=45, message="表单号长度不能超过45")
    private String formNo;

    @NotNull(message="设备所属单位不能为空!")
    @Max(value=999999999, message="设备所属单位必须为数字")
    private Integer deptId;

     @NotNull(message="deptName不能为空!")
    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="申请人不能为空!")
    @Size(max=450, message="申请人长度不能超过450")
    private String applyMan;

    @NotNull(message="申请时间不能为空!")
    @Size(max=45, message="申请时间长度不能超过45")
    private String applyManName;

    @NotNull(message="deptChargeMen不能为空!")
    @Size(max=45, message="deptChargeMen长度不能超过45")
    private String deptChargeMen;

    @NotNull(message="部门负责人不能为空!")
    @Size(max=45, message="部门负责人长度不能超过45")
    private String deptChargeMenName;

    @NotNull(message="信息化办 办理意见不能为空!")
    @Size(max=450, message="信息化办 办理意见长度不能超过450")
    private String equipmentName;

    @NotNull(message="设备编号不能为空!")
    @Size(max=450, message="设备编号长度不能超过450")
    private String equipmentNo;

    @NotNull(message="设备序列号不能为空!")
    @Size(max=450, message="设备序列号长度不能超过450")
    private String equipmentSerial;

    @NotNull(message="硬盘序列号不能为空!")
    @Size(max=45, message="硬盘序列号长度不能超过45")
    private String hardNo;

    @NotNull(message="固定资产编号不能为空!")
    @Size(max=45, message="固定资产编号长度不能超过45")
    private String assetsNo;

    @NotNull(message="报废原因不能为空!")
    @Size(max=450, message="报废原因长度不能超过450")
    private String scrapReason;

    @NotNull(message="remark不能为空!")
    @Size(max=450, message="remark长度不能超过450")
    private String remark;

    @NotNull(message="安全处理记录 硬盘不能为空!")
    private Boolean secretHard;

    @NotNull(message="secretHardNo不能为空!")
    @Size(max=45, message="secretHardNo长度不能超过45")
    private String secretHardNo;

    @NotNull(message="安全处理记录 内存不能为空!")
    private Boolean secretMemory;

    @NotNull(message="secretMemoryNo不能为空!")
    @Size(max=45, message="secretMemoryNo长度不能超过45")
    private String secretMemoryNo;

    @NotNull(message="安全处理记录其他不能为空!")
    private Boolean secretOther;

    @NotNull(message="secretOtherNo不能为空!")
    @Size(max=45, message="secretOtherNo长度不能超过45")
    private String secretOtherNo;

    @NotNull(message="启用时间不能为空!")
    @Size(max=45, message="启用时间长度不能超过45")
    private String startTime;

    @NotNull(message="原值不能为空!")
    @Size(max=45, message="原值长度不能超过45")
    private String originalValue;

    @NotNull(message="折旧年限不能为空!")
    @Size(max=45, message="折旧年限长度不能超过45")
    private String depreciationYear;

    @NotNull(message="已提折旧不能为空!")
    @Size(max=45, message="已提折旧长度不能超过45")
    private String depreciationAlready;

    @NotNull(message="净值不能为空!")
    @Size(max=45, message="净值长度不能超过45")
    private String netWorth;

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

    @NotNull(message="processInstanceId不能为空!")
    @Size(max=45, message="processInstanceId长度不能超过45")
    private String processInstanceId;

    @NotNull(message="isProcessEnd不能为空!")
    private Boolean processEnd;

    @NotNull(message="台账是否处理不能为空!")
    private Boolean disposeAsset;
}