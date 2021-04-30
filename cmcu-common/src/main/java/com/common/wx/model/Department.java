package com.common.wx.model;


import lombok.Data;

@Data
public class Department {

    private int id;

    private String name;

    private int parentid;

    private int order;
}
