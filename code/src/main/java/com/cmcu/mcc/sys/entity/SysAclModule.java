package com.cmcu.mcc.sys.entity;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class SysAclModule {

    private Integer id;

    @NotBlank(message = "权限模块编码不能为空!")
    @Size(min = 1,max = 20,message = "权限模块编码1-20个字符!")
    private String code;

    @NotBlank(message = "权限模块名称不能为空!")
    @Size(min = 1,max = 20,message = "权限模块名称1-20个字符!")
    private String name;

    @NotNull
    private Integer parentId;

    @NotNull
    private Integer seq;

    @NotNull
    private Boolean left;

    @NotNull
    private Boolean top;

    @NotNull
    private Boolean deleted;

    private String remark;

    @Size(max = 100,message = "图标样式1-100个字符!")
    private String icon;

}