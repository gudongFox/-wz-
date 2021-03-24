package com.cmcu.mcc.five.entity;

import com.cmcu.common.annotation.FieldName;
import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveEdDesignPlan {
    @FieldName("id")
    @NotNull
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @FieldName("stepId")
    @NotNull
    @Max(value=999999999, message="stepId必须为数字")
    private Integer stepId;

    @FieldName("projectId")
    @NotNull
    @Max(value=999999999, message="projectId必须为数字")
    private Integer projectId;

    @FieldName("projectName")
    @NotNull
    @Size(max=145)
    private String projectName;

    @FieldName("stageName")
    @NotNull
    @Size(max=45)
    private String stageName;

    @FieldName("stepName")
    @NotNull
    @Size(max=45)
    private String stepName;

    @FieldName("编号")
    @NotNull
    @Size(max=45)
    private String formNo;

    @FieldName("合同号")
    @NotNull
    @Size(max=45)
    private String contractNo;

    @FieldName("方案讨论日期")
    @NotNull
    @Size(max=45)
    private String planDiscussTime;

    @FieldName("设计评审方式")
    @NotNull
    @Size(max=45)
    private String designReviewType;

    @FieldName("设计评审日期")
    @NotNull
    @Size(max=45)
    private String designReviewTime;

    @FieldName("交总师时间")
    @NotNull
    @Size(max=45)
    private String submitTotalDesignerTime;

    @FieldName("验收日期")
    @NotNull
    @Size(max=45)
    private String acceptTime;

    @FieldName("发图日期")
    @NotNull
    @Size(max=45)
    private String sendPictureTime;

    @FieldName("涉及专业")
    @NotNull
    @Size(max=145)
    private String designMajorList;

    @FieldName("remark")
    @NotNull
    @Size(max=145)
    private String remark;

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
    @Size(max=45)
    private Date gmtModified;

    @FieldName("processInstanceId")
    @NotNull
    @Size(max=45)
    private String processInstanceId;

    @FieldName("processEnd")
    @NotNull
    private Boolean processEnd;
}