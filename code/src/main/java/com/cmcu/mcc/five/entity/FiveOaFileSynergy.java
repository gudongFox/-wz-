package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaFileSynergy {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @Size(max=255, message="事项长度不能超过255")
    private String content;

    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

    @Size(max=45, message="deptName长度不能超过45")
    private String deptName;

    @Size(max=450, message="会签人长度不能超过45")
    private String signer;

    @Size(max=450, message="signerName长度不能超过45")
    private String signerName;

    @Size(max=450, message="呈阅领导长度不能超过45")
    private String chargeMen;

    @Size(max=450, message="chargeMenName长度不能超过45")
    private String chargeMenName;

    @NotNull(message="remark不能为空!")
    @Size(max=45, message="remark长度不能超过45")
    private String remark;

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
}