package com.cmcu.common.dto;

import lombok.Data;

import java.util.List;

@Data
public class InputGroupDto {

    private int formDataId;

    private String groupName;

    private List<InputItemDto> items;
}
