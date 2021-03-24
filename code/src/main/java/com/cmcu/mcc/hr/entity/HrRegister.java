package com.cmcu.mcc.hr.entity;

import com.cmcu.common.annotation.FieldName;
import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class HrRegister {
    @FieldName("id")
    @NotNull
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @FieldName("businessKey")
    @NotNull
    @Size(max=45)
    private String businessKey;

    @FieldName("省份")
    @NotNull
    @Size(max=45)
    private String province;

    @FieldName("城市")
    @NotNull
    @Size(max=45)
    private String city;

    @FieldName("专业")
    @NotNull
    @Size(max=145)
    private String specialty;

    @FieldName("userLogin")
    @NotNull
    @Size(max=45)
    private String userLogin;

    @FieldName("姓名")
    @NotNull
    @Size(max=45)
    private String userName;

    @FieldName("开始时间")
    @NotNull
    @Size(max=45)
    private String startTime;

    @FieldName("结束时间")
    @NotNull
    @Size(max=45)
    private String endTime;

    @FieldName("gmtCreate")
    @NotNull
    private Date gmtCreate;

    @FieldName("gmtModified")
    @NotNull
    private Date gmtModified;

    @FieldName("备注")
    @NotNull
    @Size(max=45)
    private String remark;

    @FieldName("isDeleted")
    @NotNull
    private Boolean deleted;
}