package com.cmcu.common.dto;

import com.cmcu.common.util.JsonMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class InputConfigDto {

    private String projectType;

    private boolean underTip;

    //重要信息字段
    private boolean keyInfo;

    /**
     * 创建时候增加默认值
     */
    private boolean addDefaultValue;

    private Map headStyle;

    private Map contentStyle;

    private int rows;

    private String dateFormat;
    //值*格式化小数位数
    private int dotSize;
    //值*倍数
    private float multipleTimes;


    public InputConfigDto() {
        this.underTip = false;
        this.headStyle = Maps.newHashMap();
        this.contentStyle = Maps.newHashMap();
        this.rows = 2;
        this.addDefaultValue = true;
        this.dotSize = -1;
        this.multipleTimes=1;
        this.keyInfo=false;
    }

    public static InputConfigDto getInstance(String configData){
        InputConfigDto config=JsonMapper.string2Obj(configData,new TypeReference<InputConfigDto>() {});
        if(config==null){
            config=new InputConfigDto();
        }
        return config;
    }

}
