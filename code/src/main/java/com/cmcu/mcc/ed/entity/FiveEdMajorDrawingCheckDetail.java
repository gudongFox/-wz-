package com.cmcu.mcc.ed.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveEdMajorDrawingCheckDetail {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="checkId不能为空!")
    @Max(value=999999999, message="checkId必须为数字")
    private Integer checkId;

    @NotNull(message="stepId不能为空!")
    @Max(value=999999999, message="stepId必须为数字")
    private Integer stepId;

    @NotNull(message="buildId不能为空!")
    @Max(value=999999999, message="buildId必须为数字")
    private Integer buildId;

    @NotNull(message="子项名称不能为空!")
    @Size(max=145, message="子项名称长度不能超过145")
    private String buildName;

    @NotNull(message="专业不能为空!")
    @Size(max=45, message="专业长度不能超过45")
    private String majorName;

    @NotNull(message="图号不能为空!")
    @Size(max=45, message="图号长度不能超过45")
    private String drawNo;

    @NotNull(message="密级不能为空!")
    @Size(max=45, message="密级长度不能超过45")
    private String secretLevel;

    @NotNull(message="修改图不能为空!")
    private Boolean change;

    @NotNull(message="建筑面积不能为空!")
    @Size(max=145, message="建筑面积长度不能超过145")
    private String buildArea;

    @NotNull(message="外文页数不能为空!")
    @Size(max=45, message="外文页数长度不能超过45")
    private String foreignPage;

    @NotNull(message="中文页数不能为空!")
    @Size(max=45, message="中文页数长度不能超过45")
    private String inlandPage;

    @NotNull(message="中文A1不能为空!")
    @Size(max=45, message="中文A1长度不能超过45")
    private String inlandA1Page;

    @NotNull(message="图纸张数不能为空!")
    @Size(max=45, message="图纸张数长度不能超过45")
    private String drawNumber;

    @NotNull(message="图纸A1不能为空!")
    @Size(max=45, message="图纸A1长度不能超过45")
    private String drawA1Number;

    @Size(max=45, message="复制份数长度不能超过45")
    private String copyNumber;

    @NotNull(message="deptName不能为空!")
    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="id不能为空!")
    @Size(max=45, message="id长度不能超过45")
    private String deptId;

    @NotNull(message="remark不能为空!")
    @Size(max=450, message="remark长度不能超过450")
    private String remark;

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
}