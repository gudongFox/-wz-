package com.common.wx.service;

import com.common.model.JsonData;
import com.common.util.JsonMapper;
import com.common.wx.model.MeetingRoom;
import com.common.wx.model.MeetingRoomBook;
import com.common.wx.model.MeetingRoomSchedule;
import com.common.wx.model.UserInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.util.*;

public class MeetingRoomService {

    public static String SECRET="DmBozy7YnVD9mdoBqGnRK_v24reC5jFDAJEHrSelhX4";

    /**
     * 查询会议室
     * @return
     */
    public static List<MeetingRoom> listData() {
        List<MeetingRoom> meetingRooms = Lists.newArrayList();
        JsonData tokenResult = BaseService.getAccessToken(SECRET);
        if (tokenResult.getRet()) {
            String url = "https://qyapi.weixin.qq.com/cgi-bin/oa/meetingroom/list?access_token=" + tokenResult.getData().toString();
            JsonData response = BaseService.httpGet(url);
            if (response.getRet()) {
                Map result = (Map) response.getData();
                meetingRooms = JsonMapper.string2Obj(JsonMapper.obj2String(result.get("meetingroom_list")), new TypeReference<ArrayList<MeetingRoom>>() {/**/
                });
                meetingRooms.forEach(MeetingRoom::buildEquipmentNames);
            }
        }
        return meetingRooms;
    }

    public static JsonData delete(String meetingroom_id) {
        JsonData tokenResult = BaseService.getAccessToken(SECRET);
        if (tokenResult.getRet()) {
            String url = "https://qyapi.weixin.qq.com/cgi-bin/oa/meetingroom/del?access_token=" + tokenResult.getData().toString();
            Map data = Maps.newHashMap();
            data.put("meetingroom_id", meetingroom_id);
            JsonData response = BaseService.httpPost(url, JsonMapper.obj2String(data));
            return response;
        }
        return tokenResult;

    }

    public static  JsonData save(MeetingRoom item){
        JsonData tokenResult = BaseService.getAccessToken(SECRET);
        if (tokenResult.getRet()) {
            String token=tokenResult.getData().toString();
            String url = "https://qyapi.weixin.qq.com/cgi-bin/oa/meetingroom/add?access_token=" +token ;
            Map data = Maps.newHashMap();
            data.put("name",item.getName());
            data.put("capacity",item.getCapacity());
            if(StringUtils.isNotEmpty(item.getCity())) data.put("city",item.getCity());
            if(StringUtils.isNotEmpty(item.getBuilding())) data.put("building",item.getBuilding());
            if(StringUtils.isNotEmpty(item.getFloor())) data.put("floor",item.getFloor());
            if(StringUtils.isNotEmpty(item.getEquipmentNames())){
                item.buildEquipmentId();
                data.put("equipment",item.getEquipment());
            }
            if(StringUtils.isNotEmpty(item.getMeetingroom_id())){
                url = "https://qyapi.weixin.qq.com/cgi-bin/oa/meetingroom/edit?access_token=" +token ;
                data.put("meetingroom_id",item.getMeetingroom_id());
            }
            JsonData response = BaseService.httpPost(url, JsonMapper.obj2String(data));
            return response;
        }
        return tokenResult;
    }

    public static MeetingRoom getModelById(String meetingroom_id) {
        List<MeetingRoom> list = listData();
        Optional<MeetingRoom> optionalMeetingRoom = list.stream().filter(p -> p.getMeetingroom_id().equalsIgnoreCase(meetingroom_id)).findFirst();
        return optionalMeetingRoom.orElse(null);
    }


    /**
     * 会议室预定查询
     * @param meetingroom_id
     * @param startDate
     * @return
     */
    public static List<MeetingRoomSchedule> listBookData(String meetingroom_id,String startDate) {
        if(StringUtils.isEmpty(startDate)){
            startDate=DateFormatUtils.format(new Date(),"yyyy-MM-dd");
        }
        List<MeetingRoomSchedule> list = Lists.newArrayList();
        List<MeetingRoom> meetingRooms=listData();
        JsonData tokenResult = BaseService.getAccessToken(SECRET);
        if (tokenResult.getRet()) {
            String url = "https://qyapi.weixin.qq.com/cgi-bin/oa/meetingroom/get_booking_info?access_token=" + tokenResult.getData().toString();
            Map params = Maps.newHashMap();
            if (StringUtils.isNotEmpty(startDate)) {
                try {
                    Date startTime = DateUtils.parseDate(startDate, "yyyy-MM-dd HH:mm", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd");
                    startTime = DateUtils.parseDate(DateFormatUtils.format(startTime, "yyyy-MM-dd") + " 08:00:01", "yyyy-MM-dd HH:mm:ss");
                    Date endTime = DateUtils.parseDate(DateFormatUtils.format(startTime, "yyyy-MM-dd") + " 22:59:59", "yyyy-MM-dd HH:mm:ss");
                    params.put("start_time", startTime.getTime() / 1000);
                    params.put("end_time", endTime.getTime() / 1000);

                } catch (Exception ex) {

                }
            }
            for(MeetingRoom meetingRoom:meetingRooms) {
                if(StringUtils.isEmpty(meetingroom_id)||meetingRoom.getMeetingroom_id().equalsIgnoreCase(meetingroom_id)) {
                    params.put("meetingroom_id", meetingRoom.getMeetingroom_id());
                    JsonData response = BaseService.httpPost(url, JsonMapper.obj2String(params));
                    if (response.getRet()) {
                        Map result = (Map) response.getData();
                        List<MeetingRoomBook> meetingRoomBooks = JsonMapper.string2Obj(JsonMapper.obj2String(result.get("booking_list")), new TypeReference<ArrayList<MeetingRoomBook>>() {/**/
                        });

                        for (MeetingRoomBook meetingRoomBook : meetingRoomBooks) {
                            meetingRoomBook.getSchedule().forEach(s -> {
                                s.setStartTime(new Date(s.getStart_time()*1000));
                                s.setEndTime(new Date(s.getEnd_time()*1000));
                                UserInfo userInfo = UserService.getUserInfo(s.getBooker());
                                s.setBookUserInfo(userInfo);
                                s.setMeetingRoom(meetingRoom);
                            });
                            list.addAll(meetingRoomBook.getSchedule());
                        }
                    }
                }
            }
        }
        return list;
    }






    public static JsonData book(String subject,String meetingroom_id, Date start_time, Date end_time, String booker, List<String> attendees) {
        JsonData tokenResult = BaseService.getAccessToken(SECRET);
        if (tokenResult.getRet()) {
            String url = "https://qyapi.weixin.qq.com/cgi-bin/oa/meetingroom/book?debug=1&access_token=" + tokenResult.getData().toString();
            Map params = Maps.newHashMap();
            params.put("subject",subject);
            params.put("meetingroom_id", meetingroom_id);
            params.put("start_time", start_time.getTime() / 1000);
            params.put("end_time", end_time.getTime() / 1000);
            params.put("booker", booker);
            //params.put("attendees", attendees);
            JsonData response = BaseService.httpPost(url, JsonMapper.obj2String(params));
            return response;
        }
        return tokenResult;
    }


    public static JsonData cancelBook(String meeting_id) {
        List<MeetingRoomBook> meetingRooms = Lists.newArrayList();
        JsonData tokenResult = BaseService.getAccessToken(SECRET);
        if (tokenResult.getRet()) {
            String url = "https://qyapi.weixin.qq.com/cgi-bin/oa/meetingroom/cancel_book?access_token=" + tokenResult.getData().toString();
            Map params= Maps.newHashMap();
            params.put("meeting_id",meeting_id);
            params.put("keep_schedule","0");
            JsonData response = BaseService.httpPost(url,JsonMapper.obj2String(params));
            return response;
        }
        return tokenResult;
    }

}
