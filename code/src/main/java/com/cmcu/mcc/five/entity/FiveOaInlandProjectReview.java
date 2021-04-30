package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaInlandProjectReview {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="课题名称不能为空!")
    @Size(max=145, message="课题名称长度不能超过145")
    private String projectName;

    @NotNull(message="主要研究内容不能为空!")
    @Size(max=450, message="主要研究内容长度不能超过450")
    private String projectContent;

    @NotNull(message="项目类别不能为空!")
    @Size(max=45, message="项目类别长度不能超过45")
    private String projectType;

    @NotNull(message="成果形式及预期效益不能为空!")
    @Size(max=4500, message="成果形式及预期效益长度不能超过4500")
    private String achievement;

    @NotNull(message="deptId不能为空!")
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

    @NotNull(message="deptName不能为空!")
    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="开工日期不能为空!")
    @Size(max=45, message="开工日期长度不能超过45")
    private String startTime;

    @NotNull(message="完工日期不能为空!")
    @Size(max=45, message="完工日期长度不能超过45")
    private String endTime;

    @NotNull(message="呈阅领导不能为空!")
    @Size(max=450, message="呈阅领导长度不能超过450")
    private String chargeMen;

    @NotNull(message="chargeMenName不能为空!")
    @Size(max=450, message="chargeMenName长度不能超过450")
    private String chargeMenName;

    @NotNull(message="主要参与任不能为空!")
    @Size(max=145, message="主要参与任长度不能超过145")
    private String attender;

    @NotNull(message="attenderName不能为空!")
    @Size(max=450, message="attenderName长度不能超过450")
    private String attenderName;

    @NotNull(message="合计经费不能为空!")
    @Size(max=45, message="合计经费长度不能超过45")
    private String totalPrice;

    @NotNull(message="材料费不能为空!")
    @Size(max=45, message="材料费长度不能超过45")
    private String materialsCost;

    @NotNull(message="专用费不能为空!")
    @Size(max=45, message="专用费长度不能超过45")
    private String appropriativeCost;

    @NotNull(message="外协费不能为空!")
    @Size(max=45, message="外协费长度不能超过45")
    private String outsourceCost;

    @NotNull(message="会议费不能为空!")
    @Size(max=45, message="会议费长度不能超过45")
    private String meetingCost;

    @NotNull(message="差旅费不能为空!")
    @Size(max=45, message="差旅费长度不能超过45")
    private String travelCost;

    @NotNull(message="专家咨询费不能为空!")
    @Size(max=45, message="专家咨询费长度不能超过45")
    private String specialistCost;

    @NotNull(message="固定资产折旧费不能为空!")
    @Size(max=45, message="固定资产折旧费长度不能超过45")
    private String fixeAssetDepreciationCost;

    @NotNull(message="燃料动力费不能为空!")
    @Size(max=45, message="燃料动力费长度不能超过45")
    private String fuelPowerCost;

    @NotNull(message="工资劳务费不能为空!")
    @Size(max=45, message="工资劳务费长度不能超过45")
    private String salaryServiceCost;

    @NotNull(message="scientificFirstTrial不能为空!")
    @Size(max=145, message="scientificFirstTrial长度不能超过145")
    private String scientificFirstTrial;

    @NotNull(message="scientificFirstTrialName不能为空!")
    @Size(max=450, message="scientificFirstTrialName长度不能超过450")
    private String scientificFirstTrialName;

    @NotNull(message="remark不能为空!")
    @Size(max=450, message="remark长度不能超过450")
    private String remark;

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

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="isProcessEnd不能为空!")
    private Boolean processEnd;

    @NotNull(message="processInstanceId不能为空!")
    @Size(max=45, message="processInstanceId长度不能超过45")
    private String processInstanceId;

    @NotNull(message="是否脱密不能为空!")
    private Boolean secret;

    @NotNull(message="reviewType不能为空!")
    @Size(max=45, message="reviewType长度不能超过45")
    private String reviewType;
}