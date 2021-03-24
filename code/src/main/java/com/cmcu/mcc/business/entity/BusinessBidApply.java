package com.cmcu.mcc.business.entity;

import com.cmcu.common.annotation.FieldName;
import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class BusinessBidApply {
    @FieldName("id")
    @NotNull
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @FieldName("recordId")
    @NotNull
    @Max(value=999999999, message="recordId必须为数字")
    private Integer recordId;

    @FieldName("部门id")
    @NotNull
    @Max(value=999999999, message="部门id必须为数字")
    private Integer deptId;

    @FieldName("投标所室")
    @NotNull
    @Size(max=145)
    private String deptName;

    @FieldName("项目名称")
    @NotNull
    @Size(max=145)
    private String projectName;

    @FieldName("是否挂网")
    @NotNull
    private Boolean online;

    @FieldName("项目业主名称")
    @NotNull
    @Size(max=145)
    private String customerName;

    @FieldName("代理公司")
    @NotNull
    @Size(max=45)
    private String agentName;

    @FieldName("项目规模")
    @NotNull
    @Size(max=45)
    private String projectScale;

    @FieldName("勘察、设计、勘察设计、施工、EPC")
    @NotNull
    @Size(max=45)
    private String projectType;

    @FieldName("自营、联营、配合")
    @NotNull
    @Size(max=45)
    private String businessType;

    @FieldName("公开招标、邀标、比选")
    @NotNull
    @Size(max=45)
    private String attendType;

    @FieldName("开标时间")
    @NotNull
    @Size(max=45)
    private String openTime;

    @FieldName("remark")
    @NotNull
    @Size(max=450)
    private String remark;

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
}