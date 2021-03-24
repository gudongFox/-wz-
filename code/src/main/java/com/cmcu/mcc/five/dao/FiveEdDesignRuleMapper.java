package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveEdDesignRule;
import java.util.List;
import java.util.Map;

public interface FiveEdDesignRuleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveEdDesignRule record);

    FiveEdDesignRule selectByPrimaryKey(Integer id);

    List<FiveEdDesignRule> selectAll(Map params);

    int updateByPrimaryKey(FiveEdDesignRule record);
}