package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaResearchProjectReview;
import java.util.List;
import java.util.Map;

public interface FiveOaResearchProjectReviewMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaResearchProjectReview record);

    FiveOaResearchProjectReview selectByPrimaryKey(Integer id);

    List<FiveOaResearchProjectReview> selectAll(Map params);

    int updateByPrimaryKey(FiveOaResearchProjectReview record);
}