package com.cmcu.common.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class CommonEdSignPaper {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="tenetId不能为空!")
    @Size(max=45, message="tenetId长度不能超过45")
    private String tenetId;

    @NotNull(message="signId不能为空!")
    @Size(max=45, message="signId长度不能超过45")
    private String signId;

    @NotNull(message="fileId不能为空!")
    @Max(value=999999999, message="fileId必须为数字")
    private Integer fileId;

    @NotNull(message="projectName不能为空!")
    @Size(max=145, message="projectName长度不能超过145")
    private String projectName;

    @NotNull(message="drawingName不能为空!")
    @Size(max=145, message="drawingName长度不能超过145")
    private String drawingName;

    @NotNull(message="drawingNo不能为空!")
    @Size(max=145, message="drawingNo长度不能超过145")
    private String drawingNo;

    @NotNull(message="chargeName不能为空!")
    @Size(max=45, message="chargeName长度不能超过45")
    private String chargeName;

    @NotNull(message="校核不能为空!")
    @Size(max=45, message="校核长度不能超过45")
    private String proofreadName;

    @NotNull(message="auditName不能为空!")
    @Size(max=45, message="auditName长度不能超过45")
    private String auditName;

    @NotNull(message="otherData不能为空!")
    @Size(max=4500, message="otherData长度不能超过4500")
    private String otherData;

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
}