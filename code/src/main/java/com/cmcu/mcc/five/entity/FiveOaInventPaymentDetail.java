package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaInventPaymentDetail {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="paymentId不能为空!")
    @Max(value=999999999, message="paymentId必须为数字")
    private Integer paymentId;

    @NotNull(message="专利号不能为空!")
    @Size(max=45, message="专利号长度不能超过45")
    private String inventNo;

    @NotNull(message="专利名称不能为空!")
    @Size(max=45, message="专利名称长度不能超过45")
    private String inventName;

    @NotNull(message="维持放弃意见不能为空!")
    @Size(max=45, message="维持放弃意见长度不能超过45")
    private String keepGiveUp;

    @NotNull(message="论述放弃理由不能为空!")
    @Size(max=45, message="论述放弃理由长度不能超过45")
    private String reason;

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

    @NotNull(message="缴费年限不能为空!")
    @Size(max=45, message="缴费年限长度不能超过45")
    private String year;

    @NotNull(message="缴费金额不能为空!")
    @Size(max=45, message="缴费金额长度不能超过45")
    private String money;
}