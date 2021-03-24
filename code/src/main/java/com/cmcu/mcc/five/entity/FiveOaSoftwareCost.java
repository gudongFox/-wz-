package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaSoftwareCost {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="softwareId不能为空!")
    @Max(value=999999999, message="softwareId必须为数字")
    private Integer softwareId;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="软件使用单位不能为空!")
    @Max(value=999999999, message="软件使用单位必须为数字")
    private Integer softwareUseId;

    @NotNull(message="softwareUserName不能为空!")
    @Size(max=45, message="softwareUserName长度不能超过45")
    private String softwareUserName;

    @NotNull(message="软件花费比例不能为空!")
    @Size(max=45, message="软件花费比例长度不能超过45")
    private String softwareCostRatio;

    @NotNull(message="备注不能为空!")
    @Size(max=45, message="备注长度不能超过45")
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
}