package com.common.wx.service;

import com.common.model.JsonData;
import com.common.util.JsonMapper;
import com.common.wx.model.Department;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DepartmentService {


    /**
     * 获取指定部门及其下的子部门（以及及子部门的子部门等等，递归）。 如果不填，默认获取全量组织架构
     * @param id
     * @return
     */
    public static List<Department> listData(String id) {
        List<Department> departments = Lists.newArrayList();
        JsonData tokenResult = BaseService.getAccessToken(UserService.SECRET);
        if (tokenResult.getRet()) {
            String url = "https://qyapi.weixin.qq.com/cgi-bin/department/list?access_token=" + tokenResult.getData().toString();
            if (StringUtils.isNotEmpty(id)) {
                url = url + "&id=" + id;
            }
            JsonData response = BaseService.httpGet(url);
            if (response.getRet()) {
                Map result = (Map) response.getData();
                departments = JsonMapper.string2Obj(JsonMapper.obj2String(result.get("department")), new TypeReference<ArrayList<Department>>() {/**/
                });
            }
        }
        return departments;
    }

    public static JsonData create(String name, int parentid, int order) {
        JsonData tokenResult = BaseService.getAccessToken(UserService.SECRET);
        if (tokenResult.getRet()) {
            String url = "https://qyapi.weixin.qq.com/cgi-bin/department/create?access_token=" + tokenResult.getData().toString();
            Map data = Maps.newHashMap();
            data.put("name", name);
            data.put("parentid", parentid);
            data.put("order", order);
            JsonData response = BaseService.httpPost(url, JsonMapper.obj2String(data));
            return response;
        }
        return tokenResult;

    }

    public static JsonData update(int id, String name, int parentid, int order) {
        JsonData tokenResult= BaseService.getAccessToken(UserService.SECRET);
        if (tokenResult.getRet()) {
            String url = "https://qyapi.weixin.qq.com/cgi-bin/department/update?access_token=" + tokenResult.getData().toString();
            Map data = Maps.newHashMap();
            data.put("id", id);
            data.put("name", name);
            data.put("parentid", parentid);
            data.put("order", order);
            JsonData response = BaseService.httpPost(url, JsonMapper.obj2String(data));
            return response;
        }
        return tokenResult;

    }

    /**
     * 不能删除根部门；不能删除含有子部门、成员的部门
     * @param id
     * @return
     */
    public static JsonData delete(int id) {
        JsonData tokenResult = BaseService.getAccessToken(UserService.SECRET);
        if (tokenResult.getRet()) {
            String url = "https://qyapi.weixin.qq.com/cgi-bin/department/delete?access_token=" + tokenResult.getData().toString();
            Map data = Maps.newHashMap();
            data.put("id", id);
            JsonData response = BaseService.httpPost(url, JsonMapper.obj2String(data));
            return response;
        }
        return tokenResult;

    }

}
