package com.cmcu.mcc.finance.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveFinanceSubpackagePayment {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="formNo不能为空!")
    @Size(max=45, message="formNo长度不能超过45")
    private String formNo;

    @NotNull(message="projectId不能为空!")
    @Max(value=999999999, message="projectId必须为数字")
    private Integer projectId;

    @NotNull(message="projectName不能为空!")
    @Size(max=450, message="projectName长度不能超过450")
    private String projectName;

    @NotNull(message="项目类别不能为空!")
    @Size(max=45, message="项目类别长度不能超过45")
    private String projectType;

    @NotNull(message="附单数据不能为空!")
    @Size(max=45, message="附单数据长度不能超过45")
    private String attachedList;

    @NotNull(message="统计不能为空!")
    @Size(max=45, message="统计长度不能超过45")
    private String totalCount;

    @NotNull(message="deptId不能为空!")
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

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

    @NotNull(message="isProcessEnd不能为空!")
    private Boolean processEnd;

    @NotNull(message="processInstanceId不能为空!")
    @Size(max=45, message="processInstanceId长度不能超过45")
    private String processInstanceId;

    @NotNull(message="payName不能为空!")
    @Size(max=45, message="payName长度不能超过45")
    private String payName;

    @NotNull(message="payBank不能为空!")
    @Size(max=45, message="payBank长度不能超过45")
    private String payBank;

    @NotNull(message="payAccount不能为空!")
    @Size(max=45, message="payAccount长度不能超过45")
    private String payAccount;

    @NotNull(message="receiveName不能为空!")
    @Size(max=45, message="receiveName长度不能超过45")
    private String receiveName;

    @NotNull(message="receiveBank不能为空!")
    @Size(max=45, message="receiveBank长度不能超过45")
    private String receiveBank;

    @NotNull(message="receiveAccount不能为空!")
    @Size(max=45, message="receiveAccount长度不能超过45")
    private String receiveAccount;

    @NotNull(message="单据号不能为空!")
    @Size(max=45, message="单据号长度不能超过45")
    private String receiptsNumber;

    @NotNull(message="是否计划内不能为空!")
    private Boolean plan;
}