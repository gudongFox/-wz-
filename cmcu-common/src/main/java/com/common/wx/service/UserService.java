package com.common.wx.service;

import com.common.model.JsonData;
import com.common.util.JsonMapper;
import com.common.wx.model.User;
import com.common.wx.model.UserInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserService {

    public static String SECRET = "";

    /**
     * 获取部门成员
     *
     * @param departmentid
     * @return
     */
    public static List<User> listData(int departmentid) {
        List<User> users = Lists.newArrayList();
        JsonData tokenResult = BaseService.getAccessToken(SECRET);
        if (tokenResult.getRet()) {
            String url = "https://qyapi.weixin.qq.com/cgi-bin/user/simplelist?access_token=" + tokenResult.getData().toString() + "&department_id=" + departmentid + "&fetch_child=0";
            JsonData response = BaseService.httpGet(url);
            if (response.getRet()) {
                Map result = (Map) response.getData();
                users = JsonMapper.string2Obj(JsonMapper.obj2String(result.get("userlist")), new TypeReference<ArrayList<User>>() {/**/
                });
            }
        }
        return users;
    }

    public static UserInfo getUserInfo(String userid) {
        JsonData tokenResult = BaseService.getAccessToken(SECRET);
        if (tokenResult.getRet()) {
            String url = "https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token=" + tokenResult.getData().toString() + "&userid=" + userid;
            JsonData response = BaseService.httpGet(url);
            if (response.getRet()) {
                Map result = (Map) response.getData();
                return JsonMapper.string2Obj(JsonMapper.obj2String(result), new TypeReference<UserInfo>() {
                });
            }
        }
        return null;
    }


    public static Map getUserIdByCode(String code) {
        JsonData tokenResult = BaseService.getAccessToken(SECRET);
        if (tokenResult.getRet()) {
            String url = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=" + tokenResult.getData().toString() + "&code=" + code;
            JsonData response = BaseService.httpGet(url);
            if (response.getRet()) {
                Map result = (Map) response.getData();
                return result;
            }
        }
        return null;
    }



}
