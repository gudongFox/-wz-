package com.cmcu.common.dto;


import com.cmcu.common.entity.CommonCode;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Map;

@Data
public class CommonCodeDto extends CommonCode {


    private Map config;

    private Object codeValue;


    public CommonCodeDto(){

    }

    public CommonCodeDto(String value){
        setCode(value);
        setName(value);
        setDefaulted(false);
    }

    public CommonCodeDto(String code, String name){
        setCode(code);
        setName(name);
        setCodeValue(code);
        setDefaulted(false);
    }

    public CommonCodeDto(String code, String name,String codeCatalog){
        setCode(code);
        setName(name);
        setCodeCatalog(codeCatalog);
        setDefaulted(false);
    }

    public CommonCodeDto(String code, String name,Map config){
        setCode(code);
        setName(name);
        setDefaulted(false);
        setConfig(config);
    }


    public static CommonCodeDto adapt(CommonCode item){
        CommonCodeDto dto=new CommonCodeDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }
}
