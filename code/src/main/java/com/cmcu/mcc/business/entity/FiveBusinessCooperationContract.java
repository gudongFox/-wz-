package com.cmcu.mcc.business.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveBusinessCooperationContract {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="协作单不能为空!")
    @Max(value=999999999, message="协作单必须为数字")
    private Integer cooperationDelegationId;

    @NotNull(message="remark不能为空!")
    @Size(max=450, message="remark长度不能超过450")
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

    @NotNull(message="协作项目名称不能为空!")
    @Size(max=450, message="协作项目名称长度不能超过450")
    private String cooperationProjectName;

    @NotNull(message="协作项目号不能为空!")
    @Size(max=45, message="协作项目号长度不能超过45")
    private String cooperationProjectNo;

    @NotNull(message="协作项目类型不能为空!")
    @Size(max=45, message="协作项目类型长度不能超过45")
    private String cooperationProjectType;

    @NotNull(message="委托方不能为空!")
    @Max(value=999999999, message="委托方必须为数字")
    private Integer delegationDeptId;

    @NotNull(message="delegationDeptName不能为空!")
    @Size(max=45, message="delegationDeptName长度不能超过45")
    private String delegationDeptName;

    @NotNull(message="cooperationDeptId不能为空!")
    @Max(value=999999999, message="cooperationDeptId必须为数字")
    private Integer cooperationDeptId;

    @NotNull(message="cooperationDeptName不能为空!")
    @Size(max=45, message="cooperationDeptName长度不能超过45")
    private String cooperationDeptName;

    @NotNull(message="contractName不能为空!")
    @Size(max=450, message="contractName长度不能超过450")
    private String contractName;

    @NotNull(message="contractType不能为空!")
    @Size(max=45, message="contractType长度不能超过45")
    private String contractType;

    @NotNull(message="contractNo不能为空!")
    @Size(max=45, message="contractNo长度不能超过45")
    private String contractNo;

    @NotNull(message="收费金额不能为空!")
    @Size(max=45, message="收费金额长度不能超过45")
    private String chargeMoney;

    @NotNull(message="收费时间不能为空!")
    @Size(max=45, message="收费时间长度不能超过45")
    private String chargeMoneyTime;

    @NotNull(message="cooperationMoney不能为空!")
    @Size(max=45, message="cooperationMoney长度不能超过45")
    private String cooperationMoney;

    @NotNull(message="协作内容及要求不能为空!")
    @Size(max=450, message="协作内容及要求长度不能超过450")
    private String cooperationContent;

    @NotNull(message="协作费用结算要求不能为空!")
    @Size(max=450, message="协作费用结算要求长度不能超过450")
    private String cooperationMoneyClose;

    @NotNull(message="title不能为空!")
    @Size(max=145, message="title长度不能超过145")
    private String title;

    @NotNull(message="deptId不能为空!")
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="signTime不能为空!")
    @Size(max=45, message="signTime长度不能超过45")
    private String signTime;

    @NotNull(message="contractLibraryId不能为空!")
    @Max(value=999999999, message="contractLibraryId必须为数字")
    private Integer contractLibraryId;
}