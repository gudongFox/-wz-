package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaProjectfundplan;
import java.util.List;
import java.util.Map;

public interface FiveOaProjectfundplanMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaProjectfundplan record);

    FiveOaProjectfundplan selectByPrimaryKey(Integer id);

    List<FiveOaProjectfundplan> selectAll(Map params);

    List<FiveOaProjectfundplan> fuzzySearch(Map params);

    int updateByPrimaryKey(FiveOaProjectfundplan record);
}