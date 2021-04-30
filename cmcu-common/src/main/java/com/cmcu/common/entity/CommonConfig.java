package com.cmcu.common.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class CommonConfig {
    @NotNull
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull
    @Size(max=45)
    private String defaultPwd;

    @NotNull
    @Size(max=45)
    private String defaultUrl;

    @NotNull
    @Size(max=45)
    private String appCode;

    @NotNull
    @Size(max=45)
    private String appName;

    @NotNull
    @Size(max=45)
    private String dirPath;

    @NotNull
    @Size(max=145)
    private String companyName;

    @NotNull
    @Size(max=145)
    private String companyAddress;

    @NotNull
    @Size(max=45)
    private String companyLinkMan;

    @NotNull
    @Size(max=45)
    private String companyTel;

    @NotNull
    private String wxAuthUrl;

    @NotNull
    @Size(max=45)
    private String wxCorpId;

    @NotNull
    @Size(max=145)
    private String wxCorpSecret;

    @NotNull
    @Max(value=999999999, message="应用id必须为数字")
    private Integer wxAgentId;

    @NotNull
    private String meetingRoomSecret;

    private Integer noticeAgentId;

    private String noticeSecret;


    @NotNull
    private String owaServer;

    @NotNull
    private String owaFileType;

    @NotNull
    private Boolean enableWx;

    @NotNull
    private Boolean enableOwa;

    @NotNull
    private Date gmtCreate;

    @NotNull
    private Date gmtModified;

}