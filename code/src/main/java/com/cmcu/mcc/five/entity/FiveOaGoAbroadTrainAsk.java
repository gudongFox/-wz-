package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaGoAbroadTrainAsk {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="表单号不能为空!")
    @Size(max=45, message="表单号长度不能超过45")
    private String formNo;

    @NotNull(message="培训名称不能为空!")
    @Size(max=45, message="培训名称长度不能超过45")
    private String trainName;

    @NotNull(message="申请时间不能为空!")
    @Size(max=45, message="申请时间长度不能超过45")
    private String applyTime;

    @NotNull(message="组织单位不能为空!")
    @Max(value=999999999, message="组织单位必须为数字")
    private Integer deptId;

    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="通知名称不能为空!")
    @Size(max=450, message="通知名称长度不能超过450")
    private String askTitle;

    @NotNull(message="出国培训通知不能为空!")
    @Size(max=45, message="出国培训通知长度不能超过45")
    private String goAbroadTitle;

    @NotNull(message="拟派人员数量不能为空!")
    @Size(max=45, message="拟派人员数量长度不能超过45")
    private String staffName;

    @NotNull(message="人均费用不能为空!")
    @Size(max=45, message="人均费用长度不能超过45")
    private String staffCost;

    @NotNull(message="staffPlace不能为空!")
    @Size(max=45, message="staffPlace长度不能超过45")
    private String staffPlace;

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