package com.cmcu.mcc.five.entity;

import com.cmcu.common.annotation.FieldName;
import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveEdQualityIssue {
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

    @FieldName("projectName")
    @NotNull
    @Size(max=145)
    private String projectName;

    @FieldName("项目编号")
    @NotNull
    @Size(max=45)
    private String projectNo;

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

    @FieldName("contractNo")
    @NotNull
    @Size(max=45)
    private String contractNo;

    @FieldName("formNo")
    @NotNull
    @Size(max=45)
    private String formNo;

    @FieldName("审核级别")
    @NotNull
    @Size(max=45)
    private String reviewType;

    @FieldName("设计人")
    @NotNull
    @Size(max=145)
    private String designMen;

    @FieldName("原审核人")
    @NotNull
    @Size(max=45)
    private String checkMan;

    @FieldName("处理负责人")
    @NotNull
    @Size(max=45)
    private String chargeMan;

    @FieldName("信息来源")
    @NotNull
    @Size(max=45)
    private String informationSource;

    @FieldName("设计质量问题内容")
    @NotNull
    @Size(max=450)
    private String issueContent;


    @FieldName("处理措施")
    @NotNull
    @Size(max=450)
    private String solutionContent;

    @FieldName("totalDesigner")
    @NotNull
    @Size(max=145)
    private String totalDesigner;

    @FieldName("chargeMen")
    @NotNull
    @Size(max=145)
    private String chargeMen;

    @FieldName("totalDesignerName")
    @NotNull
    @Size(max=145)
    private String totalDesignerName;

    @FieldName("chargeMenName")
    @NotNull
    @Size(max=145)
    private String chargeMenName;

    @FieldName("isDeleted")
    @NotNull
    private Boolean deleted;

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

    @FieldName("remark")
    @NotNull
    @Size(max=450)
    private String remark;

    @FieldName("processInstanceId")
    @NotNull
    @Size(max=45)
    private String processInstanceId;

    @FieldName("isProcessEnd")
    @NotNull
    private Boolean processEnd;
}