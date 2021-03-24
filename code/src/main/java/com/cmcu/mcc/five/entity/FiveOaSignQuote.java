package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaSignQuote {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="表单号不能为空!")
    @Size(max=45, message="表单号长度不能超过45")
    private String formNo;

    @NotNull(message="是否属于 三重一大事项不能为空!")
    @Size(max=45, message="是否属于 三重一大事项长度不能超过45")
    private String belongThreeOne;

    @NotNull(message="事项不能为空!")
    @Size(max=450, message="事项长度不能超过450")
    private String item;

    @NotNull(message="送签单位不能为空!")
    @Max(value=999999999, message="送签单位必须为数字")
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

    @NotNull(message="公司办 编号不能为空!")
    @Size(max=45, message="公司办 编号长度不能超过45")
    private String companyNo;

    @NotNull(message="经办人不能为空!")
    @Size(max=45, message="经办人长度不能超过45")
    private String agent;

    @NotNull(message="agentName不能为空!")
    @Size(max=45, message="agentName长度不能超过45")
    private String agentName;

    @NotNull(message="公司办核收人不能为空!")
    @Size(max=45, message="公司办核收人长度不能超过45")
    private String companyCheckMan;

    @NotNull(message="companyCheckManName不能为空!")
    @Size(max=45, message="companyCheckManName长度不能超过45")
    private String companyCheckManName;

    @NotNull(message="报送日期不能为空!")
    @Size(max=45, message="报送日期长度不能超过45")
    private String submitTime;

    @NotNull(message="请示内容不能为空!")
    @Size(max=4500, message="请示内容长度不能超过4500")
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

    @NotNull(message="流程标志：制度会签， 通用会签 ，不会签不能为空!")
    @Size(max=45, message="流程标志：制度会签， 通用会签 ，不会签长度不能超过45")
    private String flag;

    @NotNull(message="是否法律审查不能为空!")
    @Size(max=45, message="是否法律审查长度不能超过45")
    private String sign;

    @NotNull(message="是否是基本制度会签不能为空!")
    @Size(max=45, message="是否是基本制度会签长度不能超过45")
    private String tab;

    @NotNull(message="公司领导-会签不能为空!")
    @Size(max=145, message="公司领导-会签长度不能超过145")
    private String companyLeader;

    @NotNull(message="companyLeaderName不能为空!")
    @Size(max=145, message="companyLeaderName长度不能超过145")
    private String companyLeaderName;

    @NotNull(message="批示领导不能为空!")
    @Size(max=145, message="批示领导长度不能超过145")
    private String instructLeader;

    @NotNull(message="instructLeaderName不能为空!")
    @Size(max=145, message="instructLeaderName长度不能超过145")
    private String instructLeaderName;

    @NotNull(message="会签部门不能为空!")
    @Size(max=450, message="会签部门长度不能超过450")
    private String instructDeptId;

    @NotNull(message="instructDeptName不能为空!")
    @Size(max=450, message="instructDeptName长度不能超过450")
    private String instructDeptName;

    @Size(max=45, message="会议类型长度不能超过45")
    private String meetingType;

    @Size(max=45, message="三重一大事项分类长度不能超过45")
    private String belongType;

    @Size(max=255, message="三重一大事项内容长度不能超过255")
    private String belongContent;
}