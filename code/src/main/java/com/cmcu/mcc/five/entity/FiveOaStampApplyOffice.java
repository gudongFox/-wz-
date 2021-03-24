package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaStampApplyOffice {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="表单编号不能为空!")
    @Size(max=45, message="表单编号长度不能超过45")
    private String formNo;

    @NotNull(message="部门id不能为空!")
    @Max(value=999999999, message="部门id必须为数字")
    private Integer deptId;

    @NotNull(message="deptName不能为空!")
    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="用印事由不能为空!")
    @Size(max=450, message="用印事由长度不能超过450")
    private String item;

    @NotNull(message="印章类型：公司章、法人章、法人签名、合同专用章不能为空!")
    @Size(max=145, message="印章类型：公司章、法人章、法人签名、合同专用章长度不能超过145")
    private String stampName;

    @NotNull(message="用印时间：日期不能为空!")
    @Size(max=45, message="用印时间：日期长度不能超过45")
    private String stampDate;

    @NotNull(message="文件名称/图纸名称不能为空!")
    @Size(max=145, message="文件名称/图纸名称长度不能超过145")
    private String fileName;

    @NotNull(message="文件份数不能为空!")
    @Size(max=45, message="文件份数长度不能超过45")
    private String fileCount;

    @NotNull(message="是否需法律审核不能为空!")
    private Boolean legalReview;

    @NotNull(message="副职领导不能为空!")
    @Size(max=45, message="副职领导长度不能超过45")
    private String viceLeader;

    @NotNull(message="部门领导名称不能为空!")
    @Size(max=45, message="部门领导名称长度不能超过45")
    private String viceLeaderName;

    @NotNull(message="事项归口管理部门不能为空!")
    @Max(value=999999999, message="事项归口管理部门必须为数字")
    private Integer itemDept;

    @NotNull(message="itemDeptName不能为空!")
    @Size(max=145, message="itemDeptName长度不能超过145")
    private String itemDeptName;

    @NotNull(message="项目是否备案不能为空!")
    private Boolean record;

    private Boolean contracted;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="归口管理部门负责人不能为空!")
    @Size(max=45, message="归口管理部门负责人长度不能超过45")
    private String itemDeptChargeMen;

    @NotNull(message="合同号或项目号不能为空!")
    @Size(max=145, message="合同号或项目号长度不能超过145")
    private String contractNo;

    @NotNull(message="二级单位ID不能为空!")
    @Max(value=999999999, message="二级单位ID必须为数字")
    private Integer secondDeptId;

    @NotNull(message="建研院副职领导不能为空!")
    @Size(max=45, message="建研院副职领导长度不能超过45")
    private String deptViceChargeMen;

    @NotNull(message="盖章内容是否包含军工、军队保密协议不能为空!")
    private Boolean secrecy;

    @NotNull(message="remark不能为空!")
    @Size(max=450, message="remark长度不能超过450")
    private String remark;

    @NotNull(message="创建人不能为空!")
    @Size(max=45, message="创建人长度不能超过45")
    private String creator;

    @NotNull(message="创建人名称不能为空!")
    @Size(max=45, message="创建人名称长度不能超过45")
    private String creatorName;

    @NotNull(message="gmtCreate不能为空!")
    private Date gmtCreate;

    @NotNull(message="gmtModified不能为空!")
    private Date gmtModified;

    @NotNull(message="流程实例id不能为空!")
    @Size(max=45, message="流程实例id长度不能超过45")
    private String processInstanceId;

    @NotNull(message="流程是否结束不能为空!")
    private Boolean processEnd;
}