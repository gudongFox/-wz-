package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveActRelevance;
import java.util.List;
import java.util.Map;

public interface FiveActRelevanceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveActRelevance record);

    FiveActRelevance selectByPrimaryKey(Integer id);

    List<FiveActRelevance> selectAll(Map params);

    int updateByPrimaryKey(FiveActRelevance record);
}