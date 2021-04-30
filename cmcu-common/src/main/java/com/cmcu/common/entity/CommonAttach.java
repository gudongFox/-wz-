package com.cmcu.common.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class CommonAttach {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="name不能为空!")
    @Size(max=145, message="name长度不能超过145")
        private String name;

    @NotNull(message="size不能为空!")
    @Max(value=999999999, message="size必须为数字")
    private Long size;

    @NotNull(message="sizeName不能为空!")
    @Size(max=145, message="sizeName长度不能超过145")
    private String sizeName;

    @NotNull(message="extensionName不能为空!")
    @Size(max=45, message="extensionName长度不能超过45")
    private String extensionName;

    @NotNull(message="md5不能为空!")
    @Size(max=145, message="md5长度不能超过145")
    private String md5;

    @NotNull(message="localPath不能为空!")
    @Size(max=245, message="localPath长度不能超过245")
    private String localPath;

    @NotNull(message="creator不能为空!")
    @Size(max=45, message="creator长度不能超过45")
    private String creator;

    @NotNull(message="gmtCreate不能为空!")
    private Date gmtCreate;

    @NotNull(message="gmtModified不能为空!")
    private Date gmtModified;

    @NotNull(message="remark不能为空!")
    @Size(max=450, message="remark长度不能超过450")
    private String remark;
}