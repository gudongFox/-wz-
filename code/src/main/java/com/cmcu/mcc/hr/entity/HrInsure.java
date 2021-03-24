package com.cmcu.mcc.hr.entity;

import com.cmcu.common.annotation.FieldName;
import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class HrInsure {
    @FieldName("id")
    @NotNull
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @FieldName("userLogin")
    @NotNull
    @Size(max=45)
    private String userLogin;

    @FieldName("userName")
    @NotNull
    @Size(max=45)
    private String userName;

    @FieldName("idCardType")
    @NotNull
    @Size(max=45)
    private String idCardType;

    @FieldName("idCardNo")
    @NotNull
    @Size(max=45)
    private String idCardNo;

    @FieldName("socialNo")
    @NotNull
    @Size(max=45)
    private String socialNo;

    @FieldName("companyName")
    @NotNull
    @Size(max=45)
    private String companyName;

    @FieldName("mobile")
    @NotNull
    @Size(max=45)
    private String mobile;

    @FieldName("liveAddress")
    @NotNull
    @Size(max=45)
    private String liveAddress;

    @FieldName("申请原因")
    @NotNull
    @Size(max=45)
    private String applyReason;

    @FieldName("isDeleted")
    @NotNull
    private Boolean deleted;

    @FieldName("gmtCreate")
    @NotNull
    private Date gmtCreate;

    @FieldName("processInstanceId")
    @NotNull
    @Size(max=45)
    private String processInstanceId;

    @FieldName("processEnd")
    @NotNull
    private Boolean processEnd;
}