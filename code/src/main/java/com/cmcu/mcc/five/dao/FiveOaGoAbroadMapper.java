package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaGoAbroad;
import java.util.List;
import java.util.Map;

public interface FiveOaGoAbroadMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaGoAbroad record);

    FiveOaGoAbroad selectByPrimaryKey(Integer id);

    List<FiveOaGoAbroad> selectAll(Map params);

    int updateByPrimaryKey(FiveOaGoAbroad record);
}