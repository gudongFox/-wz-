package com.cmcu.common.web;

import javax.servlet.http.HttpServletRequest;

public class RequestHolder {

    private static final ThreadLocal<String> userNameHolder = new ThreadLocal<>();

    private static final ThreadLocal<String> userLoginHolder = new ThreadLocal<>();

    private static final ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<>();

    public static void addUserLogin(String userLogin) {
        userLoginHolder.set(userLogin);
    }

    public static String getUserLogin() {
        return userLoginHolder.get();
    }


    public static void addUserName(String userName) {
        userNameHolder.set(userName);
    }



    public static String getUserName() {
        return userNameHolder.get();
    }



    public static void add(HttpServletRequest request) {
        requestHolder.set(request);
    }
    public static HttpServletRequest getCurrentRequest() {
        return requestHolder.get();
    }

    public static void remove() {
        userLoginHolder.remove();
        userNameHolder.remove();
        requestHolder.remove();
    }
}