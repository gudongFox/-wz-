package com.common.wx.model;

import lombok.Data;

import java.util.List;


@Data
public class User {

    private String userid;

    private String name;

    private List<Integer> department;
}
