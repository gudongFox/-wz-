package com.cmcu.mcc.business.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveBusinessContractLibrary {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="contractPreId不能为空!")
    @Max(value=999999999, message="contractPreId必须为数字")
    private Integer contractPreId;

    @NotNull(message="contractReviewId不能为空!")
    @Max(value=999999999, message="contractReviewId必须为数字")
    private Integer contractReviewId;

    @NotNull(message="项目启动不能为空!")
    @Max(value=999999999, message="项目启动必须为数字")
    private Integer contractId;

    @NotNull(message="customerId不能为空!")
    @Max(value=999999999, message="customerId必须为数字")
    private Integer customerId;

    @NotNull(message="customerName不能为空!")
    @Size(max=145, message="customerName长度不能超过145")
    private String customerName;

    @NotNull(message="customerCode不能为空!")
    @Size(max=45, message="customerCode长度不能超过45")
    private String customerCode;

    @NotNull(message="联系人不能为空!")
    @Size(max=45, message="联系人长度不能超过45")
    private String linkMan;

    @NotNull(message="联系电话不能为空!")
    @Size(max=45, message="联系电话长度不能超过45")
    private String linkTel;

    @NotNull(message="客户地址不能为空!")
    @Size(max=45, message="客户地址长度不能超过45")
    private String customerAddress;

    @NotNull(message="linkTitle不能为空!")
    @Size(max=45, message="linkTitle长度不能超过45")
    private String linkTitle;

    @NotNull(message="传真不能为空!")
    @Size(max=45, message="传真长度不能超过45")
    private String linkFax;

    @NotNull(message="Email不能为空!")
    @Size(max=45, message="Email长度不能超过45")
    private String linkMail;

    @NotNull(message="开户银行不能为空!")
    @Size(max=145, message="开户银行长度不能超过145")
    private String depositBank;

    @NotNull(message="银行账号不能为空!")
    @Size(max=145, message="银行账号长度不能超过145")
    private String bankAccount;

    @NotNull(message="纳税主体资格  小规模纳税人 一般纳税人不能为空!")
    @Size(max=45, message="纳税主体资格  小规模纳税人 一般纳税人长度不能超过45")
    private String taxType;

    @NotNull(message="纳税人识别号不能为空!")
    @Size(max=45, message="纳税人识别号长度不能超过45")
    private String taxNo;

    @NotNull(message="projectType不能为空!")
    @Size(max=45, message="projectType长度不能超过45")
    private String projectType;

    @NotNull(message="projectNo不能为空!")
    @Size(max=45, message="projectNo长度不能超过45")
    private String projectNo;

    @NotNull(message="备案项目名称不能为空!")
    @Size(max=145, message="备案项目名称长度不能超过145")
    private String recordProjectName;

    @NotNull(message="projectAddress不能为空!")
    @Size(max=145, message="projectAddress长度不能超过145")
    private String projectAddress;

    @Size(max=45, message="businessManager长度不能超过45")
    private String businessManager;

    @Size(max=45, message="businessManagerName长度不能超过45")
    private String businessManagerName;

    @NotNull(message="项目总师不能为空!")
    @Size(max=45, message="项目总师长度不能超过45")
    private String totalDesigner;

    @NotNull(message="totalDesignerName不能为空!")
    @Size(max=45, message="totalDesignerName长度不能超过45")
    private String totalDesignerName;

    @NotNull(message="项目总师是否兼职不能为空!")
    @Size(max=45, message="项目总师是否兼职长度不能超过45")
    private String partTimeJob;

    @NotNull(message="其他总师不能为空!")
    @Size(max=145, message="其他总师长度不能超过145")
    private String otherDesigner;

    @NotNull(message="其他总师不能为空!")
    @Size(max=145, message="其他总师长度不能超过145")
    private String otherDesignerName;

    @NotNull(message="项目负责人（总师或项目经理）不能为空!")
    @Size(max=45, message="项目负责人（总师或项目经理）长度不能超过45")
    private String chargeMen;

    @NotNull(message="chargeMenName不能为空!")
    @Size(max=45, message="chargeMenName长度不能超过45")
    private String chargeMenName;

    @NotNull(message="exeChargeMen不能为空!")
    @Size(max=145, message="exeChargeMen长度不能超过145")
    private String exeChargeMen;

    @NotNull(message="exeChargeMenName不能为空!")
    @Size(max=145, message="exeChargeMenName长度不能超过145")
    private String exeChargeMenName;

    @NotNull(message="signer不能为空!")
    @Size(max=45, message="signer长度不能超过45")
    private String signer;

    @NotNull(message="signerName不能为空!")
    @Size(max=45, message="signerName长度不能超过45")
    private String signerName;

    @NotNull(message="signDate不能为空!")
    @Size(max=45, message="signDate长度不能超过45")
    private String signDate;

    @NotNull(message="deptId不能为空!")
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="deptIds不能为空!")
    @Size(max=45, message="deptIds长度不能超过45")
    private String deptIds;

    @NotNull(message="deptNames不能为空!")
    @Size(max=145, message="deptNames长度不能超过145")
    private String deptNames;

    @NotNull(message="stageNames不能为空!")
    @Size(max=145, message="stageNames长度不能超过145")
    private String stageNames;

    @NotNull(message="projectCode不能为空!")
    @Size(max=45, message="projectCode长度不能超过45")
    private String projectCode;

    @NotNull(message="密级不能为空!")
    @Size(max=45, message="密级长度不能超过45")
    private String secType;

    @NotNull(message="planBeginTime不能为空!")
    @Size(max=45, message="planBeginTime长度不能超过45")
    private String planBeginTime;

    @NotNull(message="planEndTime不能为空!")
    @Size(max=45, message="planEndTime长度不能超过45")
    private String planEndTime;

    @NotNull(message="审图级别 项目组审 所审 院审 公司审不能为空!")
    @Size(max=45, message="审图级别 项目组审 所审 院审 公司审长度不能超过45")
    private String reviewType;

    @NotNull(message="是否招投标不能为空!")
    private Boolean bid;

    @NotNull(message="评审级别不能为空!")
    @Size(max=45, message="评审级别长度不能超过45")
    private String reviewLevel;

    @NotNull(message="评审日期不能为空!")
    @Size(max=45, message="评审日期长度不能超过45")
    private String reviewTime;

    @NotNull(message="合同号不能为空!")
    @Size(max=45, message="合同号长度不能超过45")
    private String contractNo;

    @NotNull(message="合同额：（万元）不能为空!")
    @Size(max=45, message="合同额：（万元）长度不能超过45")
    private String contractMoney;

    @NotNull(message="建设规模不能为空!")
    @Size(max=450, message="建设规模长度不能超过450")
    private String constructionScale;

    @NotNull(message="contractType不能为空!")
    @Size(max=45, message="contractType长度不能超过45")
    private String contractType;

    @Size(max=50, message="customerType长度不能超过50")
    private String customerType;

    @Size(max=45, message="合同行业长度不能超过45")
    private String contractScope;

    @Size(max=45, message="投资额（万元）长度不能超过45")
    private String investMoney;

    @Size(max=45, message="projectProvince长度不能超过45")
    private String projectProvince;

    @Size(max=45, message="projectCity长度不能超过45")
    private String projectCity;

    @Size(max=45, message="建筑面积（万㎡）长度不能超过45")
    private String constructionArea;

    @Size(max=45, message="占地面积（万㎡）长度不能超过45")
    private String floorArea;

    @Size(max=45, message="道路长度（km）长度不能超过45")
    private String roadLength;

    @Size(max=45, message="工期(月)长度不能超过45")
    private String constructionTime;

    private Boolean associated;

    @Size(max=45, message="业务承接方式长度不能超过45")
    private String acceptMode;

    @Size(max=145, message="otherScale长度不能超过145")
    private String otherScale;

    @Size(max=145, message="报量情况长度不能超过145")
    private String reportAmount;

    private Boolean bidNotice;

    private Boolean bidBack;

    private Boolean group;

    private Boolean signed;

    private Boolean company;

    @Size(max=45, message="用印时间长度不能超过45")
    private String stampTime;

    @Size(max=45, message="返回时间长度不能超过45")
    private String backTime;

    @Max(value=999999999, message="原件份数必须为数字")
    private Integer originalCount;

    @Max(value=999999999, message="复印件份数必须为数字")
    private Integer copyCount;

    @Max(value=999999999, message="用印份数必须为数字")
    private Integer stampCount;

    @Size(max=145, message="shortDesc长度不能超过145")
    private String shortDesc;

    @Size(max=145, message="imgUrl长度不能超过145")
    private String imgUrl;

    @NotNull(message="合同附件不能为空!")
    private Boolean attach;

    @NotNull(message="isEd不能为空!")
    private Boolean ed;

    @NotNull(message="seq不能为空!")
    @Max(value=999999999, message="seq必须为数字")
    private Integer seq;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="gmtCreate不能为空!")
    private Date gmtCreate;

    @NotNull(message="gmtModified不能为空!")
    private Date gmtModified;

    @NotNull(message="creator不能为空!")
    @Size(max=45, message="creator长度不能超过45")
    private String creator;

    @NotNull(message="creatorName不能为空!")
    @Size(max=45, message="creatorName长度不能超过45")
    private String creatorName;

    @NotNull(message="remark不能为空!")
    @Size(max=450, message="remark长度不能超过450")
    private String remark;

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

    @NotNull(message="外贸标记不能为空!")
    @Size(max=45, message="外贸标记长度不能超过45")
    private String foreignTradeMark;

    @NotNull(message="签约日期不能为空!")
    @Size(max=145, message="签约日期长度不能超过145")
    private String signTime;

    @NotNull(message="投资来源不能为空!")
    @Size(max=45, message="投资来源长度不能超过45")
    private String investSource;

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

    @NotNull(message="分包状态不能为空!")
    @Size(max=450, message="分包状态长度不能超过450")
    private String subpackageIds;

    @NotNull(message="isBackletter不能为空!")
    private Boolean backletter;

    @NotNull(message="保函额不能为空!")
    @Size(max=45, message="保函额长度不能超过45")
    private String backletterMoney;

    @NotNull(message="是否补充合同不能为空!")
    private Boolean main;

    @NotNull(message="subpackageId不能为空!")
    @Max(value=999999999, message="subpackageId必须为数字")
    private Integer subpackageId;

    @NotNull(message="合同已领金额不能为空!")
    @Size(max=45, message="合同已领金额长度不能超过45")
    private String contractIncomeMoney;

    @NotNull(message="incomeIds不能为空!")
    @Size(max=45, message="incomeIds长度不能超过45")
    private String incomeIds;

    @NotNull(message="补充合同评审id不能为空!")
    @Size(max=45, message="补充合同评审id长度不能超过45")
    private String mainReviewIds;

    @NotNull(message="参评人员不能为空!")
    @Size(max=45, message="参评人员长度不能超过45")
    private String reviewUser;

    @NotNull(message="reviewUserName不能为空!")
    @Size(max=45, message="reviewUserName长度不能超过45")
    private String reviewUserName;

    @NotNull(message="contractName不能为空!")
    @Size(max=450, message="contractName长度不能超过450")
    private String contractName;

    @NotNull(message="主合同不能为空!")
    @Max(value=999999999, message="主合同必须为数字")
    private Integer mainContractLibraryId;

    @NotNull(message="mainContractLibraryName不能为空!")
    @Size(max=450, message="mainContractLibraryName长度不能超过450")
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

    @NotNull(message="projectName不能为空!")
    @Size(max=145, message="projectName长度不能超过145")
    private String projectName;

    @NotNull(message="businessChargeLeader不能为空!")
    @Size(max=45, message="businessChargeLeader长度不能超过45")
    private String businessChargeLeader;

    @NotNull(message="businessChargeLeaderName不能为空!")
    @Size(max=45, message="businessChargeLeaderName长度不能超过45")
    private String businessChargeLeaderName;

    @NotNull(message="关联发票不能为空!")
    @Size(max=450, message="关联发票长度不能超过450")
    private String invoiceIds;

    @NotNull(message="合同已开具发票额不能为空!")
    @Size(max=45, message="合同已开具发票额长度不能超过45")
    private String contractInvoiceMoney;

    @Size(max=45, message="合同预收款长度不能超过45")
    private String preContractMoney;

    @Size(max=45, message="应收款长度不能超过45")
    private String shouldContractMoney;

    @Size(max=45, message="projectBudgetIds长度不能超过45")
    private String projectBudgetIds;

    private Boolean open;

    @NotNull(message="stampTaxId不能为空!")
    @Max(value=999999999, message="stampTaxId必须为数字")
    private Integer stampTaxId;

    @NotNull(message="isOpenStamp不能为空!")
    private Boolean openStamp;

    @NotNull(message="新增类型 1补录不能为空!")
    @Max(value=999999999, message="新增类型 1补录必须为数字")
    private Integer insertType;

    @NotNull(message="内部协作合同不能为空!")
    @Max(value=999999999, message="内部协作合同必须为数字")
    private Integer cooperationContractId;

    @Size(max=45, message="主管院长长度不能超过45")
    private String projectChargeMan;

    @Size(max=45, message="projectChargeManName长度不能超过45")
    private String projectChargeManName;

    private String count;
}