package com.cmcu.common.entity;

import java.util.Date;
import javax.validation.constraints.*;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class CommonCode {

    public CommonCode() {
    }

    public CommonCode(String code,String name) {
        this.setCode(code);
        this.setName(name);
        this.setParentId(0);
    }

    public CommonCode(String code,String name,int seq) {
        this.setCode(code);
        this.setName(name);
        this.setSeq(seq);
    }

    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;




    @NotNull(message="parentId不能为空!")
    @Max(value=999999999, message="parentId必须为数字")
    private Integer parentId;

    @NotNull(message="公司代码不能为空!")
    @Size(max=45, message="公司代码长度不能超过45")
    private String tenetId;

    @NotNull(message="name不能为空!")
    @Size(max=450, message="name长度不能超过450")
    private String name;

    @NotNull(message="code不能为空!")
    @Size(max=450, message="code长度不能超过450")
    private String code;

    @NotNull(message="codeCatalog不能为空!")
    @Size(max=45, message="codeCatalog长度不能超过45")
    private String codeCatalog;

    @NotNull(message="String,Integer不能为空!")
    @Size(max=45, message="String,Integer长度不能超过45")
    private String codeType;

    @NotNull(message="seq不能为空!")
    @Max(value=999999999, message="seq必须为数字")
    private Integer seq;

    @NotNull(message="isDefaulted不能为空!")
    private Boolean defaulted;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="gmtCreate不能为空!")
    private Date gmtCreate;

    @NotNull(message="gmtModified不能为空!")
    private Date gmtModified;

    @NotNull(message="remark不能为空!")
    @Size(max=145, message="remark长度不能超过145")
    private String remark;
}