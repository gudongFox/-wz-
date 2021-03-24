package com.cmcu.mcc.business.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveBusinessContractReview {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="contractLibraryId不能为空!")
    @Max(value=999999999, message="contractLibraryId必须为数字")
    private Integer contractLibraryId;

    @NotNull(message="contractId不能为空!")
    @Max(value=999999999, message="contractId必须为数字")
    private Integer contractId;

    @NotNull(message="项目信息备案不能为空!")
    @Max(value=999999999, message="项目信息备案必须为数字")
    private Integer recordId;

    @NotNull(message="合同补录标志不能为空!")
    @Max(value=999999999, message="合同补录标志必须为数字")
    private Integer preContractId;

    @NotNull(message="项目名称不能为空!")
    @Size(max=145, message="项目名称长度不能超过145")
    private String projectName;

    @NotNull(message="businessChargeLeader不能为空!")
    @Size(max=45, message="businessChargeLeader长度不能超过45")
    private String businessChargeLeader;

    @NotNull(message="businessChargeLeaderName不能为空!")
    @Size(max=45, message="businessChargeLeaderName长度不能超过45")
    private String businessChargeLeaderName;

    @NotNull(message="单位名称不能为空!")
    @Max(value=999999999, message="单位名称必须为数字")
    private Integer deptId;

    @NotNull(message="承办部门不能为空!")
    @Size(max=45, message="承办部门长度不能超过45")
    private String deptName;

    @NotNull(message="是否招投标不能为空!")
    private Boolean bid;

    @NotNull(message="评审级别不能为空!")
    @Size(max=45, message="评审级别长度不能超过45")
    private String reviewLevel;

    @NotNull(message="评审日期不能为空!")
    @Size(max=45, message="评审日期长度不能超过45")
    private String reviewTime;

    @NotNull(message="投资额不能为空!")
    @Size(max=45, message="投资额长度不能超过45")
    private String investMoney;

    @NotNull(message="customerId不能为空!")
    @Max(value=999999999, message="customerId必须为数字")
    private Integer customerId;

    @NotNull(message="customerCode不能为空!")
    @Size(max=45, message="customerCode长度不能超过45")
    private String customerCode;

    @NotNull(message="customerName不能为空!")
    @Size(max=45, message="customerName长度不能超过45")
    private String customerName;

    @NotNull(message="客户地址不能为空!")
    @Size(max=45, message="客户地址长度不能超过45")
    private String customerAddress;

    @NotNull(message="linkMan不能为空!")
    @Size(max=45, message="linkMan长度不能超过45")
    private String linkMan;

    @NotNull(message="linkTel不能为空!")
    @Size(max=45, message="linkTel长度不能超过45")
    private String linkTel;

    @NotNull(message="linkTitle不能为空!")
    @Size(max=45, message="linkTitle长度不能超过45")
    private String linkTitle;

    @NotNull(message="stageName不能为空!")
    @Size(max=45, message="stageName长度不能超过45")
    private String stageName;

    @NotNull(message="投资来源不能为空!")
    @Size(max=45, message="投资来源长度不能超过45")
    private String investSource;

    @NotNull(message="合同号不能为空!")
    @Size(max=45, message="合同号长度不能超过45")
    private String contractNo;

    @NotNull(message="合同额：（万元）不能为空!")
    @Size(max=45, message="合同额：（万元）长度不能超过45")
    private String contractMoney;

    @NotNull(message="建设规模不能为空!")
    @Size(max=45, message="建设规模长度不能超过45")
    private String constructionScale;

    @NotNull(message="contractType不能为空!")
    @Size(max=45, message="contractType长度不能超过45")
    private String contractType;

    @NotNull(message="建设面积不能为空!")
    @Size(max=45, message="建设面积长度不能超过45")
    private String constructionArea;

    @NotNull(message="主要描述不能为空!")
    @Size(max=145, message="主要描述长度不能超过145")
    private String mainDescription;

    @NotNull(message="法人章不能为空!")
    private Boolean legalPerson;

    @NotNull(message="财务法人章不能为空!")
    private Boolean businessLegalPerson;

    @NotNull(message="isCommonSeal不能为空!")
    private Boolean commonSeal;

    @NotNull(message="法人电子章不能为空!")
    private Boolean eleLegalPerson;

    @NotNull(message="财务法人 电子章不能为空!")
    private Boolean eleBusinessLegalPerson;

    @NotNull(message="合同附件不能为空!")
    private Boolean attach;

    @NotNull(message="remark不能为空!")
    @Size(max=450, message="remark长度不能超过45")
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

    @NotNull(message="isProcessEnd不能为空!")
    private Boolean processEnd;

    @NotNull(message="processInstanceId不能为空!")
    @Size(max=45, message="processInstanceId长度不能超过45")
    private String processInstanceId;

    @NotNull(message="部内外不能为空!")
    @Size(max=45, message="部内外长度不能超过45")
    private String inOrOut;

    @NotNull(message="项目专业分类 一级不能为空!")
    @Size(max=45, message="项目专业分类 一级长度不能超过45")
    private String projectMajorTypeFirst;

    @NotNull(message="项目专业分类 二级不能为空!")
    @Size(max=45, message="项目专业分类 二级长度不能超过45")
    private String projectMajorTypeSecond;

    @NotNull(message="项目专业分类 三级不能为空!")
    @Size(max=45, message="项目专业分类 三级长度不能超过45")
    private String projectMajorTypeThird;

    @NotNull(message="项目性质不能为空!")
    @Size(max=45, message="项目性质长度不能超过45")
    private String projectNature;

    @NotNull(message="甲方行业分类不能为空!")
    @Size(max=45, message="甲方行业分类长度不能超过45")
    private String industryType;

    @NotNull(message="民用标记不能为空!")
    @Size(max=45, message="民用标记长度不能超过45")
    private String civilMark;

    @NotNull(message="militaryMark不能为空!")
    @Size(max=45, message="militaryMark长度不能超过45")
    private String militaryMark;

    @Size(max=45, message="外贸标记长度不能超过45")
    private String foreignTradeMark;

    @NotNull(message="签约日期不能为空!")
    @Size(max=145, message="签约日期长度不能超过145")
    private String signTime;

    @NotNull(message="总设计师不能为空!")
    @Size(max=45, message="总设计师长度不能超过45")
    private String totalDesigner;

    @NotNull(message="totalDesignerName不能为空!")
    @Size(max=45, message="totalDesignerName长度不能超过45")
    private String totalDesignerName;

    @NotNull(message="reviewUser不能为空!")
    @Size(max=45, message="reviewUser长度不能超过45")
    private String reviewUser;

    @NotNull(message="reviewUserName不能为空!")
    @Size(max=45, message="reviewUserName长度不能超过45")
    private String reviewUserName;

    @NotNull(message="项目号不能为空!")
    @Size(max=45, message="项目号长度不能超过45")
    private String projectNo;

    @NotNull(message="是否为主合同不能为空!")
    private Boolean main;

    @NotNull(message="isBackletter不能为空!")
    private Boolean backletter;

    @NotNull(message="保函额不能为空!")
    @Size(max=45, message="保函额长度不能超过45")
    private String backletterMoney;

    @NotNull(message="subpackageId不能为空!")
    @Max(value=999999999, message="subpackageId必须为数字")
    private Integer subpackageId;

    @NotNull(message="主合同不能为空!")
    @Max(value=999999999, message="主合同必须为数字")
    private Integer mainContractLibraryId;

    @NotNull(message="mainContractLibraryName不能为空!")
    @Size(max=450, message="mainContractLibraryName长度不能超过45")
    private String mainContractLibraryName;

    @NotNull(message="mainContractLibraryNo不能为空!")
    @Size(max=45, message="mainContractLibraryNo长度不能超过45")
    private String mainContractLibraryNo;

    @NotNull(message="经营经理不能为空!")
    @Size(max=45, message="经营经理长度不能超过45")
    private String projectManager;

    @NotNull(message="projectManagerName不能为空!")
    @Size(max=45, message="projectManagerName长度不能超过45")
    private String projectManagerName;

    @NotNull(message="contractName不能为空!")
    @Size(max=450, message="contractName长度不能超过450")
    private String contractName;

    @NotNull(message="院级会签人不能为空!")
    @Size(max=45, message="院级会签人长度不能超过45")
    private String deptReviewUser;

    @NotNull(message="deptReviewUserName不能为空!")
    @Size(max=45, message="deptReviewUserName长度不能超过45")
    private String deptReviewUserName;

    @NotNull(message="isOpen不能为空!")
    private Boolean open;

    @NotNull(message="预计签约时间不能为空!")
    @Size(max=45, message="预计签约时间长度不能超过45")
    private String planSignTime;

    @NotNull(message="合同扫描件不能为空!")
    @Size(max=45, message="合同扫描件长度不能超过45")
    private String contractAttachUrl;

    @NotNull(message="税目不能为空!")
    @Size(max=45, message="税目长度不能超过45")
    private String taxType;

    @NotNull(message="税率不能为空!")
    @Size(max=45, message="税率长度不能超过45")
    private String taxNum;

    @NotNull(message="印花税额不能为空!")
    @Size(max=45, message="印花税额长度不能超过45")
    private String stampTaxMoney;

    @NotNull(message="是否开票不能为空!")
    private Boolean openStamp;
}