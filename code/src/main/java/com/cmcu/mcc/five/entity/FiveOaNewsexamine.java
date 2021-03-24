package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveOaNewsexamine {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="表单号不能为空!")
    @Size(max=45, message="表单号长度不能超过45")
    private String formNo;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="标题不能为空!")
    @Size(max=45, message="标题长度不能超过45")
    private String title;

    @NotNull(message="报送单位不能为空!")
    @Max(value=999999999, message="报送单位必须为数字")
    private Integer deptId;

    @Size(max=145, message="deptName长度不能超过145")
    private String deptName;

    @NotNull(message="报送时间不能为空!")
    @Size(max=45, message="报送时间长度不能超过45")
    private String sendTime;

    @NotNull(message="作者不能为空!")
    @Size(max=45, message="作者长度不能超过45")
    private String author;

    @NotNull(message="authorName不能为空!")
    @Size(max=45, message="authorName长度不能超过45")
    private String authorName;

    @NotNull(message="作者联系电话不能为空!")
    @Size(max=45, message="作者联系电话长度不能超过45")
    private String authorLink;

    @NotNull(message="送审人不能为空!")
    @Size(max=45, message="送审人长度不能超过45")
    private String sendMan;

    @NotNull(message="sendManName不能为空!")
    @Size(max=45, message="sendManName长度不能超过45")
    private String sendManName;

    @NotNull(message="报送人联系方式不能为空!")
    @Size(max=45, message="报送人联系方式长度不能超过45")
    private String sendManLink;

    @NotNull(message="建议发布平台不能为空!")
    @Size(max=450, message="建议发布平台长度不能超过450")
    private String publishingPlatform;

    @NotNull(message="部门负责人不能为空!")
    @Size(max=45, message="部门负责人长度不能超过45")
    private String deptChargeMan;

    @NotNull(message="deptChargeManName不能为空!")
    @Size(max=45, message="deptChargeManName长度不能超过45")
    private String deptChargeManName;

    @NotNull(message="部门审查要点不能为空!")
    @Size(max=450, message="部门审查要点长度不能超过450")
    private String deptExamineTips;

    @NotNull(message="党群工作部 审查意见不能为空!")
    @Size(max=45, message="党群工作部 审查意见长度不能超过45")
    private String partyComment;

    @NotNull(message="党群工作部 负责人不能为空!")
    @Size(max=45, message="党群工作部 负责人长度不能超过45")
    private String partyChargeMan;

    @NotNull(message="partyChargeManName不能为空!")
    @Size(max=45, message="partyChargeManName长度不能超过45")
    private String partyChargeManName;

    @NotNull(message="remark不能为空!")
    @Size(max=450, message="remark长度不能超过450")
    private String remark;

    @NotNull(message="creator不能为空!")
    @Size(max=45, message="creator长度不能超过45")
    private String creator;

    @NotNull(message="创建人姓名不能为空!")
    @Size(max=45, message="创建人姓名长度不能超过45")
    private String creatorName;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="创建时间不能为空!")
    private Date gmtCreate;

    @NotNull(message="修改时间不能为空!")
    private Date gmtModified;

    @NotNull(message="processInstanceId不能为空!")
    @Size(max=45, message="processInstanceId长度不能超过45")
    private String processInstanceId;

    @NotNull(message="isProcessEnd不能为空!")
    private Boolean processEnd;
}