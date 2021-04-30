package com.cmcu.common.dto;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;
import org.springframework.security.web.PortResolverImpl;

import java.util.List;
import java.util.Map;

@Data
public class UserDto extends FastUserDto {

    public UserDto() {
        extendProperty = Maps.newHashMap();
        qualifyList= Lists.newArrayList();
        roleIdList=Lists.newArrayList();
        positionIdList=Lists.newArrayList();
    }

    private int id;

    private String userNo;

    private String logoGram;

    private String cnMobile;

    private String cnMajor;

    private String cnQualify;


    private String encryptPwd;


    private String signUrl;

    private String signPicUrl;

    private String orderMethod;

    private List<String> roleIdList;

    private List<String> roleNameList;

    private List<String> positionIdList;

    private List<String> qualifyList;

    private Map extendProperty;

    private boolean selected;

    private String userStatus;//员工状态



}
