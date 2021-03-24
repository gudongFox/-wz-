package com.cmcu.mcc.business.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveBusinessOutAssist {
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

    @NotNull(message="deptId不能为空!")
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

     @NotNull(message="deptName不能为空!")
    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

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

    @NotNull(message="合同金额不能为空!")
    @Size(max=45, message="合同金额长度不能超过45")
    private String contractMoney;

    @NotNull(message="本月应付款不能为空!")
    @Size(max=45, message="本月应付款长度不能超过45")
    private String shouldPayMoney;

    @NotNull(message="本月实付款不能为空!")
    @Size(max=45, message="本月实付款长度不能超过45")
    private String realPayMoney;

    @NotNull(message="外协单位名称不能为空!")
    @Size(max=45, message="外协单位名称长度不能超过45")
    private String assistOutDept;

    @NotNull(message="title不能为空!")
    @Size(max=450, message="title长度不能超过450")
    private String title;

    @NotNull(message="applyMonth不能为空!")
    @Size(max=45, message="applyMonth长度不能超过45")
    private String applyMonth;
}