package com.common.wx.model;

import lombok.Data;

import java.util.Date;

@Data
public class MeetingRoomSchedule {

    private String meeting_id;

    private String schedule_id;

    private long start_time;

    private long end_time;

    private String booker;


    private Date startTime;

    private Date endTime;

    private UserInfo bookUserInfo;

    private MeetingRoom meetingRoom;

}
