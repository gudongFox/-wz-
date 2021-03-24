package com.cmcu.mcc.oa.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class OaSoftware {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="软件 系统名称不能为空!")
    @Size(max=450,message="软件 系统名称长度不能大于45!")
    private String softwareName;

    @NotNull(message="软件系统类别不能为空!")
    @Size(max=45,message="软件系统类别长度不能大于45!")
    private String softwareType;

    @NotNull(message="生产开发单位不能为空!")
    @Size(max=45,message="生产开发单位长度不能大于45!")
    private String developDept;

    @NotNull(message="主要功能说明不能为空!")
    @Size(max=450,message="主要功能说明长度不能大于45!")
    private String funcationContent;

    @NotNull(message="使用范围不能为空!")
    @Size(max=45,message="使用范围长度不能大于45!")
    private String manager;

    @NotNull(message="维护管理部门不能为空!")
    @Size(max=45,message="维护管理部门长度不能大于45!")
    private String maintainDept;

    @NotNull(message="数量（套）不能为空!")
    @Size(max=45,message="数量（套）长度不能大于45!")
    private String number;

    @NotNull(message="价格（元）不能为空!")
    @Size(max=45,message="价格（元）长度不能大于45!")
    private String price;

    @NotNull(message="运行启动日期不能为空!")
    @Size(max=45,message="运行启动日期长度不能大于45!")
    private String startTime;

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