package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaMessageExport {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="送签部门不能为空!")
    @Max(value=999999999, message="送签部门必须为数字")
    private Integer deptId;

     @NotNull(message="deptName不能为空!")
    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="职工号不能为空!")
    @Size(max=45, message="职工号长度不能超过45")
    private String userLogin;

    @NotNull(message="姓名不能为空!")
    @Size(max=45, message="姓名长度不能超过45")
    private String userName;

    @NotNull(message="电话不能为空!")
    @Size(max=45, message="电话长度不能超过45")
    private String phone;

    @NotNull(message="导出硬盘序列号不能为空!")
    @Size(max=45, message="导出硬盘序列号长度不能超过45")
    private String hardDiskNo;

    @NotNull(message="导出服务器地址不能为空!")
    @Size(max=45, message="导出服务器地址长度不能超过45")
    private String serviceAddress;

    @NotNull(message="fileName不能为空!")
    @Size(max=450, message="fileName长度不能超过450")
    private String fileName;

    @NotNull(message="申请日期不能为空!")
    @Size(max=45, message="申请日期长度不能超过45")
    private String applyTime;

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
}