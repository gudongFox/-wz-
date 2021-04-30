package com.cmcu.mcc.business.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveBusinessAdvanceCollectDetail {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="collectId不能为空!")
    @Max(value=999999999, message="collectId必须为数字")
    private Integer collectId;

    @NotNull(message="单位不能为空!")
    @Size(max=45, message="单位长度不能超过45")
    private String deptId;

    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="申请额度不能为空!")
    @Size(max=45, message="申请额度长度不能超过45")
    private String applyMoney;

    @NotNull(message="公司核定额度不能为空!")
    @Size(max=45, message="公司核定额度长度不能超过45")
    private String companyMoney;

    @NotNull(message="实际额度不能为空!")
    @Size(max=45, message="实际额度长度不能超过45")
    private String realMoney;

    @Max(value=999999999, message="排序必须为数字")
    private Integer seq;

    @Size(max=45, message="creator长度不能超过45")
    private String creator;

    @Size(max=45, message="创建人姓名长度不能超过45")
    private String creatorName;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="创建时间不能为空!")
    private Date gmtCreate;

    @NotNull(message="修改时间不能为空!")
    private Date gmtModified;

    @Size(max=450, message="remark长度不能超过450")
    private String remark;

    @NotNull(message="declareType不能为空!")
    @Size(max=45, message="declareType长度不能超过45")
    private String declareType;
}