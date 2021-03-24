package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaInstitutionCountSign {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="表单号不能为空!")
    @Size(max=45, message="表单号长度不能超过45")
    private String formNo;

    @NotNull(message="制度类型不能为空!")
    @Size(max=45, message="制度类型长度不能超过45")
    private String institutionType;

    @NotNull(message="制度名称不能为空!")
    @Size(max=45, message="制度名称长度不能超过45")
    private String institutionName;

    @NotNull(message="法律审查不能为空!")
    @Size(max=450, message="法律审查长度不能超过450")
    private String lawExamine;

    @NotNull(message="公司办 意见不能为空!")
    @Size(max=45, message="公司办 意见长度不能超过45")
    private String processExamine;

    @NotNull(message="送签单位不能为空!")
    @Size(max=45, message="送签单位长度不能超过45")
    private String managerProductExamine;

    @NotNull(message="deptId不能为空!")
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

     @NotNull(message="deptName不能为空!")
    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="部门负责人不能为空!")
    @Size(max=45, message="部门负责人长度不能超过45")
    private String deptChargeMan;

    @NotNull(message="deptChargeManName不能为空!")
    @Size(max=45, message="deptChargeManName长度不能超过45")
    private String deptChargeManName;

    @NotNull(message="经办人不能为空!")
    @Size(max=45, message="经办人长度不能超过45")
    private String agent;

    @NotNull(message="agentName不能为空!")
    @Size(max=45, message="agentName长度不能超过45")
    private String agentName;

    @NotNull(message="报送日期不能为空!")
    @Size(max=45, message="报送日期长度不能超过45")
    private String submitTime;

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