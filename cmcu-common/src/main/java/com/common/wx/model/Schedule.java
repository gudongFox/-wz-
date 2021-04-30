package com.common.wx.model;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Schedule {

    public Schedule(){
        this.attendees= Lists.newArrayList();
    }

    private String organizer;

    private long start_time;

    private long end_time;

    private String userid;

    private String summary;

    private String description;

    private String location;

    private List<Map> attendees;



    public void addAttendees(List<String> userIds){
        for(String userid:userIds) {
            Map user = Maps.newHashMap();
            user.put("userid",userid);
            attendees.add(user);
        }
    }

}
