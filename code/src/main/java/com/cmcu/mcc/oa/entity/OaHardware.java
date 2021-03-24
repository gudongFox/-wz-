package com.cmcu.mcc.oa.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class OaHardware {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="设配名称不能为空!")
    @Size(max=450,message="设配名称长度不能大于45!")
    private String equipmentName;

    @NotNull(message="项目名称不能为空!")
    @Size(max=450,message="项目名称长度不能大于45!")
    private String equipmentType;

    @NotNull(message="品牌不能为空!")
    @Size(max=45,message="品牌长度不能大于45!")
    private String bank;

    @NotNull(message="型号不能为空!")
    @Size(max=45,message="型号长度不能大于45!")
    private String model;

    @NotNull(message="数量不能为空!")
    @Size(max=45,message="数量长度不能大于45!")
    private String number;

    @NotNull(message="主要配置参数不能为空!")
    @Size(max=45,message="主要配置参数长度不能大于45!")
    private String parameter;

    @NotNull(message="放置地点不能为空!")
    @Size(max=450,message="放置地点长度不能大于45!")
    private String address;

    @NotNull(message="使用管理人员 部门不能为空!")
    @Size(max=450,message="使用管理人员 部门长度不能大于45!")
    private String manager;

    @NotNull(message="remark不能为空!")
    @Size(max=450,message="remark长度不能大于450!")
    private String remark;

    @NotNull(message="creator不能为空!")
    @Size(max=45,message="creator长度不能大于45!")
    private String creator;

    @NotNull(message="创建人姓名不能为空!")
    @Size(max=45,message="创建人姓名长度不能大于45!")
    private String creatorName;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="创建时间不能为空!")
    private Date gmtCreate;

    @NotNull(message="修改时间不能为空!")
    private Date gmtModified;

    @NotNull(message="processInstanceId不能为空!")
    @Size(max=45,message="processInstanceId长度不能大于45!")
    private String processInstanceId;

    @NotNull(message="isProcessEnd不能为空!")
    private Boolean processEnd;
}