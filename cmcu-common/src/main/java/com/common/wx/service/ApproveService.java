package com.common.wx.service;


import com.common.model.JsonData;
import com.common.util.JsonMapper;
import com.common.wx.model.Approve;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ApproveService {

    public static String testTemplateId="3TmB9d6F6xhz896XUgW9UnTka1GiFkxiRTuYqUJV";


    public static String SECRET = "8krlT2tO5Yamc2CV3hgfpdSZ3zINAP9QSgVfrsPTunw";

    public static Integer AgentId = 3010040;

    public static JsonData getTemplateDetail(String templateId){
        JsonData token=BaseService.getAccessToken(SECRET);
        if(token.getRet()) {
            String url = "https://qyapi.weixin.qq.com/cgi-bin/oa/gettemplatedetail?access_token=" + token.getData().toString();
            Map params = Maps.newHashMap();
            params.put("template_id", templateId);
            JsonData result = BaseService.httpPost(url, JsonMapper.obj2String(params));
            return result;
        }
        return token;
    }

    public static JsonData listSpNo(String templateId, Date start, Date end){
        JsonData token=BaseService.getAccessToken(SECRET);
        if(token.getRet()) {
            String url = "https://qyapi.weixin.qq.com/cgi-bin/oa/getapprovalinfo?access_token=" + token.getData().toString();
            Map params = Maps.newHashMap();
            params.put("starttime",start.getTime()/1000);
            params.put("endtime",end.getTime()/1000);
            params.put("cursor",0);
            params.put("size",100);
            List<Map> filters= Lists.newArrayList();
            params.put("filters",filters);
            Map filter=Maps.newHashMap();
            filter.put("key","template_id");
            filter.put("value",templateId);
            filters.add(filter);
            JsonData result = BaseService.httpPost(url, JsonMapper.obj2String(params));
            return result;
        }
        return token;


    }

    public static Approve getApproveDetail(String spNo){
        JsonData token=BaseService.getAccessToken(SECRET);
        if(token.getRet()) {
            String url = "https://qyapi.weixin.qq.com/cgi-bin/oa/getapprovaldetail?access_token=" + token.getData().toString();
            Map params = Maps.newHashMap();
            params.put("sp_no",spNo);
            JsonData result = BaseService.httpPost(url, JsonMapper.obj2String(params));
            if(result.getRet()){

                Map info= (Map) ((Map)result.getData()).get("info");
                Approve item=new Approve();
                item.setSp_no(info.get("sp_no").toString());
                item.setSp_name(info.get("sp_name").toString());
                item.setSp_status((Integer) info.get("sp_status"));
                item.setTemplate_id(info.get("template_id").toString());
                item.setApply_time((Integer) info.get("apply_time"));
                item.setApplyer((LinkedHashMap) info.get("applyer"));
                item.setSp_record((List<LinkedHashMap>) info.get("sp_record"));
                item.setNotifyer((List<LinkedHashMap>) info.get("notifyer"));
                item.setApply_data((LinkedHashMap) info.get("apply_data"));
                item.setComments((List<LinkedHashMap>) info.get("comments"));
                return  item;
            }
        }
        return null;
    }

}
