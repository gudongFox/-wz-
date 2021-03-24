package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaEmployeeRetireExamine {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="formNo不能为空!")
    @Size(max=45, message="formNo长度不能超过45")
    private String formNo;

    @NotNull(message="姓名不能为空!")
    @Size(max=45, message="姓名长度不能超过45")
    private String name;

    @NotNull(message="职工号不能为空!")
    @Size(max=45, message="职工号长度不能超过45")
    private String login;

    @NotNull(message="联系方式不能为空!")
    @Size(max=450, message="联系方式长度不能超过450")
    private String link;

    @NotNull(message="调入前单位不能为空!")
    @Max(value=999999999, message="调入前单位必须为数字")
    private Integer deptId;

     @NotNull(message="deptName不能为空!")
    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="调入后单位不能为空!")
    @Max(value=999999999, message="调入后单位必须为数字")
    private Integer retireDeptId;

    @NotNull(message="标题不能为空!")
    @Size(max=45, message="标题长度不能超过45")
    private String retireDeptName;

    @NotNull(message="变动时间不能为空!")
    @Size(max=45, message="变动时间长度不能超过45")
    private String retireTime;

    @NotNull(message="remark不能为空!")
    @Size(max=450, message="remark长度不能超过450")
    private String remark;

    @NotNull(message="creator不能为空!")
    @Size(max=45, message="creator长度不能超过45")
    private String creator;

    @NotNull(message="创建人姓名不能为空!")
    @Size(max=45, message="创建人姓名长度不能超过45")
    private String creatorName;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="创建时间不能为空!")
    private Date gmtCreate;

    @NotNull(message="修改时间不能为空!")
    private Date gmtModified;

    @NotNull(message="processInstanceId不能为空!")
    @Size(max=45, message="processInstanceId长度不能超过45")
    private String processInstanceId;

    @NotNull(message="isProcessEnd不能为空!")
    private Boolean processEnd;
}