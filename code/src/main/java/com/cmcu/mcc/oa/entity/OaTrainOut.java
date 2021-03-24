package com.cmcu.mcc.oa.entity;

import com.cmcu.common.annotation.FieldName;
import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class OaTrainOut {
    @FieldName("id")
    @NotNull
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @FieldName("businessKey")
    @NotNull
    @Size(max=45)
    private String businessKey;

    @FieldName("deptId")
    @NotNull
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

    @FieldName("deptName")
    @NotNull
    @Size(max=145)
    private String deptName;

    @FieldName("培训名称")
    @NotNull
    @Size(max=145)
    private String trainTitle;

    @FieldName("培训内容")
    @NotNull
    @Size(max=450)
    private String trainDesc;

    @FieldName("培训单位")
    @NotNull
    @Size(max=145)
    private String trainOrg;

    @FieldName("参加培训人员名单")
    @NotNull
    @Size(max=145)
    private String attendUsers;

    @FieldName("attendUserCount")
    @NotNull
    @Max(value=999999999, message="attendUserCount必须为数字")
    private Integer attendUserCount;

    @FieldName("培训费")
    @NotNull
    @Size(max=145)
    private String trainCost;

    @FieldName("合计培训费")
    @NotNull
    @Size(max=145)
    private String trainTotalCost;

    @FieldName("开始时间")
    @NotNull
    @Size(max=145)
    private String startTime;

    @FieldName("结束时间")
    @NotNull
    @Size(max=145)
    private String endTime;

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