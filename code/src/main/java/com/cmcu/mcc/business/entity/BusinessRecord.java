package com.cmcu.mcc.business.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class BusinessRecord {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="preContractIds不能为空!")
    @Size(max=45, message="preContractIds长度不能超过45")
    private String preContractIds;

    @NotNull(message="contractReviewIds不能为空!")
    @Size(max=45, message="contractReviewIds长度不能超过45")
    private String contractReviewIds;

    @NotNull(message="customerId不能为空!")
    @Max(value=999999999, message="customerId必须为数字")
    private Integer customerId;

    @NotNull(message="customerCode不能为空!")
    @Size(max=45, message="customerCode长度不能超过45")
    private String customerCode;

    @NotNull(message="客户名称不能为空!")
    @Size(max=45, message="客户名称长度不能超过45")
    private String customerName;

    @NotNull(message="客户地址不能为空!")
    @Size(max=45, message="客户地址长度不能超过45")
    private String customerAddress;

    @NotNull(message="联系人不能为空!")
    @Size(max=45, message="联系人长度不能超过45")
    private String linkMan;

    @NotNull(message="联系电话不能为空!")
    @Size(max=45, message="联系电话长度不能超过45")
    private String linkTel;

    @NotNull(message="联系人职务不能为空!")
    @Size(max=45, message="联系人职务长度不能超过45")
    private String linkTitle;

    @NotNull(message="项目名称不能为空!")
    @Size(max=145, message="项目名称长度不能超过145")
    private String projectName;

    @NotNull(message="项目地址不能为空!")
    @Size(max=45, message="项目地址长度不能超过45")
    private String projectAddress;

    @NotNull(message="项目规模不能为空!")
    @Size(max=45, message="项目规模长度不能超过45")
    private String projectScale;

    @Size(max=45, message="业务内容长度不能超过45")
    private String businessContent;

    @NotNull(message="项目类型不能为空!")
    @Size(max=45, message="项目类型长度不能超过45")
    private String projectType;

    @NotNull(message="投资额度不能为空!")
    @Size(max=45, message="投资额度长度不能超过45")
    private String projectInvest;

    @NotNull(message="投资来源不能为空!")
    @Size(max=45, message="投资来源长度不能超过45")
    private String investSource;

    @NotNull(message="是否开具保函不能为空!")
    private Boolean tenderBond;

    @NotNull(message="保函方式不能为空!")
    @Size(max=45, message="保函方式长度不能超过45")
    private String tenderBondType;

    @NotNull(message="保函额度不能为空!")
    @Size(max=45, message="保函额度长度不能超过45")
    private String tenderBondMoney;

    @NotNull(message="部门ID不能为空!")
    @Max(value=999999999, message="部门ID必须为数字")
    private Integer deptId;

    @NotNull(message="部门名称不能为空!")
    @Size(max=145, message="部门名称长度不能超过145")
    private String deptName;

    @NotNull(message="经营人员不能为空!")
    @Size(max=245, message="经营人员长度不能超过245")
    private String businessUser;

    @NotNull(message="经办人不能为空!")
    @Size(max=245, message="经办人长度不能超过245")
    private String businessUserName;

    @NotNull(message="计划开标时间不能为空!")
    @Size(max=45, message="计划开标时间长度不能超过45")
    private String bidPlanOpen;

    @NotNull(message="实际开标时间不能为空!")
    @Size(max=45, message="实际开标时间长度不能超过45")
    private String bidRealOpen;

    @NotNull(message="是否参与不能为空!")
    private Boolean attend;

    @NotNull(message="是否中标不能为空!")
    private Boolean win;

    @NotNull(message="remark不能为空!")
    @Size(max=450, message="remark长度不能超过450")
    private String remark;

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

    @NotNull(message="processInstanceId不能为空!")
    @Size(max=45, message="processInstanceId长度不能超过45")
    private String processInstanceId;

    @NotNull(message="isProcessEnd不能为空!")
    private Boolean processEnd;

    @NotNull(message="五洲：合同超前、任务单使用标志不能为空!")
    @Size(max=45, message="五洲：合同超前、任务单使用标志长度不能超过45")
    private String status;

    @NotNull(message="信息提供部门不能为空!")
    @Max(value=999999999, message="信息提供部门必须为数字")
    private Integer messageDeptId;

    @NotNull(message="messageDeptName不能为空!")
    @Size(max=45, message="messageDeptName长度不能超过45")
    private String messageDeptName;

    @NotNull(message="信息提供人员不能为空!")
    @Size(max=45, message="信息提供人员长度不能超过45")
    private String messageUser;

    @NotNull(message="messageUserName不能为空!")
    @Size(max=45, message="messageUserName长度不能超过45")
    private String messageUserName;

    @NotNull(message="isSecret不能为空!")
    private Boolean secret;

    @NotNull(message="项目号不能为空!")
    @Size(max=45, message="项目号长度不能超过45")
    private String projectNo;

    @NotNull(message="计划开始时间不能为空!")
    @Size(max=45, message="计划开始时间长度不能超过45")
    private String planBeginTime;

    @NotNull(message="计划结束时间不能为空!")
    @Size(max=45, message="计划结束时间长度不能超过45")
    private String planEndTime;

    @NotNull(message="各地公共资源平台备案不能为空!")
    @Max(value=999999999, message="各地公共资源平台备案必须为数字")
    private Integer platformId;

    @NotNull(message="分包合同库不能为空!")
    @Max(value=999999999, message="分包合同库必须为数字")
    private Integer subpackageId;

    @NotNull(message="投标申请不能为空!")
    @Max(value=999999999, message="投标申请必须为数字")
    private Integer bidApplyId;

    @NotNull(message="行业分类不能为空!")
    @Size(max=45, message="行业分类长度不能超过45")
    private String industryType;
}