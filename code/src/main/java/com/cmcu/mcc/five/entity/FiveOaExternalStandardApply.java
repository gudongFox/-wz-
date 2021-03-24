package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaExternalStandardApply {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="表单编号不能为空!")
    @Size(max=45, message="表单编号长度不能超过45")
    private String formNo;

    @NotNull(message="taskNo不能为空!")
    @Size(max=45, message="taskNo长度不能超过45")
    private String taskNo;

    @NotNull(message="课题名称不能为空!")
    @Size(max=45, message="课题名称长度不能超过45")
    private String taskName;

    @NotNull(message="主要研究内容不能为空!")
    @Size(max=1000, message="主要研究内容长度不能超过1000")
    private String majorResearchContent;

    @NotNull(message="成果形式及预期效益不能为空!")
    @Size(max=1000, message="成果形式及预期效益长度不能超过1000")
    private String achivement;

    @NotNull(message="开工日期不能为空!")
    @Size(max=45, message="开工日期长度不能超过45")
    private String commencementDate;

    @NotNull(message="完工日期不能为空!")
    @Size(max=45, message="完工日期长度不能超过45")
    private String completionDate;

    @NotNull(message="单位不能为空!")
    @Max(value=999999999, message="单位必须为数字")
    private Integer deptId;

    @NotNull(message="deptName不能为空!")
    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="课题负责人不能为空!")
    @Size(max=450, message="课题负责人长度不能超过450")
    private String taskCharger;

    @NotNull(message="taskChargerName不能为空!")
    @Size(max=450, message="taskChargerName长度不能超过450")
    private String taskChargerName;

    @NotNull(message="主要参加人不能为空!")
    @Size(max=450, message="主要参加人长度不能超过450")
    private String mainParticipant;

    @NotNull(message="mainParticipantName不能为空!")
    @Size(max=450, message="mainParticipantName长度不能超过450")
    private String mainParticipantName;

    @NotNull(message="公司专家委不能为空!")
    @Size(max=145, message="公司专家委长度不能超过145")
    private String scientificFirstTrial;

    @NotNull(message="scientificFirstTrialName不能为空!")
    @Size(max=450, message="scientificFirstTrialName长度不能超过450")
    private String scientificFirstTrialName;

    @NotNull(message="外拨款（万元）不能为空!")
    @Size(max=45, message="外拨款（万元）长度不能超过45")
    private String outsidePayment;

    @NotNull(message="本年度预计拨付（万元）不能为空!")
    @Size(max=45, message="本年度预计拨付（万元）长度不能超过45")
    private String yearExceptPayment;

    @NotNull(message="remark不能为空!")
    @Size(max=450, message="remark长度不能超过450")
    private String remark;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="creator不能为空!")
    @Size(max=45, message="creator长度不能超过45")
    private String creator;

    @NotNull(message="创建人姓名不能为空!")
    @Size(max=45, message="创建人姓名长度不能超过45")
    private String creatorName;

    @NotNull(message="isProcessEnd不能为空!")
    private Boolean processEnd;

    @NotNull(message="创建时间不能为空!")
    private Date gmtCreate;

    @NotNull(message="修改时间不能为空!")
    private Date gmtModified;

    @NotNull(message="processInstanceId不能为空!")
    @Size(max=45, message="processInstanceId长度不能超过45")
    private String processInstanceId;

    @NotNull(message="是否脱密不能为空!")
    private Boolean secret;
}