package com.cmcu.common.dto;

import lombok.Data;

@Data
public class FastUserDto {

    private String tenetId;

    private String enLogin;

    private String cnName;

    private String wxId;

    private int deptId;

    private String avatar;

    private String signPicUrl;

    private String deptName;

    private boolean deleted;//是否删除
}
