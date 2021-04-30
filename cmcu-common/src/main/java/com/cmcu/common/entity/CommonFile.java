package com.cmcu.common.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class CommonFile {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @NotNull(message="businessKey不能为空!")
    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @NotNull(message="默认可为0不能为空!")
    @Max(value=999999999, message="默认可为0必须为数字")
    private Integer dirId;

    @NotNull(message="fileType不能为空!")
    @Size(max=45, message="fileType长度不能超过45")
    private String fileType;

    @NotNull(message="fileName不能为空!")
    @Size(max=145, message="fileName长度不能超过145")
    private String fileName;

    @NotNull(message="fileProperty不能为空!")
    @Size(max=2450, message="fileProperty长度不能超过2450")
    private String fileProperty;

    @NotNull(message="attachId不能为空!")
    @Max(value=999999999, message="attachId必须为数字")
    private Integer attachId;

    @NotNull(message="如果是电子传递后的文件,则说明来源file_id不能为空!")
    @Max(value=999999999, message="如果是电子传递后的文件,则说明来源file_id必须为数字")
    private Integer sourceId;


    @NotNull(message="图层提取模板Id")
    @Max(value=999999999, message="图层提取模板Id")
    private Integer extractId;

    @NotNull(message="seq不能为空!")
    @Max(value=999999999, message="seq必须为数字")
    private Integer seq;

    @NotNull(message="isLocked不能为空!")
    private Boolean locked;

    @NotNull(message="lockedLogin不能为空!")
    @Size(max=45, message="lockedLogin长度不能超过45")
    private String lockedLogin;

    @NotNull(message="lockedName不能为空!")
    @Size(max=45, message="lockedName长度不能超过45")
    private String lockedName;

    @NotNull(message="creator不能为空!")
    @Size(max=45, message="creator长度不能超过45")
    private String creator;

    @NotNull(message="creatorName不能为空!")
    @Size(max=45, message="creatorName长度不能超过45")
    private String creatorName;

    @NotNull(message="历史记录列表不能为空!")
    @Size(max=2450, message="历史记录列表长度不能超过2450")
    private String attachIdList;

    @NotNull(message="isDeleted不能为空!")
    private Boolean deleted;

    @NotNull(message="gmtCreate不能为空!")
    private Date gmtCreate;

    @NotNull(message="gmtModified不能为空!")
    private Date gmtModified;

    @NotNull(message="remark不能为空!")
    @Size(max=450, message="remark长度不能超过450")
    private String remark;
}