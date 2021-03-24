package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaDecisionMarkingDetail;
import java.util.List;
import java.util.Map;

public interface FiveOaDecisionMarkingDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaDecisionMarkingDetail record);

    FiveOaDecisionMarkingDetail selectByPrimaryKey(Integer id);

    List<FiveOaDecisionMarkingDetail> selectAll(Map params);

    int updateByPrimaryKey(FiveOaDecisionMarkingDetail record);
}