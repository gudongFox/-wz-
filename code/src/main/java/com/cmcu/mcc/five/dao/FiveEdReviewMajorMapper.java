package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveEdReviewMajor;
import java.util.List;
import java.util.Map;

public interface FiveEdReviewMajorMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveEdReviewMajor record);

    FiveEdReviewMajor selectByPrimaryKey(Integer id);

    List<FiveEdReviewMajor> selectAll(Map params);

    int updateByPrimaryKey(FiveEdReviewMajor record);
}