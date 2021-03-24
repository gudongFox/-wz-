package com.cmcu.common.util;

import java.text.DecimalFormat;

public  class  MyFormNo {


    public static String getFormNo(String key,int id){
        String stringToday = MyDateUtil.getStringToday1();
        DecimalFormat mFormat = new DecimalFormat("000");
        String format = mFormat.format(id);
        return stringToday+key+format;
    }




}
