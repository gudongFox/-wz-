package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaComputerSafe;
import java.util.List;
import java.util.Map;

public interface FiveOaComputerSafeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaComputerSafe record);

    FiveOaComputerSafe selectByPrimaryKey(Integer id);

    List<FiveOaComputerSafe> selectAll(Map params);

    int updateByPrimaryKey(FiveOaComputerSafe record);
}