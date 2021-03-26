package com.cmcu.mcc.business.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveBusinessAdvance {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="formNo不能为空!")
    @Size(max=45, message="formNo长度不能超过45")
    private String formNo;

    @NotNull(message="deptId不能为空!")
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

    @NotNull(message="deptName不能为空!")
    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="年份不能为空!")
    @Size(max=45, message="年份长度不能超过45")
    private String year;

    @NotNull(message="月份不能为空!")
    @Size(max=45, message="月份长度不能超过45")
    private String month;

    @NotNull(message="总计不能为空!")
    @Size(max=45, message="总计长度不能超过45")
    private String totalPrice;

    @NotNull(message="发放说明不能为空!")
    @Size(max=450, message="发放说明长度不能超过450")
    private String remark;

    @NotNull(message="creator不能为空!")
    @Size(max=45, message="creator长度不能超过45")
    private String creator;

    @NotNull(message="creatorName不能为空!")
    @Size(max=45, message="creatorName长度不能超过45")
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

    @NotNull(message="projectName不能为空!")
    @Size(max=45, message="projectName长度不能超过45")
    private String projectName;

    @NotNull(message="申报类型不能为空!")
    @Size(max=45, message="申报类型长度不能超过45")
    private String declareType;
}