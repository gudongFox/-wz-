package com.cmcu.mcc.hr.entity;

import com.cmcu.common.annotation.FieldName;
import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class HrIncomeProof {
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

    @FieldName("证件类别")
    @NotEmpty
    @Size(max=45)
    private String idCardType;

    @FieldName("证件号码")
    @NotEmpty
    @Size(max=45)
    private String idCardNo;

    @FieldName("工作年限")
    @NotEmpty
    @Size(max=45)
    private String workYearNumber;

    @FieldName("申请年份")
    @NotEmpty
    @Size(max=45)
    private String requestIncomeYear;

    @FieldName("工作职位")
    @NotEmpty
    @Size(max=45)
    private String workPosition;

    @FieldName("年收入")
    @NotEmpty
    @Size(max=45)
    private String yearIncome;

    @FieldName("月收入")
    @NotEmpty
    @Size(max=45)
    private String monthIncome;

    @FieldName("公司名称")
    @NotEmpty
    @Size(max=45)
    private String companyName;

    @FieldName("公司地址")
    @NotEmpty
    @Size(max=45)
    private String companyAddress;

    @FieldName("公司电话")
    @NotEmpty
    @Size(max=45)
    private String companyTel;

    @FieldName("联系人")
    @NotEmpty
    @Size(max=45)
    private String companyLinkMan;

    @FieldName("creator")
    @NotEmpty
    @Size(max=45)
    private String creator;

    @FieldName("creatorName")
    @NotEmpty
    @Size(max=45)
    private String creatorName;

    @FieldName("isDeleted")
    @NotNull
    private Boolean deleted;

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