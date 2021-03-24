package com.cmcu.mcc.sys.entity;

import com.cmcu.common.annotation.FieldName;
import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class SysConfig {
    @FieldName("id")
    @NotNull
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @FieldName("defaultPwd")
    @NotNull
    @Size(max=45)
    private String defaultPwd;

    @FieldName("defaultUrl")
    @NotNull
    @Size(max=45)
    private String defaultUrl;

    @FieldName("appCode")
    @NotNull
    @Size(max=45)
    private String appCode;

    @FieldName("appName")
    @NotNull
    @Size(max=45)
    private String appName;

    @FieldName("dirPath")
    @NotNull
    @Size(max=45)
    private String dirPath;

    @FieldName("companyName")
    @NotNull
    @Size(max=145)
    private String companyName;

    @FieldName("companyAddress")
    @NotNull
    @Size(max=145)
    private String companyAddress;

    @FieldName("companyLinkMan")
    @NotNull
    @Size(max=45)
    private String companyLinkMan;

    @FieldName("companyTel")
    @NotNull
    @Size(max=45)
    private String companyTel;

    @FieldName("gmtCreate")
    @NotNull
    private Date gmtCreate;

    @FieldName("gmtModified")
    @NotNull
    private Date gmtModified;

    @FieldName("腾讯企业微信id")
    @NotNull
    @Size(max=45)
    private String wxCorpId;

    @FieldName("腾讯企业微信密码")
    @NotNull
    @Size(max=145)
    private String wxCorpSecret;

    @FieldName("应用id")
    @NotNull
    @Max(value=999999999, message="应用id必须为数字")
    private Integer wxAgentId;
}