package com.cmcu.mcc.oa.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class OaKnowledge {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45,message="businessKey长度不能大于45!")
    private String businessKey;

    @NotNull(message="系统通知、规章制度、知识库不能为空!")
    @Size(max=45,message="系统通知、规章制度、知识库长度不能大于45!")
    private String noticeType;

    @NotNull(message="公告标题不能为空!")
    @Size(max=145,message="公告标题长度不能大于145!")
    private String noticeTitle;

    @NotNull(message="发布人不能为空!")
    @Size(max=45,message="发布人长度不能大于45!")
    private String publishUserName;

    @NotNull(message="发布部门不能为空!")
    @Size(max=145,message="发布部门长度不能大于145!")
    private String publishDeptName;

    @NotNull(message="创建人不能为空!")
    @Size(max=45,message="创建人长度不能大于45!")
    private String creator;

    @NotNull(message="creatorName不能为空!")
    @Size(max=45,message="creatorName长度不能大于45!")
    private String creatorName;

    @NotNull(message="deptId不能为空!")
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

    @NotNull(message="deptName不能为空!")
    @Size(max=145,message="deptName长度不能大于145!")
    private String deptName;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="是否置顶不能为空!")
    private Boolean top;

    @NotNull(message="是否发布不能为空!")
    private Boolean publish;

    @NotNull(message="gmtCreate不能为空!")
    private Date gmtCreate;

    @NotNull(message="gmtModified不能为空!")
    private Date gmtModified;

    @NotNull(message="remark不能为空!")
    @Size(max=450,message="remark长度不能大于450!")
    private String remark;

    @NotNull(message="processInstanceId不能为空!")
    @Size(max=45,message="processInstanceId长度不能大于45!")
    private String processInstanceId;

    @NotNull(message="isProcessEnd不能为空!")
    private Boolean processEnd;

    @NotNull(message="公告内容不能为空!")
    @Size(max=2147483647,message="公告内容长度不能大于2147483647!")
    private String noticeContent;
}