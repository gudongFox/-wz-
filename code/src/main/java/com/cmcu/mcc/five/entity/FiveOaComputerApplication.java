package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaComputerApplication {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="formNo不能为空!")
    @Size(max=45, message="formNo长度不能超过45")
    private String formNo;

    @NotNull(message="申请部门不能为空!")
    @Size(max=45, message="申请部门长度不能超过45")
    private String deptId;

     @NotNull(message="deptName不能为空!")
    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="申请人不能为空!")
    @Size(max=45, message="申请人长度不能超过45")
    private String applicationMan;

    @NotNull(message="申请人不能为空!")
    @Size(max=45, message="申请人长度不能超过45")
    private String applicationManName;

    @NotNull(message="申请时间不能为空!")
    @Size(max=45, message="申请时间长度不能超过45")
    private String applicationTime;

    @NotNull(message="设备编号不能为空!")
    @Size(max=45, message="设备编号长度不能超过45")
    private String equipmentNo;

    @NotNull(message="房间号不能为空!")
    @Size(max=45, message="房间号长度不能超过45")
    private String roomNo;

    @NotNull(message="IP地址不能为空!")
    @Size(max=45, message="IP地址长度不能超过45")
    private String ipAddress;

    @NotNull(message="部门审批不能为空!")
    @Size(max=450, message="部门审批长度不能超过450")
    private String departmentComment;

    @NotNull(message="科技质量部审批不能为空!")
    @Size(max=450, message="科技质量部审批长度不能超过450")
    private String scienceComment;

    @NotNull(message="计算机所执行情况不能为空!")
    @Size(max=450, message="计算机所执行情况长度不能超过450")
    private String computerComment;

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
}