package com.cmcu.mcc.hr.entity;

import com.cmcu.common.annotation.FieldName;
import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class HrVacation {
    @FieldName("id")
    @NotNull
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @FieldName("自助账号")
    @NotNull
    @Size(max=45)
    private String userLogin;

    @FieldName("员工姓名")
    @NotNull
    @Size(max=45)
    private String userName;

    @FieldName("部门名称")
    @NotNull
    @Size(max=145)
    private String deptName;

    @FieldName("职位")
    @NotNull
    @Size(max=45)
    private String userType;

    @FieldName("参加工作时间")
    @NotNull
    @Size(max=45)
    private String joinWorkTime;

    @FieldName("加入公司时间")
    @NotNull
    @Size(max=45)
    private String joinCompanyTime;

    @FieldName("度假年份")
    @NotNull
    @Max(value=999999999, message="度假年份必须为数字")
    private Integer vocationYear;

    @FieldName("年度总计 员工今年还剩多少年假")
    @NotNull
    @Max(value=999999999, message="年度总计 员工今年还剩多少年假必须为数字")
    private Integer annualTotal;

    @FieldName("集体休假  默认为0")
    @NotNull
    @Max(value=999999999, message="集体休假  默认为0必须为数字")
    private Integer forceHoliday;

    @FieldName("creator")
    @NotNull
    @Size(max=45)
    private String creator;

    @FieldName("creatorName")
    @NotNull
    @Size(max=45)
    private String creatorName;

    @FieldName("remark")
    @NotNull
    @Size(max=450)
    private String remark;

    @FieldName("gmtCreate")
    @NotNull
    private Date gmtCreate;

    @FieldName("gmtModified")
    @NotNull
    private Date gmtModified;

    @FieldName("isDeleted")
    @NotNull
    private Boolean deleted;

    @FieldName("去年休假")
    @NotNull
    @Max(value=999999999, message="去年休假必须为数字")
    private Integer annualLeft;

    @FieldName("processInstanceId")
    @NotNull
    @Size(max=45)
    private String processInstanceId;

    @FieldName("isProcessEnd")
    @NotNull
    private Boolean processEnd;
}