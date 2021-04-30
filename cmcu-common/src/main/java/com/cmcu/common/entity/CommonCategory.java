package com.cmcu.common.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class CommonCategory {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="tenetId不能为空!")
    @Size(max=45, message="tenetId长度不能超过45")
    private String tenetId;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="name不能为空!")
    @Size(max=45, message="name长度不能超过45")
    private String name;

    @NotNull(message="顶级为0不能为空!")
    @Max(value=999999999, message="顶级为0必须为数字")
    private Integer parentId;

    @NotNull(message="treeIcon不能为空!")
    @Size(max=45, message="treeIcon长度不能超过45")
    private String treeIcon;

    @NotNull(message="jsIcon不能为空!")
    @Size(max=45, message="jsIcon长度不能超过45")
    private String jsIcon;

    @NotNull(message="seq不能为空!")
    @Max(value=999999999, message="seq必须为数字")
    private Integer seq;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="gmtCreate不能为空!")
    private Date gmtCreate;

    @NotNull(message="gmtModified不能为空!")
    private Date gmtModified;

    @NotNull(message="拥有人:公司、事业部、部门、某某项目、个人不能为空!")
    @Size(max=50, message="拥有人:公司、事业部、部门、某某项目、个人长度不能超过50")
    private String ownerLevel;

    @NotNull(message="ownerName不能为空!")
    @Size(max=45, message="ownerName长度不能超过45")
    private String ownerName;

    @NotNull(message="remark不能为空!")
    @Size(max=450, message="remark长度不能超过450")
    private String remark;
}