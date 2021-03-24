package com.cmcu.mcc.five.entity;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FiveContentFile {
    @NotNull(message="id不能为空!")
    @Max(value=999999999, message="id必须为数字")
    private Integer id;

    @Size(max=45, message="businessKey长度不能超过45")
    private String businessKey;

    @Size(max=25, message="表名长度不能超过25")
    private String tableName;

    @Max(value=999999999, message="表id必须为数字")
    private Integer tableKey;

    @Max(value=999999999, message="文件类型 0 正文  1红头必须为数字")
    private Integer fileType;

    @Size(max=255, message="文件名称长度不能超过255")
    private String fileName;

    @Size(max=145, message="存储地址长度不能超过145")
    private String localPath;

    @Max(value=999999999, message="seq必须为数字")
    private Integer seq;

    private Boolean delete;

    @Size(max=50, message="creator长度不能超过50")
    private String creator;

    @Size(max=50, message="creatorName长度不能超过50")
    private String creatorName;

    private Date gmtCreate;

    @Size(max=50, message="文件大小长度不能超过50")
    private String size;

    @Size(max=255, message="remark长度不能超过255")
    private String remark;

    @Size(max=45, message="红头名称长度不能超过45")
    private String redName;
}