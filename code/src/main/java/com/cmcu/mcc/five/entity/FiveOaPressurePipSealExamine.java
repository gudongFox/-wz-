package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaPressurePipSealExamine {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="表单号不能为空!")
    @Size(max=45, message="表单号长度不能超过45")
    private String formNo;

    @NotNull(message="印章不能为空!")
    @Size(max=45, message="印章长度不能超过45")
    private String seal;

    @NotNull(message="申请人不能为空!")
    @Size(max=45, message="申请人长度不能超过45")
    private String applyMan;

    @NotNull(message="applyManName不能为空!")
    @Size(max=45, message="applyManName长度不能超过45")
    private String applyManName;

    @NotNull(message="申请人联系电话不能为空!")
    @Size(max=45, message="申请人联系电话长度不能超过45")
    private String applyManLink;

    @NotNull(message="用印单位不能为空!")
    @Max(value=999999999, message="用印单位必须为数字")
    private Integer deptId;

    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="用印时间不能为空!")
    @Size(max=45, message="用印时间长度不能超过45")
    private String useTime;

    @NotNull(message="项目名称不能为空!")
    @Size(max=45, message="项目名称长度不能超过45")
    private String projectName;

    @NotNull(message="管道平面布置图名称不能为空!")
    @Size(max=450, message="管道平面布置图名称长度不能超过450")
    private String pipDrawName;

    @NotNull(message="图纸完成时间不能为空!")
    @Size(max=45, message="图纸完成时间长度不能超过45")
    private String drawCompleteTime;

    @NotNull(message="压力管道级别不能为空!")
    @Size(max=45, message="压力管道级别长度不能超过45")
    private String pressurePipType;

    @NotNull(message="图纸用印份数不能为空!")
    @Size(max=45, message="图纸用印份数长度不能超过45")
    private String drawNum;

    @NotNull(message="部门负责人不能为空!")
    @Size(max=45, message="部门负责人长度不能超过45")
    private String deptChargeMan;

    @NotNull(message="deptChargeManName不能为空!")
    @Size(max=45, message="deptChargeManName长度不能超过45")
    private String deptChargeManName;

    @NotNull(message="deptChargeManComment不能为空!")
    @Size(max=450, message="deptChargeManComment长度不能超过450")
    private String deptChargeManComment;

    @NotNull(message="压力管道设计技术负责人不能为空!")
    @Size(max=45, message="压力管道设计技术负责人长度不能超过45")
    private String pressurePipChargeMan;

    @NotNull(message="pressurePipChargeManName不能为空!")
    @Size(max=45, message="pressurePipChargeManName长度不能超过45")
    private String pressurePipChargeManName;

    @NotNull(message="pressurePipChargeManComment不能为空!")
    @Size(max=450, message="pressurePipChargeManComment长度不能超过450")
    private String pressurePipChargeManComment;

    @NotNull(message="科技质量部 负责人不能为空!")
    @Size(max=45, message="科技质量部 负责人长度不能超过45")
    private String technologyChargeMan;

    @NotNull(message="technologyChargeManName不能为空!")
    @Size(max=45, message="technologyChargeManName长度不能超过45")
    private String technologyChargeManName;

    @NotNull(message="technologyChargeManComment不能为空!")
    @Size(max=450, message="technologyChargeManComment长度不能超过450")
    private String technologyChargeManComment;

    @NotNull(message="盖章人不能为空!")
    @Size(max=45, message="盖章人长度不能超过45")
    private String sealMan;

    @NotNull(message="sealManName不能为空!")
    @Size(max=45, message="sealManName长度不能超过45")
    private String sealManName;

    @NotNull(message="监章人不能为空!")
    @Size(max=45, message="监章人长度不能超过45")
    private String superviseMan;

    @NotNull(message="superviseManName不能为空!")
    @Size(max=45, message="superviseManName长度不能超过45")
    private String superviseManName;

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