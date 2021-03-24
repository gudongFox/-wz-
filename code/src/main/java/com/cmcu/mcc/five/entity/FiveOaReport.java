package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaReport {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="负责人不能为空!")
    @Size(max=45, message="负责人长度不能超过45")
    private String chargeMan;

    @NotNull(message="负责人姓名不能为空!")
    @Size(max=45, message="负责人姓名长度不能超过45")
    private String chargeManName;

    @NotNull(message="送签部门不能为空!")
    @Max(value=999999999, message="送签部门必须为数字")
    private Integer deptId;

     @NotNull(message="deptName不能为空!")
    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="报送日期不能为空!")
    @Size(max=45, message="报送日期长度不能超过45")
    private String reportTime;

    @NotNull(message="公司办编号不能为空!")
    @Size(max=45, message="公司办编号长度不能超过45")
    private String officeNo;

    @NotNull(message="事项不能为空!")
    @Size(max=450, message="事项长度不能超过450")
    private String reportContent;

    @NotNull(message="公司领导不能为空!")
    @Size(max=45, message="公司领导长度不能超过45")
    private String companyLeader;

    @NotNull(message="companyLeaderName不能为空!")
    @Size(max=45, message="companyLeaderName长度不能超过45")
    private String companyLeaderName;

    @NotNull(message="公司办主任不能为空!")
    @Size(max=45, message="公司办主任长度不能超过45")
    private String companyOfficeMan;

    @NotNull(message="companyOfficeManName不能为空!")
    @Size(max=45, message="companyOfficeManName长度不能超过45")
    private String companyOfficeManName;

    @NotNull(message="副职领导不能为空!")
    @Size(max=45, message="副职领导长度不能超过45")
    private String viceLeader;

    @NotNull(message="viceLeaderName不能为空!")
    @Size(max=45, message="viceLeaderName长度不能超过45")
    private String viceLeaderName;

    @NotNull(message="流程判断 标志 不能为空!")
    @Size(max=45, message="流程判断 标志 长度不能超过45")
    private String flag;

    @NotNull(message="creator不能为空!")
    @Size(max=45, message="creator长度不能超过45")
    private String creator;

    @NotNull(message="creatorName不能为空!")
    @Size(max=45, message="creatorName长度不能超过45")
    private String creatorName;

    @NotNull(message="gmtCreate不能为空!")
    private Date gmtCreate;

    @NotNull(message="gmtModified不能为空!")
    private Date gmtModified;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="isProcessEnd不能为空!")
    private Boolean processEnd;

    @NotNull(message="processInstanceId不能为空!")
    @Size(max=45, message="processInstanceId长度不能超过45")
    private String processInstanceId;

    @Size(max=450, message="请示内容长度不能超过450")
    private String remark;
}