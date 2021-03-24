package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaDecisionMaking;
import java.util.List;
import java.util.Map;

public interface FiveOaDecisionMakingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaDecisionMaking record);

    FiveOaDecisionMaking selectByPrimaryKey(Integer id);

    List<FiveOaDecisionMaking> selectAll(Map params);

    int updateByPrimaryKey(FiveOaDecisionMaking record);
}