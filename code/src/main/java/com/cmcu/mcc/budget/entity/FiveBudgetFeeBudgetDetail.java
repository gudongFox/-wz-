package com.cmcu.mcc.budget.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveBudgetFeeBudgetDetail {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="feeBudgetId不能为空!")
    @Max(value=999999999, message="feeBudgetId必须为数字")
    private Integer feeBudgetId;

    @NotNull(message="deptId不能为空!")
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

     @NotNull(message="deptName不能为空!")
    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="勘察设计、咨询收费不能为空!")
    @Size(max=45, message="勘察设计、咨询收费长度不能超过45")
    private String designFee;

    @NotNull(message="工程承包收费不能为空!")
    @Size(max=45, message="工程承包收费长度不能超过45")
    private String projectFee;

    @NotNull(message="其他收费不能为空!")
    @Size(max=45, message="其他收费长度不能超过45")
    private String otherFee;

    @NotNull(message="合计不能为空!")
    @Size(max=45, message="合计长度不能超过45")
    private String plus;

    @NotNull(message="总计不能为空!")
    @Size(max=45, message="总计长度不能超过45")
    private String total;

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