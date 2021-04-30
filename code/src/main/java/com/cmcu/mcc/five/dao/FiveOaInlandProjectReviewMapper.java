package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaInlandProjectReview;
import java.util.List;
import java.util.Map;

public interface FiveOaInlandProjectReviewMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaInlandProjectReview record);

    FiveOaInlandProjectReview selectByPrimaryKey(Integer id);

    List<FiveOaInlandProjectReview> selectAll(Map params);

    int updateByPrimaryKey(FiveOaInlandProjectReview record);
}