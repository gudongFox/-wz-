package com.common;


import com.cmcu.common.util.CryptionUtil;
import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

@Data
public class CommonConfig {

    public static boolean OWA = false;

    public static String OWA_SERVER = "https://owa.wuzhou.com.cn/op/embed.aspx?src=https://co.wuzhou.com.cn/";

    public static List<String> OWA_FILE_TYPE= Lists.newArrayList("doc","docx","xls","xlsx","pdf","ppt","pptx");

}
