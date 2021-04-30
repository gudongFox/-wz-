package com.cmcu.mcc.finance.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveFinanceSubpackagePaymentDetail {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="subpackagePaymentId不能为空!")
    @Max(value=999999999, message="subpackagePaymentId必须为数字")
    private Integer subpackagePaymentId;

    @NotNull(message="deptId不能为空!")
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @Size(max=45, message="applicant长度不能超过45")
    private String applicant;

    @Size(max=45, message="列支人长度不能超过45")
    private String applicantName;

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

    @NotNull(message="付款用途不能为空!")
    @Size(max=45, message="付款用途长度不能超过45")
    private String paymentPurpose;

    @NotNull(message="申请金额不能为空!")
    @Size(max=45, message="申请金额长度不能超过45")
    private String applyMoney;

    @NotNull(message="批复金额不能为空!")
    @Size(max=45, message="批复金额长度不能超过45")
    private String replayMoney;


}