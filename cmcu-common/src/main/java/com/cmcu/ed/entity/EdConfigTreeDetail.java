package com.cmcu.ed.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class EdConfigTreeDetail {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="templateId不能为空!")
    @Max(value=999999999, message="templateId必须为数字")
    private Integer templateId;

    @NotNull(message="projectType不能为空!")
    @Size(max=45, message="projectType长度不能超过45")
    private String projectType;

    @NotNull(message="nodeText不能为空!")
    @Size(max=45, message="nodeText长度不能超过45")
    private String nodeText;

    @NotNull(message="nodeKey不能为空!")
    @Size(max=45, message="nodeKey长度不能超过45")
    private String nodeKey;

    @NotNull(message="nodeIcon不能为空!")
    @Size(max=45, message="nodeIcon长度不能超过45")
    private String nodeIcon;

    @NotNull(message="parentNodeKey不能为空!")
    @Size(max=45, message="parentNodeKey长度不能超过45")
    private String parentNodeKey;

    @NotNull(message="nodeUrl不能为空!")
    @Size(max=45, message="nodeUrl长度不能超过45")
    private String nodeUrl;

    @NotNull(message="enablePreKey不能为空!")
    @Size(max=45, message="enablePreKey长度不能超过45")
    private String enablePreKey;

    @NotNull(message="nodeConfig不能为空!")
    @Size(max=450, message="nodeConfig长度不能超过450")
    private String nodeConfig;

    @NotNull(message="isKeyNode不能为空!")
    private Boolean keyNode;

    @NotNull(message="seq不能为空!")
    @Max(value=999999999, message="seq必须为数字")
    private Integer seq;

    @NotNull(message="creator不能为空!")
    @Size(max=45, message="creator长度不能超过45")
    private String creator;

    @NotNull(message="creatorName不能为空!")
    @Size(max=45, message="creatorName长度不能超过45")
    private String creatorName;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="gmtCreate不能为空!")
    private Date gmtCreate;

    @NotNull(message="gmtModified不能为空!")
    private Date gmtModified;

    @NotNull(message="remark不能为空!")
    @Size(max=450, message="remark长度不能超过450")
    private String remark;
}