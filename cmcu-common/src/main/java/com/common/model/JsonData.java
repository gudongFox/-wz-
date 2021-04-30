package com.common.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class JsonData {

    private Boolean ret;

    private String msg;

    private Object data;

    private Long time;

    private String url;

    public JsonData(boolean ret) {
        this.ret = ret;
        this.time=System.currentTimeMillis();
        this.msg=ret?"success":"error";
    }

    public static JsonData success(Object object, String msg) {
        JsonData jsonData = new JsonData(true);
        jsonData.data = object;
        jsonData.msg = msg;
        return jsonData;
    }

    public static JsonData success(Object object) {
        JsonData jsonData = new JsonData(true);
        jsonData.data = object;
        return jsonData;
    }

    public static JsonData success() {
        return new JsonData(true);
    }

    public static JsonData fail(String msg) {
        JsonData jsonData = new JsonData(false);
        jsonData.msg = msg;
        jsonData.data="";
        return jsonData;
    }


    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("ret", ret);
        result.put("msg", msg);
        result.put("data", data);
        result.put("time",time);
        return result;
    }


}
