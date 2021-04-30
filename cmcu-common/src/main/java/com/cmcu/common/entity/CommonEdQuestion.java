package com.cmcu.common.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class CommonEdQuestion {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="tenetId不能为空!")
    @Size(max=45, message="tenetId长度不能超过45")
    private String tenetId;

    @NotNull(message="majorName不能为空!")
    @Size(max=45, message="majorName长度不能超过45")
    private String majorName;

    @NotNull(message="问题所属领域不能为空!")
    @Size(max=45, message="问题所属领域长度不能超过45")
    private String questionScope;

    @NotNull(message="问题类别不能为空!")
    @Size(max=45, message="问题类别长度不能超过45")
    private String questionCategory;

    @NotNull(message="规范号不能为空!")
    @Size(max=45, message="规范号长度不能超过45")
    private String standardNo;

    @NotNull(message="规范描述不能为空!")
    @Size(max=450, message="规范描述长度不能超过450")
    private String standardDesc;

    @NotNull(message="questionNo不能为空!")
    @Size(max=45, message="questionNo长度不能超过45")
    private String questionNo;

    @NotNull(message="questionLv不能为空!")
    @Size(max=145, message="questionLv长度不能超过145")
    private String questionLv;

    @NotNull(message="questionContent不能为空!")
    @Size(max=450, message="questionContent长度不能超过450")
    private String questionContent;

    @NotNull(message="关键字匹配不能为空!")
    @Size(max=145, message="关键字匹配长度不能超过145")
    private String keyWord;

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

    @NotNull(message="remark不能为空!")
    @Size(max=450, message="remark长度不能超过450")
    private String remark;
}