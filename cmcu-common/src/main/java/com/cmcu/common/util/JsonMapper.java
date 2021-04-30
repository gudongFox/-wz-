package com.cmcu.common.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Roy
 * Time: 2018/8/24 15:52
 * To change this template use File | Settings | File Templates.
 */
@Slf4j
public class JsonMapper {

    public static ObjectMapper objectMapper = new ObjectMapper();
    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
        objectMapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        objectMapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
            @Override
            public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeString("");
            }
        });
    }

    public static <T> String obj2String(T src) {
        if (src == null) {
            return null;
        }
        try {
            return src instanceof String ? (String) src : objectMapper.writeValueAsString(src);
        } catch (Exception e) {
            log.warn("parse object to String exception, error:{}", e);
            return null;
        }
    }

    public static <T> T string2Obj(String src, TypeReference<T> typeReference) {
        if (src == null || typeReference == null) {
            return null;
        }
        try {
            return (T) (typeReference.getType().equals(String.class) ? src : objectMapper.readValue(src, typeReference));
        } catch (Exception e) {
            log.warn("parse String to Object exception, String:{}, TypeReference<T>:{}, error:{}", src, typeReference.getType(), e);
            return null;
        }
    }


    public static Map<String,Object> string2Map(String src){
        if (src == null) {
            return null;
        }
        try {
            return objectMapper.readValue(src, new TypeReference<Map<String,Object>>(){});
        } catch (Exception e) {
            log.warn("parse String to Map exception, String:{},error:{}", src, e);
            return null;
        }
    }

    public static List<Map<String,Object>> string2ListMap(Object src){
        if (src == null) {
            return null;
        }
        try {
            if(!(src instanceof String)){
                return objectMapper.readValue(obj2String(src), new TypeReference<List<Map<String,Object>>>(){});
            }
            return objectMapper.readValue(src.toString(), new TypeReference<List<Map<String,Object>>>(){});
        } catch (Exception e) {
            log.warn("parse String to Map exception, String:{},error:{}", src, e);
            return null;
        }
    }



    public static List<String> getListValue(Map data,String key){
        List<String> result= Lists.newArrayList();
        try{
            if(data!=null&&data.containsKey(key)){
                Object value=data.get(key);
                if(value!=null){
                    if(value instanceof List){
                        return (List<String>)value;
                    }else{
                        if(value.toString().startsWith("[")&&value.toString().endsWith("]")){
                            value=value.toString().substring(1,value.toString().length()-1);
                        }
                        return MyStringUtil.getStringList(value.toString());
                    }
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return result;
    }
}
