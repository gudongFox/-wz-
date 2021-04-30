package com.cmcu.ed.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class EdConfigPlanTemplate {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="tenetId不能为空!")
    @Size(max=45, message="tenetId长度不能超过45")
    private String tenetId;

    @NotNull(message="templateName不能为空!")
    @Size(max=45, message="templateName长度不能超过45")
    private String templateName;

    @NotNull(message="projectType不能为空!")
    @Size(max=145, message="projectType长度不能超过145")
    private String projectType;

    @NotNull(message="stageName不能为空!")
    @Size(max=145, message="stageName长度不能超过145")
    private String stageName;

    @NotNull(message="templateVersion不能为空!")
    @Max(value=999999999, message="templateVersion必须为数字")
    private Integer templateVersion;

    @NotNull(message="templateDesc不能为空!")
    @Size(max=450, message="templateDesc长度不能超过450")
    private String templateDesc;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="creator不能为空!")
    @Size(max=45, message="creator长度不能超过45")
    private String creator;

    @NotNull(message="creatorName不能为空!")
    @Size(max=45, message="creatorName长度不能超过45")
    private String creatorName;

    @NotNull(message="gmtCreate不能为空!")
    private Date gmtCreate;

    @NotNull(message="gmtModified不能为空!")
    private Date gmtModified;

    @NotNull(message="remark不能为空!")
    @Size(max=450, message="remark长度不能超过450")
    private String remark;
}