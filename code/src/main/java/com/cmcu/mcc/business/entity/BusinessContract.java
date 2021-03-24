package com.cmcu.mcc.business.entity;

import java.math.BigDecimal;
import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class BusinessContract {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="bidContractId不能为空!")
    @Max(value=999999999, message="bidContractId必须为数字")
    private Integer bidContractId;

    @NotNull(message="超前任务单不能为空!")
    @Max(value=999999999, message="超前任务单必须为数字")
    private Integer preContractId;

    @NotNull(message="合同评审不能为空!")
    @Max(value=999999999, message="合同评审必须为数字")
    private Integer contractReviewId;

    @NotNull(message="合同库不能为空!")
    @Max(value=999999999, message="合同库必须为数字")
    private Integer contractLibraryId;

    @NotNull(message="customerId不能为空!")
    @Max(value=999999999, message="customerId必须为数字")
    private Integer customerId;

    @NotNull(message="customerName不能为空!")
    @Size(max=145, message="customerName长度不能超过145")
    private String customerName;

    @NotNull(message="联系人不能为空!")
    @Size(max=45, message="联系人长度不能超过45")
    private String linkMan;

    @NotNull(message="联系电话不能为空!")
    @Size(max=45, message="联系电话长度不能超过45")
    private String linkTel;

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

    @NotNull(message="contractNo不能为空!")
    @Size(max=45, message="contractNo长度不能超过45")
    private String contractNo;

    @NotNull(message="设计：S ；勘察：K；EPC项目：EPC；施工项目：施工不能为空!")
    @Size(max=45, message="设计：S ；勘察：K；EPC项目：EPC；施工项目：施工长度不能超过45")
    private String contractType;

    @NotNull(message="projectType不能为空!")
    @Size(max=45, message="projectType长度不能超过45")
    private String projectType;

    @NotNull(message="projectName不能为空!")
    @Size(max=145, message="projectName长度不能超过145")
    private String projectName;

    @NotNull(message="contractMoney不能为空!")
    @Max(value=999999999, message="contractMoney必须为数字")
    private BigDecimal contractMoney;

    @NotNull(message="projectAddress不能为空!")
    @Size(max=145, message="projectAddress长度不能超过145")
    private String projectAddress;

    @NotNull(message="chargeMen不能为空!")
    @Size(max=145, message="chargeMen长度不能超过145")
    private String chargeMen;

    @NotNull(message="exeChargeMen不能为空!")
    @Size(max=145, message="exeChargeMen长度不能超过145")
    private String exeChargeMen;

    @NotNull(message="chargeMenName不能为空!")
    @Size(max=145, message="chargeMenName长度不能超过145")
    private String chargeMenName;

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

    @NotNull(message="isEd不能为空!")
    private Boolean ed;

    @NotNull(message="seq不能为空!")
    @Max(value=999999999, message="seq必须为数字")
    private Integer seq;

    @Max(value=999999999, message="道路长度（km）必须为数字")
    private BigDecimal roadLength;

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

    @NotNull(message="processInstanceId不能为空!")
    @Size(max=45, message="processInstanceId长度不能超过45")
    private String processInstanceId;

    @NotNull(message="isProcessEnd不能为空!")
    private Boolean processEnd;

    @NotNull(message="remark不能为空!")
    @Size(max=450, message="remark长度不能超过450")
    private String remark;

    @Size(max=50, message="customerType长度不能超过50")
    private String customerType;

    @Size(max=45, message="合同行业长度不能超过45")
    private String contractScope;

    @Max(value=999999999, message="投资额（万元）必须为数字")
    private BigDecimal investMoney;

    @Size(max=45, message="projectProvince长度不能超过45")
    private String projectProvince;

    @Size(max=45, message="projectCity长度不能超过45")
    private String projectCity;

    @Max(value=999999999, message="建筑面积（万㎡）必须为数字")
    private BigDecimal constructionArea;

    @Max(value=999999999, message="占地面积（万㎡）必须为数字")
    private BigDecimal floorArea;

    @Max(value=999999999, message="工期(月)必须为数字")
    private BigDecimal constructionTime;

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

    @Size(max=45, message="totalDesigner长度不能超过45")
    private String totalDesigner;

    @Size(max=45, message="businessManager长度不能超过45")
    private String businessManager;

    @Size(max=45, message="businessManagerName长度不能超过45")
    private String businessManagerName;

    @Size(max=145, message="shortDesc长度不能超过145")
    private String shortDesc;

    @Size(max=145, message="imgUrl长度不能超过145")
    private String imgUrl;

    private Boolean success;

    @Size(max=45, message="项目完成时间长度不能超过45")
    private String successTime;

    @Size(max=45, message="前期长度不能超过45")
    private String recordNoEarly;

    @Size(max=45, message="初步长度不能超过45")
    private String recordNoFirst;

    @Size(max=45, message="施工长度不能超过45")
    private String recordNoConstruction;
}