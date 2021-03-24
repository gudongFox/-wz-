package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaFileInstruction {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="fileId不能为空!")
    @Max(value=999999999, message="fileId必须为数字")
    private Integer fileId;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="deptId不能为空!")
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

     @NotNull(message="deptName不能为空!")
    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="文件标题不能为空!")
    @Size(max=450, message="文件标题长度不能超过450")
    private String fileTitle;

    @NotNull(message="签收人不能为空!")
    @Size(max=45, message="签收人长度不能超过45")
    private String signer;

    @NotNull(message="签收人姓名不能为空!")
    @Size(max=45, message="签收人姓名长度不能超过45")
    private String signerName;

    @NotNull(message="密级不能为空!")
    @Size(max=45, message="密级长度不能超过45")
    private String security;

    @NotNull(message="来文单位不能为空!")
    @Size(max=450, message="来文单位长度不能超过450")
    private String sendDeptName;

    @NotNull(message="来文号不能为空!")
    @Size(max=45, message="来文号长度不能超过45")
    private String sendWordSize;

    @NotNull(message="收文日期不能为空!")
    @Size(max=45, message="收文日期长度不能超过45")
    private String receiveTime;

    @NotNull(message="收文号不能为空!")
    @Size(max=45, message="收文号长度不能超过45")
    private String receiveWordSize;

    @NotNull(message="正文份数不能为空!")
    @Size(max=45, message="正文份数长度不能超过45")
    private String textNumber;

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

    @NotNull(message="承办部门不能为空!")
    @Size(max=450, message="承办部门长度不能超过145")
    private String undertakeDeptId;

    @Size(max=450, message="undertakeDeptName长度不能超过145")
    private String undertakeDeptName;

    @Size(max=145, message="批阅公司领导长度不能超过145")
    private String companyLeader;

    @Size(max=145, message="companyLeaderName长度不能超过145")
    private String companyLeaderName;

    @Size(max=45, message="fiveOaFileInstructioncol长度不能超过45")
    private String fiveOaFileInstructioncol;

    @Size(max=145, message="阅示领导长度不能超过145")
    private String readLeader;

    @Size(max=145, message="readLeaderName长度不能超过145")
    private String readLeaderName;

    @Max(value=999999999, message="年份必须为数字")
    private Integer year;
}