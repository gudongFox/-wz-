package com.cmcu.mcc.sys.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SysAttach {
    private Integer id;

    private String source;

    private String name;

    private Long size;

    private String md5;

    private String localPath;

    private String creator;

    private Date gmtCreate;

    private Date gmtModified;

    private String remark;


}