package com.cmcu.mcc.business.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class BusinessInputContract {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="contractId不能为空!")
    @Size(max=45, message="contractId长度不能超过45")
    private String contractId;

    @NotNull(message="超前任务单id 不能为空!")
    @Max(value=999999999, message="超前任务单id 必须为数字")
    private Integer preId;

    @NotNull(message="recordId不能为空!")
    @Max(value=999999999, message="recordId必须为数字")
    private Integer recordId;

    @NotNull(message="合同号不能为空!")
    @Size(max=45, message="合同号长度不能超过45")
    private String contractNo;

    @NotNull(message="工程号不能为空!")
    @Size(max=45, message="工程号长度不能超过45")
    private String projectNo;

    @NotNull(message="承接部门不能为空!")
    @Max(value=999999999, message="承接部门必须为数字")
    private Integer deptId;

    @NotNull(message="deptName不能为空!")
    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="项目名称不能为空!")
    @Size(max=145, message="项目名称长度不能超过145")
    private String projectName;

    @NotNull(message="合同标志不能为空!")
    @Size(max=45, message="合同标志长度不能超过45")
    private String contractMark;

    @NotNull(message="合同进度不能为空!")
    @Size(max=45, message="合同进度长度不能超过45")
    private String contractSchedule;

    @NotNull(message="合同摘要不能为空!")
    @Size(max=45, message="合同摘要长度不能超过45")
    private String contractDes;

    @NotNull(message="合同额(万元)不能为空!")
    @Size(max=45, message="合同额(万元)长度不能超过45")
    private String contractMoney;

    @NotNull(message="部内外 单选不能为空!")
    @Size(max=45, message="部内外 单选长度不能超过45")
    private String inOrOut;

    @NotNull(message="签约日期不能为空!")
    @Size(max=145, message="签约日期长度不能超过145")
    private String signTime;

    @NotNull(message="承办人不能为空!")
    @Size(max=145, message="承办人长度不能超过145")
    private String chargeMen;

    @NotNull(message="chargeMenName不能为空!")
    @Size(max=145, message="chargeMenName长度不能超过145")
    private String chargeMenName;

    @NotNull(message="输入日期不能为空!")
    @Size(max=145, message="输入日期长度不能超过145")
    private String enterTime;

    @NotNull(message="季度不能为空!")
    @Size(max=45, message="季度长度不能超过45")
    private String quarter;

    @NotNull(message="开户银行不能为空!")
    @Size(max=145, message="开户银行长度不能超过145")
    private String depositBank;

    @NotNull(message="银行账号不能为空!")
    @Size(max=45, message="银行账号长度不能超过45")
    private String bankAccount;

    @NotNull(message="邮政编码不能为空!")
    @Size(max=145, message="邮政编码长度不能超过145")
    private String postalCode;

    @NotNull(message="通讯处不能为空!")
    @Size(max=450, message="通讯处长度不能超过450")
    private String postalAddress;

    @NotNull(message="联系电话不能为空!")
    @Size(max=450, message="联系电话长度不能超过450")
    private String linkPhone;

    @NotNull(message="合同类型 单选不能为空!")
    @Size(max=45, message="合同类型 单选长度不能超过45")
    private String contractType;

    @NotNull(message="项数不能为空!")
    @Size(max=45, message="项数长度不能超过45")
    private String itemNum;

    @Size(max=45, message="customerId长度不能超过45")
    private String customerId;

    @NotNull(message="委托方不能为空!")
    @Size(max=145, message="委托方长度不能超过145")
    private String customerName;

    @NotNull(message="委托代理不能为空!")
    @Size(max=145, message="委托代理长度不能超过145")
    private String agency;

    @NotNull(message="项目性质不能为空!")
    @Size(max=45, message="项目性质长度不能超过45")
    private String projectNature;

    @NotNull(message="甲方行业分类不能为空!")
    @Size(max=45, message="甲方行业分类长度不能超过45")
    private String industryType;

    @NotNull(message="militaryMark不能为空!")
    @Size(max=45, message="militaryMark长度不能超过45")
    private String militaryMark;

    @NotNull(message="外贸标记不能为空!")
    @Size(max=45, message="外贸标记长度不能超过45")
    private String foreignTradeMark;

    @NotNull(message="民用标记不能为空!")
    @Size(max=45, message="民用标记长度不能超过45")
    private String civilMark;

    @NotNull(message="项目专业分类 一级不能为空!")
    @Size(max=45, message="项目专业分类 一级长度不能超过45")
    private String projectMajorTypeFirst;

    @NotNull(message="项目专业分类 二级不能为空!")
    @Size(max=45, message="项目专业分类 二级长度不能超过45")
    private String projectMajorTypeSecond;

    @NotNull(message="项目专业分类 三级不能为空!")
    @Size(max=45, message="项目专业分类 三级长度不能超过45")
    private String projectMajorTypeThird;

    @NotNull(message="creator不能为空!")
    @Size(max=45, message="creator长度不能超过45")
    private String creator;

    @NotNull(message="creatorName不能为空!")
    @Size(max=45, message="creatorName长度不能超过45")
    private String creatorName;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="gmtCreate不能为空!")
    private Date gmtCreate;

    @NotNull(message="gmtModified不能为空!")
    private Date gmtModified;

    @NotNull(message="isProcessEnd不能为空!")
    private Boolean processEnd;

    @NotNull(message="processInstanceId不能为空!")
    @Size(max=45, message="processInstanceId长度不能超过45")
    private String processInstanceId;

    @NotNull(message="remark不能为空!")
    @Size(max=450, message="remark长度不能超过450")
    private String remark;

    private Boolean ed;
}