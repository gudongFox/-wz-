package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveEdTaskUser {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="taskId不能为空!")
    @Max(value=999999999, message="taskId必须为数字")
    private Integer taskId;

    @NotNull(message="子项不能为空!")
    @Max(value=999999999, message="子项必须为数字")
    private Integer stepBuildId;

    @NotNull(message="子项名称不能为空!")
    @Size(max=45, message="子项名称长度不能超过45")
    private String stepBuildName;

    @NotNull(message="majorName不能为空!")
    @Size(max=45, message="majorName长度不能超过45")
    private String majorName;

    @NotNull(message="专业负责人不能为空!")
    @Size(max=45, message="专业负责人长度不能超过45")
    private String majorChargeMen;

    @NotNull(message="专业负责人名字不能为空!")
    @Size(max=45, message="专业负责人名字长度不能超过45")
    private String majorChargeMenName;

    @NotNull(message="专业负责人电话不能为空!")
    @Size(max=145, message="专业负责人电话长度不能超过145")
    private String majorChargeMenMobile;

    @NotNull(message="审定人不能为空!")
    @Size(max=45, message="审定人长度不能超过45")
    private String approveMen;

    @NotNull(message="approveMenName不能为空!")
    @Size(max=45, message="approveMenName长度不能超过45")
    private String approveMenName;

    @NotNull(message="校队不能为空!")
    @Size(max=45, message="校队长度不能超过45")
    private String proofreadMen;

    @NotNull(message="proofreadMenName不能为空!")
    @Size(max=45, message="proofreadMenName长度不能超过45")
    private String proofreadMenName;

    @NotNull(message="设计人不能为空!")
    @Size(max=145, message="设计人长度不能超过145")
    private String designMen;

    @NotNull(message="designMenName不能为空!")
    @Size(max=145, message="designMenName长度不能超过145")
    private String designMenName;

    @NotNull(message="审核不能为空!")
    @Size(max=45, message="审核长度不能超过45")
    private String auditMen;

    @NotNull(message="auditMenName不能为空!")
    @Size(max=45, message="auditMenName长度不能超过45")
    private String auditMenName;

    @NotNull(message="标准化审查不能为空!")
    @Size(max=45, message="标准化审查长度不能超过45")
    private String criterionExamineMen;

    @NotNull(message="criterionExamineMenName不能为空!")
    @Size(max=45, message="criterionExamineMenName长度不能超过45")
    private String criterionExamineMenName;

    @NotNull(message="所有人不能为空!")
    @Size(max=4500, message="所有人长度不能超过4500")
    private String allMen;

    @NotNull(message="allMenName不能为空!")
    @Size(max=4500, message="allMenName长度不能超过4500")
    private String allMenName;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="gmtCreate不能为空!")
    private Date gmtCreate;

    @NotNull(message="子项不能为空!")
    private Date gmtModified;

    @NotNull(message="seq不能为空!")
    @Max(value=999999999, message="seq必须为数字")
    private Integer seq;
}