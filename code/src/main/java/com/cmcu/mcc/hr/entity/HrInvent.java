package com.cmcu.mcc.hr.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class HrInvent {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="userLogin不能为空!")
    @Size(max=45, message="userLogin长度不能超过45")
    private String userLogin;

    @NotNull(message="userName不能为空!")
    @Size(max=45, message="userName长度不能超过45")
    private String userName;

    @NotNull(message="公司内申请单位不能为空!")
    @Max(value=999999999, message="公司内申请单位必须为数字")
    private Integer deptId;

     @NotNull(message="deptName不能为空!")
    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="公司外合作单位不能为空!")
    @Size(max=45, message="公司外合作单位长度不能超过45")
    private String cooperator;

    @NotNull(message="申请人顺序（法人单位名称）不能为空!")
    @Size(max=450, message="申请人顺序（法人单位名称）长度不能超过450")
    private String applyNameSort;

    @NotNull(message="所属专业不能为空!")
    @Size(max=45, message="所属专业长度不能超过45")
    private String majorName;

    @NotNull(message="发明名称不能为空!")
    @Size(max=45, message="发明名称长度不能超过45")
    private String inventName;

    @NotNull(message="第一发明人不能为空!")
    @Size(max=45, message="第一发明人长度不能超过45")
    private String firstInventMan;

    @NotNull(message="firstInventManName不能为空!")
    @Size(max=45, message="firstInventManName长度不能超过45")
    private String firstInventManName;

    @NotNull(message="身份证号不能为空!")
    @Size(max=45, message="身份证号长度不能超过45")
    private String idNumber;

    @NotNull(message="其他发明人顺序不能为空!")
    @Size(max=45, message="其他发明人顺序长度不能超过45")
    private String otherInventMen;

    @NotNull(message="日期不能为空!")
    @Size(max=45, message="日期长度不能超过45")
    private String applyTime;

    @NotNull(message="remark不能为空!")
    @Size(max=450, message="remark长度不能超过450")
    private String remark;

    @NotNull(message="creator不能为空!")
    @Size(max=45, message="creator长度不能超过45")
    private String creator;

    @NotNull(message="creatorName不能为空!")
    @Size(max=45, message="creatorName长度不能超过45")
    private String creatorName;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="gmtCreate不能为空!")
    private Date gmtCreate;

    @NotNull(message="gmtModified不能为空!")
    private Date gmtModified;

    @NotNull(message="processInstanceId不能为空!")
    @Size(max=45, message="processInstanceId长度不能超过45")
    private String processInstanceId;

    @NotNull(message="processEnd不能为空!")
    private Boolean processEnd;

    @NotNull(message="与关键技术或创新点有关保密情况不能为空!")
    @Size(max=65535, message="与关键技术或创新点有关保密情况长度不能超过65535")
    private String securityDec;

    @NotNull(message="关键技术或创新点不能为空!")
    @Size(max=65535, message="关键技术或创新点长度不能超过65535")
    private String technologyAndInnovate;

    @NotNull(message="检索查新情况不能为空!")
    @Size(max=65535, message="检索查新情况长度不能超过65535")
    private String retruevalDec;

    @NotNull(message="technologyMarket不能为空!")
    @Size(max=65535, message="technologyMarket长度不能超过65535")
    private String technologyMarket;
}