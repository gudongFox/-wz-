package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaComputerPersonRepair;
import java.util.List;
import java.util.Map;

public interface FiveOaComputerPersonRepairMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaComputerPersonRepair record);

    FiveOaComputerPersonRepair selectByPrimaryKey(Integer id);

    List<FiveOaComputerPersonRepair> selectAll(Map params);

    int updateByPrimaryKey(FiveOaComputerPersonRepair record);
}