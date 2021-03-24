package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaMeetingRoom;
import java.util.List;
import java.util.Map;

public interface FiveOaMeetingRoomMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaMeetingRoom record);

    FiveOaMeetingRoom selectByPrimaryKey(Integer id);

    List<FiveOaMeetingRoom> selectAll(Map params);

    int updateByPrimaryKey(FiveOaMeetingRoom record);
}