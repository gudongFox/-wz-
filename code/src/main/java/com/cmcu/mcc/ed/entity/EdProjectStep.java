package com.cmcu.mcc.ed.entity;

import com.cmcu.common.annotation.FieldName;
import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class EdProjectStep {
    @FieldName("id")
    @NotNull
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @FieldName("projectId")
    @NotNull
    @Max(value=999999999, message="projectId必须为数字")
    private Integer projectId;

    @FieldName("项目名称")
    @NotNull
    @Size(max=45)
    private String projectName;

    @FieldName("工程编号")
    @NotNull
    @Size(max=45)
    private String projectNo;

    @FieldName("åˆåŒå·")
    @NotNull
    @Size(max=45)
    private String contractNo;

    @FieldName("阶段名称")
    @NotNull
    @Size(max=45)
    private String stageName;

    @FieldName("分期名称")
    @NotNull
    @Size(max=45)
    private String stepName;

    @FieldName("项目总师")
    @NotNull
    @Size(max=45)
    private String chargeMan;

    @FieldName("chargeManName")
    @NotNull
    @Size(max=45)
    private String chargeManName;

    @FieldName("项目经理")
    @NotNull
    @Size(max=45)
    private String exeChargeMan;

    @FieldName("exeChargeManName")
    @NotNull
    @Size(max=45)
    private String exeChargeManName;

    @FieldName("isCp")
    @NotNull
    private Boolean cp;

    @FieldName("isCpClosed")
    @NotNull
    private Boolean cpClosed;

    @FieldName("分期状态")
    @NotNull
    @Size(max=45)
    private String stepStatus;

    @FieldName("seq")
    @NotNull
    @Max(value=999999999, message="seq必须为数字")
    private Integer seq;

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

    @FieldName("attendUser")
    @NotNull
    @Size(max=4500)
    private String attendUser;

    @FieldName("remark")
    @NotNull
    @Size(max=450)
    private String remark;

    @FieldName("图纸版次")
    @NotNull
    @Size(max=45)
    private String dwgVersion;

    @FieldName("图纸日期")
    @NotNull
    @Size(max=45)
    private String dwgTime;

    @FieldName("文件夹内容排序方式:名称,时间,数字,默认按名称")
    @NotNull
    @Size(max=45)
    private String contentSortMethod;

    @FieldName("其它总师")
    @Size(max=45)
    private String otherChargeMan;

    @FieldName("otherChargeManName")
    @Size(max=45)
    private String otherChargeManName;

    @FieldName("项目分管院长")
    @Size(max=45)
    private String projectChargeMan;

    @FieldName("otherChargeManName")
    @Size(max=45)
    private String projectChargeManName;
}