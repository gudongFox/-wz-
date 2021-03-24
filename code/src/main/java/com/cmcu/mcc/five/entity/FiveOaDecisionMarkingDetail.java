package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaDecisionMarkingDetail {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="本身business不能为空!")
    @Size(max=45, message="本身business长度不能超过45")
    private String businessKey;

    @NotNull(message="主流程business不能为空!")
    @Size(max=45, message="主流程business长度不能超过45")
    private String mainBusiness;

    @Size(max=45, message="关联流程business长度不能超过45")
    private String linkedBusiness;

    @NotNull(message="流程id不能为空!")
    @Size(max=450, message="流程id长度不能超过450")
    private String title;

    @NotNull(message="seq不能为空!")
    @Max(value=999999999, message="seq必须为数字")
    private Integer seq;

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

    @Size(max=450, message="detail长度不能超过450")
    private String detail;

    @Size(max=450, message="列席人长度不能超过450")
    private String attendance;

    @Size(max=800, message="attendanceName长度不能超过800")
    private String attendanceName;

    @NotNull(message="议题部门不能为空!")
    @Max(value=999999999, message="议题部门必须为数字")
    private Integer deptId;

    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @Size(max=50, message="明细类型：分为flow 流程 和text 文本长度不能超过50")
    private String detailType;

    @Size(max=450, message="原始流程标题长度不能超过450")
    private String originalTitle;

    @Size(max=45, message="原始流程标题长度不能超过45")
    private String leader;

    @NotNull(message="议题状态不能为空!")
    @Size(max=45, message="议题状态长度不能超过45")
    private String issueStatus;

    @Size(max=45, message="附件id长度不能超过45")
    private String attachId;

    @Size(max=45, message="attachName长度不能超过45")
    private String attachName;

    @Size(max=2000, message="议题结论长度不能超过2000")
    private String conclusion;

    @Size(max=45, message="arrangeMan长度不能超过45")
    private String arrangeMan;

    @Size(max=45, message="arrangeManName长度不能超过45")
    private String arrangeManName;
}