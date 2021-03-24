package com.cmcu.mcc.sys.entity;


import com.cmcu.common.annotation.FieldName;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class SysRole {

    private Integer id;

    private String roleCategory;

    private String name;

    private Boolean deleted;

    private Integer seq;

    private String remark;


    @FieldName("数据权限")
    @NotNull
    @Size(max=450)
    private String childDeptIds;
}