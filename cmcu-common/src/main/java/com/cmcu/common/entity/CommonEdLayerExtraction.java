package com.cmcu.common.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class CommonEdLayerExtraction {

    private Integer id;

    @NotNull(message="tenetId不能为空!")
    @Size(max=45, message="tenetId长度不能超过45")
    private String tenetId;

    @NotNull(message="deptId不能为空!")
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

    @NotNull(message="deptName不能为空!")
    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="extractName不能为空!")
    @Size(max=145, message="extractName长度不能超过145")
    private String extractName;

    @NotNull(message="extractLayer不能为空!")
    @Size(max=2500, message="extractLayer长度不能超过2500")
    private String extractLayer;

    @NotNull(message="extractDesc不能为空!")
    @Size(max=2500, message="extractDesc长度不能超过2500")
    private String extractDesc;

    @NotNull(message="sourceMajor不能为空!")
    @Size(max=45, message="sourceMajor长度不能超过45")
    private String sourceMajor;

    @NotNull(message="destMajor不能为空!")
    @Size(max=100, message="destMajor长度不能超过100")
    private String destMajor;

    @NotNull(message="seq不能为空!")
    @Max(value=999999999, message="seq必须为数字")
    private Integer seq;

    @NotNull(message="isStandard不能为空!")
    private Boolean standard;

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
    @Size(max=245, message="remark长度不能超过245")
    private String remark;
}