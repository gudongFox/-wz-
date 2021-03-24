package com.cmcu.mcc.hr.entity;

import com.cmcu.common.annotation.FieldName;
import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class HrVacationDetail {
    @FieldName("id")
    @NotNull
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @FieldName("vacationId")
    @NotNull
    @Max(value=999999999, message="vacationId必须为数字")
    private Integer vacationId;

    @FieldName("vacationType")
    @NotEmpty
    @Size(max=45)
    private String vacationType;

    @FieldName("计划时间")
    @NotEmpty
    @Size(max=45)
    private String planBegin;

    @FieldName("planEnd")
    @NotEmpty
    @Size(max=45)
    private String planEnd;

    @FieldName("planDay")
    @NotNull
    @Max(value=999999999, message="planDay必须为数字")
    private Integer planDay;

    @FieldName("实际开始时间")
    @NotEmpty
    @Size(max=45)
    private String actualBegin;

    @FieldName("实际结束时间")
    @NotEmpty
    @Size(max=45)
    private String actualEnd;

    @FieldName("上年剩余天数")
    @NotNull
    @Max(value=999999999, message="上年剩余天数必须为数字")
    private Integer lastLeft;

    @FieldName("gmtCreate")
    @NotNull
    private Date gmtCreate;

    @FieldName("gmtModified")
    @NotNull
    private Date gmtModified;
}