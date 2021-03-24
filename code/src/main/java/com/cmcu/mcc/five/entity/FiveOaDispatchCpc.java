package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaDispatchCpc {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="fileId不能为空!")
    @Max(value=999999999, message="fileId必须为数字")
    private Integer fileId;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45,message="businessKey长度不能大于45!")
    private String businessKey;

    @NotNull(message="发文类型不能为空!")
    @Size(max=45,message="发文类型长度不能大于45!")
    private String dispatchType;

    @NotNull(message="签发人不能为空!")
    @Size(max=45,message="签发人长度不能大于45!")
    private String signer;

    @NotNull(message="签发人姓名不能为空!")
    @Size(max=45,message="签发人姓名长度不能大于45!")
    private String signerName;

    @NotNull(message="会签人不能为空!")
    @Size(max=45,message="会签人长度不能大于45!")
    private String countersign;

    @NotNull(message="会签人姓名不能为空!")
    @Size(max=45,message="会签人姓名长度不能大于45!")
    private String countersignName;

    @NotNull(message="核稿人不能为空!")
    @Size(max=45,message="核稿人长度不能大于45!")
    private String auditMan;

    @NotNull(message="核稿人姓名不能为空!")
    @Size(max=45,message="核稿人姓名长度不能大于45!")
    private String auditManName;

    @NotNull(message="deptId不能为空!")
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

    @NotNull(message="主办单位不能为空!")
    @Size(max=45,message="主办单位长度不能大于45!")
    private String deptName;

    @NotNull(message="拟稿人不能为空!")
    @Size(max=45,message="拟稿人长度不能大于45!")
    private String drafter;

    @NotNull(message="拟稿人姓名不能为空!")
    @Size(max=45,message="拟稿人姓名长度不能大于45!")
    private String drafterName;

    @NotNull(message="发文标题不能为空!")
    @Size(max=450,message="发文标题长度不能大于450!")
    private String dispatchTitle;

    @NotNull(message="主送不能为空!")
    @Size(max=450,message="主送长度不能大于450!")
    private String realSendManName;

    @NotNull(message="抄送不能为空!")
    @Size(max=450,message="抄送长度不能大于450!")
    private String copySendManName;

    @NotNull(message="主题词不能为空!")
    @Size(max=45,message="主题词长度不能大于45!")
    private String subjectTerm;

    @NotNull(message="党字号不能为空!")
    @Size(max=45,message="党字号长度不能大于45!")
    private String cpcWordSize;

    @NotNull(message="封发不能为空!")
    @Size(max=45,message="封发长度不能大于45!")
    private String dispatching;

    @NotNull(message="打印份数不能为空!")
    @Size(max=45,message="打印份数长度不能大于45!")
    private String printNumber;

    @NotNull(message="creator不能为空!")
    @Size(max=45,message="creator长度不能大于45!")
    private String creator;

    @NotNull(message="creatorName不能为空!")
    @Size(max=45,message="creatorName长度不能大于45!")
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
    @Size(max=45,message="processInstanceId长度不能大于45!")
    private String processInstanceId;
}