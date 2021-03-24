package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaScientificTaskCostApply {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="表单号不能为空!")
    @Size(max=45, message="表单号长度不能超过45")
    private String formNo;

    @NotNull(message="课题名称不能为空!")
    @Size(max=45, message="课题名称长度不能超过45")
    private String taskName;

    @NotNull(message="课题编号不能为空!")
    @Size(max=45, message="课题编号长度不能超过45")
    private String taskNo;

    @NotNull(message="技术服务单位不能为空!")
    @Max(value=999999999, message="技术服务单位必须为数字")
    private Integer deptId;

    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="课题负责人不能为空!")
    @Size(max=45, message="课题负责人长度不能超过45")
    private String taskChargeMan;

    @NotNull(message="taskChargeManName不能为空!")
    @Size(max=45, message="taskChargeManName长度不能超过45")
    private String taskChargeManName;

    @NotNull(message="经费用途不能为空!")
    @Size(max=45, message="经费用途长度不能超过45")
    private String costUse;

    @NotNull(message="是否列入课题费用计划不能为空!")
    @Size(max=45, message="是否列入课题费用计划长度不能超过45")
    private String taskCostPlan;

    @NotNull(message="经费使用时间不能为空!")
    @Size(max=45, message="经费使用时间长度不能超过45")
    private String costUseTime;

    @NotNull(message="部门负责人不能为空!")
    @Size(max=45, message="部门负责人长度不能超过45")
    private String deptChargeMen;

    @NotNull(message="deptChargeMenName不能为空!")
    @Size(max=45, message="deptChargeMenName长度不能超过45")
    private String deptChargeMenName;

    @NotNull(message="科技质量部 负责人不能为空!")
    @Size(max=45, message="科技质量部 负责人长度不能超过45")
    private String technologyMan;

    @NotNull(message="technologyManName不能为空!")
    @Size(max=45, message="technologyManName长度不能超过45")
    private String technologyManName;

    @NotNull(message="科技质量部 意见不能为空!")
    @Size(max=450, message="科技质量部 意见长度不能超过450")
    private String technologyManComment;

    @NotNull(message="主管领导不能为空!")
    @Size(max=45, message="主管领导长度不能超过45")
    private String chargeLeaderMan;

    @NotNull(message="chargeLeaderManName不能为空!")
    @Size(max=45, message="chargeLeaderManName长度不能超过45")
    private String chargeLeaderManName;

    @NotNull(message="科技质量部 意见不能为空!")
    @Size(max=450, message="科技质量部 意见长度不能超过450")
    private String chargeLeaderManComment;

    @NotNull(message="总会计师不能为空!")
    @Size(max=45, message="总会计师长度不能超过45")
    private String totalAccountantMen;

    @NotNull(message="totalAccountantMenName不能为空!")
    @Size(max=45, message="totalAccountantMenName长度不能超过45")
    private String totalAccountantMenName;

    @NotNull(message="设备购置部门 领导意见不能为空!")
    @Size(max=450, message="设备购置部门 领导意见长度不能超过450")
    private String totalAccountantMenComment;

    @NotNull(message="总经理不能为空!")
    @Size(max=45, message="总经理长度不能超过45")
    private String totalManagerMen;

    @NotNull(message="totalManagerMenName不能为空!")
    @Size(max=45, message="totalManagerMenName长度不能超过45")
    private String totalManagerMenName;

    @NotNull(message="totalManagerMenComment不能为空!")
    @Size(max=450, message="totalManagerMenComment长度不能超过450")
    private String totalManagerMenComment;

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