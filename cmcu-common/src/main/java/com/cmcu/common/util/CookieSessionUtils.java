package com.cmcu.common.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 * User: Roy
 * Time: 2018/11/29 14:28
 * To change this template use File | Settings | File Templates.
 */
public class CookieSessionUtils {


    public static String getCookie(String name){
        HttpServletRequest request=getRequest();
        if(request.getCookies()!=null) {
            Optional<Cookie> cookie = Arrays.stream(request.getCookies()).filter(p -> p.getName().equals(name)).findFirst();
            if (cookie.isPresent()) {
                return cookie.get().getValue();
            }
        }
        return "";
    }

    public static void addCookie(String name,String value){
        HttpServletResponse response=getResponse();
        Cookie cookie=new Cookie(name,value);
        cookie.setMaxAge(7*24*60*60);
        cookie.setPath("/");
        cookie.setSecure(true);
        response.addCookie(cookie);
    }

    public static void addCookie(String name,String value,int seconds){
        HttpServletResponse response=getResponse();
        Cookie cookie=new Cookie(name,value);
        cookie.setMaxAge(seconds);
        cookie.setPath("/");
        cookie.setSecure(true);
        response.addCookie(cookie);
    }


    public static void deleteCookie(String name){
        HttpServletResponse response=getResponse();
        Cookie cookie = new Cookie(name,null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }


    public static void addSession(String name,Object value){
        HttpServletRequest request=getRequest();
        request.getSession().setAttribute(name,value);
    }

    public static String getSession(String name){
        HttpServletRequest request=getRequest();
        Object value= request.getSession().getAttribute(name);
        if(value!=null) return value.toString();
        return "";
    }

    public static  void deleteSession(String name){
        HttpServletRequest request=getRequest();
        request.getSession().removeAttribute(name);
    }



    public static void addSessionCookie(String name,String value) {
        addSession(name, value);
        addCookie(name, value);
    }

    public static void deleteSessionCookie(String name){
        deleteSession(name);
        deleteCookie(name);
    }


    public static HttpServletRequest getRequest(){
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return  req;
    }


    public static HttpServletResponse getResponse(){
        HttpServletResponse resp =((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        return resp;
    }


}
