package com.common.wx.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;

@Data
public class Calendar {

    public  Calendar(){
        this.shares= Lists.newArrayList();
    }


    private String cal_id;

    @NotEmpty
    private String organizer;

    private int readOnly;

    private int set_as_default;

    @NotEmpty
    @Size(max = 128)
    private String summary;

    @NotEmpty
    private String color;


    @Size(max = 512)
    private String description;

    /**
     * userId readonly 最多2000人
     */
    private List<Map> shares;


    public void addShares(List<String> userIds){
        for(String userid:userIds) {
            Map user = Maps.newHashMap();
            user.put("userid", userid);
            user.put("readonly", 1);
            shares.add(user);
        }
    }

}
