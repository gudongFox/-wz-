package com.common.wx.model;

import lombok.Data;

import java.util.List;

@Data
public class MeetingRoomBook {

    private String meetingroom_id;

    private List<MeetingRoomSchedule> schedule;
}
