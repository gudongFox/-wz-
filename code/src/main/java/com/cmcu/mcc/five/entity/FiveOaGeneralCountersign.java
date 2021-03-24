package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaGeneralCountersign {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="负责人不能为空!")
    @Size(max=45, message="负责人长度不能超过45")
    private String chargeMan;

    @NotNull(message="负责人姓名不能为空!")
    @Size(max=45, message="负责人姓名长度不能超过45")
    private String chargeManName;

    @NotNull(message="送签部门不能为空!")
    @Max(value=999999999, message="送签部门必须为数字")
    private Integer deptId;

     @NotNull(message="deptName不能为空!")
    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="报送日期不能为空!")
    @Size(max=45, message="报送日期长度不能超过45")
    private String submitTime;

    @NotNull(message="经办人不能为空!")
    @Size(max=45, message="经办人长度不能超过45")
    private String manager;

    @NotNull(message="managerName不能为空!")
    @Size(max=45, message="managerName长度不能超过45")
    private String managerName;

    @NotNull(message="事项不能为空!")
    @Size(max=145, message="事项长度不能超过145")
    private String content;

    @NotNull(message="法律审查不能为空!")
    @Size(max=45, message="法律审查长度不能超过45")
    private String legalReview;

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

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="isProcessEnd不能为空!")
    private Boolean processEnd;

    @NotNull(message="processInstanceId不能为空!")
    @Size(max=45, message="processInstanceId长度不能超过45")
    private String processInstanceId;

    @NotNull(message="流程方向 控制不能为空!")
    @Size(max=45, message="流程方向 控制长度不能超过45")
    private String processFlag;
}