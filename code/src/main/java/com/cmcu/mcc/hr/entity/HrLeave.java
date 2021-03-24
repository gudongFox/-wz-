package com.cmcu.mcc.hr.entity;

import com.cmcu.common.annotation.FieldName;
import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class HrLeave {
    @FieldName("id")
    @NotNull
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @FieldName("userLogin")
    @NotNull
    @Size(max=45)
    private String userLogin;

    @FieldName("userName")
    @NotNull
    @Size(max=45)
    private String userName;

    @FieldName("公司名称")
    @NotNull
    @Size(max=45)
    private String companyName;

    @FieldName("deptName")
    @NotNull
    @Size(max=145)
    private String deptName;

    @FieldName("入职时间")
    @NotNull
    @Size(max=45)
    private String joinCompanyTime;

    @FieldName("电话")
    @NotNull
    @Size(max=45)
    private String mobile;

    @FieldName("userType")
    @NotNull
    @Size(max=45)
    private String userType;

    @FieldName("离职原因")
    @NotNull
    @Size(max=450)
    private String leaveReason;

    @FieldName("申请离职时间")
    @NotNull
    @Size(max=45)
    private String applyLeaveTime;

    @FieldName("批准离职时间")
    @NotNull
    @Size(max=45)
    private String approveLeaveTime;

    @FieldName("部门工作交接")
    @NotNull
    @Size(max=450)
    private String deptResult;

    @FieldName("财务交接")
    @NotNull
    @Size(max=450)
    private String financeResult;

    @FieldName("综合管理部交接")
    @NotNull
    @Size(max=450)
    private String officeResult;

    @FieldName("离职说明(hr)")
    @NotNull
    @Size(max=145)
    private String hrRemark;

    @FieldName("离职去向")
    @NotNull
    @Size(max=145)
    private String hrDest;

    @FieldName("gmtCreate")
    @NotNull
    private Date gmtCreate;

    @FieldName("gmtModified")
    @NotNull
    private Date gmtModified;

    @FieldName("processInstanceId")
    @NotNull
    @Size(max=45)
    private String processInstanceId;

    @FieldName("isProcessEnd")
    @NotNull
    private Boolean processEnd;

    @FieldName("creator")
    @NotNull
    @Size(max=45)
    private String creator;

    @FieldName("creatorName")
    @NotNull
    @Size(max=45)
    private String creatorName;

    @FieldName("isDeleted")
    @NotNull
    private Boolean deleted;
}