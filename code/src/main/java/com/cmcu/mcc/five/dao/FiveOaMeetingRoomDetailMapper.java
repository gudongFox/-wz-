package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaMeetingRoomDetail;
import java.util.List;
import java.util.Map;

public interface FiveOaMeetingRoomDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaMeetingRoomDetail record);

    FiveOaMeetingRoomDetail selectByPrimaryKey(Integer id);

    List<FiveOaMeetingRoomDetail> selectAll(Map params);

    int updateByPrimaryKey(FiveOaMeetingRoomDetail record);
}