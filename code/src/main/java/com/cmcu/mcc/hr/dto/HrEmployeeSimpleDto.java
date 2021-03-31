package com.cmcu.mcc.hr.dto;

import lombok.Data;

@Data
public class HrEmployeeSimpleDto {

    private String userLogin;

    private String userName;

    private String officeTel;

    private String mobile;

    private int deptId;

    private String deptName;

    private String specialty;

    private String positionIds;

    private String positionNames;

    private String shortPositionNames;

    private String roleIds;

    private String roleNames;

    private String shortRoleNames;

    private  int checkYear;

    private String majorName;

    private String projectType;

    private boolean projectCharge;

    private boolean projectExeCharge;

    private boolean majorCharge;

    private boolean design;

    private boolean proofread;

    private boolean audit;

    private  boolean approve;

    private boolean companyApprove;

    private boolean chiefDesigner;

    private boolean proChief;

    private String userStatus;

    private int userSeq;

    private String userType;

}
