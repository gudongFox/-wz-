package com.cmcu.mcc.business.entity;

import javax.validation.constraints.*;
import lombok.Data;

@Data
public class BusinessContractDetail {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="contractId不能为空!")
    @Max(value=999999999, message="contractId必须为数字")
    private Integer contractId;

    @NotNull(message="projectCode不能为空!")
    @Size(max=45,message="projectCode长度不能大于45!")
    private String projectCode;

    @NotNull(message="密级不能为空!")
    @Size(max=45,message="密级长度不能大于45!")
    private String secType;

    @NotNull(message="planBeginTime不能为空!")
    @Size(max=45,message="planBeginTime长度不能大于45!")
    private String planBeginTime;

    @NotNull(message="planEndTime不能为空!")
    @Size(max=45,message="planEndTime长度不能大于45!")
    private String planEndTime;

    @NotNull(message="审图级别 项目组审 所审 院审 公司审不能为空!")
    @Size(max=45,message="审图级别 项目组审 所审 院审 公司审长度不能大于45!")
    private String reviewType;

    @NotNull(message="项目总师不能为空!")
    @Size(max=45,message="项目总师长度不能大于45!")
    private String totalDesigner;

    @NotNull(message="totalDesignerName不能为空!")
    @Size(max=45,message="totalDesignerName长度不能大于45!")
    private String totalDesignerName;

    @NotNull(message="项目总师是否兼职不能为空!")
    @Size(max=45,message="项目总师是否兼职长度不能大于45!")
    private String partTimeJob;

    @NotNull(message="其他总师不能为空!")
    @Size(max=145,message="其他总师长度不能大于145!")
    private String otherDesigner;

    @NotNull(message="其他总师不能为空!")
    @Size(max=145,message="其他总师长度不能大于145!")
    private String otherDesignerName;

    @NotNull(message="projectNo不能为空!")
    @Size(max=45,message="projectNo长度不能大于45!")
    private String projectNo;

    @NotNull(message="项目负责人（总师或项目经理）不能为空!")
    @Size(max=45,message="项目负责人（总师或项目经理）长度不能大于45!")
    private String chargeMen;

    @NotNull(message="chargeMenName不能为空!")
    @Size(max=45,message="chargeMenName长度不能大于45!")
    private String chargeMenName;
}