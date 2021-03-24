package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveEdOutReview;
import java.util.List;
import java.util.Map;

public interface FiveEdOutReviewMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveEdOutReview record);

    FiveEdOutReview selectByPrimaryKey(Integer id);

    List<FiveEdOutReview> selectAll(Map params);

    int updateByPrimaryKey(FiveEdOutReview record);
}