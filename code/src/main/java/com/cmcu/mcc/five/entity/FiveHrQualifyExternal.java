package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveHrQualifyExternal {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45,message="businessKey长度不能大于45!")
    private String businessKey;

    @NotNull(message="formNo不能为空!")
    @Size(max=45,message="formNo长度不能大于45!")
    private String formNo;

    @NotNull(message="userLogin不能为空!")
    @Size(max=45,message="userLogin长度不能大于45!")
    private String userLogin;

    @NotNull(message="姓名不能为空!")
    @Size(max=45,message="姓名长度不能大于45!")
    private String userName;

    @NotNull(message="性别不能为空!")
    private Boolean man;

    @NotNull(message="检查年份不能为空!")
    @Size(max=45,message="检查年份长度不能大于45!")
    private String checkYear;

    @NotNull(message="deptId不能为空!")
    @Max(value=999999999, message="deptId必须为数字")
    private Integer deptId;

    @NotNull(message="deptName不能为空!")
    @Size(max=145,message="deptName长度不能大于145!")
    private String deptName;

    @NotNull(message="学历不能为空!")
    @Size(max=45,message="学历长度不能大于45!")
    private String educationBackground;

    @NotNull(message="年龄不能为空!")
    @Size(max=45,message="年龄长度不能大于45!")
    private String age;

    @NotNull(message="毕业院校不能为空!")
    @Size(max=45,message="毕业院校长度不能大于45!")
    private String graduateCollege;

    @NotNull(message="所学专业不能为空!")
    @Size(max=45,message="所学专业长度不能大于45!")
    private String graduateMajor;

    @NotNull(message="拟聘专业不能为空!")
    @Size(max=45,message="拟聘专业长度不能大于45!")
    private String planMajor;

    @NotNull(message="拟聘岗位（设计，校核，审核，总设计师，其他）不能为空!")
    @Size(max=45,message="拟聘岗位（设计，校核，审核，总设计师，其他）长度不能大于45!")
    private String planPost;

    @NotNull(message="职称不能为空!")
    @Size(max=45,message="职称长度不能大于45!")
    private String title;

    @NotNull(message="资质不能为空!")
    @Size(max=45,message="资质长度不能大于45!")
    private String qualificationCertificate;

    @NotNull(message="workExperience不能为空!")
    @Size(max=45,message="workExperience长度不能大于45!")
    private String workExperience;

    @NotNull(message="业绩不能为空!")
    @Size(max=45,message="业绩长度不能大于45!")
    private String performance;

    @NotNull(message="聘用人员类型  外单位退休人员　外单位内退人员　劳务派遣人员　 其他不能为空!")
    @Size(max=45,message="聘用人员类型  外单位退休人员　外单位内退人员　劳务派遣人员　 其他长度不能大于45!")
    private String userType;

    @NotNull(message="拟聘报酬不能为空!")
    @Size(max=45,message="拟聘报酬长度不能大于45!")
    private String planSalary;

    @NotNull(message="试用期工资 80% 100%不能为空!")
    @Size(max=45,message="试用期工资 80% 100%长度不能大于45!")
    private String probationSalary;

    @NotNull(message="用人单位意见（含聘用理由）不能为空!")
    @Size(max=450,message="用人单位意见（含聘用理由）长度不能大于450!")
    private String userDepartmentOpinion;

    @NotNull(message="人力资源部意见不能为空!")
    @Size(max=450,message="人力资源部意见长度不能大于450!")
    private String hrDepartmentOpinion;

    @NotNull(message="信息化建设与管理部意见（针对技术岗位认定）不能为空!")
    @Size(max=450,message="信息化建设与管理部意见（针对技术岗位认定）长度不能大于450!")
    private String technologyDepartmentOpinion;

    @NotNull(message="公司主管领导意见不能为空!")
    @Size(max=45,message="公司主管领导意见长度不能大于45!")
    private String chargeLeaderOpinion;

    @NotNull(message="公司主管人事工作领导意见不能为空!")
    @Size(max=45,message="公司主管人事工作领导意见长度不能大于45!")
    private String chargeHrLeaderOpinion;

    @NotNull(message="remark不能为空!")
    @Size(max=450,message="remark长度不能大于450!")
    private String remark;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="gmtCreate不能为空!")
    private Date gmtCreate;

    @NotNull(message="gmtModified不能为空!")
    private Date gmtModified;

    @NotNull(message="creator不能为空!")
    @Size(max=45,message="creator长度不能大于45!")
    private String creator;

    @NotNull(message="creatorName不能为空!")
    @Size(max=45,message="creatorName长度不能大于45!")
    private String creatorName;

    @NotNull(message="processInstanceId不能为空!")
    @Size(max=45,message="processInstanceId长度不能大于45!")
    private String processInstanceId;

    @NotNull(message="isProcessEnd不能为空!")
    private Boolean processEnd;

    @NotNull(message="是否已经处理入库不能为空!")
    private Boolean handled;
}