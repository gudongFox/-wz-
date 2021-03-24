package com.cmcu.mcc.business.entity;

import com.cmcu.common.annotation.FieldName;
import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class BusinessBidAttend {
    @FieldName("id")
    @NotNull
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @FieldName("bidApplyId")
    @NotNull
    @Max(value=999999999, message="bidApplyId必须为数字")
    private Integer bidApplyId;

    @FieldName("deptId")
    @NotNull
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

    @FieldName("报审单位")
    @NotEmpty
    @Size(max=45)
    private String deptName;

    @FieldName("项目名称")
    @NotEmpty
    @Size(max=45)
    private String projectName;

    @FieldName("招标人名称")
    @NotEmpty
    @Size(max=145)
    private String customerName;

    @FieldName("项目地点")
    @NotEmpty
    @Size(max=145)
    private String projectAddress;

    @FieldName("合同工期")
    @NotEmpty
    @Size(max=45)
    private String projectTime;

    @FieldName("项目规模")
    @NotEmpty
    @Size(max=45)
    private String projectScale;

    @FieldName("项目类别")
    @NotEmpty
    @Size(max=45)
    private String projectType;

    @FieldName("质量要求")
    @NotEmpty
    @Size(max=45)
    private String qualityRequest;

    @FieldName("投标保证金或保函")
    @NotEmpty
    @Size(max=45)
    private String tenderBond;

    @FieldName("技术标准")
    @NotEmpty
    @Size(max=45)
    private String techStandard;

    @FieldName("履约保证金或保函")
    @NotEmpty
    @Size(max=45)
    private String performanceBond;

    @FieldName("经营性质")
    @NotEmpty
    @Size(max=45)
    private String businessType;

    @FieldName("工程款支付条件")
    @NotEmpty
    @Size(max=145)
    private String paymentRule;

    @FieldName("派遣项目经理")
    @NotEmpty
    @Size(max=45)
    private String chargeMan;

    @FieldName("开标时间")
    @NotEmpty
    @Size(max=45)
    private String openTime;

    @FieldName("招标方式")
    @NotEmpty
    @Size(max=45)
    private String attendType;

    @FieldName("投标范围")
    @NotEmpty
    @Size(max=45)
    private String attendScope;

    @FieldName("投标报价")
    @NotEmpty
    @Size(max=45)
    private String attendPrice;

    @FieldName("remark")
    @NotEmpty
    @Size(max=450)
    private String remark;

    @FieldName("creator")
    @NotEmpty
    @Size(max=45)
    private String creator;

    @FieldName("经办人")
    @NotEmpty
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
    @NotEmpty
    @Size(max=45)
    private String processInstanceId;

    @FieldName("isProcessEnd")
    @NotNull
    private Boolean processEnd;
}