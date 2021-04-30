package com.common.wx.service;

import com.common.model.JsonData;
import com.common.util.JsonMapper;
import com.common.wx.model.Calendar;
import com.google.common.collect.Maps;

import java.util.Map;

public class CalendarService {


    public static String calId="wc4LMaEAAA571fdmxBcvGry0qGUjSBTg";


    public static JsonData listCalId(){
        JsonData token=BaseService.getAccessToken();
        if(token.getRet()) {
            String url = "https://qyapi.weixin.qq.com/cgi-bin/oa/calendar/get?access_token=" + token.getData().toString();
            Map params = Maps.newHashMap();
            JsonData result = BaseService.httpPost(url, JsonMapper.obj2String(params));
            return result;
        }
        return token;
    }


    public static JsonData add(Calendar calendar){
        JsonData token=BaseService.getAccessToken();
        if(token.getRet()) {
            String url = "https://qyapi.weixin.qq.com/cgi-bin/oa/calendar/add?access_token=" + token.getData().toString();
            Map params = Maps.newHashMap();
            params.put("calendar",calendar);
            JsonData result = BaseService.httpPost(url, JsonMapper.obj2String(params));
            return result;
        }
        return token;
    }


    public static JsonData update(Calendar calendar){
        JsonData token=BaseService.getAccessToken();
        if(token.getRet()) {
            String url = "https://qyapi.weixin.qq.com/cgi-bin/oa/calendar/update?access_token=" + token.getData().toString();
            Map params = Maps.newHashMap();
            params.put("calendar",calendar);
            JsonData result = BaseService.httpPost(url, JsonMapper.obj2String(params));
            return result;
        }
        return token;
    }



}
