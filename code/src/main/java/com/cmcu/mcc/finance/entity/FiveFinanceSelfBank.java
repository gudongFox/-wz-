package com.cmcu.mcc.finance.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveFinanceSelfBank {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="formNo不能为空!")
    @Size(max=45, message="formNo长度不能超过45")
    private String formNo;

    @NotNull(message="deptId不能为空!")
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

     @NotNull(message="deptName不能为空!")
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

    @NotNull(message="title不能为空!")
    @Size(max=45, message="title长度不能超过45")
    private String title;

    @NotNull(message="userLogin不能为空!")
    @Size(max=45, message="userLogin长度不能超过45")
    private String userLogin;

    @NotNull(message="userName不能为空!")
    @Size(max=45, message="userName长度不能超过45")
    private String userName;

    @NotNull(message="银行名称不能为空!")
    @Size(max=45, message="银行名称长度不能超过45")
    private String bankName;

    @NotNull(message="bankCode不能为空!")
    @Size(max=45, message="bankCode长度不能超过45")
    private String bankCode;

    @NotNull(message="银行卡号不能为空!")
    @Size(max=45, message="银行卡号长度不能超过45")
    private String userBankNo;

    @NotNull(message="个人身份证不能为空!")
    @Size(max=45, message="个人身份证长度不能超过45")
    private String userIdNo;
}