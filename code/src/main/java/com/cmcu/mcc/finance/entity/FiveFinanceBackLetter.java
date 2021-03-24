package com.cmcu.mcc.finance.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveFinanceBackLetter {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="formNo不能为空!")
    @Size(max=45, message="formNo长度不能超过45")
    private String formNo;

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

    @NotNull(message="deptId不能为空!")
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

    @NotNull(message="部门不能为空!")
    @Size(max=145, message="部门长度不能超过145")
    private String deptName;

    @NotNull(message="user不能为空!")
    @Size(max=45, message="user长度不能超过45")
    private String user;

    @NotNull(message="填报人不能为空!")
    @Size(max=45, message="填报人长度不能超过45")
    private String userName;

    @NotNull(message="相关类型不能为空!")
    @Size(max=45, message="相关类型长度不能超过45")
    private String relateType;

    @NotNull(message="项目名称不能为空!")
    @Size(max=45, message="项目名称长度不能超过45")
    private String projectName;

    @NotNull(message="项目号不能为空!")
    @Size(max=45, message="项目号长度不能超过45")
    private String projectNo;

    @NotNull(message="合同名称不能为空!")
    @Size(max=45, message="合同名称长度不能超过45")
    private String contractName;

    @NotNull(message="合同号不能为空!")
    @Size(max=45, message="合同号长度不能超过45")
    private String contractNo;

    @NotNull(message="标书编号不能为空!")
    @Size(max=45, message="标书编号长度不能超过45")
    private String bidNo;

    @NotNull(message="是否为联合体不能为空!")
    @Size(max=45, message="是否为联合体长度不能超过45")
    private String combo;

    @NotNull(message="联合体名称不能为空!")
    @Size(max=45, message="联合体名称长度不能超过45")
    private String comboName;

    @NotNull(message="保函类型不能为空!")
    @Size(max=45, message="保函类型长度不能超过45")
    private String backLetterType;

    @NotNull(message="去除保证金不能为空!")
    @Size(max=45, message="去除保证金长度不能超过45")
    private String cash;

    @NotNull(message="担保日期不能为空!")
    @Size(max=45, message="担保日期长度不能超过45")
    private String assureDate;

    @NotNull(message="担保日期不能为空!")
    @Size(max=45, message="担保日期长度不能超过45")
    private String assureDateEnd;

    @NotNull(message="担保月份不能为空!")
    @Size(max=45, message="担保月份长度不能超过45")
    private String assureMonth;

    @NotNull(message="保函编号不能为空!")
    @Size(max=45, message="保函编号长度不能超过45")
    private String backLetterNo;

    @NotNull(message="开立保函合同号不能为空!")
    @Size(max=45, message="开立保函合同号长度不能超过45")
    private String backContractNo;

    @NotNull(message="担保银行不能为空!")
    @Size(max=45, message="担保银行长度不能超过45")
    private String assureBank;

    @NotNull(message="保函开立日期不能为空!")
    @Size(max=45, message="保函开立日期长度不能超过45")
    private String backLetterDate;

    @NotNull(message="失效日期不能为空!")
    @Size(max=45, message="失效日期长度不能超过45")
    private String cancelDate;

    @NotNull(message="续期日期不能为空!")
    @Size(max=45, message="续期日期长度不能超过45")
    private String continueDate;

    @NotNull(message="续期结束不能为空!")
    @Size(max=45, message="续期结束长度不能超过45")
    private String continueDateEnd;

    @NotNull(message="续期月份不能为空!")
    @Size(max=45, message="续期月份长度不能超过45")
    private String continueMonth;
}