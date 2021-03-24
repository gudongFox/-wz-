package com.cmcu.mcc.business.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class BusinessSupplier {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="code不能为空!")
    @Size(max=45, message="code长度不能超过45")
    private String code;

    @NotNull(message="名称不能为空!")
    @Size(max=145, message="名称长度不能超过145")
    private String name;

    @NotNull(message="入库类别不能为空!")
    @Size(max=45, message="入库类别长度不能超过45")
    private String inType;

    @NotNull(message="地址不能为空!")
    @Size(max=45, message="地址长度不能超过45")
    private String address;

    @NotNull(message="社会统一信用代码不能为空!")
    @Size(max=45, message="社会统一信用代码长度不能超过45")
    private String creditCode;

    @NotNull(message="legalPerson不能为空!")
    @Size(max=45, message="legalPerson长度不能超过45")
    private String legalPerson;

    @NotNull(message="legalPersonTel不能为空!")
    @Size(max=45, message="legalPersonTel长度不能超过45")
    private String legalPersonTel;

    @NotNull(message="联系人不能为空!")
    @Size(max=45, message="联系人长度不能超过45")
    private String linkMan;

    @NotNull(message="联系电话不能为空!")
    @Size(max=45, message="联系电话长度不能超过45")
    private String linkTel;

    @NotNull(message="linkTitle不能为空!")
    @Size(max=45, message="linkTitle长度不能超过45")
    private String linkTitle;

    @NotNull(message="传真不能为空!")
    @Size(max=45, message="传真长度不能超过45")
    private String linkFax;

    @NotNull(message="Email不能为空!")
    @Size(max=90, message="Email长度不能超过90")
    private String linkMail;

    @NotNull(message="开户银行不能为空!")
    @Size(max=145, message="开户银行长度不能超过145")
    private String depositBank;

    @NotNull(message="银行账号不能为空!")
    @Size(max=145, message="银行账号长度不能超过145")
    private String bankAccount;

    @NotNull(message="纳税人识别号不能为空!")
    @Size(max=45, message="纳税人识别号长度不能超过45")
    private String taxNo;

    @NotNull(message="纳税主体资格  小规模纳税人 一般纳税人不能为空!")
    @Size(max=45, message="纳税主体资格  小规模纳税人 一般纳税人长度不能超过45")
    private String taxType;

    @NotNull(message="经营类别 建筑 化工石化医药 电力 机械不能为空!")
    @Size(max=145, message="经营类别 建筑 化工石化医药 电力 机械长度不能超过145")
    private String businessScope;

    @NotNull(message="供方单位类型 企事业单位 民用事业 政府机关单位 教育单位不能为空!")
    @Size(max=45, message="供方单位类型 企事业单位 民用事业 政府机关单位 教育单位长度不能超过45")
    private String supplierType;

    @NotNull(message="deptId不能为空!")
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

    @NotNull(message="deptName不能为空!")
    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="注册资金不能为空!")
    @Size(max=45, message="注册资金长度不能超过45")
    private String registeredFund;

    @NotNull(message="seq不能为空!")
    @Max(value=999999999, message="seq必须为数字")
    private Integer seq;

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

    @NotNull(message="系统内代号不能为空!")
    @Size(max=45, message="系统内代号长度不能超过45")
    private String systemInName;
}