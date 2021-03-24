package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaProjectfundplanDetail;
import java.util.List;
import java.util.Map;

public interface FiveOaProjectFundPlanDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaProjectfundplanDetail record);

    FiveOaProjectfundplanDetail selectByPrimaryKey(Integer id);

    List<FiveOaProjectfundplanDetail> selectAll(Map params);

    int updateByPrimaryKey(FiveOaProjectfundplanDetail record);
}