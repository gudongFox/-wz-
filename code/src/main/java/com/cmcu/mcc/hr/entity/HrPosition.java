package com.cmcu.mcc.hr.entity;

import com.cmcu.common.annotation.FieldName;
import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class HrPosition {
    @FieldName("id")
    @NotNull
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @FieldName("positionName")
    @NotNull
    @Size(max=45)
    private String positionName;

    @FieldName("positionLv")
    @NotNull
    @Max(value=999999999, message="positionLv必须为数字")
    private Integer positionLv;

    @FieldName("seq")
    @NotNull
    @Max(value=999999999, message="seq必须为数字")
    private Integer seq;

    @FieldName("remark")
    @NotNull
    @Size(max=450)
    private String remark;

    @FieldName("isDeleted")
    @NotNull
    private Boolean deleted;

    @FieldName("gmtCreate")
    @NotNull
    private Date gmtCreate;

    @FieldName("gmtModified")
    @NotNull
    private Date gmtModified;

    @FieldName("isDeptCharge")
    @NotNull
    private Boolean deptCharge;

    @FieldName("childDeptIds")
    @NotNull
    @Size(max=4500)
    private String childDeptIds;
}