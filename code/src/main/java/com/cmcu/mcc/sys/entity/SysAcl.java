package com.cmcu.mcc.sys.entity;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class SysAcl {
    private Integer id;

    private String code;

    private String name;

    private Integer aclModuleId;

    private String uiSref;

    private String url;

    private String icon;

    private Integer type;

    private Boolean deleted;

    private Integer seq;

    @NotNull
    private String remark;

    private String opts;

}