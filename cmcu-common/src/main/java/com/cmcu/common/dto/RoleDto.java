package com.cmcu.common.dto;

import lombok.Data;

@Data
public class RoleDto {

    private String name;

    private String id;

    private int userCount;

    private String childDeptIdList;
}
