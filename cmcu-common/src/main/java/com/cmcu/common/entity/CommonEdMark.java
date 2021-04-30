package com.cmcu.common.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class CommonEdMark {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=145, message="businessKey长度不能超过145")
    private String businessKey;

    @Size(max=45, message="roleName长度不能超过45")
    private String roleName;

    @NotNull(message="commonFileId不能为空!")
    @Max(value=999999999, message="commonFileId必须为数字")
    private Integer commonFileId;

    @NotNull(message="commonFileName不能为空!")
    @Size(max=145, message="commonFileName长度不能超过145")
    private String commonFileName;

    @NotNull(message="drawNo不能为空!")
    @Size(max=145, message="drawNo长度不能超过145")
    private String drawNo;

    @NotNull(message="drawName不能为空!")
    @Size(max=145, message="drawName长度不能超过145")
    private String drawName;

    @NotNull(message="questionLevel不能为空!")
    @Size(max=145, message="questionLevel长度不能超过145")
    private String questionLevel;

    @NotNull(message="questionContent不能为空!")
    @Size(max=450, message="questionContent长度不能超过450")
    private String questionContent;

    @NotNull(message="cloudLocation不能为空!")
    @Size(max=4500, message="cloudLocation长度不能超过2450")
    private String cloudLocation;

    @NotNull(message="questionColor不能为空!")
    @Size(max=145, message="questionColor长度不能超过145")
    private String questionColor;

    @NotNull(message="questionAnswer不能为空!")
    @Size(max=450, message="questionAnswer长度不能超过450")
    private String questionAnswer;

    @NotNull(message="answer不能为空!")
    @Size(max=45, message="answer长度不能超过45")
    private String answer;

    @NotNull(message="answerName不能为空!")
    @Size(max=45, message="answerName长度不能超过45")
    private String answerName;

    private Date answerTime;

    @NotNull(message="来源于什么文件,目前只有一个场景,就是从协同文件上同步过来的意见不能为空!")
    @Max(value=999999999, message="来源于什么文件,目前只有一个场景,就是从协同文件上同步过来的意见必须为数字")
    private Integer sourceFileId;

    @NotNull(message="意见截图")
    @Max(value=999999999, message="意见截图")
    private Integer picAttachId;

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
}