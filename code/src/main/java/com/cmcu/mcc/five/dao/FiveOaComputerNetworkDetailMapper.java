package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaComputerNetworkDetail;
import java.util.List;
import java.util.Map;

public interface FiveOaComputerNetworkDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaComputerNetworkDetail record);

    FiveOaComputerNetworkDetail selectByPrimaryKey(Integer id);

    List<FiveOaComputerNetworkDetail> selectAll(Map params);

    int updateByPrimaryKey(FiveOaComputerNetworkDetail record);
}