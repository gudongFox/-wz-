package com.cmcu.mcc.hr.entity;

import com.cmcu.common.annotation.FieldName;
import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class HrContract {
    @FieldName("id")
    @NotNull
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @FieldName("userLogin")
    @NotEmpty
    @Size(max=45)
    private String userLogin;

    @FieldName("合同性质：公司用工、非公司用工")
    @NotEmpty
    @Size(max=45)
    private String contractType;

    @FieldName("用工形式：固定期限、无固定期限、退休返聘、以任务为限、非全日制用工、兼职协议、劳务派遣")
    @NotEmpty
    @Size(max=45)
    private String workType;

    @FieldName("合同区域")
    @NotEmpty
    @Size(max=45)
    private String contractLocation;

    @FieldName("社保缴纳地")
    @NotEmpty
    @Size(max=45)
    private String insureLocation;

    @FieldName("contractYear")
    @NotNull
    @Max(value=999999999, message="contractYear必须为数字")
    private Integer contractYear;

    @FieldName("beginTime")
    @NotEmpty
    @Size(max=45)
    private String beginTime;

    @FieldName("endTime")
    @NotEmpty
    @Size(max=45)
    private String endTime;

    @FieldName("通知领取时间")
    @NotEmpty
    @Size(max=45)
    private String noticeTime;

    @FieldName("用户领取时间")
    @NotEmpty
    @Size(max=45)
    private String receiveTime;

    @FieldName("isDeleted")
    @NotNull
    private Boolean deleted;

    @FieldName("gmtCreate")
    @NotNull
    private Date gmtCreate;

    @FieldName("gmtModified")
    @NotNull
    private Date gmtModified;
}