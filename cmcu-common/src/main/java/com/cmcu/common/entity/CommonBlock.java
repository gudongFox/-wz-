package com.cmcu.common.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class CommonBlock {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="tenetId不能为空!")
    @Size(max=45, message="tenetId长度不能超过45")
    private String tenetId;

    @NotNull(message="categoryId不能为空!")
    @Max(value=999999999, message="categoryId必须为数字")
    private Integer categoryId;

    @NotNull(message="fileName不能为空!")
    @Size(max=45, message="fileName长度不能超过45")
    private String fileName;

    @NotNull(message="attachId不能为空!")
    @Max(value=999999999, message="attachId必须为数字")
    private Integer attachId;

    @NotNull(message="thumbId不能为空!")
    @Max(value=999999999, message="thumbId必须为数字")
    private Integer thumbId;

    @NotNull(message="ownerName不能为空!")
    @Size(max=45, message="ownerName长度不能超过45")
    private String ownerName;

    @NotNull(message="不能为空!")
    @Size(max=45, message="长度不能超过45")
    private String owner;


    @NotNull(message="1不能为空!")
    @Size(max=45, message="1长度不能超过45")
    private String ownerLevel;

    @NotNull(message="minVersion不能为空!")
    @Max(value=999999999, message="minVersion必须为数字")
    private Integer minVersion;

    @NotNull(message="maxVersion不能为空!")
    @Max(value=999999999, message="maxVersion必须为数字")
    private Integer maxVersion;

    @NotNull(message="insertLayer不能为空!")
    @Size(max=45, message="insertLayer长度不能超过45")
    private String insertLayer;

    @NotNull(message="otherData不能为空!")
    @Size(max=450, message="otherData长度不能超过450")
    private String otherData;

    @NotNull(message="seq不能为空!")
    @Max(value=999999999, message="seq必须为数字")
    private Integer seq;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="creator不能为空!")
    @Size(max=45, message="creator长度不能超过45")
    private String creator;

    @NotNull(message="creatorName不能为空!")
    @Size(max=45, message="creatorName长度不能超过45")
    private String creatorName;

    @NotNull(message="gmtCreate不能为空!")
    private Date gmtCreate;

    @NotNull(message="gmtModified不能为空!")
    private Date gmtModified;

    @NotNull(message="remark不能为空!")
    @Size(max=450, message="remark长度不能超过450")
    private String remark;
}