package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaMeetingRoomApply;
import java.util.List;
import java.util.Map;

public interface FiveOaMeetingRoomApplyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaMeetingRoomApply record);

    FiveOaMeetingRoomApply selectByPrimaryKey(Integer id);

    List<FiveOaMeetingRoomApply> selectAll(Map params);

    int updateByPrimaryKey(FiveOaMeetingRoomApply record);
}