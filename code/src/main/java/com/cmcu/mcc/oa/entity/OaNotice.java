package com.cmcu.mcc.oa.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class OaNotice {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="系统通知、规章制度、知识库不能为空!")
    @Size(max=45, message="系统通知、规章制度、知识库长度不能超过45")
    private String noticeType;

    @NotNull(message="公告标题不能为空!")
    @Size(max=145, message="公告标题长度不能超过145")
    private String noticeTitle;

    @NotNull(message="发布人不能为空!")
    @Size(max=45, message="发布人长度不能超过45")
    private String publishUserName;

    @NotNull(message="发布部门不能为空!")
    @Size(max=145, message="发布部门长度不能超过145")
    private String publishDeptName;

    @NotNull(message="创建人不能为空!")
    @Size(max=45, message="创建人长度不能超过45")
    private String creator;

    @NotNull(message="creatorName不能为空!")
    @Size(max=45, message="creatorName长度不能超过45")
    private String creatorName;

    @NotNull(message="deptId不能为空!")
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

    @NotNull(message="deptName不能为空!")
    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="是否置顶不能为空!")
    private Boolean top;

    @NotNull(message="是否发布不能为空!")
    private Boolean publish;

    @NotNull(message="viewMans不能为空!")
    @Size(max=255, message="viewMans长度不能超过255")
    private String viewMans;

    @NotNull(message="viewMansName不能为空!")
    @Size(max=255, message="viewMansName长度不能超过255")
    private String viewMansName;

    @NotNull(message="gmtCreate不能为空!")
    private Date gmtCreate;

    @NotNull(message="gmtModified不能为空!")
    private Date gmtModified;

    @NotNull(message="remark不能为空!")
    @Size(max=450, message="remark长度不能超过450")
    private String remark;

    @NotNull(message="processInstanceId不能为空!")
    @Size(max=45, message="processInstanceId长度不能超过45")
    private String processInstanceId;

    @NotNull(message="isProcessEnd不能为空!")
    private Boolean processEnd;

    @NotNull(message="企业动态不能为空!")
    private Boolean firm;

    @NotNull(message="访问日志不能为空!")
    @Max(value=999999999, message="访问日志必须为数字")
    private Integer pageLoad;

    @NotNull(message="大分类 公司新闻 正式文件 通知公告不能为空!")
    @Size(max=45, message="大分类 公司新闻 正式文件 通知公告长度不能超过45")
    private String noticeLevel;

    private Boolean pass;

    @Size(max=450, message="文件名称长度不能超过450")
    private String attachIds;

    @Size(max=450, message="attachName长度不能超过450")
    private String attachName;

    @NotNull(message="公告概述...（发布部门：XXX)不能为空!")
    @Size(max=450, message="公告概述...（发布部门：XXX)长度不能超过450")
    private String noticeDesc;

    @NotNull(message="公告图片（长宽=2：1）不能为空!")
    @Size(max=145, message="公告图片（长宽=2：1）长度不能超过145")
    private String picUrl;

    @Size(max=45, message="制度类别长度不能超过45")
    private String noticeSystemType;

    @NotNull(message="公告内容不能为空!")
    @Size(max=2147483647, message="公告内容长度不能超过2147483647")
    private String noticeContent;

    private String readUser;
    @Size(max=45, message="图片名称ID长度不能超过45")
    private String photoAttachId;
    @Size(max=45, message="图片名称长度不能超过45")
    private String photoAttachName;

}