package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaProjectfundplan {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="表单号不能为空!")
    @Size(max=45, message="表单号长度不能超过45")
    private String projectNo;

    @NotNull(message="项目名称不能为空!")
    @Size(max=450, message="项目名称长度不能超过450")
    private String projectName;

    @NotNull(message="计划时间不能为空!")
    @Size(max=45, message="计划时间长度不能超过45")
    private String planTime;

    @NotNull(message="送审单位不能为空!")
    @Max(value=999999999, message="送审单位必须为数字")
    private Integer deptId;

     @NotNull(message="deptName不能为空!")
    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="总经理不能为空!")
    @Size(max=45, message="总经理长度不能超过45")
    private String totalManger;

    @NotNull(message="totalMangerName不能为空!")
    @Size(max=45, message="totalMangerName长度不能超过45")
    private String totalMangerName;

    @NotNull(message="总会计师不能为空!")
    @Size(max=45, message="总会计师长度不能超过45")
    private String totalAccountant;

    @NotNull(message="totalAccountantName不能为空!")
    @Size(max=45, message="totalAccountantName长度不能超过45")
    private String totalAccountantName;

    @NotNull(message="项目经理不能为空!")
    @Size(max=45, message="项目经理长度不能超过45")
    private String chargeLeaderMan;

    @NotNull(message="chargeLeaderManName不能为空!")
    @Size(max=45, message="chargeLeaderManName长度不能超过45")
    private String chargeLeaderManName;

    @NotNull(message="财务金融部不能为空!")
    @Size(max=45, message="财务金融部长度不能超过45")
    private String financeDeptMen;

    @NotNull(message="financeDeptMenName不能为空!")
    @Size(max=45, message="financeDeptMenName长度不能超过45")
    private String financeDeptMenName;

    @NotNull(message="工程管理部不能为空!")
    @Size(max=45, message="工程管理部长度不能超过45")
    private String projectManagementMen;

    @NotNull(message="projectManagementMenName不能为空!")
    @Size(max=45, message="projectManagementMenName长度不能超过45")
    private String projectManagementMenName;

    @NotNull(message="部门负责人不能为空!")
    @Size(max=45, message="部门负责人长度不能超过45")
    private String deptChargeMen;

    @NotNull(message="deptChargeMenName不能为空!")
    @Size(max=45, message="deptChargeMenName长度不能超过45")
    private String deptChargeMenName;

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

    @Size(max=45, message="合同号长度不能超过45")
    private String contractNo;

    @Size(max=45, message="总合收同额长度不能超过45")
    private String totalContractPriceReceipt;

    @Size(max=45, message="总累计收款长度不能超过45")
    private String totalAccumulativePriceReceipt;

    @Size(max=45, message="总累计应收长度不能超过45")
    private String totalReceivablePriceReceipt;

    @Size(max=45, message="总收尾款长度不能超过45")
    private String totalFinalPriceReceipt;

    @Size(max=45, message="总付合同额长度不能超过45")
    private String totalContractPricePay;

    @Size(max=45, message="总累计付款长度不能超过45")
    private String totalAccumulativePricePay;

    @Size(max=45, message="总累计付收长度不能超过45")
    private String totalReceivablePricePay;

    @Size(max=45, message="总付尾款长度不能超过45")
    private String totalFinalPricePay;

    @Size(max=45, message="项目总计长度不能超过45")
    private String allProjectMoney;
}