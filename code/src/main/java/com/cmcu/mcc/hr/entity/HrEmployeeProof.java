package com.cmcu.mcc.hr.entity;

import com.cmcu.common.annotation.FieldName;
import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class HrEmployeeProof {
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

    @FieldName("joinCompanyTime")
    @NotNull
    @Size(max=45)
    private String joinCompanyTime;

    @FieldName("companyName")
    @NotNull
    @Size(max=45)
    private String companyName;

    @FieldName("gmtCreate")
    @NotNull
    private Date gmtCreate;

    @FieldName("remark")
    @NotNull
    @Size(max=450)
    private String remark;

    @FieldName("isDeleted")
    @NotNull
    private Boolean deleted;

    @FieldName("processInstanceId")
    @NotNull
    @Size(max=45)
    private String processInstanceId;

    @FieldName("isProcessEnd")
    @NotNull
    private Boolean processEnd;
}