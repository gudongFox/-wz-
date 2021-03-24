package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveActRelevance {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="关联流程key不能为空!")
    @Size(max=45, message="关联流程key长度不能超过45")
    private String businessId;

    @NotNull(message="被关联流程key不能为空!")
    @Size(max=45, message="被关联流程key长度不能超过45")
    private String businessKey;

    @NotNull(message="流程名称不能为空!")
    @Size(max=45, message="流程名称长度不能超过45")
    private String processName;

    @NotNull(message="流程描述不能为空!")
    @Size(max=45, message="流程描述长度不能超过45")
    private String processDescription;

    @NotNull(message="我的节点不能为空!")
    @Size(max=45, message="我的节点长度不能超过45")
    private String myTask;

    @NotNull(message="流程id不能为空!")
    @Size(max=45, message="流程id长度不能超过45")
    private String processInstanceId;

    @NotNull(message="seq不能为空!")
    @Max(value=999999999, message="seq必须为数字")
    private Integer seq;

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
    @Size(max=450, message="remark长度不能超过450")
    private String remark;
}