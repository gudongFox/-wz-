package com.cmcu.mcc.sys.entity;

import com.cmcu.common.annotation.FieldName;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Getter
@Setter
public class SysUser {

    private Integer id;

    @NotBlank(message = "用户名称不能为空!")
    @NotEmpty
    @Size(min = 1,max = 20,message = "用户名称为1-20个字符!")
    private String userName;

    @NotBlank(message = "登录名不能为空!")
    @NotEmpty
    @Size(min = 1,max = 20,message = "登录名为1-20个字符!")
    private String userLogin;


    @Size(max=145)
    private String headUrl;

    @Size(max=145)
    private String signUrl;

    @NotEmpty
    private String password;

    @Size(max = 11,message = "手机号码应为11位!")
    private String mobile;

    @Size(max=45)
    private String idCard;

    @Size(max=45)
    private String specialty;

    @FieldName("岗位")
    @Size(max = 45)
    private String workPosition;

    @FieldName("职务")
    @Size(max = 45)
    private String workTitle;

    @NotNull
    private Integer deptId;

    @NotNull
    private Integer seq;

    @NotNull
    private Boolean mine;

    @NotNull
    private Boolean deleted;

    @NotNull
    private Date gmtCreate;

    @NotNull
    private Date gmtModified;

    @FieldName("备注")
    @Size(max = 100)
    private String remark;

}