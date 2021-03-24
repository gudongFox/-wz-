package com.cmcu.mcc.finance.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveFinanceRefundDetail {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="refundId不能为空!")
    @Max(value=999999999, message="refundId必须为数字")
    private Integer refundId;

    @NotNull(message="refundMan不能为空!")
    @Size(max=45, message="refundMan长度不能超过45")
    private String refundMan;

    @NotNull(message="借款人不能为空!")
    @Size(max=45, message="借款人长度不能超过45")
    private String refundManName;

    @NotNull(message="借款单不能为空!")
    @Size(max=45, message="借款单长度不能超过45")
    private String refundReceipts;

    @NotNull(message="借款余额不能为空!")
    @Size(max=45, message="借款余额长度不能超过45")
    private String refundMoney;

    @NotNull(message="本次还款金额不能为空!")
    @Size(max=45, message="本次还款金额长度不能超过45")
    private String returnMoney;

    @NotNull(message="备注不能为空!")
    @Size(max=450, message="备注长度不能超过450")
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
}