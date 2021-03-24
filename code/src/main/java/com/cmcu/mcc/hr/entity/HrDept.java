package com.cmcu.mcc.hr.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class HrDept {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="name不能为空!")
    @Size(max=145, message="name长度不能超过145")
    private String name;

    @NotNull(message="deptCode不能为空!")
    @Size(max=45, message="deptCode长度不能超过45")
    private String deptCode;

    @NotNull(message="勘察部门，设计部门，其他不能为空!")
    @Size(max=45, message="勘察部门，设计部门，其他长度不能超过45")
    private String deptType;

    @NotNull(message="parentId不能为空!")
    @Max(value=999999999, message="parentId必须为数字")
    private Integer parentId;

    @NotNull(message="seq不能为空!")
    @Max(value=999999999, message="seq必须为数字")
    private Integer seq;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="gmtCreate不能为空!")
    private Date gmtCreate;

    @NotNull(message="gmtModified不能为空!")
    private Date gmtModified;

    @NotNull(message="remark不能为空!")
    @Size(max=450, message="remark长度不能超过450")
    private String remark;

    @Size(max=45, message="部门分管领导长度不能超过45")
    private String deptChargeMan;

    @Size(max=45, message="部门分管领导名称长度不能超过45")
    private String deptChargeManName;

    @Size(max=45, message="部门正职领导长度不能超过45")
    private String deptFirstLeader;

    @Size(max=45, message="deptFirstLeaderName长度不能超过45")
    private String deptFirstLeaderName;

    @Size(max=45, message="部门副职领导长度不能超过45")
    private String deptSecondLeader;

    @Size(max=45, message="deptSecondLeaderName长度不能超过45")
    private String deptSecondLeaderName;

    @Size(max=45, message="部门财务人员长度不能超过45")
    private String deptFinanceMan;

    @Size(max=45, message="部门财务人员长度不能超过45")
    private String deptFinanceManName;

    @Size(max=45, message="设计副总长度不能超过45")
    private String designManager;

    @Size(max=45, message="设计副总长度不能超过45")
    private String designManagerName;

    @Size(max=45, message="工程副总长度不能超过45")
    private String projectManager;

    @Size(max=45, message="工程副总长度不能超过45")
    private String projectManagerName;
}