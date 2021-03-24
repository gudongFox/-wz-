package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaOutTechnicalExchange;
import java.util.List;
import java.util.Map;

public interface FiveOaOutTechnicalExchangeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaOutTechnicalExchange record);

    FiveOaOutTechnicalExchange selectByPrimaryKey(Integer id);

    List<FiveOaOutTechnicalExchange> selectAll(Map params);

    int updateByPrimaryKey(FiveOaOutTechnicalExchange record);
}