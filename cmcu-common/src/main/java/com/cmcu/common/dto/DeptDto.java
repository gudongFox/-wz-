package com.cmcu.common.dto;

import lombok.Data;

import java.util.List;

@Data
public class DeptDto {

    private int id;

    private int parentId;

    //二级部门Id
    private int secondDeptId;

    //二级部门正职
    private List<String> secondLeaderMen;

    //二级部门副职
    private List<String> secondViceLeaderMen;

    //二级单位分管领导
    private List<String> secondChargeMen;

    //自己部门正职
    private List<String> deptChargeMen;

    //自己部门副职
    private List<String> deptViceChargeMen;

    //自己部门分管领导
    private List<String> deptDivisionMen;


    private String name;




    //根节点为0
    private int level;

    private long userCount;

    private long deptCount;

    private String deptType;//职能 设计部门分类
}
