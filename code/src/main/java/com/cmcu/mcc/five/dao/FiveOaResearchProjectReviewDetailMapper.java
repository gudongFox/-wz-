package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaResearchProjectReviewDetail;
import java.util.List;
import java.util.Map;

public interface FiveOaResearchProjectReviewDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaResearchProjectReviewDetail record);

    FiveOaResearchProjectReviewDetail selectByPrimaryKey(Integer id);

    List<FiveOaResearchProjectReviewDetail> selectAll(Map params);

    int updateByPrimaryKey(FiveOaResearchProjectReviewDetail record);
}