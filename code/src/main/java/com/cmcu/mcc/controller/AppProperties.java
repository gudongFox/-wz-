package com.cmcu.mcc.controller;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
@Data
public class AppProperties {


    @Value("${appCode}")
    private String appCode;

    @Value("${customerName}")
    private String customerName;

    @Value("${dirPath}")
    private String dirPath;

    @Value("${defaultPwd}")
    private String defaultPwd;
}
