package com.cmcu.common.dto;

import com.cmcu.common.entity.CommonEdArrangeUser;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;


import java.util.List;
import java.util.Map;


@Getter
@Setter
public class CommonEdArrangeDto {

    private String enLogin;

    private String businessKey;

    private List<String> myMajorNameList;

    private List<String> majorNameList;

    private List<String> buildNameList;

    private List<String> myBuildNameList;

    private List<CommonEdArrangeUserDto> userList;

    public CommonEdArrangeDto() {
        this.myMajorNameList= Lists.newArrayList();
        this.myBuildNameList= Lists.newArrayList();
        this.majorNameList= Lists.newArrayList();
        this.buildNameList= Lists.newArrayList();
        this.userList= Lists.newArrayList();
    }
}
