package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaGoAbroadTrainDeclare;
import java.util.List;
import java.util.Map;

public interface FiveOaGoAbroadTrainDeclareMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaGoAbroadTrainDeclare record);

    FiveOaGoAbroadTrainDeclare selectByPrimaryKey(Integer id);

    List<FiveOaGoAbroadTrainDeclare> selectAll(Map params);

    int updateByPrimaryKey(FiveOaGoAbroadTrainDeclare record);
}