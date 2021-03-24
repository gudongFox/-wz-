package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaGoAbroadTrainAsk;
import java.util.List;
import java.util.Map;

public interface FiveOaGoAbroadTrainAskMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaGoAbroadTrainAsk record);

    FiveOaGoAbroadTrainAsk selectByPrimaryKey(Integer id);

    List<FiveOaGoAbroadTrainAsk> selectAll(Map params);

    int updateByPrimaryKey(FiveOaGoAbroadTrainAsk record);
}