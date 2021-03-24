package com.cmcu.mcc.hr.entity;

import com.cmcu.common.annotation.FieldName;
import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class HrCertification {
    @FieldName("id")
    @NotNull
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @FieldName("userLogin")
    @NotEmpty
    @Size(max=45)
    private String userLogin;

    @FieldName("userName")
    @NotEmpty
    @Size(max=45)
    private String userName;

    @FieldName("certificationName")
    @NotEmpty
    @Size(max=45)
    private String certificationName;

    @FieldName("证书编号")
    @NotEmpty
    @Size(max=45)
    private String certificationNo;

    @FieldName("专业类别")
    @NotEmpty
    @Size(max=45)
    private String majorName;

    @FieldName("注册证书号")
    @NotEmpty
    @Size(max=45)
    private String registerNo;

    @FieldName("印章号")
    @NotEmpty
    @Size(max=45)
    private String sealNo;

    @FieldName("职业资格证书号")
    @NotEmpty
    @Size(max=45)
    private String qualifyNo;

    @FieldName("签发时间")
    @NotEmpty
    @Size(max=45)
    private String issuseDate;

    @FieldName("是否在公司")
    @NotNull
    private Boolean incompany;

    @FieldName("注册进公司时间")
    @NotEmpty
    @Size(max=45)
    private String inDate;

    @FieldName("validDate")
    @NotEmpty
    @Size(max=45)
    private String validDate;

    @FieldName("isDeleted")
    @NotNull
    private Boolean deleted;

    @FieldName("备注")
    @NotEmpty
    @Size(max=450)
    private String remark;

    @FieldName("creator")
    @NotEmpty
    @Size(max=45)
    private String creator;

    @FieldName("creatorName")
    @NotEmpty
    @Size(max=45)
    private String creatorName;

    @FieldName("gmtCreate")
    @NotNull
    private Date gmtCreate;

    @FieldName("gmtModified")
    @NotNull
    private Date gmtModified;

    @FieldName("processInstanceId")
    @NotEmpty
    @Size(max=45)
    private String processInstanceId;

    @FieldName("isProcessEnd")
    @NotNull
    private Boolean processEnd;
}