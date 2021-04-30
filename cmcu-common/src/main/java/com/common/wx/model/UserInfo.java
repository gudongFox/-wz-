package com.common.wx.model;

import lombok.Data;

import java.util.List;

@Data
public class UserInfo extends User {


    /**
     * 部门内的排序值，默认为0。数量必须和department一致，数值越大排序越前面。
     */
    private List<Integer> order;


    private int gender;

    private String email;

    private String mobile;

    private String position;

    /**
     * 表示在所在的部门内是否为上级
     */
    private List<Integer> is_leader_in_dept;

    private String avatar;

    private String thumb_avatar;

    private String telephone;


    private String alias;

    /**
     * 激活状态: 1=已激活，2=已禁用，4=未激活，5=退出企业。
     */
    private int status;

    private String external_position;

    private String address;

    private int main_department;


}
