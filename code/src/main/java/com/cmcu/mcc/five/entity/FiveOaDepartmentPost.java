package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaDepartmentPost {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="表单号不能为空!")
    @Size(max=45, message="表单号长度不能超过45")
    private String formNo;

    @NotNull(message="核稿 不能为空!")
    @Size(max=45, message="核稿 长度不能超过45")
    private String checkMan;

    @NotNull(message="checkManName不能为空!")
    @Size(max=45, message="checkManName长度不能超过45")
    private String checkManName;

    @NotNull(message="主办单位不能为空!")
    @Max(value=999999999, message="主办单位必须为数字")
    private Integer deptId;

     @NotNull(message="deptName不能为空!")
    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="发文不能为空!")
    @Size(max=450, message="发文长度不能超过450")
    private String dispatch;

    @NotNull(message="标题不能为空!")
    @Size(max=450, message="标题长度不能超过450")
    private String title;

    @NotNull(message="主题词不能为空!")
    @Size(max=450, message="主题词长度不能超过450")
    private String subjectTerms;

    @NotNull(message="打字员不能为空!")
    @Size(max=45, message="打字员长度不能超过45")
    private String typer;

    @NotNull(message="typerName不能为空!")
    @Size(max=45, message="typerName长度不能超过45")
    private String typerName;

    @NotNull(message="校对人不能为空!")
    @Size(max=45, message="校对人长度不能超过45")
    private String proofreadMan;

    @NotNull(message="proofreadManName不能为空!")
    @Size(max=45, message="proofreadManName长度不能超过45")
    private String proofreadManName;

    @NotNull(message="打印份数不能为空!")
    @Size(max=45, message="打印份数长度不能超过45")
    private String printNumber;

    @NotNull(message="remark不能为空!")
    @Size(max=450, message="remark长度不能超过450")
    private String remark;

    @NotNull(message="拟稿人不能为空!")
    @Size(max=45, message="拟稿人长度不能超过45")
    private String drafter;

    @NotNull(message="drafterName不能为空!")
    @Size(max=45, message="drafterName长度不能超过45")
    private String drafterName;

    @NotNull(message="主送不能为空!")
    @Size(max=450, message="主送长度不能超过450")
    private String realSendMan;

    @NotNull(message="realSendManName不能为空!")
    @Size(max=450, message="realSendManName长度不能超过450")
    private String realSendManName;

    @NotNull(message="抄送不能为空!")
    @Size(max=450, message="抄送长度不能超过450")
    private String copySendMan;

    @NotNull(message="copySendManName不能为空!")
    @Size(max=450, message="copySendManName长度不能超过450")
    private String copySendManName;

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