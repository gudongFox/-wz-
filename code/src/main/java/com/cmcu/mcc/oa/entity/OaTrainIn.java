package com.cmcu.mcc.oa.entity;

import com.cmcu.common.annotation.FieldName;
import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class OaTrainIn {
    @FieldName("id")
    @NotNull
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @FieldName("businessKey")
    @NotNull
    @Size(max=45)
    private String businessKey;

    @FieldName("培训名称")
    @NotNull
    @Size(max=145)
    private String trainTitle;

    @FieldName("申请单位（部门）")
    @NotNull
    @Size(max=145)
    private String applyDeptName;

    @FieldName("开始时间")
    @NotNull
    @Size(max=145)
    private String startTime;

    @FieldName("结束时间")
    @NotNull
    @Size(max=145)
    private String endTime;

    @FieldName("培训地点")
    @NotNull
    @Size(max=145)
    private String trainAddress;

    @FieldName("培训内容")
    @NotNull
    @Size(max=450)
    private String trainDesc;

    @FieldName("培训方式")
    @NotNull
    @Size(max=45)
    private String trainType;

    @FieldName("培训学员")
    @NotNull
    @Size(max=450)
    private String trainUsers;

    @FieldName("学员人数")
    @NotNull
    @Max(value=999999999, message="学员人数必须为数字")
    private Integer trainUserCount;

    @FieldName("教师来源")
    @NotNull
    @Size(max=145)
    private String teacherSource;

    @FieldName("培训总学时")
    @NotNull
    @Size(max=45)
    private String trainTime;

    @FieldName("专业或工种")
    @NotNull
    @Size(max=45)
    private String trainMajor;

    @FieldName("培训费用及构成")
    @NotNull
    @Size(max=450)
    private String trainCostDesc;

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

    @FieldName("deptId")
    @NotNull
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

    @FieldName("deptName")
    @NotNull
    @Size(max=145)
    private String deptName;

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