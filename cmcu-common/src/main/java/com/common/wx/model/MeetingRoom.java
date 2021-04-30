package com.common.wx.model;

import com.cmcu.common.util.MyStringUtil;
import com.google.common.collect.Lists;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class MeetingRoom {


    private String meetingroom_id;

    @NotEmpty
    @Size(max=30)
    private String name;

    private int capacity;

    private String city;

    private String building;

    private String floor;

    private List<Integer> equipment;

    private String equipmentNames;

    public void buildEquipmentNames() {
        List<String> equipmentList = Lists.newArrayList();
        for (Integer equipmentId : equipment) {
            if (equipmentId.equals(1)) {
                equipmentList.add("电视");
            } else if (equipmentId.equals(2)) {
                equipmentList.add("电话");
            } else if (equipmentId.equals(3)) {
                equipmentList.add("投影");
            } else if (equipmentId.equals(4)) {
                equipmentList.add("白板");
            } else if (equipmentId.equals(5)) {
                equipmentList.add("视频");
            }
        }
        equipmentNames = StringUtils.join(equipmentList, ",");
    }

    public void buildEquipmentId() {
        List<Integer> equipmentIds = Lists.newArrayList();
        for (String equipmentName : MyStringUtil.getStringList(equipmentNames)) {
            if (equipmentName.equals("电视")) {
                equipmentIds.add(1);
            } else if (equipmentName.equals("电话")) {
                equipmentIds.add(2);
            } else if (equipmentName.equals("投影")) {
                equipmentIds.add(3);
            } else if (equipmentName.equals("白板")) {
                equipmentIds.add(4);
            } else if (equipmentName.equals("视频")) {
                equipmentIds.add(5);
            }
        }
        equipment = equipmentIds;
    }

}
