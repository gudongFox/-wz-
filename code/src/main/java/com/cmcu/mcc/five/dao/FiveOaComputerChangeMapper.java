package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaComputerChange;
import java.util.List;
import java.util.Map;

public interface FiveOaComputerChangeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaComputerChange record);

    FiveOaComputerChange selectByPrimaryKey(Integer id);

    List<FiveOaComputerChange> selectAll(Map params);

    int updateByPrimaryKey(FiveOaComputerChange record);
}