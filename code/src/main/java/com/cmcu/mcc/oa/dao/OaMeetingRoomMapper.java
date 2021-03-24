package com.cmcu.mcc.oa.dao;

import com.cmcu.mcc.oa.entity.OaMeetingRoom;
import java.util.List;
import java.util.Map;

public interface OaMeetingRoomMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OaMeetingRoom record);

    OaMeetingRoom selectByPrimaryKey(Integer id);

    List<OaMeetingRoom> selectAll(Map params);

    int updateByPrimaryKey(OaMeetingRoom record);
}