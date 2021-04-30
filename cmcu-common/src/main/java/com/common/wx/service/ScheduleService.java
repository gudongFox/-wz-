package com.common.wx.service;

import com.common.model.JsonData;
import com.common.util.JsonMapper;
import com.common.wx.model.Calendar;
import com.common.wx.model.MeetingRoomBook;
import com.common.wx.model.Schedule;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScheduleService {

    public static JsonData add(Schedule schedule){
        JsonData token=BaseService.getAccessToken();
        if(token.getRet()) {
            String url = "https://qyapi.weixin.qq.com/cgi-bin/oa/schedule/add?access_token=" + token.getData().toString();
            Map params = Maps.newHashMap();
            params.put("schedule",schedule);
            JsonData result = BaseService.httpPost(url, JsonMapper.obj2String(params));
            return result;
        }
        return token;
    }

    public static Schedule get(String scheduledId) {
        List<String> schedule_id_list = Lists.newArrayList(scheduledId);
        List<Schedule> schedules = getList(schedule_id_list);
        if (schedules.size() > 0) return schedules.get(0);
        return null;
    }

    public static List<Schedule> getList(List<String> schedule_id_list) {
        JsonData token = BaseService.getAccessToken();
        if (token.getRet()) {
            String url = "https://qyapi.weixin.qq.com/cgi-bin/oa/schedule/get?access_token=" + token.getData().toString();
            Map params = Maps.newHashMap();
            params.put("schedule_id_list", schedule_id_list);
            JsonData response = BaseService.httpPost(url, JsonMapper.obj2String(params));
            if (response.getRet()) {
                Map result = (Map) response.getData();
                List<Schedule> schedules = JsonMapper.string2Obj(JsonMapper.obj2String(result.get("schedule_list")), new TypeReference<ArrayList<Schedule>>() {/**/
                });
                return schedules;
            }

        }
        return Lists.newArrayList();
    }
}
