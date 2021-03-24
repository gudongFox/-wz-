package com.cmcu.mcc.hr.entity;

import com.cmcu.common.annotation.FieldName;
import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveHrExternalTechnicianApply {
    @FieldName("id")
    @NotNull
    @Max(value=999999999, message="id蹇呴』涓烘暟瀛�")
    private Integer id;

    @FieldName("formNo")
    @NotNull
    @Size(max=45)
    private String formNo;

    @FieldName("姓名")
    @NotNull
    @Size(max=45)
    private String userName;

    @FieldName("性别")
    @NotNull
    private Boolean man;

    @FieldName("检查年份")
    @NotNull
    @Size(max=45)
    private String checkYear;

    @FieldName("deptId")
    @NotNull
    @Max(value=999999999, message="deptId蹇呴』涓烘暟瀛�")
    private Integer deptId;

    @FieldName("deptName")
    @NotNull
    @Size(max=145)
    private String deptName;

    @FieldName("学历")
    @NotNull
    @Size(max=45)
    private String educationBackground;

    @FieldName("年龄")
    @NotNull
    @Size(max=45)
    private String age;

    @FieldName("毕业院校")
    @NotNull
    @Size(max=45)
    private String graduateCollege;

    @FieldName("所学专业")
    @NotNull
    @Size(max=45)
    private String graduateMajor;

    @FieldName("拟聘专业")
    @NotNull
    @Size(max=45)
    private String planMajor;

    @FieldName("拟聘岗位（设计，校核，审核，总设计师，其他）")
    @NotNull
    @Size(max=45)
    private String planPost;

    @FieldName("职称")
    @NotNull
    @Size(max=45)
    private String title;

    @FieldName("资质")
    @NotNull
    @Size(max=45)
    private String qualificationCertificate;

    @FieldName("workExperience")
    @NotNull
    @Size(max=45)
    private String workExperience;

    @FieldName("业绩")
    @NotNull
    @Size(max=45)
    private String performance;

    @FieldName("聘用人员类型  外单位退休人员　外单位内退人员　劳务派遣人员　 其他")
    @NotNull
    @Size(max=45)
    private String userType;

    @FieldName("拟聘报酬")
    @NotNull
    @Size(max=45)
    private String planSalary;

    @FieldName("试用期工资 80% 100%")
    @NotNull
    @Size(max=45)
    private String probationSalary;

    @FieldName("用人单位意见（含聘用理由）")
    @NotNull
    @Size(max=450)
    private String userDepartmentOpinion;

    @FieldName("人力资源部意见")
    @NotNull
    @Size(max=450)
    private String hrDepartmentOpinion;

    @FieldName("信息化建设与管理部意见（针对技术岗位认定）")
    @NotNull
    @Size(max=450)
    private String technologyDepartmentOpinion;

    @FieldName("公司主管领导意见")
    @NotNull
    @Size(max=45)
    private String chargeLeaderOpinion;

    @FieldName("公司主管人事工作领导意见")
    @NotNull
    @Size(max=45)
    private String chargeHrLeaderOpinion;

    @FieldName("remark")
    @NotNull
    @Size(max=450)
    private String remark;

    @FieldName("isDeleted")
    @NotNull
    private Boolean deleted;

    @FieldName("gmtCreate")
    @NotNull
    private Date gmtCreate;

    @FieldName("gmtModified")
    @NotNull
    private Date gmtModified;

    @FieldName("creator")
    @NotNull
    @Size(max=45)
    private String creator;

    @FieldName("creatorName")
    @NotNull
    @Size(max=45)
    private String creatorName;

    @FieldName("processInstanceId")
    @NotNull
    @Size(max=45)
    private String processInstanceId;

    @FieldName("isProcessEnd")
    @NotNull
    private Boolean processEnd;
}