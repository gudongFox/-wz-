package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaMeetingArrange;
import java.util.List;
import java.util.Map;

public interface FiveOaMeetingArrangeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaMeetingArrange record);

    FiveOaMeetingArrange selectByPrimaryKey(Integer id);

    List<FiveOaMeetingArrange> selectAll(Map params);

    int updateByPrimaryKey(FiveOaMeetingArrange record);
}