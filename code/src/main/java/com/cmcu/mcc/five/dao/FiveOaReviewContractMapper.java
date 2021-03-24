package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaReviewContract;
import java.util.List;
import java.util.Map;

public interface FiveOaReviewContractMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaReviewContract record);

    FiveOaReviewContract selectByPrimaryKey(Integer id);

    List<FiveOaReviewContract> selectAll(Map params);

    int updateByPrimaryKey(FiveOaReviewContract record);
}