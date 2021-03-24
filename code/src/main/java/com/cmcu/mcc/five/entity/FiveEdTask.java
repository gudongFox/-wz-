package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveEdTask {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45,message="businessKey长度不能大于45!")
    private String businessKey;

    @NotNull(message="stepId不能为空!")
    @Max(value=999999999, message="stepId必须为数字")
    private Integer stepId;

    @NotNull(message="projectId不能为空!")
    @Max(value=999999999, message="projectId必须为数字")
    private Integer projectId;

    @NotNull(message="projectNo不能为空!")
    @Size(max=45,message="projectNo长度不能大于45!")
    private String projectNo;

    @NotNull(message="projectName不能为空!")
    @Size(max=45,message="projectName长度不能大于45!")
    private String projectName;

    @NotNull(message="stageName不能为空!")
    @Size(max=45,message="stageName长度不能大于45!")
    private String stageName;

    @NotNull(message="stepName不能为空!")
    @Size(max=45,message="stepName长度不能大于45!")
    private String stepName;

    @NotNull(message="contractNo不能为空!")
    @Size(max=45,message="contractNo长度不能大于45!")
    private String contractNo;

    @NotNull(message="deptId不能为空!")
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

    @NotNull(message="配合部门不能为空!")
    @Size(max=45,message="配合部门长度不能大于45!")
    private String deptName;

    @NotNull(message="配合专业不能为空!")
    @Size(max=45,message="配合专业长度不能大于45!")
    private String majorName;

    @NotNull(message="creator不能为空!")
    @Size(max=45,message="creator长度不能大于45!")
    private String creator;

    @NotNull(message="creatorName不能为空!")
    @Size(max=45,message="creatorName长度不能大于45!")
    private String creatorName;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="gmtCreate不能为空!")
    private Date gmtCreate;

    @NotNull(message="gmtModified不能为空!")
    private Date gmtModified;

    @NotNull(message="processInstanceId不能为空!")
    @Size(max=45,message="processInstanceId长度不能大于45!")
    private String processInstanceId;

    @NotNull(message="isProcessEnd不能为空!")
    private Boolean processEnd;

    @NotNull(message="备注不能为空!")
    @Size(max=45,message="备注长度不能大于45!")
    private String remark;

    @NotNull(message="配合专业负责人不能为空!")
    @Size(max=45,message="配合专业负责人长度不能大于45!")
    private String deptChargeMen;

    @NotNull(message="deptChargeMenName不能为空!")
    @Size(max=45,message="deptChargeMenName长度不能大于45!")
    private String deptChargeMenName;
}