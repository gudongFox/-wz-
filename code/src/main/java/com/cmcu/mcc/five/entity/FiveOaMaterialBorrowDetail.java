package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaMaterialBorrowDetail {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="formNo不能为空!")
    @Size(max=45, message="formNo长度不能超过45")
    private String formNo;

    @NotNull(message="materialBorrowId不能为空!")
    @Max(value=999999999, message="materialBorrowId必须为数字")
    private Integer materialBorrowId;

    @NotNull(message="文件资料名称不能为空!")
    @Size(max=45, message="文件资料名称长度不能超过45")
    private String fileName;

    @NotNull(message="档号不能为空!")
    @Size(max=45, message="档号长度不能超过45")
    private String fileNo;

    @NotNull(message="档案类型不能为空!")
    @Size(max=45, message="档案类型长度不能超过45")
    private String fileType;

    @NotNull(message="专业不能为空!")
    @Size(max=45, message="专业长度不能超过45")
    private String major;

    @NotNull(message="deptId不能为空!")
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

     @NotNull(message="deptName不能为空!")
    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="密级不能为空!")
    @Size(max=45, message="密级长度不能超过45")
    private String fileLevel;

    @NotNull(message="底稿不能为空!")
    private Boolean draft;

    @NotNull(message="蓝图不能为空!")
    private Boolean blueprint;

    @NotNull(message="文书档案不能为空!")
    private Boolean word;

    @NotNull(message="dwg不能为空!")
    private Boolean dwg;

    @NotNull(message="pdf不能为空!")
    private Boolean pdf;

    @NotNull(message="件数不能为空!")
    @Size(max=45, message="件数长度不能超过45")
    private String count;

    @NotNull(message="remark不能为空!")
    @Size(max=450, message="remark长度不能超过450")
    private String remark;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="创建时间不能为空!")
    private Date gmtCreate;

    @NotNull(message="修改时间不能为空!")
    private Date gmtModified;
}