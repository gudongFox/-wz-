package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaGoAbroadTrainAskDetail;
import java.util.List;
import java.util.Map;

public interface FiveOaGoAbroadTrainAskDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaGoAbroadTrainAskDetail record);

    FiveOaGoAbroadTrainAskDetail selectByPrimaryKey(Integer id);

    List<FiveOaGoAbroadTrainAskDetail> selectAll(Map params);

    int updateByPrimaryKey(FiveOaGoAbroadTrainAskDetail record);
}