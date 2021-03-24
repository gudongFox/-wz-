package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveEdReviewPlan;
import java.util.List;
import java.util.Map;

public interface FiveEdReviewPlanMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveEdReviewPlan record);

    FiveEdReviewPlan selectByPrimaryKey(Integer id);

    List<FiveEdReviewPlan> selectAll(Map params);

    int updateByPrimaryKey(FiveEdReviewPlan record);
}