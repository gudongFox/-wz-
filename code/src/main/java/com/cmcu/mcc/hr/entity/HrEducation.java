package com.cmcu.mcc.hr.entity;

import com.cmcu.common.annotation.FieldName;
import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class HrEducation {
    @FieldName("id")
    @NotNull
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @FieldName("userLogin")
    @NotEmpty
    @Size(max=45)
    private String userLogin;

    @FieldName("schoolName")
    @NotEmpty
    @Size(max=145)
    private String schoolName;

    @FieldName("beginTime")
    @NotEmpty
    @Size(max=45)
    private String beginTime;

    @FieldName("endTime")
    @NotEmpty
    @Size(max=45)
    private String endTime;

    @FieldName("全日制、在职、函授")
    @NotEmpty
    @Size(max=45)
    private String educationType;

    @FieldName("小学、初中、高中、中专、专科、本科、研究生")
    @NotEmpty
    @Size(max=45)
    private String educationName;

    @FieldName("学制")
    @NotEmpty
    @Size(max=45)
    private String educationYear;

    @FieldName("学位")
    @NotEmpty
    @Size(max=45)
    private String educationDegree;

    @FieldName("第一专业")
    @NotEmpty
    @Size(max=45)
    private String primarySpecialty;

    @FieldName("其他专业")
    @NotEmpty
    @Size(max=145)
    private String otherSpecialty;

    @FieldName("seq")
    @NotNull
    @Max(value=999999999, message="seq必须为数字")
    private Integer seq;

    @FieldName("remark")
    @NotEmpty
    @Size(max=450)
    private String remark;

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