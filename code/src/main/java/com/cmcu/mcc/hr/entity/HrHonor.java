package com.cmcu.mcc.hr.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class HrHonor {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="userLogin不能为空!")
    @Size(max=45,message="userLogin长度不能大于45!")
    private String userLogin;

    @NotNull(message="userName不能为空!")
    @Size(max=45,message="userName长度不能大于45!")
    private String userName;

    @NotNull(message="deptId不能为空!")
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

    @NotNull(message="deptName不能为空!")
    @Size(max=145,message="deptName长度不能大于145!")
    private String deptName;

    @NotNull(message="honortNo不能为空!")
    @Size(max=45,message="honortNo长度不能大于45!")
    private String honortNo;

    @NotNull(message="获奖类别不能为空!")
    @Size(max=45,message="获奖类别长度不能大于45!")
    private String honorType;

    @NotNull(message="获奖名称不能为空!")
    @Size(max=45,message="获奖名称长度不能大于45!")
    private String honorName;

    @NotNull(message="颁发单位不能为空!")
    @Size(max=45,message="颁发单位长度不能大于45!")
    private String honorUnits;

    @NotNull(message="荣誉级别不能为空!")
    @Size(max=45,message="荣誉级别长度不能大于45!")
    private String honorLevel;

    @NotNull(message="获奖日期不能为空!")
    @Size(max=45,message="获奖日期长度不能大于45!")
    private String honorDate;

    @NotNull(message="remark不能为空!")
    @Size(max=450,message="remark长度不能大于450!")
    private String remark;

    @NotNull(message="creator不能为空!")
    @Size(max=45,message="creator长度不能大于45!")
    private String creator;

    @NotNull(message="creatorName不能为空!")
    @Size(max=45,message="creatorName长度不能大于45!")
    private String creatorName;

    @NotNull(message="gmtCreate不能为空!")
    private Date gmtCreate;

    @NotNull(message="gmtModified不能为空!")
    private Date gmtModified;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="processInstanceId不能为空!")
    @Size(max=45,message="processInstanceId长度不能大于45!")
    private String processInstanceId;

    @NotNull(message="isProcessEnd不能为空!")
    private Boolean processEnd;
}