package com.cmcu.mcc.sys.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class SysSchedule {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="userLogin不能为空!")
    @Size(max=45, message="userLogin长度不能超过45")
    private String userLogin;

    @NotNull(message="userName不能为空!")
    @Size(max=45, message="userName长度不能超过45")
    private String userName;

    @NotNull(message="类型不能为空!")
    @Size(max=45, message="类型长度不能超过45")
    private String type;

    @NotNull(message="具体事项不能为空!")
    @Size(max=450, message="具体事项长度不能超过450")
    private String title;

    @NotNull(message="是否全天不能为空!")
    private Boolean allDay;

    @NotNull(message="start不能为空!")
    @Size(max=45, message="start长度不能超过45")
    private String start;

    @NotNull(message="end不能为空!")
    @Size(max=45, message="end长度不能超过45")
    private String end;

    @NotNull(message="remark不能为空!")
    @Size(max=450, message="remark长度不能超过450")
    private String remark;

    @NotNull(message="是否完成不能为空!")
    private Boolean finished;

    @NotNull(message="提醒不能为空!")
    @Size(max=45, message="提醒长度不能超过45")
    private String remind;

    @NotNull(message="重复不能为空!")
    @Size(max=45, message="重复长度不能超过45")
    private String repetition;

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

    @NotNull(message="gmtCreate不能为空!")
    private Date gmtCreate;

    @NotNull(message="gmtModified不能为空!")
    private Date gmtModified;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="url不能为空!")
    @Size(max=45, message="url长度不能超过45")
    private String url;

    @NotNull(message="backgroundColor不能为空!")
    @Size(max=45, message="backgroundColor长度不能超过45")
    private String backgroundColor;
}