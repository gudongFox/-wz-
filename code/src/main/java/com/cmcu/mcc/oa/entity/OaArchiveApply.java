package com.cmcu.mcc.oa.entity;

import com.cmcu.common.annotation.FieldName;
import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class OaArchiveApply {
    @FieldName("id")
    @NotNull
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @FieldName("businessKey")
    @NotNull
    @Size(max=45)
    private String businessKey;

    @FieldName("applyPurpose")
    @NotNull
    @Size(max=145)
    private String applyPurpose;

    @FieldName("archiveId")
    @NotNull
    @Max(value=999999999, message="archiveId必须为数字")
    private Integer archiveId;

    @FieldName("借阅详情")
    @NotNull
    @Size(max=450)
    private String detailIds;

    @FieldName("startTime")
    @NotNull
    @Size(max=45)
    private String startTime;

    @FieldName("endTime")
    @NotNull
    @Size(max=45)
    private String endTime;

    @FieldName("deptId")
    @NotNull
    @Size(max=45)
    private String deptId;

    @FieldName("deptName")
    @NotNull
    @Size(max=145)
    private String deptName;

    @FieldName("creator")
    @NotNull
    @Size(max=45)
    private String creator;

    @FieldName("creatorName")
    @NotNull
    @Size(max=45)
    private String creatorName;

    @FieldName("gmtCreate")
    @NotNull
    private Date gmtCreate;

    @FieldName("gmtModified")
    @NotNull
    private Date gmtModified;

    @FieldName("isDeleted")
    @NotNull
    private Boolean deleted;

    @FieldName("processInstanceId")
    @NotNull
    @Size(max=45)
    private String processInstanceId;

    @FieldName("isProcessEnd")
    @NotNull
    private Boolean processEnd;

    @FieldName("批准详情")
    @NotNull
    @Size(max=450)
    private String approveIds;

    @FieldName("approve_ids")
    @NotNull
    @Size(max=450)
    private String remark;
}