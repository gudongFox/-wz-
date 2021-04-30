package com.cmcu.mcc.ed.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveEdDesignDrawingCheck {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="projectId不能为空!")
    @Max(value=999999999, message="projectId必须为数字")
    private Integer projectId;

    @NotNull(message="项目名称不能为空!")
    @Size(max=145, message="项目名称长度不能超过145")
    private String projectName;

    @NotNull(message="项目编号不能为空!")
    @Size(max=45, message="项目编号长度不能超过45")
    private String projectNo;

    @NotNull(message="母项号不能为空!")
    @Size(max=45, message="母项号长度不能超过45")
    private String parentNo;

    @NotNull(message="密级不能为空!")
    @Size(max=45, message="密级长度不能超过45")
    private String secretLevel;

    @NotNull(message="stageName不能为空!")
    @Size(max=45, message="stageName长度不能超过45")
    private String stageName;

    @NotNull(message="stepId不能为空!")
    @Max(value=999999999, message="stepId必须为数字")
    private Integer stepId;

    @NotNull(message="formNo不能为空!")
    @Size(max=45, message="formNo长度不能超过45")
    private String formNo;

    @NotNull(message="stepName不能为空!")
    @Size(max=45, message="stepName长度不能超过45")
    private String stepName;

    @NotNull(message="contractNo不能为空!")
    @Size(max=45, message="contractNo长度不能超过45")
    private String contractNo;

    @NotNull(message="项目主管院长不能为空!")
    @Size(max=45, message="项目主管院长长度不能超过45")
    private String projectChargeMan;

    @NotNull(message="projectChargeManName不能为空!")
    @Size(max=45, message="projectChargeManName长度不能超过45")
    private String projectChargeManName;

    @NotNull(message="交验日期不能为空!")
    @Size(max=45, message="交验日期长度不能超过45")
    private String checkTime;

    @NotNull(message="发至单位不能为空!")
    @Size(max=145, message="发至单位长度不能超过145")
    private String sendDeptName;

    @NotNull(message="发至份数不能为空!")
    @Max(value=999999999, message="发至份数必须为数字")
    private Integer sendCopies;

    @NotNull(message="成品加工活页不能为空!")
    @Max(value=999999999, message="成品加工活页必须为数字")
    private Integer finishProduceLeaflet;

    @NotNull(message="简装不能为空!")
    @Max(value=999999999, message="简装必须为数字")
    private Integer finishProducePaperback;

    @NotNull(message="国内精装不能为空!")
    @Max(value=999999999, message="国内精装必须为数字")
    private Integer finishProduceInlandHardbound;

    @NotNull(message="国外精装不能为空!")
    @Max(value=999999999, message="国外精装必须为数字")
    private Integer finishProduceForeignHardbound;

    @NotNull(message="追加活页不能为空!")
    @Max(value=999999999, message="追加活页必须为数字")
    private Integer addProduceLeaflet;

    @NotNull(message="追加简装不能为空!")
    @Max(value=999999999, message="追加简装必须为数字")
    private Integer addProducePaperback;

    @NotNull(message="追加国内精装不能为空!")
    @Max(value=999999999, message="追加国内精装必须为数字")
    private Integer addProduceInlandHardbound;

    @NotNull(message="追加国外精装不能为空!")
    @Max(value=999999999, message="追加国外精装必须为数字")
    private Integer addProduceForeignHardbound;

    @NotNull(message="总师活页不能为空!")
    @Max(value=999999999, message="总师活页必须为数字")
    private Integer totalProduceLeaflet;

    @NotNull(message="总师简装不能为空!")
    @Max(value=999999999, message="总师简装必须为数字")
    private Integer totalProducePaperback;

    @NotNull(message="总师国内精装不能为空!")
    @Max(value=999999999, message="总师国内精装必须为数字")
    private Integer totalProduceInlandHardbound;

    @NotNull(message="总师国外精装不能为空!")
    @Max(value=999999999, message="总师国外精装必须为数字")
    private Integer totalProduceForeignHardbound;

    @NotNull(message="buildIds不能为空!")
    @Size(max=45, message="buildIds长度不能超过45")
    private String buildIds;

    @NotNull(message="建筑物号不能为空!")
    @Size(max=450, message="建筑物号长度不能超过450")
    private String buildName;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

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

    @NotNull(message="remark不能为空!")
    @Size(max=450, message="remark长度不能超过450")
    private String remark;

    @NotNull(message="processInstanceId不能为空!")
    @Size(max=45, message="processInstanceId长度不能超过45")
    private String processInstanceId;

    @NotNull(message="isProcessEnd不能为空!")
    private Boolean processEnd;
}