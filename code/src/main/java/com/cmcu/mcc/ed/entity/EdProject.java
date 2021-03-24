package com.cmcu.mcc.ed.entity;

import com.cmcu.common.annotation.FieldName;
import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class EdProject {
    @FieldName("id")
    @NotNull
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @FieldName("deptId")
    @NotNull
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

    @FieldName("deptName")
    @NotEmpty
    @Size(max=145)
    private String deptName;


    @FieldName("contract_id")
    @NotNull
    @Max(value=999999999, message="deptId必须为数字")
    private Integer contractId;

    @FieldName("projectNo")
    @NotEmpty
    @Size(max=45)
    private String projectNo;

    @FieldName("projectName")
    @NotEmpty
    @Size(max=145)
    private String projectName;

    @FieldName("合同号")
    @NotEmpty
    @Size(max=45)
    private String contractNo;

    @FieldName("设计作业类型")
    @NotEmpty
    @Size(max=45)
    private String projectType;

    @FieldName("设计评审阶段")
    @NotEmpty
    @Size(max=45)
    private String stageNames;

    @FieldName("项目负责人")
    @NotEmpty
    @Size(max=145)
    private String chargeMen;

    @FieldName("chargeMenName")
    @NotEmpty
    @Size(max=145)
    private String chargeMenName;

    @FieldName("exeChargeMen")
    @NotEmpty
    @Size(max=145)
    private String exeChargeMen;

    @FieldName("exeChargeMenName")
    @NotEmpty
    @Size(max=145)
    private String exeChargeMenName;

    @FieldName("seq")
    @NotNull
    @Max(value=999999999, message="seq必须为数字")
    private Integer seq;

    @FieldName("0")
    @NotNull
    private Boolean deleted;

    @FieldName("gmtCreate")
    @NotNull
    private Date gmtCreate;

    @FieldName("gmtModified")
    @NotNull
    private Date gmtModified;

    @FieldName("remark")
    @NotEmpty
    @Size(max=450)
    private String remark;
}