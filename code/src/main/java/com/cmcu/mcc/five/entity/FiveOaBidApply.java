package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaBidApply {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="备案号不能为空!")
    @Size(max=45, message="备案号长度不能超过45")
    private String recordNo;

    @NotNull(message="送签部门不能为空!")
    @Max(value=999999999, message="送签部门必须为数字")
    private Integer deptId;

     @NotNull(message="deptName不能为空!")
    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="投标申请人不能为空!")
    @Size(max=45, message="投标申请人长度不能超过45")
    private String bidMan;

    @NotNull(message="bidManName不能为空!")
    @Size(max=45, message="bidManName长度不能超过45")
    private String bidManName;

    @NotNull(message="日期不能为空!")
    @Size(max=45, message="日期长度不能超过45")
    private String bidTime;

    @NotNull(message="项目名称不能为空!")
    @Size(max=450, message="项目名称长度不能超过450")
    private String projectName;

    @NotNull(message="工程类型不能为空!")
    @Size(max=45, message="工程类型长度不能超过45")
    private String projectType;

    @NotNull(message="项目所属行业不能为空!")
    @Size(max=45, message="项目所属行业长度不能超过45")
    private String projectIndustry;

    @NotNull(message="投标方名称不能为空!")
    @Size(max=45, message="投标方名称长度不能超过45")
    private String bidder;

    @NotNull(message="信息来源不能为空!")
    @Size(max=45, message="信息来源长度不能超过45")
    private String informationSource;

    @NotNull(message="联系人不能为空!")
    @Size(max=45, message="联系人长度不能超过45")
    private String bidderLinkMan;

    @NotNull(message="bidderLinkTel不能为空!")
    @Size(max=45, message="bidderLinkTel长度不能超过45")
    private String bidderLinkTel;

    @NotNull(message="预计合同额不能为空!")
    @Size(max=45, message="预计合同额长度不能超过45")
    private String contractMoney;

    @NotNull(message="建设地址不能为空!")
    @Size(max=45, message="建设地址长度不能超过45")
    private String projectAddress;

    @NotNull(message="建设规模不能为空!")
    @Size(max=45, message="建设规模长度不能超过45")
    private String projectScale;

    @NotNull(message="资金来源不能为空!")
    @Size(max=45, message="资金来源长度不能超过45")
    private String moneySource;

    @NotNull(message="资金来源-其他不能为空!")
    @Size(max=45, message="资金来源-其他长度不能超过45")
    private String moneySourceOther;

    @NotNull(message="合格条件与资格要求不能为空!")
    @Size(max=45, message="合格条件与资格要求长度不能超过45")
    private String qualification;

    @NotNull(message="合格条件与资格要求-其他不能为空!")
    @Size(max=45, message="合格条件与资格要求-其他长度不能超过45")
    private String qualificationOther;

    @NotNull(message="招标方式不能为空!")
    @Size(max=45, message="招标方式长度不能超过45")
    private String bidType;

    @NotNull(message="招标方式-其他不能为空!")
    @Size(max=45, message="招标方式-其他长度不能超过45")
    private String bidTypeOther;

    @NotNull(message="招标范围不能为空!")
    @Size(max=450, message="招标范围长度不能超过450")
    private String bidScope;

    @NotNull(message="合同方式不能为空!")
    @Size(max=45, message="合同方式长度不能超过45")
    private String contractType;

    @NotNull(message="合同方式-其他不能为空!")
    @Size(max=45, message="合同方式-其他长度不能超过45")
    private String contractTypeOther;

    @NotNull(message="工期不能为空!")
    @Size(max=45, message="工期长度不能超过45")
    private String projectTime;

    @NotNull(message="进度要求不能为空!")
    @Size(max=45, message="进度要求长度不能超过45")
    private String scheduleTarget;

    @NotNull(message="投标保证金不能为空!")
    @Size(max=45, message="投标保证金长度不能超过45")
    private String depositMoney;

    @NotNull(message="保证金期限（天）不能为空!")
    @Size(max=45, message="保证金期限（天）长度不能超过45")
    private String depositTime;

    @NotNull(message="招标文件及资料费（元）不能为空!")
    @Size(max=45, message="招标文件及资料费（元）长度不能超过45")
    private String fileDataCost;

    @NotNull(message="技术可行性不能为空!")
    @Size(max=45, message="技术可行性长度不能超过45")
    private String technologyFeasibility;

    @NotNull(message="技术可行性-其他不能为空!")
    @Size(max=45, message="技术可行性-其他长度不能超过45")
    private String technologyFeasibilityOther;

    @NotNull(message="生产能力可行性不能为空!")
    @Size(max=45, message="生产能力可行性长度不能超过45")
    private String productFeasibility;

    @NotNull(message="生产能力可行性-其他不能为空!")
    @Size(max=45, message="生产能力可行性-其他长度不能超过45")
    private String productFeasibilityOther;

    @NotNull(message="是否中标不能为空!")
    private Boolean win;

    @NotNull(message="未中标原因不能为空!")
    @Size(max=45, message="未中标原因长度不能超过45")
    private String failReason;

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

    @NotNull(message="recordId不能为空!")
    @Max(value=999999999, message="recordId必须为数字")
    private Integer recordId;

    @NotNull(message="分管副总不能为空!")
    @Size(max=45, message="分管副总长度不能超过45")
    private String chargeMan;

    @NotNull(message="chargeManName不能为空!")
    @Size(max=45, message="chargeManName长度不能超过45")
    private String chargeManName;

    @NotNull(message="投标等级不能为空!")
    @Size(max=45, message="投标等级长度不能超过45")
    private String bidRank;

    @NotNull(message="评审人员不能为空!")
    @Size(max=145, message="评审人员长度不能超过145")
    private String reviewUser;

    @NotNull(message="reviewUserName不能为空!")
    @Size(max=145, message="reviewUserName长度不能超过145")
    private String reviewUserName;

    @NotNull(message="projectNo不能为空!")
    @Size(max=45, message="projectNo长度不能超过45")
    private String projectNo;

    @NotNull(message="customerName不能为空!")
    @Size(max=45, message="customerName长度不能超过45")
    private String customerName;

    @NotNull(message="customerCode不能为空!")
    @Size(max=45, message="customerCode长度不能超过45")
    private String customerCode;

    @NotNull(message="工程款支付方式、节点与支付条件不能为空!")
    @Size(max=65535, message="工程款支付方式、节点与支付条件长度不能超过65535")
    private String paymentRule;

    @NotNull(message="合作经历不能为空!")
    @Size(max=65535, message="合作经历长度不能超过65535")
    private String cooperationExperience;

    @NotNull(message="潜在竞标对手不能为空!")
    @Size(max=65535, message="潜在竞标对手长度不能超过65535")
    private String opponent;

    @NotNull(message="其他可行性因素不能为空!")
    @Size(max=65535, message="其他可行性因素长度不能超过65535")
    private String otherFeasibility;

    @NotNull(message="合同条款风险不能为空!")
    @Size(max=65535, message="合同条款风险长度不能超过65535")
    private String contractRisk;
}