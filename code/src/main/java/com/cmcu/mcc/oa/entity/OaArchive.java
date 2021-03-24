package com.cmcu.mcc.oa.entity;

import com.cmcu.common.annotation.FieldName;
import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class OaArchive {
    @FieldName("id")
    @NotNull
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @FieldName("deptId")
    @NotNull
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

    @FieldName("projectId")
    @NotNull
    @Max(value=999999999, message="projectId必须为数字")
    private Integer projectId;

    @FieldName("deptName")
    @NotNull
    @Size(max=145)
    private String deptName;

    @FieldName("projectName")
    @NotNull
    @Size(max=145)
    private String projectName;

    @FieldName("stepName")
    @NotNull
    @Size(max=145)
    private String stepName;

    @FieldName("stepId")
    @NotNull
    @Max(value=999999999, message="stepId必须为数字")
    private Integer stepId;

    @FieldName("contractNo")
    @NotNull
    @Size(max=45)
    private String contractNo;

    @FieldName("stageName")
    @NotNull
    @Size(max=145)
    private String stageName;

    @FieldName("设计时间")
    @NotNull
    @Size(max=145)
    private String designTime;

    @FieldName("工程规模(m2)")
    @NotNull
    @Size(max=145)
    private String projectScale;

    @FieldName("工程性质")
    @NotNull
    @Size(max=45)
    private String projectType;

    @FieldName("项目简述")
    @NotNull
    @Size(max=450)
    private String projectDesc;

    @FieldName("建设单位")
    @NotNull
    @Size(max=145)
    private String constructionOrg;

    @FieldName("归档日期")
    @NotNull
    @Size(max=145)
    private String archiveTime;

    @FieldName("seq")
    @NotNull
    @Max(value=999999999, message="seq必须为数字")
    private Integer seq;

    @FieldName("isDeleted")
    @NotNull
    private Boolean deleted;

    @FieldName("gmtCreate")
    @NotNull
    private Date gmtCreate;

    @FieldName("gmtModified")
    @NotNull
    private Date gmtModified;

    @FieldName("归档人")
    @NotNull
    @Size(max=45)
    private String creator;

    @FieldName("creatorName")
    @NotNull
    @Size(max=45)
    private String creatorName;

    @FieldName("remark")
    @NotNull
    @Size(max=450)
    private String remark;

    @FieldName("部门拥有,则部门可以直接查看,否则都需要申请")
    @NotNull
    private Boolean deptOwn;

    @FieldName("processInstanceId")
    @NotNull
    @Size(max=45)
    private String processInstanceId;

    @FieldName("isProcessEnd")
    @NotNull
    private Boolean processEnd;
}