package com.cmcu.mcc.finance.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveFinanceStampTax {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="formNo不能为空!")
    @Size(max=45, message="formNo长度不能超过45")
    private String formNo;

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

    @NotNull(message="isProcessEnd不能为空!")
    private Boolean processEnd;

    @NotNull(message="创建时间不能为空!")
    private Date gmtCreate;

    @NotNull(message="修改时间不能为空!")
    private Date gmtModified;

    @NotNull(message="processInstanceId不能为空!")
    @Size(max=45, message="processInstanceId长度不能超过45")
    private String processInstanceId;

    @NotNull(message="contractId不能为空!")
    @Max(value=999999999, message="contractId必须为数字")
    private Integer contractId;

    @NotNull(message="contractName不能为空!")
    @Size(max=45, message="contractName长度不能超过45")
    private String contractName;

    @NotNull(message="contractNo不能为空!")
    @Size(max=45, message="contractNo长度不能超过45")
    private String contractNo;

    @NotNull(message="customerName不能为空!")
    @Size(max=45, message="customerName长度不能超过45")
    private String customerName;

    @NotNull(message="projectName不能为空!")
    @Size(max=45, message="projectName长度不能超过45")
    private String projectName;

    @NotNull(message="项目号不能为空!")
    @Size(max=45, message="项目号长度不能超过45")
    private String projectNo;

    @NotNull(message="signTime不能为空!")
    @Size(max=45, message="signTime长度不能超过45")
    private String signTime;

    @NotNull(message="税目不能为空!")
    @Size(max=45, message="税目长度不能超过45")
    private String taxType;

    @NotNull(message="合同金额不能为空!")
    @Size(max=45, message="合同金额长度不能超过45")
    private String contractMoney;

    @NotNull(message="税率不能为空!")
    @Size(max=45, message="税率长度不能超过45")
    private String taxNum;

    @NotNull(message="印花税额不能为空!")
    @Size(max=45, message="印花税额长度不能超过45")
    private String stampTaxMoney;

    @NotNull(message="contractSubpackageId不能为空!")
    @Max(value=999999999, message="contractSubpackageId必须为数字")
    private Integer contractSubpackageId;

    @NotNull(message="是否开票不能为空!")
    private Boolean openStamp;

    @NotNull(message="contractDeptId不能为空!")
    @Max(value=999999999, message="contractDeptId必须为数字")
    private Integer contractDeptId;

    @NotNull(message="合同所属部门不能为空!")
    @Size(max=45, message="合同所属部门长度不能超过45")
    private String contractDeptName;
}