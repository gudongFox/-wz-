package com.cmcu.mcc.business.entity;

import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveBusinessConsultingContractStatistics {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="deptId不能为空!")
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="statisticsId不能为空!")
    @Max(value=999999999, message="statisticsId必须为数字")
    private Integer statisticsId;

    @NotNull(message="咨询合同目标不能为空!")
    @Size(max=45, message="咨询合同目标长度不能超过45")
    private String consultingContractTarget;

    @NotNull(message="咨询合同完成不能为空!")
    @Size(max=45, message="咨询合同完成长度不能超过45")
    private String consultingContractComplete;

    @NotNull(message="lastYearComplete不能为空!")
    @Size(max=45, message="lastYearComplete长度不能超过45")
    private String lastYearComplete;
}