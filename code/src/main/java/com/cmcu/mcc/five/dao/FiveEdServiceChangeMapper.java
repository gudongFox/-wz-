package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveEdServiceChange;
import java.util.List;
import java.util.Map;

public interface FiveEdServiceChangeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveEdServiceChange record);

    FiveEdServiceChange selectByPrimaryKey(Integer id);

    List<FiveEdServiceChange> selectAll(Map params);

    int updateByPrimaryKey(FiveEdServiceChange record);
}