package com.cmcu.mcc.business.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class BusinessPreContract {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="contractId不能为空!")
    @Max(value=999999999, message="contractId必须为数字")
    private Integer contractId;

    @NotNull(message="recordId不能为空!")
    @Max(value=999999999, message="recordId必须为数字")
    private Integer recordId;

    @NotNull(message="备案项目名称不能为空!")
    @Size(max=45, message="备案项目名称长度不能超过45")
    private String recordProjectName;

    @NotNull(message="无合同为0 不能为空!")
    @Max(value=999999999, message="无合同为0 必须为数字")
    private Integer reviewContractId;

    @NotNull(message="项目编号不能为空!")
    @Size(max=45, message="项目编号长度不能超过45")
    private String projectNo;

    @NotNull(message="项目名称不能为空!")
    @Size(max=145, message="项目名称长度不能超过145")
    private String projectName;

    @NotNull(message="建设地点不能为空!")
    @Size(max=145, message="建设地点长度不能超过145")
    private String projectAddress;

    @Size(max=45, message="customerId长度不能超过45")
    private String customerId;

    @NotNull(message="委托方不能为空!")
    @Size(max=145, message="委托方长度不能超过145")
    private String customerName;

    @NotNull(message="customerCode不能为空!")
    @Size(max=45, message="customerCode长度不能超过45")
    private String customerCode;

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

    @NotNull(message="投资来源不能为空!")
    @Size(max=45, message="投资来源长度不能超过45")
    private String investSource;

    @NotNull(message="项目负责人(总师或项目经理)不能为空!")
    @Size(max=145, message="项目负责人(总师或项目经理)长度不能超过145")
    private String chargeMen;

    @NotNull(message="chargeMenName不能为空!")
    @Size(max=145, message="chargeMenName长度不能超过145")
    private String chargeMenName;

    @NotNull(message="设计阶段不能为空!")
    @Size(max=145, message="设计阶段长度不能超过145")
    private String stageName;

    @NotNull(message="建筑面积（万㎡）不能为空!")
    @Size(max=45, message="建筑面积（万㎡）长度不能超过45")
    private String constructionArea;

    @NotNull(message="投资额(万元)不能为空!")
    @Size(max=45, message="投资额(万元)长度不能超过45")
    private String investMoney;

    @NotNull(message="预计开始时间不能为空!")
    @Size(max=145, message="预计开始时间长度不能超过145")
    private String planBeginTime;

    @NotNull(message="预计结束时间不能为空!")
    @Size(max=145, message="预计结束时间长度不能超过145")
    private String planEndTime;

    @NotNull(message="设计任务类型：常规、非常规不能为空!")
    @Size(max=45, message="设计任务类型：常规、非常规长度不能超过45")
    private String designType;

    @NotNull(message="涉及专业：多选不能为空!")
    @Size(max=145, message="涉及专业：多选长度不能超过145")
    private String designMajor;

    @NotNull(message="主要设计内容,太多请用附件补充不能为空!")
    @Size(max=450, message="主要设计内容,太多请用附件补充长度不能超过450")
    private String designContent;

    @NotNull(message="非常规涉及条件情形的说明：多选不能为空!")
    @Size(max=450, message="非常规涉及条件情形的说明：多选长度不能超过450")
    private String preDesc;

    @NotNull(message="应对措施及具备的条件 多选不能为空!")
    @Size(max=145, message="应对措施及具备的条件 多选长度不能超过145")
    private String preCondition;

    @NotNull(message="应对措施及具备的条件补充说明不能为空!")
    @Size(max=145, message="应对措施及具备的条件补充说明长度不能超过145")
    private String preConditionRemark;

    @NotNull(message="其他不满足相关条件的情形及措施不能为空!")
    @Size(max=450, message="其他不满足相关条件的情形及措施长度不能超过450")
    private String otherSituation;

    @NotNull(message="承接部门不能为空!")
    @Max(value=999999999, message="承接部门必须为数字")
    private Integer deptId;

    @NotNull(message="deptName不能为空!")
    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="设计人员安排截止时间不能为空!")
    @Size(max=145, message="设计人员安排截止时间长度不能超过145")
    private String arrangeEndTime;

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

    @NotNull(message="项目性质不能为空!")
    @Size(max=45, message="项目性质长度不能超过45")
    private String projectNature;

    @NotNull(message="总设计师不能为空!")
    @Size(max=45, message="总设计师长度不能超过45")
    private String totalDesigner;

    @NotNull(message="totalDesignerName不能为空!")
    @Size(max=45, message="totalDesignerName长度不能超过45")
    private String totalDesignerName;

    @NotNull(message="经营经理不能为空!")
    @Size(max=45, message="经营经理长度不能超过45")
    private String projectManager;

    @NotNull(message="projectManagerName不能为空!")
    @Size(max=45, message="projectManagerName长度不能超过45")
    private String projectManagerName;
}