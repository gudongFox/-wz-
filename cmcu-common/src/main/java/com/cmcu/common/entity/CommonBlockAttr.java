package com.cmcu.common.entity;

import javax.validation.constraints.*;
import lombok.Data;

@Data
public class CommonBlockAttr {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="blockId不能为空!")
    @Max(value=999999999, message="blockId必须为数字")
    private Integer blockId;

    @NotNull(message="attrName不能为空!")
    @Size(max=45, message="attrName长度不能超过45")
    private String attrName;

    @NotNull(message="attrValue不能为空!")
    @Size(max=45, message="attrValue长度不能超过45")
    private String attrValue;

    @NotNull(message="unitType不能为空!")
    @Size(max=45, message="unitType长度不能超过45")
    private String unitType;

    @NotNull(message="attrType不能为空!")
    @Size(max=45, message="attrType长度不能超过45")
    private String attrType;

    @NotNull(message="seq不能为空!")
    @Max(value=999999999, message="seq必须为数字")
    private Integer seq;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="remark不能为空!")
    @Size(max=45, message="remark长度不能超过45")
    private String remark;
}