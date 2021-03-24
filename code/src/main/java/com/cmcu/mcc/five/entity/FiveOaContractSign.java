package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaContractSign {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="备案流水号不能为空!")
    @Size(max=45, message="备案流水号长度不能超过45")
    private String recordNo;

    @NotNull(message="主办单位不能为空!")
    @Max(value=999999999, message="主办单位必须为数字")
    private Integer deptId;

     @NotNull(message="deptName不能为空!")
    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="委托方不能为空!")
    @Size(max=45, message="委托方长度不能超过45")
    private String clientName;

    @NotNull(message="委托内容不能为空!")
    @Size(max=450, message="委托内容长度不能超过450")
    private String clientContent;

    @NotNull(message="contractNo不能为空!")
    @Size(max=45, message="contractNo长度不能超过45")
    private String contractNo;

    @NotNull(message="签发不能为空!")
    @Size(max=45, message="签发长度不能超过45")
    private String signer;

    @NotNull(message="signerName不能为空!")
    @Size(max=45, message="signerName长度不能超过45")
    private String signerName;

    @NotNull(message="核稿不能为空!")
    @Size(max=45, message="核稿长度不能超过45")
    private String auditMan;

    @NotNull(message="auditManName不能为空!")
    @Size(max=45, message="auditManName长度不能超过45")
    private String auditManName;

    @NotNull(message="会签不能为空!")
    @Size(max=45, message="会签长度不能超过45")
    private String countersign;

    @NotNull(message="countersignName不能为空!")
    @Size(max=45, message="countersignName长度不能超过45")
    private String countersignName;

    @NotNull(message="reviewContractId不能为空!")
    @Size(max=45, message="reviewContractId长度不能超过45")
    private String reviewContractId;

    @NotNull(message="合同评审单不能为空!")
    @Size(max=45, message="合同评审单长度不能超过45")
    private String reviewContractName;

    @NotNull(message="拟稿人不能为空!")
    @Size(max=45, message="拟稿人长度不能超过45")
    private String drafter;

    @NotNull(message="drafterName不能为空!")
    @Size(max=45, message="drafterName长度不能超过45")
    private String drafterName;

    @NotNull(message="日期不能为空!")
    @Size(max=45, message="日期长度不能超过45")
    private String signTime;

    @NotNull(message="remark不能为空!")
    @Size(max=45, message="remark长度不能超过45")
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

    @NotNull(message="isProcessEnd不能为空!")
    private Boolean processEnd;

    @NotNull(message="processInstanceId不能为空!")
    @Size(max=45, message="processInstanceId长度不能超过45")
    private String processInstanceId;
}