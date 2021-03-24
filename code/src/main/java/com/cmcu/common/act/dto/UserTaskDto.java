package com.cmcu.common.act.dto;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class UserTaskDto {

    private String taskKey;

    private String taskName;

    private boolean multiple;

    private List<Map> users;

    private String preKey;

    private int seq;

    public UserTaskDto() {
        this.users= Lists.newArrayList();
    }
}
