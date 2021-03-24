package com.cmcu.mcc.five.entity;

import com.cmcu.common.annotation.FieldName;
import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveEdReviewApply {
    @FieldName("id")
    @NotNull
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @FieldName("businessKey")
    @NotNull
    @Size(max=45)
    private String businessKey;

    @FieldName("projectId")
    @NotNull
    @Max(value=999999999, message="projectId必须为数字")
    private Integer projectId;

    @FieldName("项目名称")
    @NotNull
    @Size(max=145)
    private String projectName;

    @FieldName("项目编号")
    @NotNull
    @Size(max=45)
    private String projectNo;

    @FieldName("合同号")
    @NotNull
    @Size(max=45)
    private String contractNo;

    @FieldName("stageName")
    @NotNull
    @Size(max=45)
    private String stageName;

    @FieldName("stepId")
    @NotNull
    @Max(value=999999999, message="stepId必须为数字")
    private Integer stepId;

    @FieldName("stepName")
    @NotNull
    @Size(max=45)
    private String stepName;

    @FieldName("表单名")
    @NotNull
    @Size(max=45)
    private String formNo;

    @FieldName("申请单位")
    @NotNull
    @Size(max=145)
    private String deptName;

    @FieldName("申请日期")
    @NotNull
    @Size(max=45)
    private String applyTime;

    @FieldName("项目性质")
    @NotNull
    @Size(max=45)
    private String projectType;

    @FieldName("规模")
    @NotNull
    @Size(max=45)
    private String designScale;

    @FieldName("项目总师")
    @NotNull
    @Size(max=45)
    private String totalDesigner;

    @FieldName("联系电话")
    @NotNull
    @Size(max=45)
    private String totalDesignerTel;

    @FieldName("涉及专业")
    @NotNull
    @Size(max=145)
    private String referSpecialty;

    @FieldName("希望评审时间")
    @NotNull
    @Size(max=45)
    private String planReviewTime;

    @FieldName("项目概况")
    @NotNull
    @Size(max=450)
    private String projectDesc;

    @FieldName("提请评审内容/要点或申请理由")
    @NotNull
    @Size(max=450)
    private String reviewContent;

    @FieldName("remark")
    @NotNull
    @Size(max=450)
    private String remark;

    @FieldName("isDeleted")
    @NotNull
    private Boolean deleted;

    @FieldName("creator")
    @NotNull
    @Size(max=45)
    private String creator;

    @FieldName("创建人姓名")
    @NotNull
    @Size(max=45)
    private String creatorName;

    @FieldName("创建时间")
    @NotNull
    private Date gmtCreate;

    @FieldName("修改时间")
    @NotNull
    private Date gmtModified;

    @FieldName("processInstanceId")
    @NotNull
    @Size(max=45)
    private String processInstanceId;

    @FieldName("isProcessEnd")
    @NotNull
    private Boolean processEnd;
}