package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaGoAbroadTrainAskDetail {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="主表id不能为空!")
    @Size(max=45, message="主表id长度不能超过45")
    private String goAbroadTrainAskId;

    @NotNull(message="培训名称不能为空!")
    @Size(max=45, message="培训名称长度不能超过45")
    private String trainName;

    @NotNull(message="参加部门不能为空!")
    @Max(value=999999999, message="参加部门必须为数字")
    private Integer deptId;

    @NotNull(message="设备所在单位不能为空!")
    @Size(max=145, message="设备所在单位长度不能超过145")
    private String deptName;

    @NotNull(message="品牌不能为空!")
    @Size(max=450, message="品牌长度不能超过450")
    private String attendUser;

    @NotNull(message="设别型号不能为空!")
    @Size(max=450, message="设别型号长度不能超过450")
    private String attendUserName;

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
}