package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaComputerNetwork;
import java.util.List;
import java.util.Map;

public interface FiveOaComputerNetworkMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaComputerNetwork record);

    FiveOaComputerNetwork selectByPrimaryKey(Integer id);

    List<FiveOaComputerNetwork> selectAll(Map params);

    int updateByPrimaryKey(FiveOaComputerNetwork record);
}