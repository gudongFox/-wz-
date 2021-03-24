package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaDeptJournal {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="送签部门不能为空!")
    @Max(value=999999999, message="送签部门必须为数字")
    private Integer deptId;

    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="稿件题目不能为空!")
    @Size(max=45, message="稿件题目长度不能超过45")
    private String manuscriptTitle;

    @NotNull(message="第一作者不能为空!")
    @Size(max=45, message="第一作者长度不能超过45")
    private String firstAuthor;

    @NotNull(message="审稿人不能为空!")
    @Size(max=45, message="审稿人长度不能超过45")
    private String reader;

    @NotNull(message="readerName不能为空!")
    @Size(max=45, message="readerName长度不能超过45")
    private String readerName;

    @NotNull(message="submitTime不能为空!")
    @Size(max=45, message="submitTime长度不能超过45")
    private String submitTime;

    @NotNull(message="部门审查意见不能为空!")
    @Size(max=450, message="部门审查意见长度不能超过450")
    private String deptOpinion;

    @NotNull(message="单位保密人不能为空!")
    @Size(max=45, message="单位保密人长度不能超过45")
    private String deptSecrecyUser;

    @NotNull(message="deptSecrecyUserName不能为空!")
    @Size(max=45, message="deptSecrecyUserName长度不能超过45")
    private String deptSecrecyUserName;

    @NotNull(message="推荐栏目不能为空!")
    @Size(max=45, message="推荐栏目长度不能超过45")
    private String recommendColumns;

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

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="isProcessEnd不能为空!")
    private Boolean processEnd;

    @NotNull(message="processInstanceId不能为空!")
    @Size(max=45, message="processInstanceId长度不能超过45")
    private String processInstanceId;
}