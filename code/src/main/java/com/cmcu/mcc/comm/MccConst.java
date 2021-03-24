package com.cmcu.mcc.comm;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: Roy
 * Time: 2019/2/25 15:35
 * To change this template use File | Settings | File Templates.
 */
@Component
@Getter
@Setter
public class MccConst {


    public static String DEFAULT_URL="";
    public static String APP_CODE="wuzhou";


    public final static String EN_LOGIN = "enLogin";
    public final static String CN_NAME = "cnName";
    public final static String USER_PWD = "userPwd";
    public final static String DEFAULT_PWD = "1234";
    public final static String CP_SOURCE="cp";



    @Value("${customerCode}")
    private String customerCode;

    @Value("${customerName}")
    private String customerName;

    @Value("${dirPath}")
    private String dirPath;

    @Value("${defaultPwd}")
    private String defaultPwd;

}
