package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaRulelawexamine;
import java.util.List;
import java.util.Map;

public interface FiveOaRulelawexamineMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaRulelawexamine record);

    FiveOaRulelawexamine selectByPrimaryKey(Integer id);

    List<FiveOaRulelawexamine> selectAll(Map params);

    int updateByPrimaryKey(FiveOaRulelawexamine record);
}