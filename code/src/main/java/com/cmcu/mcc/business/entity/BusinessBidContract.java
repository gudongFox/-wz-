package com.cmcu.mcc.business.entity;

import com.cmcu.common.annotation.FieldName;
import java.math.BigDecimal;
import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class BusinessBidContract {
    @FieldName("id")
    @NotNull
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @FieldName("bidApplyId")
    @NotNull
    @Max(value=999999999, message="bidApplyId必须为数字")
    private Integer bidApplyId;

    @FieldName("bidAttendId")
    @NotNull
    @Max(value=999999999, message="bidAttendId必须为数字")
    private Integer bidAttendId;

    @FieldName("bidProjectChargeId")
    @NotNull
    @Max(value=999999999, message="bidProjectChargeId必须为数字")
    private Integer bidProjectChargeId;

    @FieldName("deptId")
    @NotNull
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

    @FieldName("deptName")
    @NotEmpty
    @Size(max=145)
    private String deptName;

    @FieldName("项目名称")
    @NotEmpty
    @Size(max=145)
    private String projectName;

    @FieldName("建设单位")
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
    private String projectQuality;

    @FieldName("projectContent")
    @NotEmpty
    @Size(max=45)
    private String projectContent;

    @FieldName("工程价格")
    @NotNull
    @Max(value=999999999, message="工程价格必须为数字")
    private BigDecimal contractMoney;

    @FieldName("费率")
    @NotEmpty
    @Size(max=45)
    private String contractRate;

    @FieldName("contractType")
    @NotEmpty
    @Size(max=45)
    private String contractType;

    @FieldName("guaranteeRule")
    @NotEmpty
    @Size(max=45)
    private String guaranteeRule;

    @FieldName("businessType")
    @NotEmpty
    @Size(max=45)
    private String businessType;

    @FieldName("paymentRule")
    @NotEmpty
    @Size(max=145)
    private String paymentRule;

    @FieldName("remark")
    @NotEmpty
    @Size(max=450)
    private String remark;

    @FieldName("creator")
    @NotEmpty
    @Size(max=45)
    private String creator;

    @FieldName("creatorName")
    @NotEmpty
    @Size(max=45)
    private String creatorName;

    @FieldName("isDeleted")
    @NotNull
    private Boolean deleted;

    @FieldName("gmtCreate")
    @NotNull
    private Date gmtCreate;

    @FieldName("gmtModified")
    @NotNull
    private Date gmtModified;

    @FieldName("processInstanceId")
    @NotEmpty
    @Size(max=45)
    private String processInstanceId;

    @FieldName("isProcessEnd")
    @NotNull
    private Boolean processEnd;
}