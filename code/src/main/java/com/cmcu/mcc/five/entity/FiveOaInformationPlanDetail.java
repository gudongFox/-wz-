package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaInformationPlanDetail {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="planId不能为空!")
    @Max(value=999999999, message="planId必须为数字")
    private Integer planId;

    @NotNull(message="软件名称不能为空!")
    @Size(max=145, message="软件名称长度不能超过145")
    private String softwareName;

    @NotNull(message="功能模块不能为空!")
    @Size(max=145, message="功能模块长度不能超过145")
    private String softwareModel;

    @NotNull(message="类型不能为空!")
    @Size(max=45, message="类型长度不能超过45")
    private String softwareType;

    @NotNull(message="用途不能为空!")
    @Size(max=450, message="用途长度不能超过450")
    private String softwareUseWay;

    @NotNull(message="专业不能为空!")
    @Size(max=45, message="专业长度不能超过45")
    private String useMajor;

    @NotNull(message="数量（套）不能为空!")
    @Size(max=45, message="数量（套）长度不能超过45")
    private String softwareNumber;

    @NotNull(message="单价不能为空!")
    @Size(max=45, message="单价长度不能超过45")
    private String softwarePrice;

    @NotNull(message="softwareTotal不能为空!")
    @Size(max=45, message="softwareTotal长度不能超过45")
    private String softwareTotal;

    @NotNull(message="remark不能为空!")
    @Size(max=145, message="remark长度不能超过145")
    private String remark;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="creator不能为空!")
    @Size(max=45, message="creator长度不能超过45")
    private String creator;

    @NotNull(message="创建人不能为空!")
    @Size(max=45, message="创建人长度不能超过45")
    private String creatorName;

    @NotNull(message="gmtCreate不能为空!")
    private Date gmtCreate;

    @NotNull(message="gmtModified不能为空!")
    private Date gmtModified;
}